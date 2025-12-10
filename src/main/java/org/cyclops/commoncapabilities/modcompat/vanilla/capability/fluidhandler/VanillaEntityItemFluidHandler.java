package org.cyclops.commoncapabilities.modcompat.vanilla.capability.fluidhandler;

import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.VanillaEntityItemCapabilityDelegator;

/**
 * A fluid handler for entity items that have a fluid handler.
 * @author rubensworks
 */
public class VanillaEntityItemFluidHandler extends VanillaEntityItemCapabilityDelegator<ResourceHandler<FluidResource>> implements ResourceHandler<FluidResource> {

    public VanillaEntityItemFluidHandler(ItemEntity entity) {
        super(entity);
    }

    @Override
    protected ItemCapability<ResourceHandler<FluidResource>, ItemAccess> getCapabilityType() {
        return Capabilities.Fluid.ITEM;
    }

    @Override
    public int size() {
        return getCapability()
                .map(ResourceHandler::size)
                .orElse(0);
    }

    @Override
    public FluidResource getResource(int slot) {
        return getCapability()
                .map(itemHandler -> itemHandler.getResource(slot))
                .orElse(FluidResource.EMPTY);
    }

    @Override
    public long getAmountAsLong(int slot) {
        return getCapability()
                .map(itemHandler -> itemHandler.getAmountAsLong(slot))
                .orElse(0L);
    }

    @Override
    public long getCapacityAsLong(int slot, FluidResource itemResource) {
        return getCapability()
                .map(itemHandler -> itemHandler.getCapacityAsLong(slot, itemResource))
                .orElse(0L);
    }

    @Override
    public boolean isValid(int slot, FluidResource itemResource) {
        return getCapability()
                .map(itemHandler -> itemHandler.isValid(slot, itemResource))
                .orElse(false);
    }

    @Override
    public int insert(int slot, FluidResource itemResource, int amount, TransactionContext transactionContext) {
        return getCapability()
                .map(itemHandler -> itemHandler.insert(slot, itemResource, amount, transactionContext))
                .orElse(0);
    }

    @Override
    public int insert(FluidResource itemResource, int amount, TransactionContext transactionContext) {
        return getCapability()
                .map(itemHandler -> itemHandler.insert(itemResource, amount, transactionContext))
                .orElse(0);
    }

    @Override
    public int extract(int slot, FluidResource itemResource, int amount, TransactionContext transactionContext) {
        return getCapability()
                .map(itemHandler -> itemHandler.extract(slot, itemResource, amount, transactionContext))
                .orElse(0);
    }

    @Override
    public int extract(FluidResource itemResource, int amount, TransactionContext transactionContext) {
        return getCapability()
                .map(itemHandler -> itemHandler.extract(itemResource, amount, transactionContext))
                .orElse(0);
    }
}
