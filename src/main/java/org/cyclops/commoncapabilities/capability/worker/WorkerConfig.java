package org.cyclops.commoncapabilities.capability.worker;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;

/**
 * Config for the worker capability.
 * @author rubensworks
 *
 */
public class WorkerConfig extends CapabilityConfig<IWorker> {

    public static Capability<IWorker> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public WorkerConfig() {
        super(
                CommonCapabilities._instance,
                "worker",
                IWorker.class
        );
    }
}
