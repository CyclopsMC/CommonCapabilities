package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityCropHarvester;

/**
 * Worker capability for {@link TileEntityCropHarvester}.
 * @author rubensworks
 */
public class TileCropHarvesterWorker extends TileElectricMachineWorkerBase<TileEntityCropHarvester> {
    public TileCropHarvesterWorker(TileEntityCropHarvester tile) {
        super(tile);
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return super.canWork() || (getEnergy().getEnergy() >= 201.0D);
    }
}
