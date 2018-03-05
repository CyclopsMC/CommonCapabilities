package org.cyclops.commoncapabilities.api.capability.fluidhandler;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A simple immutable list-based fluid handler.
 * @author rubensworks
 */
public class ImmutableListFluidHandler implements IFluidHandler {

    private final IFluidTankProperties[] properties;

    public ImmutableListFluidHandler(List<FluidStack> fluidStacks) {
        this.properties = new IFluidTankProperties[fluidStacks.size()];
        for (int i = 0; i < fluidStacks.size(); i++) {
            this.properties[i] = new FluidTankProperties(fluidStacks.get(i), 1000);
        }
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return this.properties;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }
}
