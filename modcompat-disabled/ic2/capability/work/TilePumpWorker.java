package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityPump;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileEntityPump;

/**
 * Worker capability for {@link TileEntityPump}.
 * @author rubensworks
 */
public class TilePumpWorker implements IWorker {

    private final TileEntityPump host;

    public TilePumpWorker(TileEntityPump host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        return host.getActive() || (host.canoperate());
    }

    @Override
    public boolean canWork() {
        return host.getActive() || (host.getEnergy() >= (host.energyConsume * host.operationLength));
    }

}
