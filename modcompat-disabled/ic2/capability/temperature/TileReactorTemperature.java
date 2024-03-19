package org.cyclops.commoncapabilities.modcompat.ic2.capability.temperature;

import ic2.api.reactor.IReactor;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;

import IReactor;

/**
 * Temperature capability for {@link IReactor}.
 * @author rubensworks
 */
public class TileReactorTemperature implements ITemperature {

    private final IReactor tile;

    public TileReactorTemperature(IReactor tile) {
        this.tile = tile;
    }

    @Override
    public double getTemperature() {
        return ITemperature.ZERO_CELCIUS + tile.getHeat();
    }

    @Override
    public double getMaximumTemperature() {
        return ITemperature.ZERO_CELCIUS + tile.getMaxHeat();
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
