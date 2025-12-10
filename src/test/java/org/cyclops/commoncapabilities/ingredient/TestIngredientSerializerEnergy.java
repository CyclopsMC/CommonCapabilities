package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.cyclops.commoncapabilities.TestInitHelpers.deserialize;
import static org.cyclops.commoncapabilities.TestInitHelpers.serialize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientSerializerEnergy {

    private static IngredientSerializerEnergy S;

    @BeforeAll
    public static void init() {
        S = new IngredientSerializerEnergy();
    }

    @Test
    public void serializeInstance() {
        CompoundTag tag1 = new CompoundTag();
        tag1.putLong("i", 0L);
        assertThat(serialize(o -> S.serializeInstance(o, 0L)), is(tag1));
        CompoundTag tag2 = new CompoundTag();
        tag2.putLong("i", 100L);
        assertThat(serialize(o -> S.serializeInstance(o, 100L)), is(tag2));
    }

    @Test
    public void deserializeInstance() {
        CompoundTag tag1 = new CompoundTag();
        tag1.putLong("i", 0L);
        assertThat(deserialize(tag1, S::deserializeInstance), is(0L));
        CompoundTag tag2 = new CompoundTag();
        tag2.putLong("i", 100L);
        assertThat(deserialize(tag2, S::deserializeInstance), is(100L));
    }

    @Test
    public void deserializeInstanceInvalid() {
        Assertions.assertThrows(NoSuchElementException.class, () -> deserialize(new CompoundTag(), S::deserializeInstance));
    }

    @Test
    public void serializeCondition() {
        assertThat(S.serializeCondition(true), is(ByteTag.valueOf((byte) 1)));
        assertThat(S.serializeCondition(false), is(ByteTag.valueOf((byte) 0)));
    }

    @Test
    public void deserializeCondition() {
        assertThat(S.deserializeCondition(ByteTag.valueOf((byte) 1)), is(true));
        assertThat(S.deserializeCondition(ByteTag.valueOf((byte) 0)), is(false));
    }

}
