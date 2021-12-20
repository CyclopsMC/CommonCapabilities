package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityBlastFurnace;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileEntityBlastFurnace;

/**
 * Worker capability for {@link TileEntityBlastFurnace}.
 * @author rubensworks
 */
public class TileBlastFurnaceWorker implements IWorker {

    private final TileEntityBlastFurnace host;

    public TileBlastFurnaceWorker(TileEntityBlastFurnace host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        return host.getActive() || host.getOutput() != null;
    }

    @Override
    public boolean canWork() {
        return host.getActive() || host.isHot();
    }

}
