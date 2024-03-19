package org.cyclops.commoncapabilities.modcompat.enderio.capability.work;

import crazypants.enderio.machine.wireless.TileWirelessCharger;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileWirelessCharger;

/**
 * Worker capability for AbstractPoweredTaskEntity machines.
 * @author rubensworks
 */
public class TileWirelessChargerWorker implements IWorker {

    private final TileWirelessCharger tile;

    public TileWirelessChargerWorker(TileWirelessCharger tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return tile.isActive();
    }

}
