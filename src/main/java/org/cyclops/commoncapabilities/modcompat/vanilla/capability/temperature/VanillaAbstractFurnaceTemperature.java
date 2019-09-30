package org.cyclops.commoncapabilities.modcompat.vanilla.capability.temperature;

import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;

/**
 * Temperature capability for vanilla furnace tile entities.
 * @author rubensworks
 */
public class VanillaAbstractFurnaceTemperature implements ITemperature {
    private final AbstractFurnaceTileEntity furnace;

    public VanillaAbstractFurnaceTemperature(AbstractFurnaceTileEntity furnace) {
        this.furnace = furnace;
    }

    @Override
    public double getTemperature() {
        return ITemperature.ZERO_CELCIUS + furnace.burnTime;
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
        return ITemperature.ZERO_CELCIUS;
    }
}
