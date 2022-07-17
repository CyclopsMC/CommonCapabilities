package org.cyclops.commoncapabilities.modcompat.vanilla.capability.temperature;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
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
        return FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY);
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
        return 0;
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
