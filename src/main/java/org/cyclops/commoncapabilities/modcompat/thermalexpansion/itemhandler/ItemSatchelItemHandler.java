package org.cyclops.commoncapabilities.modcompat.thermalexpansion.itemhandler;

import cofh.thermalexpansion.item.ItemSatchel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import org.cyclops.commoncapabilities.capability.itemhandler.ItemItemHandler;

/**
 * An item handler wrapper for {@link ItemSatchel}.
 * @author rubensworks
 */
public class ItemSatchelItemHandler extends ItemItemHandler {

    public ItemSatchelItemHandler(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected NonNullList<ItemStack> getItemList() {
        NonNullList<ItemStack> items = NonNullList.withSize(getSlots(), ItemStack.EMPTY);

        NBTTagCompound nbt = getItemStack().getTagCompound();
        if (nbt != null && nbt.hasKey("Inventory")) {
            NBTTagCompound tag = nbt.getCompoundTag("Inventory");
            for (int i = items.size(); i-- > 0; ) {
                if (tag.hasKey("Slot" + i)) {
                    items.set(i, new ItemStack(tag.getCompoundTag("Slot" + i)));
                } else if (tag.hasKey("slot" + i)) {
                    items.set(i, new ItemStack(tag.getCompoundTag("slot" + i)));
                }
            }
        }

        return items;
    }

    @Override
    protected void setItemList(NonNullList<ItemStack> itemStacks) {
        NBTTagCompound tag = new NBTTagCompound();

        for (int i = itemStacks.size(); i-- > 0; ) {
            ItemStack itemStack = itemStacks.get(i);
            if (!itemStack.isEmpty()) {
                tag.setTag("Slot" + i, itemStack.writeToNBT(new NBTTagCompound()));
            }

        }

        getItemStack().setTagInfo("Inventory", tag);
    }

    @Override
    public int getSlots() {
        return ((ItemSatchel) getItemStack().getItem()).getSizeInventory(getItemStack());
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }
}
