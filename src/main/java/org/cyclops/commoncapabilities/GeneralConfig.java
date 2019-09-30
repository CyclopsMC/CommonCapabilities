package org.cyclops.commoncapabilities;

import com.google.common.collect.Lists;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.ingredient.NBTComparator;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
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

    @ConfigurableProperty(category = "core", comment = "If the recipe loader should crash when finding invalid recipes.", requiresMcRestart = true, configLocation = ModConfig.Type.SERVER)
    public static boolean crashOnInvalidRecipe = false;

    @ConfigurableProperty(category = "core", comment = "If mod compatibility loader should crash hard if errors occur in that process.", requiresMcRestart = true, configLocation = ModConfig.Type.SERVER)
    public static boolean crashOnModCompatCrash = false;

    @ConfigurableProperty(category = "core", comment = "If an anonymous mod startup analytics request may be sent to our analytics service.")
    public static boolean analytics = true;

    @ConfigurableProperty(category = "core", comment = "If the version checker should be enabled.")
    public static boolean versionChecker = true;

    @ConfigurableProperty(category = "machine", comment = "The NBT Paths that should be filtered away when checking equality.", configLocation = ModConfig.Type.SERVER)
    public static List<String> ignoreNbtPathsForEqualityFilters = Lists.newArrayList(
            "$.ForgeCaps[\"astralsorcery:cap_item_amulet_holder\"]", // Astral Sorcery
            "$.binding" // Blood Magic Blood Orb player bindings
    );

    public GeneralConfig() {
        super(CommonCapabilities._instance, "general");
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
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
    public void onConfigLoad(ModConfig.ConfigReloading event) {
        updateNbtComparator();
    }

    @SubscribeEvent
    public void onConfigReload(ModConfig.ConfigReloading event) {
        updateNbtComparator();
    }

    protected void updateNbtComparator() {
        List<INbtPathNavigation> navigations = Lists.newArrayList();
        for (String path : ignoreNbtPathsForEqualityFilters) {
            try {
                navigations.add(NbtPath.parse(path).asNavigation());
            } catch (NbtParseException e) {
                CommonCapabilities.clog(Level.ERROR, String.format("Failed to parse NBT path to filter: %s", path));
            }
        }
        ItemMatch.NBT_COMPARATOR = NBTComparator.INSTANCE = new NBTComparator(new NbtPathNavigationList(navigations));
    }
}
