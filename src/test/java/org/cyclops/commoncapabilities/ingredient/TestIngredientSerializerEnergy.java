package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
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
        assertThat(S.serializeInstance(0), is(new NBTTagInt(0)));
        assertThat(S.serializeInstance(100), is(new NBTTagInt(100)));
    }

    @Test
    public void deserializeInstance() {
        assertThat(S.deserializeInstance(new NBTTagInt(0)), is(0));
        assertThat(S.deserializeInstance(new NBTTagInt(100)), is(100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeInstanceInvalid() {
        S.deserializeInstance(new NBTTagString("0"));
    }

    @Test
    public void serializeCondition() {
        assertThat(S.serializeCondition(null), is(new NBTTagByte((byte) 0)));
    }

    @Test
    public void deserializeCondition() {
        assertThat(S.deserializeCondition(new NBTTagByte((byte) 0)), nullValue());
    }

}
