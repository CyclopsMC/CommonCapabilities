package org.cyclops.commoncapabilities.capability.itemhandler;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ISlotlessItemHandler;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;

/**
 * Config for the slotless item handler capability.
 * @author rubensworks
 */
public class SlotlessItemHandlerConfig extends CapabilityConfig<ISlotlessItemHandler> {

    public static Capability<ISlotlessItemHandler> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public SlotlessItemHandlerConfig() {
        super(
                CommonCapabilities._instance,
                "slotlessItemHandler",
                ISlotlessItemHandler.class
        );
    }
}
