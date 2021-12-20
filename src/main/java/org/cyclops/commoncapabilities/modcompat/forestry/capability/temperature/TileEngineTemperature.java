package org.cyclops.commoncapabilities.modcompat.forestry.capability.temperature;

import forestry.core.tiles.TileEngine;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;

import TileEngine;

/**
 * Temperature capability implementation for {@link forestry.core.tiles.TileEngine}.
 * @author rubensworks
 */
public class TileEngineTemperature implements ITemperature {

    private final TileEngine engine;
    private final double maximumTemperature;
    private final double minimumTemperature;
    private final double defaultTemperature;

    public TileEngineTemperature(TileEngine engine, double maximumTemperature, double minimumTemperature, double defaultTemperature) {
        this.engine = engine;
        this.maximumTemperature = maximumTemperature;
        this.minimumTemperature = minimumTemperature;
        this.defaultTemperature = defaultTemperature;
    }

    @Override
    public double getTemperature() {
        return ITemperature.ZERO_CELCIUS + engine.getHeat();
    }

    @Override
    public double getMaximumTemperature() {
        return maximumTemperature;
    }

    @Override
    public double getMinimumTemperature() {
        return minimumTemperature;
    }

    @Override
    public double getDefaultTemperature() {
        return defaultTemperature;
    }
}
