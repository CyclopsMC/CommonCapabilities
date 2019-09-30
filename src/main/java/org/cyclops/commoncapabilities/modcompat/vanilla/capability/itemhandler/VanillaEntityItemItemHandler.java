package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.VanillaEntityItemCapabilityDelegator;

import javax.annotation.Nonnull;

/**
 * An item handler for entity items that have an item handler.
 * @author rubensworks
 */
public class VanillaEntityItemItemHandler extends VanillaEntityItemCapabilityDelegator<IItemHandler> implements IItemHandler {

    public VanillaEntityItemItemHandler(ItemEntity entity, Direction side) {
        super(entity, side);
    }

    @Override
    protected Capability<IItemHandler> getCapabilityType() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
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
