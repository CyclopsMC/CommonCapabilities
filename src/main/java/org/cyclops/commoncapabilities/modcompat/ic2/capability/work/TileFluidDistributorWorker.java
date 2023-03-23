package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityFluidDistributor;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.ic2.Ic2Helpers;
import org.cyclops.cyclopscore.helper.TileHelpers;

import TileEntityFluidDistributor;

/**
 * Worker capability for {@link TileEntityFluidDistributor}.
 * @author rubensworks
 */
public class TileFluidDistributorWorker implements IWorker {

    private final TileEntityFluidDistributor host;

    public TileFluidDistributorWorker(TileEntityFluidDistributor host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        FluidTank fluidTank = Helpers.getFieldValue(host, Ic2Helpers.FIELD_TILESFLUIDDISTRIBUTOR_FLUIDTANK);
        return host.getActive() || fluidTank.getFluidAmount() > 0;
    }

    @Override
    public boolean canWork() {
        if (host.getActive()) {
            return true;
        }
        for (EnumFacing facing : EnumFacing.VALUES) {
            IFluidHandler fluidHandler = TileHelpers.getCapability(host.getWorld(), host.getPos().offset(facing), facing.getOpposite(), ForgeCapabilities.FLUID_HANDLER);
            FluidTank fluidTank = Helpers.getFieldValue(host, Ic2Helpers.FIELD_TILESFLUIDDISTRIBUTOR_FLUIDTANK);
            if (fluidHandler != null && fluidHandler.fill(fluidTank.getFluid(), false) > 0) {
                return true;
            }
        }
        return false;
    }
}
