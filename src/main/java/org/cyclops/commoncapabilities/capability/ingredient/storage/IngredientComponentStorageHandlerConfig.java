package org.cyclops.commoncapabilities.capability.ingredient.storage;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.ingredient.storage.DefaultIngredientComponentStorageHandler;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageHandler;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityStorage;

/**
 * Config for the {@link IIngredientComponentStorageHandler} capability.
 * @author rubensworks
 */
public class IngredientComponentStorageHandlerConfig extends CapabilityConfig<IIngredientComponentStorageHandler> {

    @CapabilityInject(IIngredientComponentStorageHandler.class)
    public static Capability<IIngredientComponentStorageHandler> CAPABILITY = null;

    public IngredientComponentStorageHandlerConfig() {
        super(
                CommonCapabilities._instance,
                "inventoryState",
                IIngredientComponentStorageHandler.class,
                new DefaultCapabilityStorage<IIngredientComponentStorageHandler>(),
                DefaultIngredientComponentStorageHandler::new
        );
    }
}
