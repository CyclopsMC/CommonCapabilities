package org.cyclops.commoncapabilities.capability.itemhandler;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

/**
 * An item handler wrapper for items.
 * @author rubensworks
 */
public abstract class ItemItemHandler implements IItemHandlerModifiable {

    private final ItemStack itemStack;

    public ItemItemHandler(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    protected abstract NonNullList<ItemStack> getItemList();

    protected abstract void setItemList(NonNullList<ItemStack> itemStacks);

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        NonNullList<ItemStack> itemStacks = getItemList();
        itemStacks.set(slot, stack);
        setItemList(itemStacks);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return getItemList().get(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        NonNullList<ItemStack> itemStacks = getItemList();
        ItemStack existingStack = itemStacks.get(slot);

        int maxStackSize;
        if (!existingStack.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existingStack))
                return stack;

            maxStackSize = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - existingStack.getCount();

            if (stack.getCount() <= maxStackSize) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(existingStack.getCount());
                    setStackInSlot(slot, copy);
                }

                return ItemStack.EMPTY;
            } else  {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.split(maxStackSize);
                    copy.grow(existingStack.getCount());
                    setStackInSlot(slot, copy);
                    return stack;
                }  else {
                    stack.shrink(maxStackSize);
                    return stack;
                }
            }
        } else {
            maxStackSize = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
            if (maxStackSize < stack.getCount()) {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    setStackInSlot(slot, stack.split(maxStackSize));
                    return stack;
                } else {
                    stack.shrink(maxStackSize);
                    return stack;
                }
            } else {
                if (!simulate) {
                    setStackInSlot(slot, stack);
                }
                return ItemStack.EMPTY;
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        ItemStack existingStack = getStackInSlot(slot);

        if (existingStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack extracted = existingStack.split(amount);
        if (!simulate) {
            setStackInSlot(slot, existingStack);
        }
        return extracted;
    }
}
