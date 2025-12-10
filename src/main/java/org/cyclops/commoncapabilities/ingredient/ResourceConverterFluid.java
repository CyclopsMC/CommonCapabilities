package org.cyclops.commoncapabilities.ingredient;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.cyclops.commoncapabilities.api.ingredient.IResourceConverter;

/**
 * @author rubensworks
 */
public class ResourceConverterFluid implements IResourceConverter<FluidResource, FluidStack> {
    @Override
    public FluidStack fromResource(FluidResource resource, int amount) {
        return resource.toStack(amount);
    }

    @Override
    public FluidResource toResource(FluidStack ingredient) {
        return FluidResource.of(ingredient);
    }
}
