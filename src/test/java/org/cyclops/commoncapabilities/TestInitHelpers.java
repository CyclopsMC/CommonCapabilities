package org.cyclops.commoncapabilities;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.neoforged.fml.loading.LoadingModList;

/**
 * @author rubensworks
 */
public class TestInitHelpers {
    public static void initMinecraft() {
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        LoadingModList.of(Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), Maps.newHashMap());
        Bootstrap.bootStrap();
    }
}
