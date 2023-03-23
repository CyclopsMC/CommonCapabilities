package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.machine.tileentity.TileEntityFluidRegulator;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.TileHelpers;

/**
 * Worker capability for {@link TileEntityFluidRegulator}.
 * @author rubensworks
 */
public class TileFluidRegulatorWorker extends TileElectricMachineWorkerBase<TileEntityFluidRegulator> {

    public TileFluidRegulatorWorker(TileEntityFluidRegulator host) {
        super(host);
    }

    @Override
    public boolean hasWork() {
        return super.hasWork() || getTile().getFluidTank().getFluidAmount() > 0;
    }

    @Override
    public boolean canWork() {
        if (super.canWork()) {
            return true;
        }
        if (getEnergy().getEnergy() < 10.0D) {
            return false;
        }
        TileEntityFluidRegulator host = getTile();
        for (EnumFacing facing : EnumFacing.VALUES) {
            IFluidHandler fluidHandler = TileHelpers.getCapability(host.getWorld(), host.getPos().offset(facing), facing.getOpposite(), ForgeCapabilities.FLUID_HANDLER);
            if (fluidHandler.fill(host.getFluidTank().getFluid(), false) > 0) {
                return true;
            }
        }
        return false;
    }
}
