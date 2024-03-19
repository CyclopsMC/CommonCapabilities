package org.cyclops.commoncapabilities;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesItemStackTag;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesList;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.modcompat.vanilla.VanillaModCompat;
import org.cyclops.commoncapabilities.proxy.ClientProxy;
import org.cyclops.commoncapabilities.proxy.CommonProxy;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;

import java.util.Objects;

/**
 * The main mod class of this mod.
 * @author rubensworks (aka kroeserr)
 *
 */
@Mod(Reference.MOD_ID)
public class CommonCapabilities extends ModBaseVersionable<CommonCapabilities> {

    /**
     * The unique instance of this mod.
     */
    public static CommonCapabilities _instance;

    public CommonCapabilities(IEventBus modEventBus) {
        super(Reference.MOD_ID, (instance) -> _instance = instance, modEventBus);
        modEventBus.register(IngredientComponent.class);
        modEventBus.addListener(EventPriority.LOW, this::onRegister);
        modEventBus.addListener(EventPriority.LOW, this::onRegistriesLoad);
        modEventBus.addListener(EventPriority.LOW, this::afterCapabilitiesLoaded);
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        super.setup(event);
        Objects.requireNonNull(IngredientComponent.ITEMSTACK, "Item ingredient component is not initialized");
        Objects.requireNonNull(IngredientComponent.FLUIDSTACK, "Fluid ingredient component is not initialized");
        Objects.requireNonNull(IngredientComponent.ENERGY, "Energy ingredient component is not initialized");
    }

    @Override
    protected void loadModCompats(ModCompatLoader modCompatLoader) {
        super.loadModCompats(modCompatLoader);
        modCompatLoader.addModCompat(new VanillaModCompat());
        // TODO: temporarily disable some mod compats
        //modCompatLoader.addModCompat(new TConstructModCompat());
        //modCompatLoader.addModCompat(new ForestryModCompat());
        //modCompatLoader.addModCompat(new ThermalExpansionModCompat());
        //modCompatLoader.addModCompat(new EnderIOModCompat());
        //modCompatLoader.addModCompat(new Ic2ModCompat());
    }

    @Override
    protected IClientProxy constructClientProxy() {
        return new ClientProxy();
    }

    @Override
    protected ICommonProxy constructCommonProxy() {
        return new CommonProxy();
    }

    @Override
    protected boolean hasDefaultCreativeModeTab() {
        return false;
    }

    @Override
    protected void onConfigsRegister(ConfigHandler configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig());
    }

    public void onRegister(NewRegistryEvent event) {
        IPrototypedIngredientAlternatives.SERIALIZERS.put(
                PrototypedIngredientAlternativesList.SERIALIZER.getId(),
                PrototypedIngredientAlternativesList.SERIALIZER);
        IPrototypedIngredientAlternatives.SERIALIZERS.put(
                PrototypedIngredientAlternativesItemStackTag.SERIALIZER.getId(),
                PrototypedIngredientAlternativesItemStackTag.SERIALIZER);
    }

    public void onRegistriesLoad(RegisterEvent event) {
        event.register(IngredientComponent.REGISTRY.key(), IngredientComponents.ITEMSTACK.getName(), () -> IngredientComponents.ITEMSTACK);
        event.register(IngredientComponent.REGISTRY.key(), IngredientComponents.FLUIDSTACK.getName(), () -> IngredientComponents.FLUIDSTACK);
        event.register(IngredientComponent.REGISTRY.key(), IngredientComponents.ENERGY.getName(), () -> IngredientComponents.ENERGY);
    }

    public void afterCapabilitiesLoaded(InterModEnqueueEvent event) {
        IngredientComponents.registerStorageWrapperHandlers();
    }

    /**
     * Log a new info message for this mod.
     * @param message The message to show.
     */
    public static void clog(String message) {
        clog(Level.INFO, message);
    }

    /**
     * Log a new message of the given level for this mod.
     * @param level The level in which the message must be shown.
     * @param message The message to show.
     */
    public static void clog(Level level, String message) {
        CommonCapabilities._instance.getLoggerHelper().log(level, message);
    }

}
