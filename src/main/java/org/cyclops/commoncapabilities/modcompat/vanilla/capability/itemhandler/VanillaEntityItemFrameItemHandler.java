package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.items.IItemHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.VanillaEntityItemFrameCapabilityDelegator;

import javax.annotation.Nonnull;

/**
 * An item handler for entity item frames that have an item handler.
 * @author rubensworks
 */
public class VanillaEntityItemFrameItemHandler extends VanillaEntityItemFrameCapabilityDelegator<IItemHandler> implements IItemHandler {

    public VanillaEntityItemFrameItemHandler(ItemFrame entity) {
        super(entity);
    }

    @Override
    protected ItemCapability<IItemHandler, Void> getCapabilityType() {
        return Capabilities.ItemHandler.ITEM;
    }

    @Override
    public int getSlots() {
        return getCapability()
                .map(IItemHandler::getSlots)
                .orElse(0);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return getCapability()
                .map(itemHandler -> itemHandler.getStackInSlot(slot))
                .orElse(ItemStack.EMPTY);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return getCapability()
                .map(itemHandler -> {
                    ItemStack innerStack = getItemStack();
                    ItemStack ret = itemHandler.insertItem(slot, stack, simulate);
                    if (stack.getCount() != ret.getCount() && !simulate) {
                        updateItemStack(innerStack);
                    }
                    return ret;
                })
                .orElse(ItemStack.EMPTY);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return getCapability()
                .map(itemHandler -> {
                    ItemStack innerStack = getItemStack();
                    ItemStack ret = itemHandler.extractItem(slot, amount, simulate);
                    if (!ret.isEmpty() && !simulate) {
                        updateItemStack(innerStack);
                    }
                    return ret;
                })
                .orElse(ItemStack.EMPTY);
    }

    @Override
    public int getSlotLimit(int slot) {
        return getCapability()
                .map(itemHandler -> itemHandler.getSlotLimit(slot))
                .orElse(0);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return getCapability()
                .map(itemHandler -> itemHandler.isItemValid(slot, stack))
                .orElse(false);
    }
}
