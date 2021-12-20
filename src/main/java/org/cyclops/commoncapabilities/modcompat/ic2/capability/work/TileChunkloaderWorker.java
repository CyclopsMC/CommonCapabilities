package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityChunkloader;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileEntityChunkloader;

/**
 * Worker capability for {@link TileEntityChunkloader}.
 * @author rubensworks
 */
public class TileChunkloaderWorker implements IWorker {

    private final TileEntityChunkloader tile;

    public TileChunkloaderWorker(TileEntityChunkloader tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return tile.getActive() || !tile.getLoadedChunks().isEmpty();
    }

    @Override
    public boolean canWork() {
        return tile.getActive() || tile.getEnergy() >= tile.getLoadedChunks().size();
    }
}
