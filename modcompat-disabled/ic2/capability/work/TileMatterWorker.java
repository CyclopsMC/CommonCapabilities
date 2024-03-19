package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityMatter;

/**
 * Worker capability for {@link TileEntityMatter}.
 * @author rubensworks
 */
public class TileMatterWorker extends TileElectricMachineWorkerBase<TileEntityMatter> {
    public TileMatterWorker(TileEntityMatter tile) {
        super(tile);
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return super.canWork() || (!getTile().getWorld().isBlockPowered(getTile().getPos()) && getEnergy().getEnergy() > 0);
    }
}
