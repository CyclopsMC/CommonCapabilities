package org.cyclops.commoncapabilities.modcompat.enderio.capability.work;

import crazypants.enderio.machine.AbstractPoweredTaskEntity;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.modcompat.enderio.EnderIOHelpers;

import AbstractPoweredTaskEntity;

/**
 * Worker capability for AbstractPoweredTaskEntity machines.
 * @author rubensworks
 */
public class AbstractPoweredTaskEntityWorker implements IWorker {

    private final AbstractPoweredTaskEntity tile;

    public AbstractPoweredTaskEntityWorker(AbstractPoweredTaskEntity tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return tile.getCurrentTask() != null || EnderIOHelpers.getCachedMachineRecipe(tile) != null;
    }

    @Override
    public boolean canWork() {
        return tile.hasPower() && tile.getRedstoneChecksPassed();
    }

}
