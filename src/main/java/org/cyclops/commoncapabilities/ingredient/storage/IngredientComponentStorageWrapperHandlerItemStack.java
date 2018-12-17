package org.cyclops.commoncapabilities.ingredient.storage;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ISlotlessItemHandler;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemHandlerItemStackIterator;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageSlotted;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageWrapperHandler;
import org.cyclops.commoncapabilities.api.ingredient.storage.IngredientComponentStorageEmpty;
import org.cyclops.commoncapabilities.capability.itemhandler.SlotlessItemHandlerConfig;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.ingredient.collection.FilteredIngredientCollectionIterator;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientMapMutable;
import org.cyclops.cyclopscore.ingredient.collection.IngredientHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Item storage wrapper handler for {@link IItemHandler}.
 * @author rubensworks
 */
public class IngredientComponentStorageWrapperHandlerItemStack
        implements IIngredientComponentStorageWrapperHandler<ItemStack, Integer, IItemHandler> {

    private final IngredientComponent<ItemStack, Integer> ingredientComponent;

    public IngredientComponentStorageWrapperHandlerItemStack(IngredientComponent<ItemStack, Integer> ingredientComponent) {
        this.ingredientComponent = Objects.requireNonNull(ingredientComponent);
    }

    @Override
    public IIngredientComponentStorage<ItemStack, Integer> wrapComponentStorage(IItemHandler storage) {
        return new ComponentStorageWrapper(getComponent(), storage);
    }

    public IIngredientComponentStorage<ItemStack, Integer> wrapComponentStorage(IItemHandler storage,
                                                                                ISlotlessItemHandler slotlessStorage) {
        return new ComponentStorageWrapperCombined(getComponent(), storage, slotlessStorage);
    }

    @Override
    public IItemHandler wrapStorage(IIngredientComponentStorage<ItemStack, Integer> componentStorage) {
        if (componentStorage instanceof IIngredientComponentStorageSlotted) {
            return new ItemStorageWrapperSlotted(getComponent(),
                    (IIngredientComponentStorageSlotted<ItemStack, Integer>) componentStorage);
        }
        return new ItemStorageWrapper(getComponent(), componentStorage);
    }

    @Nullable
    @Override
    public IItemHandler getStorage(ICapabilityProvider capabilityProvider, @Nullable EnumFacing facing) {
        if (capabilityProvider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
            return capabilityProvider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
        }
        return null;
    }

    @Override
    public IIngredientComponentStorage<ItemStack, Integer> getComponentStorage(ICapabilityProvider capabilityProvider,
                                                                               @Nullable EnumFacing facing) {
        if (capabilityProvider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
            if (capabilityProvider.hasCapability(SlotlessItemHandlerConfig.CAPABILITY, facing)) {
                return wrapComponentStorage(
                        capabilityProvider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing),
                        capabilityProvider.getCapability(SlotlessItemHandlerConfig.CAPABILITY, facing)
                );
            } else {
                return wrapComponentStorage(getStorage(capabilityProvider, facing));
            }
        }
        return new IngredientComponentStorageEmpty<>(getComponent());
    }

    @Override
    public IngredientComponent<ItemStack, Integer> getComponent() {
        return this.ingredientComponent;
    }

    public static class ComponentStorageWrapper implements IIngredientComponentStorageSlotted<ItemStack, Integer> {

        private final IngredientComponent<ItemStack, Integer> ingredientComponent;
        private final IItemHandler storage;

        public ComponentStorageWrapper(IngredientComponent<ItemStack, Integer> ingredientComponent,
                                       IItemHandler storage) {
            this.ingredientComponent = ingredientComponent;
            this.storage = storage;
        }

        @Override
        public IngredientComponent<ItemStack, Integer> getComponent() {
            return this.ingredientComponent;
        }

        @Override
        public Iterator<ItemStack> iterator() {
            return new ItemHandlerItemStackIterator(storage);
        }

        @Override
        public Iterator<ItemStack> iterator(@Nonnull ItemStack prototype, Integer matchFlags) {
            if (getComponent().getMatcher().getAnyMatchCondition().equals(matchFlags)) {
                return iterator();
            }
            return new FilteredIngredientCollectionIterator<>(iterator(), getComponent().getMatcher(),
                    prototype, matchFlags);
        }

        @Override
        public long getMaxQuantity() {
            long sum = 0;
            int slots = storage.getSlots();
            for (int slot = 0; slot < slots; slot++) {
                sum = Math.addExact(sum, storage.getSlotLimit(slot));
            }
            return sum;
        }

        @Override
        public ItemStack insert(@Nonnull ItemStack ingredient, boolean simulate) {
            return ItemHandlerHelper.insertItem(storage, ingredient, simulate);
        }

        @Override
        public ItemStack extract(@Nonnull ItemStack prototype, Integer matchFlags, boolean simulate) {
            int slots = storage.getSlots();
            boolean checkStackSize = (matchFlags & ItemMatch.STACKSIZE) > 0;
            int requiredStackSize = prototype.getCount();

            // Maintain a temporary mapping of prototype items to their total count over all slots,
            // plus the list of slots in which they are present.
            IIngredientMapMutable<ItemStack, Integer, Pair<Wrapper<Integer>, List<Integer>>> validInstancesCollapsed = new IngredientHashMap<>(getComponent());
            int subMatchFlags = matchFlags & ~ItemMatch.STACKSIZE;

            for (int slot = 0; slot < slots; slot++) {
                ItemStack extractedSimulated = storage.extractItem(slot, Integer.MAX_VALUE, true);
                if (!extractedSimulated.isEmpty()
                        && getComponent().getMatcher().matches(prototype, extractedSimulated, subMatchFlags)) {
                    ItemStack storagePrototype = getComponent().getMatcher().withQuantity(extractedSimulated, 1);

                    // Get existing value from temporary mapping
                    Pair<Wrapper<Integer>, List<Integer>> existingValue = validInstancesCollapsed.get(storagePrototype);
                    if (existingValue == null) {
                        existingValue = Pair.of(new Wrapper<>(0), Lists.newLinkedList());
                        validInstancesCollapsed.put(storagePrototype, existingValue);
                    }

                    // Update the counter and slot-list for our prototype
                    int newCount = existingValue.getLeft().get() + extractedSimulated.getCount();
                    existingValue.getLeft().set(newCount);
                    existingValue.getRight().add(slot);

                    // If the count is sufficient for our query, return
                    if (newCount >= requiredStackSize) {
                        // Actually extract if we are not simulating the extraction
                        // We assume that the simulated extraction resulted in the same output
                        // as the non-simulated output, so we ignore its output
                        if (!simulate) {
                            int toExtract = requiredStackSize;
                            for (Integer finalSlot : existingValue.getRight()) {
                                ItemStack extractedActual = storage.extractItem(finalSlot, toExtract, false);
                                toExtract -= extractedActual.getCount();
                            }
                            // Quick heuristic check to see if 'storage' did not lie during its simulation
                            if (toExtract != 0) {
                                throw new IllegalStateException("An item storage resulted in inconsistent simulated and non-simulated output.");
                            }
                        }
                        return getComponent().getMatcher().withQuantity(storagePrototype, requiredStackSize);
                    }
                }
            }

            // If we reach this point, then our effective count is below requiredStackSize

            // Fail if we required an exact quantity
            if (checkStackSize) {
                return ItemStack.EMPTY;
            }

            // Extract from all valid slots if we didn't require an exact quantity
            ItemStack storagePrototype = getComponent().getMatcher().withQuantity(prototype, 1);
            Pair<Wrapper<Integer>, List<Integer>> existingValue = validInstancesCollapsed.get(storagePrototype);
            int extractedCount = existingValue != null ? existingValue.getLeft().get() : 0;
            if (!simulate && existingValue != null) {
                int toExtract = requiredStackSize;
                for (Integer finalSlot : existingValue.getRight()) {
                    ItemStack extractedActual = storage.extractItem(finalSlot, toExtract, false);
                    toExtract -= extractedActual.getCount();
                }
                // Quick heuristic check to see if 'storage' did not lie during its simulation
                if (toExtract != requiredStackSize - extractedCount) {
                    throw new IllegalStateException("An item storage resulted in inconsistent simulated and non-simulated output.");
                }
            }
            return getComponent().getMatcher().withQuantity(storagePrototype, extractedCount);
        }

        @Override
        public ItemStack extract(long maxQuantity, boolean simulate) {
            int slots = storage.getSlots();
            int amount = Helpers.castSafe(maxQuantity);
            for (int slot = 0; slot < slots; slot++) {
                ItemStack extractedSimulated = storage.extractItem(slot, amount, true);
                if (!extractedSimulated.isEmpty()) {
                    return simulate ? extractedSimulated : storage.extractItem(slot, amount, false);
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlots() {
            return storage.getSlots();
        }

        @Override
        public ItemStack getSlotContents(int slot) {
            return storage.getStackInSlot(slot);
        }

        @Override
        public long getMaxQuantity(int slot) {
            return storage.getSlotLimit(slot);
        }

        @Override
        public ItemStack insert(int slot, @Nonnull ItemStack ingredient, boolean simulate) {
            return storage.insertItem(slot, ingredient, simulate);
        }

        @Override
        public ItemStack extract(int slot, long maxQuantity, boolean simulate) {
            return storage.extractItem(slot, Helpers.castSafe(maxQuantity), simulate);
        }
    }

    public static class ComponentStorageWrapperCombined extends ComponentStorageWrapper {

        private final ISlotlessItemHandler storageSlotless;

        public ComponentStorageWrapperCombined(IngredientComponent<ItemStack, Integer> ingredientComponent,
                                               IItemHandler storage, ISlotlessItemHandler storageSlotless) {
            super(ingredientComponent, storage);
            this.storageSlotless = storageSlotless;
        }

        @Override
        public Iterator<ItemStack> iterator() {
            return storageSlotless.getItems();
        }

        @Override
        public Iterator<ItemStack> iterator(@Nonnull ItemStack prototype, Integer matchFlags) {
            return storageSlotless.findItems(prototype, matchFlags);
        }

        @Override
        public long getMaxQuantity() {
            return storageSlotless.getLimit();
        }

        @Override
        public ItemStack insert(@Nonnull ItemStack ingredient, boolean simulate) {
            return storageSlotless.insertItem(ingredient, simulate);
        }

        @Override
        public ItemStack extract(long maxQuantity, boolean simulate) {
            return storageSlotless.extractItem(Helpers.castSafe(maxQuantity), simulate);
        }

        @Override
        public ItemStack extract(@Nonnull ItemStack prototype, Integer matchFlags, boolean simulate) {
            return storageSlotless.extractItem(prototype, matchFlags, simulate);
        }
    }

    public static class ItemStorageWrapper implements IItemHandler {

        private final IngredientComponent<ItemStack, Integer> ingredientComponent;
        private final IIngredientComponentStorage<ItemStack, Integer> storage;

        public ItemStorageWrapper(IngredientComponent<ItemStack, Integer> ingredientComponent,
                                  IIngredientComponentStorage<ItemStack, Integer> storage) {
            this.ingredientComponent = ingredientComponent;
            this.storage = storage;
        }

        @Override
        public int getSlots() {
            return Iterators.size(storage.iterator());
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return Iterators.get(storage.iterator(), slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return storage.insert(stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return storage.extract(amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return Helpers.castSafe(ingredientComponent.getMatcher().getMaximumQuantity());
        }
    }

    public static class ItemStorageWrapperSlotted implements IItemHandler {

        private final IngredientComponent<ItemStack, Integer> ingredientComponent;
        private final IIngredientComponentStorageSlotted<ItemStack, Integer> storage;

        public ItemStorageWrapperSlotted(IngredientComponent<ItemStack, Integer> ingredientComponent,
                                         IIngredientComponentStorageSlotted<ItemStack, Integer> storage) {
            this.ingredientComponent = ingredientComponent;
            this.storage = storage;
        }

        @Override
        public int getSlots() {
            return storage.getSlots();
        }

        protected void validateSlotIndex(int slot) throws IndexOutOfBoundsException {
            int slots = getSlots();
            if (slot < 0 || slot >= slots) {
                throw new IndexOutOfBoundsException("Slot " + slot + " not in valid range - [0," + slots + ")");
            }
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            validateSlotIndex(slot);
            return storage.getSlotContents(slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            validateSlotIndex(slot);
            return storage.insert(slot, stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            validateSlotIndex(slot);
            return storage.extract(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            validateSlotIndex(slot);
            return Helpers.castSafe(storage.getMaxQuantity(slot));
        }
    }
}
