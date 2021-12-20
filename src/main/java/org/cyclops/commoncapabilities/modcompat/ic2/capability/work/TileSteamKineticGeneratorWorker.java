package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.kineticgenerator.tileentity.TileEntityManualKineticGenerator;
import ic2.core.block.kineticgenerator.tileentity.TileEntitySteamKineticGenerator;
import net.minecraftforge.fluids.FluidTank;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;

import TileEntitySteamKineticGenerator;

/**
 * Worker capability for {@link TileEntityManualKineticGenerator}.
 * @author rubensworks
 */
public class TileSteamKineticGeneratorWorker implements IWorker {

    private final TileEntitySteamKineticGenerator host;

    public TileSteamKineticGeneratorWorker(TileEntitySteamKineticGenerator host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        FluidTank steamTank = Helpers.getFieldValue(host, Ic2Helpers.FIELD_TILESTEAMKINETICGENERATOR_STEAMTANK);
        return steamTank.getFluidAmount() > 10 && (!(boolean) Helpers.getFieldValue(host, Ic2Helpers.FIELD_TILESTEAMKINETICGENERATOR_ISTURBINEFILLED))
                && !host.turbineSlot.isEmpty();
    }
}
