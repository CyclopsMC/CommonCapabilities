package org.cyclops.commoncapabilities.modcompat.vanilla.capability.energystorage;

import net.minecraft.world.entity.decoration.ItemFrame;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.VanillaEntityItemFrameCapabilityDelegator;

/**
 * An energy handler for entity item frames that have an energy handler.
 * @author rubensworks
 */
public class VanillaEntityItemFrameEnergyStorage extends VanillaEntityItemFrameCapabilityDelegator<EnergyHandler> implements EnergyHandler {

    public VanillaEntityItemFrameEnergyStorage(ItemFrame entity) {
        super(entity);
    }

    @Override
    protected ItemCapability<EnergyHandler, ItemAccess> getCapabilityType() {
        return Capabilities.Energy.ITEM;
    }

    @Override
    public long getAmountAsLong() {
        return getCapability()
                .map(EnergyHandler::getAmountAsLong)
                .orElse(0L);
    }

    @Override
    public long getCapacityAsLong() {
        return getCapability()
                .map(EnergyHandler::getCapacityAsLong)
                .orElse(0L);
    }

    @Override
    public int insert(int slot, TransactionContext transactionContext) {
        return getCapability()
                .map(h -> h.insert(slot, transactionContext))
                .orElse(0);
    }

    @Override
    public int extract(int slot, TransactionContext transactionContext) {
        return getCapability()
                .map(h -> h.extract(slot, transactionContext))
                .orElse(0);
    }
}
