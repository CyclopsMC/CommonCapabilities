package org.cyclops.commoncapabilities.modcompat.forestry.capability.work;

import forestry.api.core.IErrorState;
import forestry.core.errors.EnumErrorCode;
import forestry.factory.tiles.TileMoistener;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileMoistener;

/**
 * Worker capability for the TileMoistener.
 * @author rubensworks
 */
public class TileMoistenerWorker implements IWorker {

    private final TileMoistener tile;

    public TileMoistenerWorker(TileMoistener tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return doesNotHaveError(EnumErrorCode.NO_RECIPE);
    }

    protected boolean doesNotHaveError(IErrorState error) {
        return !tile.getErrorLogic().contains(error);
    }

    @Override
    public boolean canWork() {
        return doesNotHaveError(EnumErrorCode.NOT_DARK) && doesNotHaveError(EnumErrorCode.NO_RESOURCE_LIQUID);
    }
}
