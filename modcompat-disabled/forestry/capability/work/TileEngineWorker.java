package org.cyclops.commoncapabilities.modcompat.forestry.capability.work;

import forestry.core.tiles.TileEngine;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.forestry.ForestryHelpers;

import TileEngine;

/**
 * Worker capability for TileEngine machines.
 * @author rubensworks
 */
public class TileEngineWorker implements IWorker {

    private final TileEngine tile;

    public TileEngineWorker(TileEngine tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return Helpers.invokeMethod(tile, ForestryHelpers.METHOD_TILEENGINE_ISBURNING);
    }

    @Override
    public boolean canWork() {
        return Helpers.invokeMethod(tile, ForestryHelpers.METHOD_TILEENGINE_MAYBURN);
    }

}
