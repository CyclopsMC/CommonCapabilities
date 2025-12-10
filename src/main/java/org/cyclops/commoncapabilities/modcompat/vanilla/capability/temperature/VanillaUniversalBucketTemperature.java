package org.cyclops.commoncapabilities.modcompat.vanilla.capability.temperature;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;

/**
 * Temperature capability for the vanilla furnace tile entity.
 * @author rubensworks
 */
public class VanillaUniversalBucketTemperature implements ITemperature {
    private final ItemStack itemStack;

    public VanillaUniversalBucketTemperature(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    protected FluidStack getFluidStack() {
        return FluidUtil.getFirstStackContained(itemStack);
    }

    @Override
    public double getTemperature() {
        FluidStack fluidStack = getFluidStack();
        if (fluidStack == null) {
            return ITemperature.ZERO_CELCIUS;
        }
        return fluidStack.getFluid().getFluidType().getTemperature(fluidStack);
    }

    @Override
    public double getMaximumTemperature() {
        return Double.MAX_VALUE;
    }

    @Override
    public double getMinimumTemperature() {
        return ITemperature.ZERO_CELCIUS;
    }

    @Override
    public double getDefaultTemperature() {
        FluidStack fluidStack = getFluidStack();
        if (fluidStack == null) {
            return ITemperature.ZERO_CELCIUS;
        }
        return fluidStack.getFluid().getFluidType().getTemperature();
    }
}
