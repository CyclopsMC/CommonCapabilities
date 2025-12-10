package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ISlotlessItemHandler;
import org.cyclops.commoncapabilities.api.ingredient.IResourceConverter;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.capability.ICapabilityGetter;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IngredientComponentStorageEmpty;
import org.cyclops.cyclopscore.helper.IModHelpers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Optional;

/**
 * Item storage wrapper handler for {@link ResourceHandler}.
 * @author rubensworks
 */
public class IngredientComponentStorageWrapperHandlerItemStack<C>
        extends IngredientComponentStorageWrapperHandlerResourceHandler<C, ItemResource, ItemStack, Integer> {

    private final BaseCapability<ISlotlessItemHandler, C> capabilitySlotless;

    public IngredientComponentStorageWrapperHandlerItemStack(
            IngredientComponent<ItemStack, Integer> ingredientComponent,
            BaseCapability<ResourceHandler<ItemResource>, C> capability,
            BaseCapability<ISlotlessItemHandler, C> capabilitySlotless
    ) {
        super(ingredientComponent, capability, IngredientComponent.ITEMSTACK_CONVERTER);
        this.capabilitySlotless = capabilitySlotless;
    }

    public IIngredientComponentStorage<ItemStack, Integer> wrapComponentStorage(ResourceHandler<ItemResource> storage,
                                                                                ISlotlessItemHandler slotlessStorage) {
        return new ComponentStorageWrapperCombined(getComponent(), storage, resourceConverter, slotlessStorage);
    }

    @Override
    public IIngredientComponentStorage<ItemStack, Integer> getComponentStorage(ICapabilityGetter<C> capabilityProvider,
                                                                               @Nullable C context) {
        Optional<ResourceHandler<ItemResource>> storageSlotted = getStorage(capabilityProvider, context);
        Optional<ISlotlessItemHandler> storageSlotless = Optional.ofNullable(capabilityProvider.getCapability(this.capabilitySlotless, context));
        if (storageSlotted.isPresent()) {
            if (storageSlotless.isPresent()) {
                return wrapComponentStorage(storageSlotted.orElse(null), storageSlotless.orElse(null));
            } else {
                return wrapComponentStorage(storageSlotted.orElse(null));
            }
        }
        return new IngredientComponentStorageEmpty<>(getComponent());
    }

    public static class ComponentStorageWrapperCombined extends IngredientComponentStorageWrapperHandlerResourceHandler.ComponentStorageWrapper<ItemResource, ItemStack, Integer> {

        private final ISlotlessItemHandler storageSlotless;

        public ComponentStorageWrapperCombined(IngredientComponent<ItemStack, Integer> ingredientComponent,
                                               ResourceHandler<ItemResource> storage,
                                               IResourceConverter<ItemResource, ItemStack> resourceConverter,
                                               ISlotlessItemHandler storageSlotless) {
            super(ingredientComponent, storage, resourceConverter);
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
        public ItemStack insert(@Nonnull ItemStack ingredient, TransactionContext transaction) {
            return storageSlotless.insertItem(ingredient, transaction);
        }

        @Override
        public ItemStack extract(long maxQuantity, TransactionContext transaction) {
            return storageSlotless.extractItem(IModHelpers.get().getBaseHelpers().castSafe(maxQuantity), transaction);
        }

        @Override
        public ItemStack extract(@Nonnull ItemStack prototype, Integer matchFlags, TransactionContext transaction) {
            return storageSlotless.extractItem(prototype, matchFlags, transaction);
        }
    }
}
