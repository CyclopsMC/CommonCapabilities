package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.cyclops.commoncapabilities.capability.itemhandler.ItemItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * An item handler wrapper for the bundle.
 * @author rubensworks
 */
public class VanillaItemBundleItemHandler extends ItemItemHandler {

    public VanillaItemBundleItemHandler(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected NonNullList<ItemStack> getItemList() {
        NonNullList<ItemStack> itemStacks;
        CompoundTag entityTag = getItemStack().getTag();
        if (entityTag != null && entityTag.contains("Items", Tag.TAG_LIST)) {
            ListTag listTag = entityTag.getList("Items", Tag.TAG_COMPOUND);
            itemStacks = NonNullList.withSize(listTag.size() + 1, ItemStack.EMPTY);
            int slot = 0;
            for (Tag itemTag : listTag) {
                itemStacks.set(slot++, ItemStack.of((CompoundTag) itemTag));
            }
        } else {
            itemStacks = NonNullList.withSize(1, ItemStack.EMPTY);
        }
        return itemStacks;
    }

    @Override
    protected void setItemList(NonNullList<ItemStack> itemStacks) {
        CompoundTag rootTag = getItemStack().getOrCreateTag();
        ListTag listTag = new ListTag();
        for (ItemStack itemStack : itemStacks) {
            listTag.add(itemStack.save(new CompoundTag()));
        }
        rootTag.put("Items", listTag);
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
        return BundleItem.getContentWeight(getItemStack()) + BundleItem.getWeight(stack) <= 64;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (isItemValid(slot, stack)) {
            super.setStackInSlot(slot, stack);
        }
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (!isItemValid(slot, stack)) {
            return stack;
        }
        return super.insertItem(slot, stack, simulate);
    }
}
