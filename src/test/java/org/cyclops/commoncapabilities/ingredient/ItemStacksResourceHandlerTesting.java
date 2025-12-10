package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/**
 * @author rubensworks
 */
public class ItemStacksResourceHandlerTesting extends ItemStacksResourceHandler {
    public ItemStacksResourceHandlerTesting(int size) {
        super(size);
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        try (var tx = Transaction.openRoot()) {
            insert(slot, ItemResource.of(stack), stack.getCount(), tx);
            tx.commit();
        }
    }

    public ItemStack getStackInSlot(int slot) {
        return getResource(slot).toStack(getAmountAsInt(slot));
    }
}
