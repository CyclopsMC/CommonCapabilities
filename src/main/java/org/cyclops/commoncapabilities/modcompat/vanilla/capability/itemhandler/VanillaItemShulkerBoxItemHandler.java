package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.cyclops.commoncapabilities.capability.itemhandler.ItemItemHandler;

import javax.annotation.Nonnull;

/**
 * An item handler wrapper for the shulker box in item form.
 * @author rubensworks
 */
public class VanillaItemShulkerBoxItemHandler extends ItemItemHandler {

    public VanillaItemShulkerBoxItemHandler(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected NonNullList<ItemStack> getItemList() {
        ItemContainerContents container = getItemStack().get(DataComponents.CONTAINER);
        NonNullList<ItemStack> list = NonNullList.withSize(27, ItemStack.EMPTY);
        if (container != null) {
            container.copyInto(list);
        }
        return list;
    }

    @Override
    protected void setItemList(NonNullList<ItemStack> itemStacks) {
        getItemStack().set(DataComponents.CONTAINER, ItemContainerContents.fromItems(itemStacks));
    }

    @Override
    public int getSlots() {
        return getItemList().size();
    }


    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }
}
