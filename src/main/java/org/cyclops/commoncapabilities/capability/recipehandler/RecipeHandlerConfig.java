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

    /**
     * The unique instance.
     */
    public static RecipeHandlerConfig _instance;

    @CapabilityInject(IRecipeHandler.class)
    public static Capability<IRecipeHandler> CAPABILITY = null;

    /**
     * Make a new instance.
     */
    public RecipeHandlerConfig() {
        super(
                CommonCapabilities._instance,
                true,
                "recipeHandler",
                "Something that is able to process recipes",
                IRecipeHandler.class,
                new DefaultCapabilityStorage<IRecipeHandler>(),
                DefaultRecipeHandler.class
        );
    }
}
