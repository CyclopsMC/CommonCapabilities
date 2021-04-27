package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.nbt.StringNBT;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TestIngredientSerializerEnergy {

    private static IngredientSerializerEnergy S;

    @BeforeClass
    public static void init() {
        S = new IngredientSerializerEnergy();
    }

    @Test
    public void serializeInstance() {
        assertThat(S.serializeInstance(0L), is(LongNBT.valueOf(0L)));
        assertThat(S.serializeInstance(100L), is(LongNBT.valueOf(100L)));
    }

    @Test
    public void deserializeInstance() {
        assertThat(S.deserializeInstance(IntNBT.valueOf(0)), is(0L));
        assertThat(S.deserializeInstance(IntNBT.valueOf(100)), is(100L));
        assertThat(S.deserializeInstance(LongNBT.valueOf(0L)), is(0L));
        assertThat(S.deserializeInstance(LongNBT.valueOf(100L)), is(100L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeInstanceInvalid() {
        S.deserializeInstance(StringNBT.valueOf("0"));
    }

    @Test
    public void serializeCondition() {
        assertThat(S.serializeCondition(true), is(ByteNBT.valueOf((byte) 1)));
        assertThat(S.serializeCondition(false), is(ByteNBT.valueOf((byte) 0)));
    }

    @Test
    public void deserializeCondition() {
        assertThat(S.deserializeCondition(ByteNBT.valueOf((byte) 1)), is(true));
        assertThat(S.deserializeCondition(ByteNBT.valueOf((byte) 0)), is(false));
    }

}
