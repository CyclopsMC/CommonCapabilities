package org.cyclops.commoncapabilities.capability.recipehandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.recipehandler.DefaultRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeComponent;
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
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegister(RegistryEvent.Register event) {
        if (event.getRegistry() == RecipeComponent.REGISTRY) {
            event.getRegistry().registerAll(
                    new RecipeComponent<>("minecraft:itemstack").setUnlocalizedName("recipecomponent.minecraft.itemstack"),
                    new RecipeComponent<>("minecraft:fluidstack").setUnlocalizedName("recipecomponent.minecraft.fluidstack"),
                    new RecipeComponent<>("minecraft:energy").setUnlocalizedName("recipecomponent.minecraft.energy")
            );
        }
    }
}
