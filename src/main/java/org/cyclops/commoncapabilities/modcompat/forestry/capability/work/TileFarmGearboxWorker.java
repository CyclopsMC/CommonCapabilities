package org.cyclops.commoncapabilities.modcompat.forestry.capability.work;

import forestry.api.core.IErrorLogic;
import forestry.farming.tiles.TileFarmGearbox;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * Worker capability for the TileFarmGearbox.
 * @author rubensworks
 */
public class TileFarmGearboxWorker implements IWorker {

    // From TileFarmGearbox
    private static final int WORK_CYCLES = 4;
    private static final int ENERGY_PER_OPERATION = WORK_CYCLES * 50;

    private final TileFarmGearbox tile;

    public TileFarmGearboxWorker(TileFarmGearbox tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        IErrorLogic errorLogic = tile.getMultiblockLogic().getController().getErrorLogic();
        return !errorLogic.hasErrors();
    }

    @Override
    public boolean canWork() {
        return tile.getMultiblockLogic().getController().isAssembled()
                && tile.getEnergyManager().getEnergyStored() >= ENERGY_PER_OPERATION;
    }
}
