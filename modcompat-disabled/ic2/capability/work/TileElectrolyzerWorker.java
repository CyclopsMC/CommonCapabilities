package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityElectrolyzer;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;

/**
 * Worker capability for {@link TileEntityElectrolyzer}.
 * @author rubensworks
 */
public class TileElectrolyzerWorker extends TileElectricMachineWorkerBase<TileEntityElectrolyzer> {
    public TileElectrolyzerWorker(TileEntityElectrolyzer tile) {
        super(tile);
    }

    @Override
    public boolean hasWork() {
        return super.hasWork() || ((Boolean) Helpers.invokeMethod(getTile(), Ic2Helpers.METHOD_TILEELETROLYZER_CANOPERATE));
    }

    @Override
    public boolean canWork() {
        return super.canWork() || (getEnergy().getEnergy() >= 32);
    }
}
