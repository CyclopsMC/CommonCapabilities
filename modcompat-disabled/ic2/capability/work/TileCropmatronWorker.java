package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityCropmatron;

/**
 * Worker capability for {@link TileEntityCropmatron}.
 * @author rubensworks
 */
public class TileCropmatronWorker extends TileElectricMachineWorkerBase<TileEntityCropmatron> {
    public TileCropmatronWorker(TileEntityCropmatron tile) {
        super(tile);
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return super.canWork() || (getEnergy().getEnergy() >= 31.0D);
    }
}
