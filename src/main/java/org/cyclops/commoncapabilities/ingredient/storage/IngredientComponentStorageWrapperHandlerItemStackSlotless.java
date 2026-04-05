package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ISlotlessItemHandler;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.capability.ICapabilityGetter;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageWrapperHandler;
import org.cyclops.cyclopscore.helper.IModHelpers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

/**
 * Item storage wrapper handler for {@link ISlotlessItemHandler}.
 * @author rubensworks
 */
public class IngredientComponentStorageWrapperHandlerItemStackSlotless<C>
        implements IIngredientComponentStorageWrapperHandler<ItemStack, Integer, ISlotlessItemHandler, C> {

    private final IngredientComponent<ItemStack, Integer> ingredientComponent;
    private final BaseCapability<ISlotlessItemHandler, C> capability;

    public IngredientComponentStorageWrapperHandlerItemStackSlotless(
            IngredientComponent<ItemStack, Integer> ingredientComponent,
            BaseCapability<ISlotlessItemHandler, C> capability
    ) {
        this.ingredientComponent = Objects.requireNonNull(ingredientComponent);
        this.capability = capability;
    }

    @Override
    public IIngredientComponentStorage<ItemStack, Integer> wrapComponentStorage(ISlotlessItemHandler storage) {
        return new ComponentStorageWrapper(getComponent(), storage);
    }

    @Override
    public ISlotlessItemHandler wrapStorage(IIngredientComponentStorage<ItemStack, Integer> componentStorage) {
        return new ItemStorageWrapper(componentStorage);
    }

    @Override
    public Optional<ISlotlessItemHandler> getStorage(ICapabilityGetter<C> capabilityProvider, @Nullable C context) {
        return Optional.ofNullable(capabilityProvider.getCapability(this.capability, context));
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
        public ItemStack insert(@Nonnull ItemStack ingredient, TransactionContext transaction) {
            return storage.insertItem(ingredient, transaction);
        }

        @Override
        public ItemStack extract(@Nonnull ItemStack prototype, Integer matchFlags, TransactionContext transaction) {
            return storage.extractItem(prototype, matchFlags, transaction);
        }

        @Override
        public ItemStack extract(long maxQuantity, TransactionContext transaction) {
            return storage.extractItem(IModHelpers.get().getBaseHelpers().castSafe(maxQuantity), transaction);
        }
    }

    public static class ItemStorageWrapper implements ISlotlessItemHandler {

        private final IIngredientComponentStorage<ItemStack, Integer> storage;

        public ItemStorageWrapper(IIngredientComponentStorage<ItemStack, Integer> storage) {
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
        public ItemStack insertItem(@Nonnull ItemStack stack, TransactionContext transaction) {
            return storage.insert(stack, transaction);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int amount, TransactionContext transaction) {
            return storage.extract(amount, transaction);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(@Nonnull ItemStack matchStack, int matchFlags, TransactionContext transaction) {
            return storage.extract(matchStack, matchFlags, transaction);
        }

        @Override
        public long getLimit() {
            return IModHelpers.get().getBaseHelpers().castSafe(storage.getMaxQuantity());
        }
    }
}
