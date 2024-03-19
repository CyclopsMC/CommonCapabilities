package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.comp.Energy;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;

import Energy;
import TileEntityElectricMachine;

/**
 * Base Worker capability for TileEntityElectricMachine machines.
 * @author rubensworks
 */
public class TileElectricMachineWorkerBase<T extends TileEntityElectricMachine> implements IWorker {

    private final T tile;

    public TileElectricMachineWorkerBase(T tile) {
        this.tile = tile;
    }

    protected T getTile() {
        return this.tile;
    }

    protected Energy getEnergy() {
        return Helpers.getFieldValue(tile, Ic2Helpers.FIELD_TILEELECTRICMACHINE_ENERGY);
    }

    @Override
    public boolean hasWork() {
        return tile.getActive();
    }

    @Override
    public boolean canWork() {
        return tile.getActive();
    }

}
