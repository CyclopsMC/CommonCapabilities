package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientSerializerFluidStack {

    private static final HolderLookup.Provider HL = TestHolderLookupProvider.get();

    private static IngredientSerializerFluidStack S;
    private static DataComponentPatch DATA;
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

        DATA = DataComponentPatch.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .build();

        F_TAG1 = new CompoundTag();
        F_TAG1.putString("id", "minecraft:water");
        F_TAG1.putInt("amount", 1000);

        F_TAG2 = new CompoundTag();
        F_TAG2.putString("id", "minecraft:lava");
        F_TAG2.putInt("amount", 123);
        F_TAG2.put("components", DataComponentPatch.CODEC.encodeStart(NbtOps.INSTANCE, DATA).getOrThrow());

        F1 = new FluidStack(Fluids.WATER, 1000);
        F2 = new FluidStack(Holder.direct(Fluids.LAVA), 123, DATA);
    }

    @Test
    public void serializeInstance() {
        assertThat(S.serializeInstance(HL, F1), is(F_TAG1));
        assertThat(S.serializeInstance(HL, F2), is(F_TAG2));
        assertThat(S.serializeInstance(HL, FluidStack.EMPTY), is(new CompoundTag()));
    }

    @Test
    public void deserializeInstance() {
        assertThat(eq(S.deserializeInstance(HL, F_TAG1), F1), is(true));
        assertThat(eq(S.deserializeInstance(HL, F_TAG2), F2), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeInstanceInvalid() {
        S.deserializeInstance(HL, StringTag.valueOf("0"));
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

    public static boolean eq(FluidStack a, FluidStack b) {
        return IngredientComponents.FLUIDSTACK.getMatcher().matchesExactly(a, b);
    }

}
