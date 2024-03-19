package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityMagnetizer;

/**
 * Worker capability for {@link TileEntityMagnetizer}.
 * @author rubensworks
 */
public class TileMagnetizerWorker extends TileElectricMachineWorkerBase<TileEntityMagnetizer> {

    public TileMagnetizerWorker(TileEntityMagnetizer host) {
        super(host);
    }

    @Override
    public boolean canWork() {
        return super.canWork() || getTile().canBoost();
    }
}
