package org.cyclops.commoncapabilities.capability.ingredient.storage;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.inventorystate.IInventoryState;
import org.cyclops.commoncapabilities.api.capability.inventorystate.ItemHandlerModifiableInventoryState;
import org.cyclops.commoncapabilities.api.ingredient.storage.DefaultIngredientComponentStorageHandler;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageHandler;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityStorage;

/**
 * Config for the {@link IIngredientComponentStorageHandler} capability.
 * @author rubensworks
 */
public class IngredientComponentStorageHandlerConfig extends CapabilityConfig<IIngredientComponentStorageHandler> {

    /**
     * The unique instance.
     */
    public static IngredientComponentStorageHandlerConfig _instance;

    @CapabilityInject(IIngredientComponentStorageHandler.class)
    public static Capability<IIngredientComponentStorageHandler> CAPABILITY = null;

    /**
     * Make a new instance.
     */
    public IngredientComponentStorageHandlerConfig() {
        super(
                CommonCapabilities._instance,
                true,
                "inventoryState",
                "Holds ingredient component storages",
                IIngredientComponentStorageHandler.class,
                new DefaultCapabilityStorage<IIngredientComponentStorageHandler>(),
                DefaultIngredientComponentStorageHandler.class
        );
    }
}
