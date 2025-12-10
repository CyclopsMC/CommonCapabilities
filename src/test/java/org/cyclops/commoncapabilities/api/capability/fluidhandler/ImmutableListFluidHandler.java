package org.cyclops.commoncapabilities.api.capability.fluidhandler;

import net.minecraft.core.NonNullList;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

import java.util.List;

/**
 * A simple immutable list-based fluid handler.
 * @author rubensworks
 */
public class ImmutableListFluidHandler extends FluidStacksResourceHandler {

    public ImmutableListFluidHandler(List<FluidStack> fluidStacks) {
        super(NonNullList.copyOf(fluidStacks), fluidStacks.size());
    }
}
