package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.heatgenerator.tileentity.TileEntityFluidHeatGenerator;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileEntityFluidHeatGenerator;

/**
 * Worker capability for {@link TileEntityFluidHeatGenerator}.
 * @author rubensworks
 */
public class TileFluidHeatGeneratorWorker implements IWorker {

    private final TileEntityFluidHeatGenerator host;

    public TileFluidHeatGeneratorWorker(TileEntityFluidHeatGenerator host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return host.getTankAmount() > 0;
    }
}
