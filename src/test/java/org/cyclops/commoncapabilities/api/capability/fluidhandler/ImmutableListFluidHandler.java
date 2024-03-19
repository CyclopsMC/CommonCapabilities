package org.cyclops.commoncapabilities.api.capability.fluidhandler;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.FluidHelpers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * A simple immutable list-based fluid handler.
 * @author rubensworks
 */
public class ImmutableListFluidHandler implements IFluidHandler {

    private final List<FluidStack> fluidStacks;

    public ImmutableListFluidHandler(List<FluidStack> fluidStacks) {
        this.fluidStacks = fluidStacks;
    }

    @Override
    public int getTanks() {
        return this.fluidStacks.size();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.fluidStacks.get(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return FluidHelpers.BUCKET_VOLUME;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }
}
