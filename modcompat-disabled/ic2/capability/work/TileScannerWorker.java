package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityScanner;

/**
 * Worker capability for {@link TileEntityScanner}.
 * @author rubensworks
 */
public class TileScannerWorker extends TileElectricMachineWorkerBase<TileEntityScanner> {
    public TileScannerWorker(TileEntityScanner tile) {
        super(tile);
    }

    @Override
    public boolean hasWork() {
        return super.hasWork() || (getTile().getState() != TileEntityScanner.State.ALREADY_RECORDED
                && getTile().getState() != TileEntityScanner.State.IDLE
                && getTile().getState() != TileEntityScanner.State.COMPLETED);
    }

    @Override
    public boolean canWork() {
        return super.canWork() || (getTile().getState() != TileEntityScanner.State.IDLE
                && getTile().getState() != TileEntityScanner.State.NO_STORAGE
                && getTile().getState() != TileEntityScanner.State.NO_ENERGY);
    }
}
