package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityReplicator;

/**
 * Worker capability for {@link TileEntityReplicator}.
 * @author rubensworks
 */
public class TileReplicatorWorker extends TileElectricMachineWorkerBase<TileEntityReplicator> {
    public TileReplicatorWorker(TileEntityReplicator tile) {
        super(tile);
    }

    @Override
    public boolean hasWork() {
        return super.hasWork() || getTile().pattern != null;
    }

    @Override
    public boolean canWork() {
        return super.canWork()
                || (getTile().getMode() != TileEntityReplicator.Mode.STOPPED
                && getEnergy().getEnergy() >= 512.0D
                && getTile().outputSlot.canAdd(getTile().pattern));
    }
}
