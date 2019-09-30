package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.IntNBT;
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
        assertThat(S.serializeInstance(0), is(new IntNBT(0)));
        assertThat(S.serializeInstance(100), is(new IntNBT(100)));
    }

    @Test
    public void deserializeInstance() {
        assertThat(S.deserializeInstance(new IntNBT(0)), is(0));
        assertThat(S.deserializeInstance(new IntNBT(100)), is(100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeInstanceInvalid() {
        S.deserializeInstance(new StringNBT("0"));
    }

    @Test
    public void serializeCondition() {
        assertThat(S.serializeCondition(true), is(new ByteNBT((byte) 1)));
        assertThat(S.serializeCondition(false), is(new ByteNBT((byte) 0)));
    }

    @Test
    public void deserializeCondition() {
        assertThat(S.deserializeCondition(new ByteNBT((byte) 1)), is(true));
        assertThat(S.deserializeCondition(new ByteNBT((byte) 0)), is(false));
    }

}
