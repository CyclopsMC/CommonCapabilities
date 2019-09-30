package org.cyclops.commoncapabilities.capability.itemhandler;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.ItemStackHandler;
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

    @CapabilityInject(ISlotlessItemHandler.class)
    public static Capability<ISlotlessItemHandler> CAPABILITY = null;

    public SlotlessItemHandlerConfig() {
        super(
                CommonCapabilities._instance,
                "slotlessItemHandler",
                ISlotlessItemHandler.class,
                new DefaultCapabilityStorage<ISlotlessItemHandler>(),
                () -> new DefaultSlotlessItemHandlerWrapper(new ItemStackHandler())
        );
    }
}
