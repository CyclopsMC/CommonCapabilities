package org.cyclops.commoncapabilities.api.capability.fluidhandler;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

/**
 * Copy-pasted from Forge 1.12 and adapted.
 * If they re-add it, this can be removed.
 * @author rubensworks
 */
public class FluidHandlerConcatenate extends CombinedResourceHandler<FluidResource> {

    public FluidHandlerConcatenate(ResourceHandler<FluidResource>... subHandlers) {
        super(subHandlers);
    }

    public FluidHandlerConcatenate(ResourceHandler<FluidResource> subHandlers) {
        super(subHandlers);
    }

    public FluidStack getFluidInTank(int tank) {
        return getResource(tank).toStack(getAmountAsInt(tank));
    }
}
