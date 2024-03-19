package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.generator.tileentity.TileEntitySolarGenerator;

/**
 * Worker capability for TileEntitySolarGenerator.
 * @author rubensworks
 */
public class TileSolarGeneratorWorker extends TileBaseGeneratorWorker<TileEntitySolarGenerator> {

    public TileSolarGeneratorWorker(TileEntitySolarGenerator tile) {
        super(tile);
    }

    @Override
    public boolean canWork() {
        return getTile().skyLight > 0;
    }

}
