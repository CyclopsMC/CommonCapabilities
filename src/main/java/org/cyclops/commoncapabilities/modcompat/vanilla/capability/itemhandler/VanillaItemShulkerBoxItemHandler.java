package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
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
        CompoundTag rootTag = getItemStack().getTag();
        if (rootTag != null && rootTag.contains("BlockEntityTag", Tag.TAG_COMPOUND)) {
            CompoundTag entityTag = rootTag.getCompound("BlockEntityTag");
            if (entityTag.contains("Items", Tag.TAG_LIST)) {
                ContainerHelper.loadAllItems(entityTag, itemStacks);
            }
        }
        return itemStacks;
    }

    @Override
    protected void setItemList(NonNullList<ItemStack> itemStacks) {
        CompoundTag rootTag = getItemStack().getOrCreateTag();
        if (!rootTag.contains("BlockEntityTag", Tag.TAG_COMPOUND)) {
            rootTag.put("BlockEntityTag", new CompoundTag());
        }
        ContainerHelper.saveAllItems(rootTag.getCompound("BlockEntityTag"), itemStacks);
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
