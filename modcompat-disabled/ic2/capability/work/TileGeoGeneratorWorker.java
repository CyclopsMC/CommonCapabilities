package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.generator.tileentity.TileEntityGeoGenerator;
import net.neoforged.neoforge.fluids.FluidTank;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;

/**
 * Worker capability for {@link TileEntityGeoGenerator}.
 * @author rubensworks
 */
public class TileGeoGeneratorWorker extends TileBaseGeneratorWorker<TileEntityGeoGenerator> {

    public TileGeoGeneratorWorker(TileEntityGeoGenerator host) {
        super(host);
    }

    @Override
    public boolean canWork() {
        FluidTank fluidTank = Helpers.getFieldValue(getTile(), Ic2Helpers.FIELD_TILEGEOGENERATOR_FLUIDTANK);
        return fluidTank.getFluidAmount() > 2;
    }
}
