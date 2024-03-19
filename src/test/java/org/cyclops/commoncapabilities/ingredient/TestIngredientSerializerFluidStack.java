package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientSerializerFluidStack {

    private static IngredientSerializerFluidStack S;
    private static CompoundTag TAG;
    private static CompoundTag F_TAG1;
    private static CompoundTag F_TAG2;
    private static FluidStack F1;
    private static FluidStack F2;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();

        S = new IngredientSerializerFluidStack();

        TAG = new CompoundTag();
        TAG.putBoolean("flag", true);

        F_TAG1 = new CompoundTag();
        F_TAG1.putString("FluidName", "minecraft:water");
        F_TAG1.putInt("Amount", 1000);

        F_TAG2 = new CompoundTag();
        F_TAG2.putString("FluidName", "minecraft:lava");
        F_TAG2.putInt("Amount", 123);
        F_TAG2.put("Tag", TAG);

        F1 = new FluidStack(Fluids.WATER, 1000);
        F2 = new FluidStack(Fluids.LAVA, 123, TAG);
    }

    @Test
    public void serializeInstance() {
        assertThat(S.serializeInstance(F1), is(F_TAG1));
        assertThat(S.serializeInstance(F2), is(F_TAG2));
        assertThat(S.serializeInstance(FluidStack.EMPTY), is(new CompoundTag()));
    }

    @Test
    public void deserializeInstance() {
        assertThat(S.deserializeInstance(F_TAG1), is(F1));
        assertThat(S.deserializeInstance(F_TAG2), is(F2));
        assertThat(S.deserializeInstance(new CompoundTag()), is(FluidStack.EMPTY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeInstanceInvalid() {
        S.deserializeInstance(StringTag.valueOf("0"));
    }

    @Test
    public void serializeCondition() {
        assertThat(S.serializeCondition(1), is(IntTag.valueOf(1)));
    }

    @Test
    public void deserializeCondition() {
        assertThat(S.deserializeCondition(IntTag.valueOf(1)), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeConditionInvalid() {
        S.deserializeCondition(StringTag.valueOf("0"));
    }

}
