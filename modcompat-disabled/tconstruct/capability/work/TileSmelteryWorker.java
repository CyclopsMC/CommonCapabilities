package org.cyclops.commoncapabilities.modcompat.tconstruct.capability.work;

import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import slimeknights.tconstruct.smeltery.tileentity.TileSmeltery;

/**
 * Worker capability for the smeltery.
 * @author rubensworks
 */
public class TileSmelteryWorker implements IWorker {

    private final TileSmeltery tile;

    public TileSmelteryWorker(TileSmeltery tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        for(int i = 0; i < tile.getSizeInventory(); i++) {
            if (!tile.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canWork() {
        return tile.isActive();
    }

}
