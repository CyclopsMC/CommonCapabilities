package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.heatgenerator.tileentity.TileEntitySolidHeatGenerator;
import ic2.core.item.type.MiscResourceType;
import ic2.core.ref.ItemName;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileEntitySolidHeatGenerator;

/**
 * Worker capability for {@link TileEntitySolidHeatGenerator}.
 * @author rubensworks
 */
public class TileSolidHeatGeneratorWorker implements IWorker {

    private final TileEntitySolidHeatGenerator host;

    public TileSolidHeatGeneratorWorker(TileEntitySolidHeatGenerator host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return host.outputslot.canAdd(ItemName.misc_resource.getItemStack(MiscResourceType.ashes));
    }
}
