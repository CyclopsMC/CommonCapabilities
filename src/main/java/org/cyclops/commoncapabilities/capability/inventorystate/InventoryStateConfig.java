package org.cyclops.commoncapabilities.capability.inventorystate;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.inventorystate.IInventoryState;
import org.cyclops.commoncapabilities.api.capability.inventorystate.ItemHandlerModifiableInventoryState;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityStorage;

/**
 * Config for the inventory state capability.
 * @author rubensworks
 */
public class InventoryStateConfig extends CapabilityConfig<IInventoryState> {

    /**
     * The unique instance.
     */
    public static InventoryStateConfig _instance;

    @CapabilityInject(IInventoryState.class)
    public static Capability<IInventoryState> CAPABILITY = null;

    /**
     * Make a new instance.
     */
    public InventoryStateConfig() {
        super(
                CommonCapabilities._instance,
                true,
                "inventoryState",
                "Represents the current state of an inventory",
                IInventoryState.class,
                new DefaultCapabilityStorage<IInventoryState>(),
                ItemHandlerModifiableInventoryState.class
        );
    }
}
