package org.cyclops.commoncapabilities.capability.inventorystate;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.inventorystate.IInventoryState;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;

/**
 * Config for the inventory state capability.
 * @author rubensworks
 */
public class InventoryStateConfig extends CapabilityConfig<IInventoryState> {

    public static Capability<IInventoryState> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public InventoryStateConfig() {
        super(
                CommonCapabilities._instance,
                "inventoryState",
                IInventoryState.class
        );
    }
}
