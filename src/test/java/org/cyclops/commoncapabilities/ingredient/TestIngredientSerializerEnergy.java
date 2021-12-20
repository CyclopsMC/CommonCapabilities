package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.StringTag;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientSerializerEnergy {

    private static IngredientSerializerEnergy S;

    @BeforeClass
    public static void init() {
        S = new IngredientSerializerEnergy();
    }

    @Test
    public void serializeInstance() {
        assertThat(S.serializeInstance(0L), is(LongTag.valueOf(0L)));
        assertThat(S.serializeInstance(100L), is(LongTag.valueOf(100L)));
    }

    @Test
    public void deserializeInstance() {
        assertThat(S.deserializeInstance(IntTag.valueOf(0)), is(0L));
        assertThat(S.deserializeInstance(IntTag.valueOf(100)), is(100L));
        assertThat(S.deserializeInstance(LongTag.valueOf(0L)), is(0L));
        assertThat(S.deserializeInstance(LongTag.valueOf(100L)), is(100L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeInstanceInvalid() {
        S.deserializeInstance(StringTag.valueOf("0"));
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
