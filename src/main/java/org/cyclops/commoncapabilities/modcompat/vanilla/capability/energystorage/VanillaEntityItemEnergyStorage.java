package org.cyclops.commoncapabilities.modcompat.vanilla.capability.energystorage;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.VanillaEntityItemCapabilityDelegator;

/**
 * An energy handler for entity items that have an energy handler.
 * @author rubensworks
 */
public class VanillaEntityItemEnergyStorage extends VanillaEntityItemCapabilityDelegator<IEnergyStorage> implements IEnergyStorage {

    public VanillaEntityItemEnergyStorage(ItemEntity entity, Direction side) {
        super(entity, side);
    }

    @Override
    protected Capability<IEnergyStorage> getCapabilityType() {
        return ForgeCapabilities.ENERGY;
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
