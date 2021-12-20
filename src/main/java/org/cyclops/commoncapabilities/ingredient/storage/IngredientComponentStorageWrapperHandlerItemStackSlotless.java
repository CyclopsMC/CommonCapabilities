package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ISlotlessItemHandler;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageWrapperHandler;
import org.cyclops.commoncapabilities.capability.itemhandler.SlotlessItemHandlerConfig;
import org.cyclops.cyclopscore.helper.Helpers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;

/**
 * Item storage wrapper handler for {@link ISlotlessItemHandler}.
 * @author rubensworks
 */
public class IngredientComponentStorageWrapperHandlerItemStackSlotless
        implements IIngredientComponentStorageWrapperHandler<ItemStack, Integer, ISlotlessItemHandler> {

    private final IngredientComponent<ItemStack, Integer> ingredientComponent;

    public IngredientComponentStorageWrapperHandlerItemStackSlotless(IngredientComponent<ItemStack, Integer> ingredientComponent) {
        this.ingredientComponent = Objects.requireNonNull(ingredientComponent);
    }

    @Override
    public IIngredientComponentStorage<ItemStack, Integer> wrapComponentStorage(ISlotlessItemHandler storage) {
        return new ComponentStorageWrapper(getComponent(), storage);
    }

    @Override
    public ISlotlessItemHandler wrapStorage(IIngredientComponentStorage<ItemStack, Integer> componentStorage) {
        return new ItemStorageWrapper(getComponent(), componentStorage);
    }

    @Override
    public LazyOptional<ISlotlessItemHandler> getStorage(ICapabilityProvider capabilityProvider, @Nullable Direction facing) {
        return capabilityProvider.getCapability(SlotlessItemHandlerConfig.CAPABILITY, facing);
    }

    @Override
    public IngredientComponent<ItemStack, Integer> getComponent() {
        return this.ingredientComponent;
    }

    public static class ComponentStorageWrapper implements IIngredientComponentStorage<ItemStack, Integer> {

        private final IngredientComponent<ItemStack, Integer> ingredientComponent;
        private final ISlotlessItemHandler storage;

        public ComponentStorageWrapper(IngredientComponent<ItemStack, Integer> ingredientComponent,
                                       ISlotlessItemHandler storage) {
            this.ingredientComponent = ingredientComponent;
            this.storage = storage;
        }

        @Override
        public IngredientComponent<ItemStack, Integer> getComponent() {
            return this.ingredientComponent;
        }

        @Override
        public Iterator<ItemStack> iterator() {
            return storage.getItems();
        }

        @Override
        public Iterator<ItemStack> iterator(@Nonnull ItemStack prototype, Integer matchFlags) {
            return storage.findItems(prototype, matchFlags);
        }

        @Override
        public long getMaxQuantity() {
            return storage.getLimit();
        }

        @Override
        public ItemStack insert(@Nonnull ItemStack ingredient, boolean simulate) {
            return storage.insertItem(ingredient, simulate);
        }

        @Override
        public ItemStack extract(@Nonnull ItemStack prototype, Integer matchFlags, boolean simulate) {
            return storage.extractItem(prototype, matchFlags, simulate);
        }

        @Override
        public ItemStack extract(long maxQuantity, boolean simulate) {
            return storage.extractItem(Helpers.castSafe(maxQuantity), simulate);
        }
    }

    public static class ItemStorageWrapper implements ISlotlessItemHandler {

        private final IngredientComponent<ItemStack, Integer> ingredientComponent;
        private final IIngredientComponentStorage<ItemStack, Integer> storage;

        public ItemStorageWrapper(IngredientComponent<ItemStack, Integer> ingredientComponent,
                                  IIngredientComponentStorage<ItemStack, Integer> storage) {
            this.ingredientComponent = ingredientComponent;
            this.storage = storage;
        }

        @Override
        public Iterator<ItemStack> getItems() {
            return storage.iterator();
        }

        @Override
        public Iterator<ItemStack> findItems(@Nonnull ItemStack stack, int matchFlags) {
            return storage.iterator(stack, matchFlags);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate) {
            return storage.insert(stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int amount, boolean simulate) {
            return storage.extract(amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(@Nonnull ItemStack matchStack, int matchFlags, boolean simulate) {
            return storage.extract(matchStack, matchFlags, simulate);
        }

        @Override
        public int getLimit() {
            return Helpers.castSafe(storage.getMaxQuantity());
        }
    }
}
