package org.cyclops.commoncapabilities;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.Level;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.ingredient.NBTBaseComparator;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.IChangedCallback;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.nbt.path.NbtParseException;
import org.cyclops.cyclopscore.nbt.path.NbtPath;
import org.cyclops.cyclopscore.nbt.path.navigate.INbtPathNavigation;
import org.cyclops.cyclopscore.nbt.path.navigate.NbtPathNavigationList;
import org.cyclops.cyclopscore.tracking.Analytics;
import org.cyclops.cyclopscore.tracking.Versions;

import java.util.List;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfig {

    /**
     * The current mod version, will be used to check if the player's config isn't out of date and
     * warn the player accordingly.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "Config version for " + Reference.MOD_NAME +".\nDO NOT EDIT MANUALLY!", showInGui = false)
    public static String version = Reference.MOD_VERSION;

    /**
     * If the debug mode should be enabled. @see Debug
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "Set 'true' to enable development debug mode. This will result in a lower performance!", requiresMcRestart = true)
    public static boolean debug = false;

    /**
     * If the recipe loader should crash when finding invalid recipes.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If the recipe loader should crash when finding invalid recipes.", requiresMcRestart = true)
    public static boolean crashOnInvalidRecipe = false;

    /**
     * If mod compatibility loader should crash hard if errors occur in that process.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If mod compatibility loader should crash hard if errors occur in that process.", requiresMcRestart = true)
    public static boolean crashOnModCompatCrash = false;

    /**
     * If an anonymous mod startup analytics request may be sent to our analytics service.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If an anonymous mod startup analytics request may be sent to our analytics service.")
    public static boolean analytics = true;

    /**
     * If the version checker should be enabled.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If the version checker should be enabled.")
    public static boolean versionChecker = true;

    /**
     * The NBT Paths that should be filtered away when checking equality.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The NBT Paths that should be filtered away when checking equality.", changedCallback = IgnoreNbtPathsForEqualityChangedCallback.class)
    public static String[] ignoreNbtPathsForEqualityFilters = {
            "$.ForgeCaps[\"astralsorcery:cap_item_amulet_holder\"]", // Astral Sorcery
            "$.binding" // Blood Magic Blood Orb player bindings
    };

    /**
     * The type of this config.
     */
    public static ConfigurableType TYPE = ConfigurableType.DUMMY;

    /**
     * Create a new instance.
     */
    public GeneralConfig() {
        super(CommonCapabilities._instance, true, "general", null, GeneralConfig.class);
    }

    @Override
    public void onRegistered() {
        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_INVALID_RECIPE, GeneralConfig.crashOnInvalidRecipe);
        getMod().putGenericReference(ModBase.REFKEY_DEBUGCONFIG, GeneralConfig.debug);
        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_MODCOMPAT_CRASH, GeneralConfig.crashOnModCompatCrash);

        if(analytics) {
            Analytics.registerMod(getMod(), Reference.GA_TRACKING_ID);
        }
        if(versionChecker) {
            Versions.registerMod(getMod(), CommonCapabilities._instance, Reference.VERSION_URL);
        }
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }

    public static class IgnoreNbtPathsForEqualityChangedCallback implements IChangedCallback {

        @Override
        public void onChanged(Object value) {
            List<INbtPathNavigation> navigations = Lists.newArrayList();
            for (String path : (String[]) value) {
                try {
                    navigations.add(NbtPath.parse(path).asNavigation());
                } catch (NbtParseException e) {
                    CommonCapabilities.clog(Level.ERROR, String.format("Failed to parse NBT path to filter: %s", path));
                }
            }
            ItemMatch.NBT_COMPARATOR = NBTBaseComparator.INSTANCE = new NBTBaseComparator(new NbtPathNavigationList(navigations));
        }

        @Override
        public void onRegisteredPostInit(Object value) {

        }
    }
}
