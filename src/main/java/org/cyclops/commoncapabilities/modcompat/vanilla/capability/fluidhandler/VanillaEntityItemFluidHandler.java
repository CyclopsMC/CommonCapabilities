package org.cyclops.commoncapabilities.modcompat.vanilla.capability.fluidhandler;

import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.VanillaEntityItemCapabilityDelegator;

import javax.annotation.Nonnull;

/**
 * A fluid handler for entity items that have a fluid handler.
 * @author rubensworks
 */
public class VanillaEntityItemFluidHandler extends VanillaEntityItemCapabilityDelegator<IFluidHandlerItem> implements IFluidHandler {

    public VanillaEntityItemFluidHandler(ItemEntity entity) {
        super(entity);
    }

    @Override
    protected ItemCapability<IFluidHandlerItem, Void> getCapabilityType() {
        return Capabilities.FluidHandler.ITEM;
    }

    @Override
    public int getTanks() {
        return getCapability()
                .map(IFluidHandlerItem::getTanks)
                .orElse(0);
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return getCapability()
                .map(fluidHandler -> fluidHandler.getFluidInTank(tank))
                .orElse(FluidStack.EMPTY);
    }

    @Override
    public int getTankCapacity(int tank) {
        return getCapability()
                .map(fluidHandler -> fluidHandler.getTankCapacity(tank))
                .orElse(0);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return getCapability()
                .map(fluidHandler -> fluidHandler.isFluidValid(tank, stack))
                .orElse(false);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return getCapability()
                .map(fluidHandler -> {
                    int ret = fluidHandler.fill(resource, action);
                    if (ret > 0 && action.execute()) {
                        updateItemStack(fluidHandler.getContainer());
                    }
                    return ret;
                })
                .orElse(0);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return getCapability()
                .map(fluidHandler -> {
                    FluidStack ret = fluidHandler.drain(resource, action);
                    if (!ret.isEmpty() && action.execute()) {
                        updateItemStack(fluidHandler.getContainer());
                    }
                    return ret;
                })
                .orElse(FluidStack.EMPTY);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return getCapability()
                .map(fluidHandler -> {
                    FluidStack ret = fluidHandler.drain(maxDrain, action);
                    if (!ret.isEmpty() && action.execute()) {
                        updateItemStack(fluidHandler.getContainer());
                    }
                    return ret;
                })
                .orElse(FluidStack.EMPTY);
    }
}
