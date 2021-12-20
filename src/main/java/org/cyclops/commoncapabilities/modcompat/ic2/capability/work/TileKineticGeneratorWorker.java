package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.api.energy.tile.IKineticSource;
import ic2.core.block.comp.Energy;
import ic2.core.block.generator.tileentity.TileEntityKineticGenerator;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;
import org.cyclops.cyclopscore.helper.TileHelpers;

import TileEntityKineticGenerator;

/**
 * Worker capability for {@link TileEntityKineticGenerator}.
 * @author rubensworks
 */
public class TileKineticGeneratorWorker implements IWorker {

    private final TileEntityKineticGenerator host;

    public TileKineticGeneratorWorker(TileEntityKineticGenerator host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        Energy energy = Helpers.getFieldValue(host, Ic2Helpers.FIELD_TILEKINETICGENERATOR_ENERGY);
        double production = Helpers.getFieldValue(host, Ic2Helpers.FIELD_TILEKINETICGENERATOR_PRODUCTION);
        return energy.getEnergy() + production <= energy.getCapacity();
    }

    @Override
    public boolean canWork() {
        return TileHelpers.getSafeTile(host.getWorld(), host.getPos().offset(host.getFacing()), IKineticSource.class) != null;
    }
}
