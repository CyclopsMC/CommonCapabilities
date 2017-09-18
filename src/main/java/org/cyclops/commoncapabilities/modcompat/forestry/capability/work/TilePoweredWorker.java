package org.cyclops.commoncapabilities.modcompat.forestry.capability.work;

import forestry.core.errors.EnumErrorCode;
import forestry.core.tiles.TilePowered;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * Worker capability for TilePowered machines.
 * @author rubensworks
 */
public class TilePoweredWorker implements IWorker {

    private final TilePowered tile;

    public TilePoweredWorker(TilePowered tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        try {
            return tile.hasWork();
        } catch (NullPointerException e) {
            return false;
        }
    }

    protected boolean isRedstoneEnabled() {
        return tile.getWorld().isBlockIndirectlyGettingPowered(tile.getPos()) > 0;
    }

    protected boolean hasEnoughEnergy() {
        return tile.getWorkCounter() > 0 ||
                (tile.getEnergyManager().getEnergyStored() > 0 && !tile.getErrorLogic().contains(EnumErrorCode.NO_POWER));
    }

    @Override
    public boolean canWork() {
        return !isRedstoneEnabled() && hasEnoughEnergy();
    }

}
