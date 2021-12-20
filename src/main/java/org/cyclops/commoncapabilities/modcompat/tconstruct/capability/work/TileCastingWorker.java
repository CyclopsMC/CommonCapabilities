package org.cyclops.commoncapabilities.modcompat.tconstruct.capability.work;

import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import slimeknights.tconstruct.smeltery.tileentity.TileCasting;

import TileCasting;

/**
 * Worker capability for the casting tiles.
 * @author rubensworks
 */
public class TileCastingWorker implements IWorker {

    private final TileCasting tile;

    public TileCastingWorker(TileCasting tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return tile.getCurrentResult() != null;
    }

    @Override
    public boolean canWork() {
        return true;
    }

}
