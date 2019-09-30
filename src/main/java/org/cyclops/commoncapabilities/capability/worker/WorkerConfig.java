package org.cyclops.commoncapabilities.capability.worker;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.work.DefaultWorker;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityStorage;

/**
 * Config for the worker capability.
 * @author rubensworks
 *
 */
public class WorkerConfig extends CapabilityConfig<IWorker> {

    @CapabilityInject(IWorker.class)
    public static Capability<IWorker> CAPABILITY = null;

    public WorkerConfig() {
        super(
                CommonCapabilities._instance,
                "worker",
                IWorker.class,
                new DefaultCapabilityStorage<IWorker>(),
                DefaultWorker::new
        );
    }
}
