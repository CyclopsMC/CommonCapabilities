package org.cyclops.commoncapabilities.modcompat.thermalexpansion.itemhandler;

import cofh.core.util.helpers.ItemHelper;
import cofh.thermalexpansion.block.storage.ItemBlockCache;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import org.cyclops.commoncapabilities.capability.itemhandler.ItemItemHandler;

/**
 * An item handler wrapper for {@link ItemBlockCache}.
 * @author rubensworks
 */
public class ItemBlockCacheItemHandler extends ItemItemHandler {

    public ItemBlockCacheItemHandler(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected NonNullList<ItemStack> getItemList() {
        NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
        if (getItemStack().getTagCompound().hasKey("Item")) {
            items.set(0, ItemHelper.readItemStackFromNBT(getItemStack().getTagCompound().getCompoundTag("Item")));
        }
        return items;
    }

    @Override
    protected void setItemList(NonNullList<ItemStack> itemStacks) {
        if (!getItemStack().hasTagCompound()) {
            getItemStack().setTagCompound(new NBTTagCompound());
        }
        ItemStack itemStack = itemStacks.get(0);
        NBTTagCompound itemTag = new NBTTagCompound();
        ItemHelper.writeItemStackToNBT(itemStack, itemStack.getCount(), itemTag);
        getItemStack().getTagCompound().setTag("Item", itemTag);
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public int getSlotLimit(int slot) {
        return ((ItemBlockCache) getItemStack().getItem()).getSizeInventory(getItemStack());
    }
}
