package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.generator.tileentity.TileEntitySemifluidGenerator;
import net.neoforged.neoforge.fluids.FluidTank;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;

/**
 * Worker capability for {@link TileEntitySemifluidGenerator}.
 * @author rubensworks
 */
public class TileSemifluidGeneratorWorker extends TileBaseGeneratorWorker<TileEntitySemifluidGenerator> {

    public TileSemifluidGeneratorWorker(TileEntitySemifluidGenerator host) {
        super(host);
    }

    @Override
    public boolean canWork() {
        FluidTank fluidTank = Helpers.getFieldValue(getTile(), Ic2Helpers.FIELD_TILESEMIFLUIDGENERATOR_FLUIDTANK);
        return fluidTank.getFluidAmount() > 0;
    }
}
