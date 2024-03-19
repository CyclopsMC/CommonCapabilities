package org.cyclops.commoncapabilities.modcompat.forestry.capability.work;

import forestry.factory.tiles.TileMillRainmaker;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.forestry.ForestryHelpers;

import TileMillRainmaker;

/**
 * Worker capability for the TileMillRainmaker.
 * @author rubensworks
 */
public class TileRainmakerWorker implements IWorker {

    private final TileMillRainmaker tile;

    public TileRainmakerWorker(TileMillRainmaker tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return ((int) Helpers.getFieldValue(tile, ForestryHelpers.FIELD_TILEMILL_CHARGE)) > 0;
    }
}
