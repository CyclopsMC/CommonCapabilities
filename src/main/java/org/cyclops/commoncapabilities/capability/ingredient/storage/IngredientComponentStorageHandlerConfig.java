package org.cyclops.commoncapabilities.capability.ingredient.storage;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageHandler;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;

/**
 * Config for the {@link IIngredientComponentStorageHandler} capability.
 * @author rubensworks
 */
public class IngredientComponentStorageHandlerConfig extends CapabilityConfig<IIngredientComponentStorageHandler> {

    public static Capability<IIngredientComponentStorageHandler> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public IngredientComponentStorageHandlerConfig() {
        super(
                CommonCapabilities._instance,
                "inventoryState",
                IIngredientComponentStorageHandler.class
        );
    }
}
