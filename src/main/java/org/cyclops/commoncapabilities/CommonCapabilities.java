package org.cyclops.commoncapabilities;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesItemStackOredictionary;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesList;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.capability.ingredient.storage.IngredientComponentStorageHandlerConfig;
import org.cyclops.commoncapabilities.capability.inventorystate.InventoryStateConfig;
import org.cyclops.commoncapabilities.capability.itemhandler.SlotlessItemHandlerConfig;
import org.cyclops.commoncapabilities.capability.recipehandler.RecipeHandlerConfig;
import org.cyclops.commoncapabilities.capability.temperature.TemperatureConfig;
import org.cyclops.commoncapabilities.capability.worker.WorkerConfig;
import org.cyclops.commoncapabilities.capability.wrench.WrenchConfig;
import org.cyclops.commoncapabilities.modcompat.vanilla.VanillaModCompat;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.proxy.ICommonProxy;

/**
 * The main mod class of this mod.
 * @author rubensworks (aka kroeserr)
 *
 */
@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        useMetadata = true,
        version = Reference.MOD_VERSION,
        dependencies = Reference.MOD_DEPENDENCIES,
        guiFactory = "org.cyclops.commoncapabilities.GuiConfigOverview$ExtendedConfigGuiFactory",
        certificateFingerprint = Reference.MOD_FINGERPRINT
)
public class CommonCapabilities extends ModBaseVersionable {
    
    /**
     * The proxy of this mod, depending on 'side' a different proxy will be inside this field.
     * @see SidedProxy
     */
    @SidedProxy(clientSide = "org.cyclops.commoncapabilities.proxy.ClientProxy", serverSide = "org.cyclops.commoncapabilities.proxy.CommonProxy")
    public static ICommonProxy proxy;
    
    /**
     * The unique instance of this mod.
     */
    @Instance(value = Reference.MOD_ID)
    public static CommonCapabilities _instance;

    public CommonCapabilities() {
        super(Reference.MOD_ID, Reference.MOD_NAME, Reference.MOD_VERSION);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected RecipeHandler constructRecipeHandler() {
        return new RecipeHandler(this);
    }

    @Override
    protected void loadModCompats(ModCompatLoader modCompatLoader) {
        super.loadModCompats(modCompatLoader);
        modCompatLoader.addModCompat(new VanillaModCompat());
        // TODO: temporarily disable some mod compats
        //modCompatLoader.addModCompat(new EnderIOModCompat());
        //modCompatLoader.addModCompat(new TConstructModCompat());
        //modCompatLoader.addModCompat(new ForestryModCompat());
        //modCompatLoader.addModCompat(new Ic2ModCompat());
    }

    /**
     * The pre-initialization, will register required configs.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }
    
    /**
     * Register the config dependent things like world generation and proxy handlers.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }
    
    /**
     * Register the event hooks.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
    
    /**
     * Register the things that are related to server starting, like commands.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        super.onServerStarting(event);
    }

    /**
     * Register the things that are related to server starting.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void onServerStarted(FMLServerStartedEvent event) {
        super.onServerStarted(event);
    }

    /**
     * Register the things that are related to server stopping, like persistent storage.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void onServerStopping(FMLServerStoppingEvent event) {
        super.onServerStopping(event);
    }

    @Override
    public CreativeTabs constructDefaultCreativeTab() {
        return null;
    }

    @Override
    public void onGeneralConfigsRegister(ConfigHandler configHandler) {
        configHandler.add(new GeneralConfig());
    }

    @Override
    public void onMainConfigsRegister(ConfigHandler configHandler) {
        super.onMainConfigsRegister(configHandler);
        configHandler.add(new WorkerConfig());
        configHandler.add(new WrenchConfig());
        configHandler.add(new TemperatureConfig());
        configHandler.add(new InventoryStateConfig());
        configHandler.add(new SlotlessItemHandlerConfig());
        configHandler.add(new RecipeHandlerConfig());
        configHandler.add(new IngredientComponentStorageHandlerConfig());
    }

    @Override
    public ICommonProxy getProxy() {
        return proxy;
    }

    @SubscribeEvent
    public void onRegister(RegistryEvent.Register event) {
        if (event.getRegistry() == IngredientComponent.REGISTRY) {
            IPrototypedIngredientAlternatives.SERIALIZERS.put(
                    PrototypedIngredientAlternativesList.SERIALIZER.getId(),
                    PrototypedIngredientAlternativesList.SERIALIZER);
            IPrototypedIngredientAlternatives.SERIALIZERS.put(
                    PrototypedIngredientAlternativesItemStackOredictionary.SERIALIZER.getId(),
                    PrototypedIngredientAlternativesItemStackOredictionary.SERIALIZER);

            event.getRegistry().registerAll(
                    IngredientComponents.ITEMSTACK,
                    IngredientComponents.FLUIDSTACK,
                    IngredientComponents.ENERGY
            );
        }
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
