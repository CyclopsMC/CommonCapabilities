package org.cyclops.commoncapabilities.capability.temperature;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;

/**
 * Config for the temperature capability.
 * @author rubensworks
 *
 */
public class TemperatureConfig extends CapabilityConfig<ITemperature> {

    public static TemperatureConfig _instance;

    public static Capability<ITemperature> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public TemperatureConfig() {
        super(
                CommonCapabilities._instance,
                "temperature",
                ITemperature.class
        );
    }
}
