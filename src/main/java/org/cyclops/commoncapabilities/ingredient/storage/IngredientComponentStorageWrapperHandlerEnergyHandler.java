package org.cyclops.commoncapabilities.ingredient.storage;

import com.google.common.collect.Iterators;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.capability.ICapabilityGetter;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageWrapperHandler;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.ingredient.collection.FilteredIngredientCollectionIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

/**
 * Energy storage wrapper handler for {@link EnergyHandler}.
 * @author rubensworks
 */
public class IngredientComponentStorageWrapperHandlerEnergyHandler<C> implements
        IIngredientComponentStorageWrapperHandler<Long, Boolean, EnergyHandler, C> {

    private final IngredientComponent<Long, Boolean> ingredientComponent;
    private final BaseCapability<EnergyHandler, C> capability;

    public IngredientComponentStorageWrapperHandlerEnergyHandler(
            IngredientComponent<Long, Boolean> ingredientComponent,
            BaseCapability<EnergyHandler, C> capability
    ) {
        this.ingredientComponent = Objects.requireNonNull(ingredientComponent);
        this.capability = capability;
    }

    @Override
    public IIngredientComponentStorage<Long, Boolean> wrapComponentStorage(EnergyHandler storage) {
        return new ComponentStorageWrapper(getComponent(), storage);
    }

    @Override
    public EnergyHandler wrapStorage(IIngredientComponentStorage<Long, Boolean> componentStorage) {
        return new EnergyStorageWrapper(componentStorage);
    }

    @Override
    public Optional<EnergyHandler> getStorage(ICapabilityGetter<C> capabilityProvider, @Nullable C context) {
        return Optional.ofNullable(capabilityProvider.getCapability(this.capability, context));
    }

    @Override
    public IngredientComponent<Long, Boolean> getComponent() {
        return this.ingredientComponent;
    }

    public static class ComponentStorageWrapper implements IIngredientComponentStorage<Long, Boolean> {

        private final IngredientComponent<Long, Boolean> ingredientComponent;
        private final EnergyHandler storage;

        public ComponentStorageWrapper(IngredientComponent<Long, Boolean> ingredientComponent, EnergyHandler storage) {
            this.ingredientComponent = ingredientComponent;
            this.storage = storage;
        }

        @Override
        public IngredientComponent<Long, Boolean> getComponent() {
            return this.ingredientComponent;
        }

        @Override
        public Iterator<Long> iterator() {
            return Iterators.forArray(storage.getAmountAsLong());
        }

        @Override
        public Iterator<Long> iterator(@Nonnull Long prototype, Boolean matchFlags) {
            return new FilteredIngredientCollectionIterator<>(iterator(), getComponent().getMatcher(), prototype, matchFlags);
        }

        @Override
        public long getMaxQuantity() {
            return storage.getCapacityAsLong();
        }

        @Override
        public Long insert(@Nonnull Long ingredient, TransactionContext transactionContext) {
            return ingredient - storage.insert(IModHelpers.get().getBaseHelpers().castSafe(ingredient), transactionContext);
        }

        @Override
        public Long extract(@Nonnull Long prototype, Boolean matchFlags, TransactionContext transactionContext) {
            if (matchFlags) {
                try (var tx = Transaction.open(transactionContext)) {
                    int extractable = storage.extract(IModHelpers.get().getBaseHelpers().castSafe(prototype), tx);
                    if (extractable != prototype) {
                        return 0L;
                    }
                }
            }
            return (long) storage.extract(IModHelpers.get().getBaseHelpers().castSafe(prototype), transactionContext);
        }

        @Override
        public Long extract(long maxQuantity, TransactionContext transactionContext) {
            return (long) storage.extract(IModHelpers.get().getBaseHelpers().castSafe(maxQuantity), transactionContext);
        }
    }

    public static class EnergyStorageWrapper implements EnergyHandler {

        private final IIngredientComponentStorage<Long, Boolean> storage;

        public EnergyStorageWrapper(IIngredientComponentStorage<Long, Boolean> storage) {
            this.storage = storage;
        }

        @Override
        public long getAmountAsLong() {
            long total = 0;
            for (Long stored : storage) {
                total = Math.addExact(total, stored);
            }
            return IModHelpers.get().getBaseHelpers().castSafe(total);
        }

        @Override
        public long getCapacityAsLong() {
            return storage.getMaxQuantity();
        }

        @Override
        public int insert(int max, TransactionContext transactionContext) {
            return max - IModHelpers.get().getBaseHelpers().castSafe(storage.insert((long) max, transactionContext));
        }

        @Override
        public int extract(int max, TransactionContext transactionContext) {
            return IModHelpers.get().getBaseHelpers().castSafe(storage.extract(max, transactionContext));
        }
    }
}
