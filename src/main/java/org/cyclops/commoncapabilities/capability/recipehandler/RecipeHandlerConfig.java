package org.cyclops.commoncapabilities.capability.recipehandler;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;

/**
 * Config for the recipe handler capability.
 * @author rubensworks
 */
public class RecipeHandlerConfig extends CapabilityConfig<IRecipeHandler> {

    public static Capability<IRecipeHandler> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public RecipeHandlerConfig() {
        super(
                CommonCapabilities._instance,
                "recipeHandler",
                IRecipeHandler.class
        );
    }
}
