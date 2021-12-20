package org.cyclops.commoncapabilities.modcompat.thermalexpansion.itemhandler;

import cofh.thermalexpansion.block.storage.ItemBlockStrongbox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.core.NonNullList;
import org.cyclops.commoncapabilities.capability.itemhandler.ItemItemHandler;

/**
 * An item handler wrapper for {@link ItemBlockStrongbox}.
 * @author rubensworks
 */
public class ItemBlockStrongboxItemHandler extends ItemItemHandler {

    public ItemBlockStrongboxItemHandler(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected NonNullList<ItemStack> getItemList() {
        NonNullList<ItemStack> items = NonNullList.withSize(getSlots(), ItemStack.EMPTY);

        NBTTagCompound nbt = getItemStack().getTagCompound();
        if (nbt.hasKey("Inventory")) {
            NBTTagList list = nbt.getTagList("Inventory", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                int slot = tag.getInteger("Slot");
                if (slot >= 0 && slot < items.size()) {
                    items.set(slot, new ItemStack(tag));
                }
            }
        }

        return items;
    }

    @Override
    protected void setItemList(NonNullList<ItemStack> itemStacks) {
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < itemStacks.size(); i++) {
            if (!itemStacks.get(i).isEmpty()) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("Slot", i);
                itemStacks.get(i).writeToNBT(tag);
                list.appendTag(tag);
            }
        }
        if (list.tagCount() > 0) {
            if (!getItemStack().hasTagCompound()) {
                getItemStack().setTagCompound(new NBTTagCompound());
            }
            getItemStack().getTagCompound().setTag("Inventory", list);
        }
    }

    @Override
    public int getSlots() {
        return ((ItemBlockStrongbox) getItemStack().getItem()).getSizeInventory(getItemStack());
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }
}
