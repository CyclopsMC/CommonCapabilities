package org.cyclops.commoncapabilities;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesItemStackTag;
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
import org.cyclops.commoncapabilities.proxy.ClientProxy;
import org.cyclops.commoncapabilities.proxy.CommonProxy;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;

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

    public CommonCapabilities() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegister);
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
    protected ItemGroup constructDefaultItemGroup() {
        return null;
    }

    @Override
    protected void onConfigsRegister(ConfigHandler configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig());

        configHandler.addConfigurable(new WorkerConfig());
        configHandler.addConfigurable(new WrenchConfig());
        configHandler.addConfigurable(new TemperatureConfig());
        configHandler.addConfigurable(new InventoryStateConfig());
        configHandler.addConfigurable(new SlotlessItemHandlerConfig());
        configHandler.addConfigurable(new RecipeHandlerConfig());
        configHandler.addConfigurable(new IngredientComponentStorageHandlerConfig());
    }

    public void onRegister(RegistryEvent.Register event) {
        if (event.getRegistry() == IngredientComponent.REGISTRY) {
            IPrototypedIngredientAlternatives.SERIALIZERS.put(
                    PrototypedIngredientAlternativesList.SERIALIZER.getId(),
                    PrototypedIngredientAlternativesList.SERIALIZER);
            IPrototypedIngredientAlternatives.SERIALIZERS.put(
                    PrototypedIngredientAlternativesItemStackTag.SERIALIZER.getId(),
                    PrototypedIngredientAlternativesItemStackTag.SERIALIZER);

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
