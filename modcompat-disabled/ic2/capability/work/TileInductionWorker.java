package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityInduction;

/**
 * Worker capability for {@link TileEntityInduction}.
 * @author rubensworks
 */
public class TileInductionWorker extends TileElectricMachineWorkerBase<TileEntityInduction> {
    public TileInductionWorker(TileEntityInduction tile) {
        super(tile);
    }

    @Override
    public boolean hasWork() {
        return super.hasWork() || getTile().canOperate();
    }

    @Override
    public boolean canWork() {
        return super.canWork() || getEnergy().getEnergy() >= 15.0D;
    }
}
