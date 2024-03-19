package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.comp.Energy;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;

/**
 * Worker capability for TileEntityBaseGenerator.
 * @author rubensworks
 */
public class TileBaseGeneratorWorker<T extends TileEntityBaseGenerator> implements IWorker {

    private final T tile;

    public TileBaseGeneratorWorker(T tile) {
        this.tile = tile;
    }

    protected T getTile() {
        return this.tile;
    }

    @Override
    public boolean hasWork() {
        Energy energy = Helpers.getFieldValue(tile, Ic2Helpers.FIELD_TILEBASEGENERATOR_ENERGY);
        double production = Helpers.getFieldValue(tile, Ic2Helpers.FIELD_TILEBASEGENERATOR_PRODUCTION);
        return energy.getEnergy() + production <= energy.getCapacity();
    }

    @Override
    public boolean canWork() {
        return tile.fuel > 0;
    }

}
