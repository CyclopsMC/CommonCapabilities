package org.cyclops.commoncapabilities.api.capability.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;

/**
 * A simple immutable list-based item handler.
 * @author rubensworks
 */
public class ImmutableListItemHandler implements IItemHandler {

    private final NonNullList<ItemStack> itemStacks;

    public ImmutableListItemHandler(NonNullList<ItemStack> itemStacks) {
        this.itemStacks = itemStacks;
    }

    @Override
    public int getSlots() {
        return itemStacks.size();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemStacks.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
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
