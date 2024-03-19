package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.api.energy.tile.IHeatSource;
import ic2.core.block.comp.Energy;
import ic2.core.block.generator.tileentity.TileEntityStirlingGenerator;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;
import org.cyclops.cyclopscore.helper.TileHelpers;

import TileEntityStirlingGenerator;

/**
 * Worker capability for {@link TileEntityStirlingGenerator}.
 * @author rubensworks
 */
public class TileStirlingGeneratorWorker implements IWorker {

    private final TileEntityStirlingGenerator host;

    public TileStirlingGeneratorWorker(TileEntityStirlingGenerator host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        Energy energy = Helpers.getFieldValue(host, Ic2Helpers.FIELD_TILESTIRLINGGENERATOR_ENERGY);
        double production = Helpers.getFieldValue(host, Ic2Helpers.FIELD_TILESTIRLINGGENERATOR_PRODUCTION);
        return energy.getEnergy() + production <= energy.getCapacity();
    }

    @Override
    public boolean canWork() {
        return TileHelpers.getSafeTile(host.getWorld(), host.getPos().offset(host.getFacing()), IHeatSource.class) != null;
    }
}
