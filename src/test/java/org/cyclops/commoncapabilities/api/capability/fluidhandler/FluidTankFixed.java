package org.cyclops.commoncapabilities.api.capability.fluidhandler;

import net.minecraft.core.Holder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * A temporary override of FluidTank with a fix for Forge PR 6196.
 * @author rubensworks
 */
public class FluidTankFixed extends FluidTank { // TODO: rm when Forge fixes the bug
    public FluidTankFixed(int capacity) {
        super(capacity);
    }

    public FluidTankFixed(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int drained = maxDrain;
        if (fluid.getAmount() < drained)
        {
            drained = fluid.getAmount();
        }
        FluidStack stack = new FluidStack(Holder.direct(fluid.getFluid()), drained, fluid.getComponentsPatch());
        if (action.execute() && drained > 0)
        {
            fluid.shrink(drained);
        }
        return stack;
    }
}
