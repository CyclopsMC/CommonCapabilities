package org.cyclops.commoncapabilities.modcompat.enderio.capability.work;

import crazypants.enderio.machine.killera.TileKillerJoe;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileKillerJoe;

/**
 * Worker capability for AbstractPoweredTaskEntity machines.
 *
 * @author rubensworks
 */
public class TileKillerJoeWorker implements IWorker {

    private final TileKillerJoe tile;

    public TileKillerJoeWorker(TileKillerJoe tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        // The multiplication factor is copied from TileKillerJoe#getActivationAmount()
        return tile.getStackInSlot(0) != null
                && tile.getNutrientTank().getFluidAmount() >= (tile.getNutrientTank().getCapacity() * 0.7F);
    }

}
