package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.cyclops.commoncapabilities.TestInitHelpers.deserialize;
import static org.cyclops.commoncapabilities.TestInitHelpers.serialize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientSerializerFluidStack {

    private static IngredientSerializerFluidStack S;
    private static DataComponentPatch DATA;
    private static CompoundTag F_TAG1;
    private static CompoundTag F_TAG2;
    private static CompoundTag F_TAGEMPTY;
    private static FluidStack F1;
    private static FluidStack F2;

    @BeforeAll
    public static void init() {
        S = new IngredientSerializerFluidStack();

        DATA = DataComponentPatch.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .build();

        F_TAG1 = new CompoundTag();
        CompoundTag subF_TAG1 = new CompoundTag();
        F_TAG1.put("i", subF_TAG1);
        subF_TAG1.putString("id", "minecraft:water");
        subF_TAG1.putInt("amount", 1000);

        F_TAG2 = new CompoundTag();
        CompoundTag subF_TAG2 = new CompoundTag();
        F_TAG2.put("i", subF_TAG2);
        subF_TAG2.putString("id", "minecraft:lava");
        subF_TAG2.putInt("amount", 123);
        subF_TAG2.put("components", DataComponentPatch.CODEC.encodeStart(NbtOps.INSTANCE, DATA).getOrThrow());

        F_TAGEMPTY = new CompoundTag();
        CompoundTag subF_TAG3 = new CompoundTag();
        F_TAGEMPTY.put("i", subF_TAG3);

        F1 = new FluidStack(Fluids.WATER, 1000);
        F2 = new FluidStack(Holder.direct(Fluids.LAVA), 123, DATA);
    }

    @Test
    public void serializeInstance() {
        assertThat(serialize(o -> S.serializeInstance(o, F1)), is(F_TAG1));
        assertThat(serialize(o -> S.serializeInstance(o, F2)), is(F_TAG2));
        assertThat(serialize(o -> S.serializeInstance(o, FluidStack.EMPTY)), is(F_TAGEMPTY));
    }

    @Test
    public void deserializeInstance() {
        assertThat(eq(deserialize(F_TAG1, S::deserializeInstance), F1), is(true));
        assertThat(eq(deserialize(F_TAG2, S::deserializeInstance), F2), is(true));
    }

    @Test
    public void deserializeInstanceInvalid() {
        Assertions.assertThrows(NoSuchElementException.class, () -> deserialize(new CompoundTag(), S::deserializeInstance));
    }

    @Test
    public void serializeCondition() {
        assertThat(S.serializeCondition(1), is(IntTag.valueOf(1)));
    }

    @Test
    public void deserializeCondition() {
        assertThat(S.deserializeCondition(IntTag.valueOf(1)), is(1));
    }

    @Test
    public void deserializeConditionInvalid() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> S.deserializeCondition(StringTag.valueOf("0")));
    }

    public static boolean eq(FluidStack a, FluidStack b) {
        return IngredientComponents.FLUIDSTACK.getMatcher().matchesExactly(a, b);
    }

}
