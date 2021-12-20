package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityTesla;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileEntityTesla;

/**
 * Worker capability for {@link TileEntityTesla}.
 * @author rubensworks
 */
public class TileTeslaWorker implements IWorker {

    private final TileEntityTesla host;

    public TileTeslaWorker(TileEntityTesla host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return host.getActive() || (host.getWorld().isBlockPowered(host.getPos()));
    }

}
