package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntitySolarDestiller;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;

import TileEntitySolarDestiller;

/**
 * Worker capability for {@link TileEntitySolarDestiller}.
 * @author rubensworks
 */
public class TileSolarDistillerWorker implements IWorker {

    private final TileEntitySolarDestiller host;

    public TileSolarDistillerWorker(TileEntitySolarDestiller host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        return host.getActive() || (host.inputTank.getFluidAmount() > 0 && host.outputTank.getFluidAmount() < host.outputTank.getCapacity());
    }

    @Override
    public boolean canWork() {
        return host.getActive() || (((float) Helpers.getFieldValue(host, Ic2Helpers.FIELD_TILESOLARDISTILLER_SKYLIGHT)) > 0.5D);
    }

}
