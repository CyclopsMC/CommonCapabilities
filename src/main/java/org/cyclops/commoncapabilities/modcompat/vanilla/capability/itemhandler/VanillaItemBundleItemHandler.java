package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
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
        BundleContents container = getItemStack().get(DataComponents.BUNDLE_CONTENTS);
        if (container != null) {
            NonNullList<ItemStack> list = NonNullList.create();
            container.itemCopyStream().forEach(list::add);
            list.add(ItemStack.EMPTY);
            return list;
        }
        return NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    protected void setItemList(NonNullList<ItemStack> itemStacks) {
        getItemStack().set(DataComponents.BUNDLE_CONTENTS, new BundleContents(itemStacks.stream().filter(s -> !s.isEmpty()).toList()));
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
        return stack.isEmpty() || getMaxAmountToAdd(stack) > 0;
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
        int insertCount = Math.min(getMaxAmountToAdd(stack), stack.getCount());
        int notInserting = stack.getCount() - insertCount;
        ItemStack remaining = super.insertItem(slot, stack.copyWithCount(insertCount), simulate);
        if (notInserting > 0) {
            if (remaining.isEmpty()) {
                remaining = stack.copy();
                remaining.setCount(notInserting);
            } else {
                remaining.grow(notInserting);
            }
        }
        return remaining;
    }

    // Copied from BundleContents

    private int getMaxAmountToAdd(ItemStack stackToAdd) {
        BundleContents container = getItemStack().get(DataComponents.BUNDLE_CONTENTS);
        Fraction fraction = Fraction.ONE.subtract(container.weight());
        return Math.max(fraction.divideBy(BundleContents.getWeight(stackToAdd)).intValue(), 0);
    }
}
