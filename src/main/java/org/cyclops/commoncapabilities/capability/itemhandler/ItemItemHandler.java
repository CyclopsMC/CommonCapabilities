package org.cyclops.commoncapabilities.capability.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import javax.annotation.Nonnull;

/**
 * An item handler wrapper for items.
 * @author rubensworks
 */
public abstract class ItemItemHandler implements ResourceHandler<ItemResource> {

    private final ItemStack itemStack;
    private final SnapshotJournal<NonNullList<ItemStack>> snapshotJournal;

    public ItemItemHandler(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.snapshotJournal = new SnapshotJournal<>() {
            @Override
            protected NonNullList<ItemStack> createSnapshot() {
                return NonNullList.copyOf(getItemList());
            }

            @Override
            protected void revertToSnapshot(NonNullList<ItemStack> snapshot) {
                setItemList(snapshot);
            }
        };
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    protected abstract NonNullList<ItemStack> getItemList();

    protected abstract void setItemList(NonNullList<ItemStack> itemStacks);

    public void setStackInSlot(int slot, ItemStack stack) {
        NonNullList<ItemStack> itemStacks = getItemList();
        itemStacks.set(slot, stack);
        setItemList(itemStacks);
    }

    @Nonnull
    @Override
    public ItemResource getResource(int slot) {
        return ItemResource.of(getItemList().get(slot));
    }

    @Override
    public long getAmountAsLong(int slot) {
        return getItemList().get(slot).getCount();
    }

    @Nonnull
    @Override
    public int insert(int slot, ItemResource itemResource, int amount, TransactionContext transaction) {
        if (itemResource.isEmpty()) {
            return 0;
        }

        NonNullList<ItemStack> itemStacks = getItemList();
        ItemStack existingStack = itemStacks.get(slot);

        int maxStackSize;
        snapshotJournal.updateSnapshots(transaction);
        if (!existingStack.isEmpty()) {
            if (!itemResource.matches(existingStack))
                return 0;

            maxStackSize = Math.min(itemResource.getMaxStackSize(), getCapacityAsInt(slot, itemResource)) - existingStack.getCount();

            if (amount <= maxStackSize) {
                setStackInSlot(slot, itemResource.toStack(existingStack.getCount() + amount));
                return amount;
            } else  {
                setStackInSlot(slot, itemResource.toStack(maxStackSize));
                return maxStackSize - existingStack.getCount();
            }
        } else {
            maxStackSize = Math.min(itemResource.getMaxStackSize(), getCapacityAsInt(slot, itemResource));
            if (maxStackSize < amount) {
                setStackInSlot(slot, itemResource.toStack(maxStackSize));
                return maxStackSize - amount;
            } else {
                setStackInSlot(slot, itemResource.toStack(amount));
                return amount;
            }
        }
    }

    @Nonnull
    @Override
    public int extract(int slot, ItemResource itemResource, int amount, TransactionContext transaction) {
        if (amount == 0)
            return 0;

        ItemStack existingStack = getResource(slot).toStack(getAmountAsInt(slot));

        if (existingStack.isEmpty()) {
            return 0;
        }

        ItemStack extracted = existingStack.split(amount);
        snapshotJournal.updateSnapshots(transaction);
        setStackInSlot(slot, existingStack);
        return extracted.getCount();
    }
}
