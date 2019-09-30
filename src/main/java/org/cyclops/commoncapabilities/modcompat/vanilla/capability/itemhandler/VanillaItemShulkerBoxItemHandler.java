package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
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
        NonNullList<ItemStack> itemStacks = NonNullList.withSize(getSlots(), ItemStack.EMPTY);
        CompoundNBT rootTag = getItemStack().getTag();
        if (rootTag != null && rootTag.contains("BlockEntityTag", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT entityTag = rootTag.getCompound("BlockEntityTag");
            if (entityTag.contains("Items", Constants.NBT.TAG_LIST)) {
                ItemStackHelper.loadAllItems(entityTag, itemStacks);
            }
        }
        return itemStacks;
    }

    @Override
    protected void setItemList(NonNullList<ItemStack> itemStacks) {
        CompoundNBT rootTag = getItemStack().getOrCreateTag();
        if (!rootTag.contains("BlockEntityTag", Constants.NBT.TAG_COMPOUND)) {
            rootTag.put("BlockEntityTag", new CompoundNBT());
        }
        ItemStackHelper.saveAllItems(rootTag.getCompound("BlockEntityTag"), itemStacks);
    }

    @Override
    public int getSlots() {
        return 27;
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
