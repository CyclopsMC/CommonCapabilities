package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityFluidDistributor;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.helper.TileHelpers;

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
        return host.getActive() || host.getTankAmount() > 0;
    }

    @Override
    public boolean canWork() {
        if (host.getActive()) {
            return true;
        }
        for (EnumFacing facing : EnumFacing.VALUES) {
            IFluidHandler fluidHandler = TileHelpers.getCapability(host.getWorld(), host.getPos().offset(facing), facing.getOpposite(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
            if (fluidHandler != null && fluidHandler.fill(host.getFluidStackfromTank(), false) > 0) {
                return true;
            }
        }
        return false;
    }
}
