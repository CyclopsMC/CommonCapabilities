package org.cyclops.commoncapabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.cyclops.commoncapabilities.ingredient.TestHolderLookupProvider;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author rubensworks
 */
public class TestInitHelpers {

    private static final HolderLookup.Provider HL = TestHolderLookupProvider.get();

    public static <T> CompoundTag serialize(Consumer<ValueOutput> deserializer) {
        TagValueOutput valueOutput = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
        deserializer.accept(valueOutput);
        return valueOutput.buildResult();
    }

    public static <T> T deserialize(CompoundTag tag, Function<ValueInput, T> serializer) {
        ValueInput valueInput = TagValueInput.create(null, HL, tag);
        return serializer.apply(valueInput);
    }
}
