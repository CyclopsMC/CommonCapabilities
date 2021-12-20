package org.cyclops.commoncapabilities.modcompat.tconstruct.capability.work;

import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import slimeknights.tconstruct.gadgets.tileentity.TileDryingRack;

import TileDryingRack;

/**
 * Worker capability for the drying rack.
 * @author rubensworks
 */
public class TileDryingRackWorker implements IWorker {

    private final TileDryingRack tile;

    public TileDryingRackWorker(TileDryingRack tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return !tile.getStackInSlot(0).isEmpty();
    }

    @Override
    public boolean canWork() {
        return true;
    }

}
