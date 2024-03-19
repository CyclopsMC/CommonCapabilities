package org.cyclops.commoncapabilities.modcompat.forestry.capability.work;

import forestry.core.errors.EnumErrorCode;
import forestry.factory.tiles.TileRaintank;
import net.neoforged.neoforge.fluids.FluidRegistry;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * Worker capability for the TileRaintank.
 * @author rubensworks
 */
public class TileRaintankWorker implements IWorker {

    private static final FluidStack WATER = new FluidStack(FluidRegistry.WATER, 1);
    private final TileRaintank tile;

    public TileRaintankWorker(TileRaintank tile) {
        this.tile = tile;
    }

    @Override
    public boolean hasWork() {
        return tile.getTankManager().canFillFluidType(WATER);
    }

    @Override
    public boolean canWork() {
        return !tile.getErrorLogic().contains(EnumErrorCode.NO_RAIN_BIOME)
                && !tile.getErrorLogic().contains(EnumErrorCode.NO_SKY_RAIN_TANK)
                && !tile.getErrorLogic().contains(EnumErrorCode.NOT_RAINING);
    }
}
