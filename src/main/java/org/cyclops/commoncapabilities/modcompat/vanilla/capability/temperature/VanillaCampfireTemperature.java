package org.cyclops.commoncapabilities.modcompat.vanilla.capability.temperature;

import net.minecraft.block.CampfireBlock;
import net.minecraft.tileentity.CampfireTileEntity;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;

/**
 * Temperature capability for vanilla campfire tile entities.
 * @author rubensworks
 */
public class VanillaCampfireTemperature implements ITemperature {
    private final CampfireTileEntity campfire;

    public VanillaCampfireTemperature(CampfireTileEntity campfire) {
        this.campfire = campfire;
    }

    @Override
    public double getTemperature() {
        return campfire.getBlockState().getValue(CampfireBlock.LIT) ? getMaximumTemperature() : getDefaultTemperature();
    }

    @Override
    public double getMaximumTemperature() {
        return ITemperature.ZERO_CELCIUS + 1100;
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
