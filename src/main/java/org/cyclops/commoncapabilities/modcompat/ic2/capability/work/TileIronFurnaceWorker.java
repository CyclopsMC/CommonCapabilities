package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityIronFurnace;
import net.minecraft.tileentity.TileEntityFurnace;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;

import TileEntityIronFurnace;

/**
 * Worker capability for TileEntityIronFurnace.
 * @author rubensworks
 */
public class TileIronFurnaceWorker implements IWorker {

    private final TileEntityIronFurnace tile;

    public TileIronFurnaceWorker(TileEntityIronFurnace tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return tile.getActive() || (boolean) Helpers.invokeMethod(tile, Ic2Helpers.METHOD_TILEIRONFURNACE_CANOPERATE);
    }

    @Override
    public boolean canWork() {
        return tile.getActive() || tile.fuel > 0 || TileEntityFurnace.getItemBurnTime(tile.fuelSlot.get()) > 0;
    }

}
