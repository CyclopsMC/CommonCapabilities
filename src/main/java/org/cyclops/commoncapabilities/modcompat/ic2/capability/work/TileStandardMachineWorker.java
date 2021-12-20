package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityStandardMachine;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;

import TileEntityStandardMachine;

/**
 * Worker capability for TileEntityStandardMachine machines.
 * @author rubensworks
 */
public class TileStandardMachineWorker implements IWorker {

    private final TileEntityStandardMachine tile;

    public TileStandardMachineWorker(TileEntityStandardMachine tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return tile.getActive() || Helpers.invokeMethod(tile, Ic2Helpers.METHOD_TILESTANDARDMACHINE_GETOUTPUT) != null;
    }

    @Override
    public boolean canWork() {
        return tile.getActive() || tile.getEnergy() >= tile.energyConsume;
    }

}
