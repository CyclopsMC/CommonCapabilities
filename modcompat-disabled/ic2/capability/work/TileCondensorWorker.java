package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityCondenser;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;

/**
 * Worker capability for {@link TileEntityCondenser}.
 * @author rubensworks
 */
public class TileCondensorWorker extends TileElectricMachineWorkerBase<TileEntityCondenser> {
    public TileCondensorWorker(TileEntityCondenser tile) {
        super(tile);
    }

    @Override
    public boolean hasWork() {
        return super.hasWork()
                || (Helpers.invokeMethod(getTile(), Ic2Helpers.METHOD_TILECONDENSER_PROCESSOUTPUTSLOT, true) != null
        && this.getTile().getInputTank().getFluidAmount() > 0 && this.getTile().getOutputTank().getCapacity() - this.getTile().getOutputTank().getFluidAmount() >= 100);
    }

    @Override
    public boolean canWork() {
        return super.canWork() || getEnergy().getEnergy() >= getTile().getVents() * 2;
    }
}
