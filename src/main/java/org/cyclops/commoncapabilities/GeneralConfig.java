package org.cyclops.commoncapabilities;

import com.google.common.collect.Lists;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.ingredient.DataComparator;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfigCommon;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfigCommon<CommonCapabilities> {

    @ConfigurablePropertyCommon(category = "machine", comment = "The data component types that should be filtered away when checking equality.", configLocation = ModConfigLocation.SERVER)
    public static List<String> ignoreDataComponentsForEqualityFilters = Lists.newArrayList();

    @ConfigurablePropertyCommon(category = "core", comment = "Only enable this if you know what you are doing, as this will cause lag! This will log checks to the DataComparator where equal items have unequal data components.")
    public static boolean debugLogUnequalItemDataComponents = false;

    public GeneralConfig() {
        super(CommonCapabilities._instance, "general");
        CommonCapabilities._instance.getModEventBus().register(this);
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
        ItemMatch.DATA_COMPARATOR = DataComparator.INSTANCE = new DataComparator(ignoreDataComponentsForEqualityFilters.stream().map(Identifier::parse).collect(Collectors.toSet()));
    }
}
