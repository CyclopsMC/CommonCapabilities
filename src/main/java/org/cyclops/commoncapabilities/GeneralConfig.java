package org.cyclops.commoncapabilities;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.ingredient.DataComparator;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.tracking.Analytics;
import org.cyclops.cyclopscore.tracking.Versions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfig {

    @ConfigurableProperty(category = "core", comment = "If the recipe loader should crash when finding invalid recipes.", requiresMcRestart = true, configLocation = ModConfig.Type.SERVER)
    public static boolean crashOnInvalidRecipe = false;

    @ConfigurableProperty(category = "core", comment = "If mod compatibility loader should crash hard if errors occur in that process.", requiresMcRestart = true, configLocation = ModConfig.Type.SERVER)
    public static boolean crashOnModCompatCrash = false;

    @ConfigurableProperty(category = "core", comment = "If an anonymous mod startup analytics request may be sent to our analytics service.")
    public static boolean analytics = true;

    @ConfigurableProperty(category = "core", comment = "If the version checker should be enabled.")
    public static boolean versionChecker = true;

    @ConfigurableProperty(category = "machine", comment = "The data component types that should be filtered away when checking equality.", configLocation = ModConfig.Type.SERVER)
    public static List<String> ignoreDataComponentsForEqualityFilters = Lists.newArrayList(
            "integrateddynamics:energy" // Integrated Dynamics batteries
    );

    public GeneralConfig() {
        super(CommonCapabilities._instance, "general");
        CommonCapabilities._instance.getModEventBus().register(this);
    }

    @Override
    public void onRegistered() {
        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_INVALID_RECIPE, GeneralConfig.crashOnInvalidRecipe);
        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_MODCOMPAT_CRASH, GeneralConfig.crashOnModCompatCrash);

        if(analytics) {
            Analytics.registerMod(getMod(), Reference.GA_TRACKING_ID);
        }
        if(versionChecker) {
            Versions.registerMod(getMod(), CommonCapabilities._instance, Reference.VERSION_URL);
        }
    }

    @SubscribeEvent
    public void onConfigLoad(ModConfigEvent.Loading event) {
        updateNbtComparator();
    }

    @SubscribeEvent
    public void onConfigReload(ModConfigEvent.Reloading event) {
        updateNbtComparator();
    }

    protected void updateNbtComparator() {
        ItemMatch.DATA_COMPARATOR = DataComparator.INSTANCE = new DataComparator(ignoreDataComponentsForEqualityFilters.stream().map(ResourceLocation::parse).collect(Collectors.toSet()));
    }
}
