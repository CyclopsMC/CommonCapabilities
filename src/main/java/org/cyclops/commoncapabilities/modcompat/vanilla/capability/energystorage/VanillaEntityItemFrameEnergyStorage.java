package org.cyclops.commoncapabilities.modcompat.vanilla.capability.energystorage;

import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.VanillaEntityItemFrameCapabilityDelegator;

/**
 * An energy handler for entity item frames that have an energy handler.
 * @author rubensworks
 */
public class VanillaEntityItemFrameEnergyStorage extends VanillaEntityItemFrameCapabilityDelegator<IEnergyStorage> implements IEnergyStorage {

    public VanillaEntityItemFrameEnergyStorage(ItemFrame entity) {
        super(entity);
    }

    @Override
    protected ItemCapability<IEnergyStorage, Void> getCapabilityType() {
        return Capabilities.EnergyStorage.ITEM;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        ItemStack itemStack = getItemStack();
        return getCapability(itemStack)
                .map(energyStorage -> {
                    int ret = energyStorage.receiveEnergy(maxReceive, simulate);
                    if (!simulate && ret > 0) {
                        updateItemStack(itemStack);
                    }
                    return ret;
                })
                .orElse(0);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        ItemStack itemStack = getItemStack();
        return getCapability(itemStack)
                .map(energyStorage -> {
                    int ret = energyStorage.extractEnergy(maxExtract, simulate);
                    if (!simulate && ret > 0) {
                        updateItemStack(itemStack);
                    }
                    return ret;
                }).orElse(0);
    }

    @Override
    public int getEnergyStored() {
        return getCapability()
                .map(IEnergyStorage::getEnergyStored)
                .orElse(0);
    }

    @Override
    public int getMaxEnergyStored() {
        return getCapability()
                .map(IEnergyStorage::getMaxEnergyStored)
                .orElse(0);
    }

    @Override
    public boolean canExtract() {
        return getCapability()
                .map(IEnergyStorage::canExtract)
                .orElse(false);
    }

    @Override
    public boolean canReceive() {
        return getCapability()
                .map(IEnergyStorage::canReceive)
                .orElse(false);
    }
}
