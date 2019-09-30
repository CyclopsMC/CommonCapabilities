package org.cyclops.commoncapabilities.capability.recipehandler;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.recipehandler.DefaultRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityStorage;

/**
 * Config for the recipe handler capability.
 * @author rubensworks
 */
public class RecipeHandlerConfig extends CapabilityConfig<IRecipeHandler> {

    @CapabilityInject(IRecipeHandler.class)
    public static Capability<IRecipeHandler> CAPABILITY = null;

    public RecipeHandlerConfig() {
        super(
                CommonCapabilities._instance,
                "recipeHandler",
                IRecipeHandler.class,
                new DefaultCapabilityStorage<IRecipeHandler>(),
                DefaultRecipeHandler::new
        );
    }
}
