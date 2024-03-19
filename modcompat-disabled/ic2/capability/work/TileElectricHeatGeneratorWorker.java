package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.heatgenerator.tileentity.TileEntityElectricHeatGenerator;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileEntityElectricHeatGenerator;

/**
 * Worker capability for {@link TileEntityElectricHeatGenerator}.
 * @author rubensworks
 */
public class TileElectricHeatGeneratorWorker implements IWorker {

    private final TileEntityElectricHeatGenerator host;

    public TileElectricHeatGeneratorWorker(TileEntityElectricHeatGenerator host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return host.getMaxHeatEmittedPerTick() > 0 && host.getChargeLevel() > 0;
    }
}
