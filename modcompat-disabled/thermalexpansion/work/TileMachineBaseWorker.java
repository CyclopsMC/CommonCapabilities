package org.cyclops.commoncapabilities.modcompat.thermalexpansion.work;

import cofh.thermalexpansion.block.machine.TileMachineBase;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.thermalexpansion.ThermalExpansionHelpers;

import TileMachineBase;

/**
 * Worker capability for TileMachineBase machines.
 * @author rubensworks
 */
public class TileMachineBaseWorker implements IWorker {

    private final TileMachineBase tile;

    public TileMachineBaseWorker(TileMachineBase tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return Helpers.invokeMethod(tile, ThermalExpansionHelpers.METHOD_TILEMACHINEBASE_HASVALIDINPUT);
    }

    @Override
    public boolean canWork() {
        return tile.getEnergyStorage().getEnergyStored() > 0;
    }

}
