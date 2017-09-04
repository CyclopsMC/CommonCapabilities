package org.cyclops.commoncapabilities.capability.itemhandler;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.itemhandler.DefaultSlotlessItemHandlerWrapper;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ISlotlessItemHandler;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityStorage;

/**
 * Config for the slotless item handler capability.
 * @author rubensworks
 */
public class SlotlessItemHandlerConfig extends CapabilityConfig<ISlotlessItemHandler> {

    /**
     * The unique instance.
     */
    public static SlotlessItemHandlerConfig _instance;

    @CapabilityInject(ISlotlessItemHandler.class)
    public static Capability<ISlotlessItemHandler> CAPABILITY = null;

    /**
     * Make a new instance.
     */
    public SlotlessItemHandlerConfig() {
        super(
                CommonCapabilities._instance,
                true,
                "slotlessItemHandler",
                "An item handler that is slot agnostic",
                ISlotlessItemHandler.class,
                new DefaultCapabilityStorage<ISlotlessItemHandler>(),
                DefaultSlotlessItemHandlerWrapper.class
        );
    }
}
