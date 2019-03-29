package org.cyclops.commoncapabilities.capability.wrench;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.wrench.DefaultWrench;
import org.cyclops.commoncapabilities.api.capability.wrench.IWrench;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityStorage;

/**
 * Config for the wrench capability.
 * @author rubensworks
 *
 */
public class WrenchConfig extends CapabilityConfig<IWrench> {

    /**
     * The unique instance.
     */
    public static WrenchConfig _instance;

    @CapabilityInject(IWrench.class)
    public static Capability<IWrench> CAPABILITY = null;

    /**
     * Make a new instance.
     */
    public WrenchConfig() {
        super(
                CommonCapabilities._instance,
                true,
                "wrench",
                "Indicates if something is a wrench",
                IWrench.class,
                new DefaultCapabilityStorage<IWrench>(),
                DefaultWrench.class
        );
    }
}
