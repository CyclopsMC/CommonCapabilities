package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityTeleporter;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileEntityTeleporter;

/**
 * Worker capability for {@link TileEntityTeleporter}.
 * @author rubensworks
 */
public class TileTeleporterWorker implements IWorker {

    private final TileEntityTeleporter host;

    public TileTeleporterWorker(TileEntityTeleporter host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return host.getActive() || (host.getWorld().isBlockPowered(host.getPos()) && host.hasTarget() && host.getAvailableEnergy() > 0);
    }

}
