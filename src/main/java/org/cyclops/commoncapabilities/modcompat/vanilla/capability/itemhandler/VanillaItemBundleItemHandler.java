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
        getItemStack().set(DataComponents.BUNDLE_CONTENTS, new BundleContents(itemStacks));
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
        return getMaxAmountToAdd(stack) > 0;
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

    // Copied from BundleContents

    private int getMaxAmountToAdd(ItemStack stackToAdd) {
        BundleContents container = getItemStack().get(DataComponents.BUNDLE_CONTENTS);
        Fraction fraction = Fraction.ONE.subtract(container.weight());
        return Math.max(fraction.divideBy(BundleContents.getWeight(stackToAdd)).intValue(), 0);
    }
}
