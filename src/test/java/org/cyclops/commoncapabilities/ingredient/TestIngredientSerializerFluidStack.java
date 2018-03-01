package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.init.Bootstrap;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TestIngredientSerializerFluidStack {

    private static IngredientSerializerFluidStack S;
    private static NBTTagCompound TAG;
    private static NBTTagCompound F_TAG1;
    private static NBTTagCompound F_TAG2;
    private static FluidStack F1;
    private static FluidStack F2;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        Bootstrap.register();

        S = new IngredientSerializerFluidStack();

        TAG = new NBTTagCompound();
        TAG.setBoolean("flag", true);

        F_TAG1 = new NBTTagCompound();
        F_TAG1.setString("FluidName", "water");
        F_TAG1.setInteger("Amount", 1000);

        F_TAG2 = new NBTTagCompound();
        F_TAG2.setString("FluidName", "lava");
        F_TAG2.setInteger("Amount", 123);
        F_TAG2.setTag("Tag", TAG);

        F1 = new FluidStack(FluidRegistry.WATER, 1000);
        F2 = new FluidStack(FluidRegistry.LAVA, 123, TAG);
    }

    @Test
    public void serializeInstance() {
        assertThat(S.serializeInstance(F1), is(F_TAG1));
        assertThat(S.serializeInstance(F2), is(F_TAG2));
        assertThat(S.serializeInstance(null), is(new NBTTagCompound()));
    }

    @Test
    public void deserializeInstance() {
        assertThat(S.deserializeInstance(F_TAG1), is(F1));
        assertThat(S.deserializeInstance(F_TAG2), is(F2));
        assertThat(S.deserializeInstance(new NBTTagCompound()), nullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeInstanceInvalid() {
        S.deserializeInstance(new NBTTagString("0"));
    }

    @Test
    public void serializeCondition() {
        assertThat(S.serializeCondition(1), is(new NBTTagInt(1)));
    }

    @Test
    public void deserializeCondition() {
        assertThat(S.deserializeCondition(new NBTTagInt(1)), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeConditionInvalid() {
        S.deserializeCondition(new NBTTagString("0"));
    }

}
