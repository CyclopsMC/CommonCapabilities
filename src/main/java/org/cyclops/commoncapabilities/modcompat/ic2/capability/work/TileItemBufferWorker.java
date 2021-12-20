package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityItemBuffer;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileEntityItemBuffer;

/**
 * Worker capability for {@link TileEntityItemBuffer}.
 * @author rubensworks
 */
public class TileItemBufferWorker implements IWorker {

    private final TileEntityItemBuffer host;

    public TileItemBufferWorker(TileEntityItemBuffer host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        return host.getActive() || (!host.leftcontentSlot.isEmpty());
    }

    @Override
    public boolean canWork() {
        return host.getActive() || (host.upgradeSlot.get(0) != null && host.upgradeSlot.get(1) != null);
    }

}
