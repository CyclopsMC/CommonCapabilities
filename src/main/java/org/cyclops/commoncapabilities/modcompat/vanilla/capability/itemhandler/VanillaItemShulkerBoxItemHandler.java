package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.cyclops.commoncapabilities.capability.itemhandler.ItemItemHandler;


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
    public int size() {
        return getItemList().size();
    }


    @Override
    public long getCapacityAsLong(int slot, ItemResource itemResource) {
        return 64;
    }

    @Override
    public boolean isValid(int slot, ItemResource itemResource) {
        return true;
    }
}
