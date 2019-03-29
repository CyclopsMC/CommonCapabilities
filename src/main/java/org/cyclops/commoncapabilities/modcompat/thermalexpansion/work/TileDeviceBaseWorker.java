package org.cyclops.commoncapabilities.modcompat.thermalexpansion.work;

import cofh.thermalexpansion.block.device.TileDeviceBase;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * Worker capability for TileDeviceBase machines.
 * @author rubensworks
 */
public class TileDeviceBaseWorker implements IWorker {

    private final TileDeviceBase tile;

    public TileDeviceBaseWorker(TileDeviceBase tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return tile.redstoneControlOrDisable();
    }

}
