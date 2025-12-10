package org.cyclops.commoncapabilities.ingredient.storage;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.commoncapabilities.api.capability.resourcehandler.ResourceHandlerIngredientIterator;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IResourceConverter;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.capability.ICapabilityGetter;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageSlotted;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageWrapperHandler;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.ingredient.collection.FilteredIngredientCollectionIterator;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientMapMutable;
import org.cyclops.cyclopscore.ingredient.collection.IngredientHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Generic storage wrapper handler for {@link ResourceHandler}.
 * @author rubensworks
 */
public class IngredientComponentStorageWrapperHandlerResourceHandler<C, R extends Resource, T, M>
        implements IIngredientComponentStorageWrapperHandler<T, M, ResourceHandler<R>, C> {

    private final IngredientComponent<T, M> ingredientComponent;
    private final BaseCapability<? extends ResourceHandler<R>, C> capability;
    protected final IResourceConverter<R, T> resourceConverter;

    public IngredientComponentStorageWrapperHandlerResourceHandler(
            IngredientComponent<T, M> ingredientComponent,
            BaseCapability<? extends ResourceHandler<R>, C> capability,
            IResourceConverter<R, T> resourceConverter
    ) {
        this.ingredientComponent = ingredientComponent;
        this.capability = capability;
        this.resourceConverter = resourceConverter;
    }

    @Override
    public IIngredientComponentStorage<T, M> wrapComponentStorage(ResourceHandler<R> storage) {
        return new ComponentStorageWrapper<>(getComponent(), storage, resourceConverter);
    }

    @Override
    public ResourceHandler<R> wrapStorage(IIngredientComponentStorage<T, M> componentStorage) {
        if (componentStorage instanceof IIngredientComponentStorageSlotted) {
            return new ResourceStorageWrapperSlotted<>((IIngredientComponentStorageSlotted<T, M>) componentStorage, resourceConverter);
        }
        return new ResourceStorageWrapper<>(componentStorage, resourceConverter);
    }

    @Override
    public Optional<ResourceHandler<R>> getStorage(ICapabilityGetter<C> capabilityProvider, @Nullable C context) {
        return Optional.ofNullable(capabilityProvider.getCapability(this.capability, context));
    }

    @Override
    public IngredientComponent<T, M> getComponent() {
        return this.ingredientComponent;
    }

    public static class ComponentStorageWrapper<R extends Resource, T, M> implements IIngredientComponentStorageSlotted<T, M> {

        private final IngredientComponent<T, M> ingredientComponent;
        private final ResourceHandler<R> storage;
        private final IResourceConverter<R, T> resourceConverter;

        public ComponentStorageWrapper(IngredientComponent<T, M> ingredientComponent, ResourceHandler<R> storage, IResourceConverter<R, T> resourceConverter) {
            this.ingredientComponent = ingredientComponent;
            this.storage = storage;
            this.resourceConverter = resourceConverter;
        }

        @Override
        public IngredientComponent<T, M> getComponent() {
            return this.ingredientComponent;
        }

        @Override
        public Iterator<T> iterator() {
            return new ResourceHandlerIngredientIterator<>(storage, resourceConverter);
        }

        @Override
        public Iterator<T> iterator(@Nonnull T prototype, M matchFlags) {
            if (getComponent().getMatcher().getAnyMatchCondition().equals(matchFlags)) {
                return iterator();
            }
            return new FilteredIngredientCollectionIterator<>(iterator(), getComponent().getMatcher(), prototype, matchFlags);
        }

        @Override
        public long getMaxQuantity() {
            long sum = 0;
            R emptyResource = resourceConverter.toResource(getComponent().getMatcher().getEmptyInstance());
            for (int i = 0; i < storage.size(); i++) {
                sum = Math.addExact(sum, storage.getCapacityAsLong(i, emptyResource));
            }
            return sum;
        }

        @Override
        public T insert(@Nonnull T ingredient, TransactionContext transaction) {
            IIngredientMatcher<T, M> matcher = ingredientComponent.getMatcher();

            // Don't continue if stack is empty
            if (matcher.isEmpty(ingredient)) {
                return matcher.getEmptyInstance();
            }

            long totalAmount = matcher.getQuantity(ingredient);
            int filledAmount = storage.insert(resourceConverter.toResource(ingredient), (int) totalAmount, transaction);
            if (filledAmount >= totalAmount) {
                return matcher.getEmptyInstance();
            } else {
                long remaining = totalAmount - filledAmount;
                return matcher.withQuantity(ingredient, remaining);
            }
        }

        @Override
        public T extract(@Nonnull T prototype, M matchFlags, TransactionContext transaction) {
            IIngredientMatcher<T, M> matcher = ingredientComponent.getMatcher();

            // Don't continue if stack is empty
            if (matcher.isEmpty(prototype)) {
                return matcher.getEmptyInstance();
            }

            // If we have an AMOUNT condition,
            // first simulate extraction to determine all valid instances,
            // and then do actual extraction based on first valid instance.
            if (matcher.hasCondition(matchFlags, matcher.getQuantityMatchCondition())) {
                IIngredientMapMutable<T, M, Pair<Wrapper<Integer>, List<Integer>>> validInstancesCollapsed = new IngredientHashMap<>(getComponent());
                M matchFlagsNoQuantity = matcher.withoutCondition(matchFlags, matcher.getQuantityMatchCondition());
                int amount = (int) matcher.getQuantity(prototype);
                T finalizeStoragePrototype = null;
                Pair<Wrapper<Integer>, List<Integer>> finalizeExistingValue = null;
                try (var tx = Transaction.open(transaction)) {
                    for (int i = 0; i < storage.size(); i++) {
                        R contents = storage.getResource(i);
                        T contentsInstance = resourceConverter.fromResource(contents, storage.getAmountAsInt(i));
                        if (!contents.isEmpty() && matcher.matches(contentsInstance, prototype, matchFlagsNoQuantity)) {
                            int drained = storage.extract(i, contents, amount, tx);
                            T drainedInstance = resourceConverter.fromResource(contents, drained);
                            T storagePrototype = getComponent().getMatcher().withQuantity(drainedInstance, 1);

                            // Get existing value from temporary mapping
                            Pair<Wrapper<Integer>, List<Integer>> existingValue = validInstancesCollapsed.get(storagePrototype);
                            if (existingValue == null) {
                                existingValue = Pair.of(new Wrapper<>(0), Lists.newLinkedList());
                                validInstancesCollapsed.put(storagePrototype, existingValue);
                            }

                            // Update the counter and slot-list for our prototype
                            int newCount = existingValue.getLeft().get() + drained;
                            existingValue.getLeft().set(newCount);
                            existingValue.getRight().add(i);

                            // If the count is sufficient for our query, return
                            if (newCount >= amount) {
                                // Actually extract if we are not simulating the extraction
                                // We assume that the simulated extraction resulted in the same output
                                // as the non-simulated output, so we ignore its output
                                existingValue.getLeft().set(amount);
                                finalizeStoragePrototype = storagePrototype;
                                finalizeExistingValue = existingValue;
                                // Break here, so we close the transaction, and finalize from a clean slate.
                                break;
                            }
                        }
                    }
                }
                if (finalizeExistingValue != null) {
                    return finalizeExtractionForAmount(finalizeStoragePrototype, finalizeExistingValue, amount, transaction);
                } else {
                    return matcher.getEmptyInstance();
                }
            }

            // If we require an EXACT match (without quantity), we defer to the slot-less extraction method,
            // as the implementor may be able to optimize for this.
            // This is the most efficient case, so ideally most calls will end up here.
            if (matchFlags.equals(matcher.getExactMatchNoQuantityCondition())) {
                int drained = storage.extract(resourceConverter.toResource(prototype), (int) matcher.getQuantity(prototype), transaction);
                return matcher.withQuantity(prototype, drained);
            }

            // In all other cases, we have to iterate over the tank contents,
            // and drain based on their contents.
            int amount = (int) matcher.getQuantity(prototype);
            T extractedAcc = matcher.getEmptyInstance();
            for (int i = 0; i < storage.size(); i++) {
                R contents = storage.getResource(i);
                T contentsInstance = resourceConverter.fromResource(contents, storage.getAmountAsInt(i));
                if (!contents.isEmpty()
                        && (matcher.isEmpty(extractedAcc) || matcher.matches(contentsInstance, extractedAcc, matcher.getExactMatchNoQuantityCondition()))
                        && matcher.matches(contentsInstance, prototype, matchFlags)) {
                    try (var tx = Transaction.open(transaction)) {
                        int drained = storage.extract(i, contents, amount, tx);
                        T drainedInstance = resourceConverter.fromResource(contents, drained);
                        if (matcher.matches(drainedInstance, prototype, matchFlags)) {
                            tx.commit();
                            if (matcher.isEmpty(extractedAcc)) {
                                extractedAcc = drainedInstance;
                            } else {
                                extractedAcc = matcher.withQuantity(extractedAcc, matcher.getQuantity(extractedAcc) + drained);
                            }
                            amount -= drained;
                            if (amount <= 0) {
                                break;
                            }
                        }
                    }
                }
            }

            return extractedAcc;
        }

        protected T finalizeExtractionForAmount(T instancePrototype, Pair<Wrapper<Integer>, List<Integer>> value,
                                                int requiredQuantity, TransactionContext transaction) {
            long extractedCount = value.getLeft().get();
            if (extractedCount > 0) {
                int toExtract = requiredQuantity;
                for (Integer finalSlot : value.getRight()) {
                    toExtract -= storage.extract(finalSlot, storage.getResource(finalSlot), toExtract, transaction);
                }
                // Quick heuristic check to see if 'storage' did not lie during its simulation
                if (toExtract != requiredQuantity - extractedCount) {
                    throw new IllegalStateException("An ingredient storage resulted in inconsistent simulated and non-simulated output.");
                }
            }
            return getComponent().getMatcher().withQuantity(instancePrototype, extractedCount);
        }

        @Override
        public T extract(long maxQuantity, TransactionContext transaction) {
            IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
            return extract(matcher.withQuantity(matcher.getNonEmptyInstance(), maxQuantity), matcher.getAnyMatchCondition(), transaction);
        }

        @Override
        public int getSlots() {
            return storage.size();
        }

        @Override
        public T getSlotContents(int slot) {
            return resourceConverter.fromResource(storage.getResource(slot), storage.getAmountAsInt(slot));
        }

        @Override
        public long getMaxQuantity(int slot) {
            return storage.getCapacityAsLong(slot, resourceConverter.toResource(getComponent().getMatcher().getEmptyInstance()));
        }

        @Override
        public T insert(int slot, @Nonnull T ingredient, TransactionContext transaction) {
            try (var tx = Transaction.open(transaction)) {
                int totalAmount = IModHelpers.get().getBaseHelpers().castSafe(ingredientComponent.getMatcher().getQuantity(ingredient));
                int inserted = storage.insert(slot, resourceConverter.toResource(ingredient), totalAmount, tx);
                if (inserted > 0) {
                    tx.commit();
                    long remaining = totalAmount - inserted;
                    return ingredientComponent.getMatcher().withQuantity(ingredient, remaining);
                }
            }
            return ingredientComponent.getMatcher().getEmptyInstance();
        }

        @Override
        public T extract(int slot, long maxQuantity, TransactionContext transaction) {
            R contents = storage.getResource(slot);
            if (!contents.isEmpty()) {
                try (var tx = Transaction.open(transaction)) {
                    int extracted = storage.extract(slot, contents, IModHelpers.get().getBaseHelpers().castSafe(maxQuantity), tx);
                    if (extracted > 0) {
                        tx.commit();
                        return resourceConverter.fromResource(contents, extracted);
                    }
                }
            }
            return ingredientComponent.getMatcher().getEmptyInstance();
        }
    }

    public static class ResourceStorageWrapper<R extends Resource, T, M> implements ResourceHandler<R> {

        private final IIngredientComponentStorage<T, M> storage;
        protected final IResourceConverter<R, T> resourceConverter;

        public ResourceStorageWrapper(IIngredientComponentStorage<T, M> storage, IResourceConverter<R, T> resourceConverter) {
            this.storage = storage;
            this.resourceConverter = resourceConverter;
        }

        @Override
        public int size() {
            // +1 so that at least one slot appears empty, for when others want to insert
            return Iterators.size(storage.iterator()) + 1;
        }

        @Override
        public R getResource(int slot) {
            return resourceConverter.toResource(Iterables.get(this.storage, slot, storage.getComponent().getMatcher().getEmptyInstance()));
        }

        @Override
        public long getAmountAsLong(int slot) {
            return storage.getComponent().getMatcher().getQuantity(Iterables.get(this.storage, slot, storage.getComponent().getMatcher().getEmptyInstance()));
        }

        @Override
        public long getCapacityAsLong(int slot, R resource) {
            // Yes, this is an overestimate, as this is the total across ALL tanks
            return IModHelpers.get().getBaseHelpers().castSafe(this.storage.getMaxQuantity());
        }

        @Override
        public boolean isValid(int slot, R resource) {
            return true;
        }

        @Override
        public int insert(int slot, R resource, int amount, TransactionContext transaction) {
            return insert(resource, amount, transaction);
        }

        @Override
        public int insert(R resource, int amount, TransactionContext transaction) {
            if (resource.isEmpty()) {
                return 0;
            }

            IIngredientMatcher<T, M> matcher = storage.getComponent().getMatcher();
            T remaining = storage.insert(resourceConverter.fromResource(resource, amount), transaction);
            return matcher.isEmpty(remaining) ? amount : amount - IModHelpers.get().getBaseHelpers().castSafe(matcher.getQuantity(remaining));
        }

        @Override
        public int extract(int slot, R resource, int amount, TransactionContext transaction) {
            IIngredientMatcher<T, M> matcher = storage.getComponent().getMatcher();
            T slotInstance = Iterators.get(storage.iterator(), slot, matcher.getEmptyInstance());
            if (matcher.isEmpty(slotInstance)) {
                return 0;
            }
            T extracted = storage.extract(slotInstance, matcher.getExactMatchNoQuantityCondition(), transaction);
            return IModHelpers.get().getBaseHelpers().castSafe(matcher.getQuantity(extracted));
        }

        @Override
        public int extract(R resource, int amount, TransactionContext transaction) {
            if (resource.isEmpty()) {
                return 0;
            }

            IIngredientMatcher<T, M> matcher = storage.getComponent().getMatcher();
            T extracted = storage.extract(resourceConverter.fromResource(resource, amount), matcher.getExactMatchNoQuantityCondition(), transaction);
            return IModHelpers.get().getBaseHelpers().castSafe(matcher.getQuantity(extracted));
        }
    }

    public static class ResourceStorageWrapperSlotted<R extends Resource, T, M> extends ResourceStorageWrapper<R, T, M> {

        private final IIngredientComponentStorageSlotted<T, M> storage;

        public ResourceStorageWrapperSlotted(IIngredientComponentStorageSlotted<T, M> storage, IResourceConverter<R, T> resourceConverter) {
            super(storage, resourceConverter);
            this.storage = storage;
        }

        @Override
        public int size() {
            return this.storage.getSlots();
        }

        @Override
        public R getResource(int slot) {
            if (slot < 0 || slot >= size()) {
                throw new IndexOutOfBoundsException("Slot " + slot + " not in valid range - [0," + size() + ")");
            }
            return this.resourceConverter.toResource(this.storage.getSlotContents(slot));
        }

        @Override
        public long getAmountAsLong(int slot) {
            if (slot < 0 || slot >= size()) {
                throw new IndexOutOfBoundsException("Slot " + slot + " not in valid range - [0," + size() + ")");
            }
            return this.storage.getComponent().getMatcher().getQuantity(this.storage.getSlotContents(slot));
        }

        @Override
        public long getCapacityAsLong(int slot, R resource) {
            return IModHelpers.get().getBaseHelpers().castSafe(this.storage.getMaxQuantity(slot));
        }

        @Override
        public int insert(int slot, R resource, int amount, TransactionContext transaction) {
            if (resource.isEmpty()) {
                return 0;
            }

            IIngredientMatcher<T, M> matcher = storage.getComponent().getMatcher();
            T remaining = storage.insert(slot, resourceConverter.fromResource(resource, amount), transaction);
            return matcher.isEmpty(remaining) ? amount : amount - IModHelpers.get().getBaseHelpers().castSafe(matcher.getQuantity(remaining));
        }

        @Override
        public int extract(int slot, R resource, int amount, TransactionContext transaction) {
            if (resource.isEmpty()) {
                return 0;
            }

            IIngredientMatcher<T, M> matcher = storage.getComponent().getMatcher();
            if (!matcher.matches(storage.getSlotContents(slot), resourceConverter.fromResource(resource, amount), matcher.getExactMatchNoQuantityCondition())) {
                return 0;
            }
            T extracted = storage.extract(slot, amount, transaction);
            return IModHelpers.get().getBaseHelpers().castSafe(matcher.getQuantity(extracted));
        }
    }
}
