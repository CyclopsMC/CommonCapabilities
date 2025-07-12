package org.cyclops.commoncapabilities;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.fml.loading.LoadingModList;
import org.cyclops.commoncapabilities.ingredient.TestHolderLookupProvider;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author rubensworks
 */
public class TestInitHelpers {

    private static final HolderLookup.Provider HL = TestHolderLookupProvider.get();

    public static void initMinecraft() {
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        LoadingModList.of(Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), Maps.newHashMap());
        Bootstrap.bootStrap();
    }

    public static <T> CompoundTag serialize(Consumer<ValueOutput> deserializer) {
        TagValueOutput valueOutput = TagValueOutput.createWithContext(null, HL);
        deserializer.accept(valueOutput);
        return valueOutput.buildResult();
    }

    public static <T> T deserialize(CompoundTag tag, Function<ValueInput, T> serializer) {
        ValueInput valueInput = TagValueInput.create(null, HL, tag);
        return serializer.apply(valueInput);
    }
}
