package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.init.Bootstrap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientMatcherFluidStack {

    private static IngredientMatcherFluidStack M;

    private static FluidStack W_1;
    private static FluidStack W_2;
    private static FluidStack W_1_T1;
    private static FluidStack W_2_T1;
    private static FluidStack W_1_T2;
    private static FluidStack W_2_T2;

    private static FluidStack L_1;
    private static FluidStack L_1_T1;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        Bootstrap.register();

        M = new IngredientMatcherFluidStack();

        NBTTagCompound tag1 = new NBTTagCompound();
        tag1.setInteger("key", 1);

        NBTTagCompound tag2 = new NBTTagCompound();
        tag2.setInteger("key", 2);

        W_1 = new FluidStack(FluidRegistry.WATER, 1);
        W_2 = new FluidStack(FluidRegistry.WATER, 2);
        W_1_T1 = new FluidStack(FluidRegistry.WATER, 1, tag1);
        W_2_T1 = new FluidStack(FluidRegistry.WATER, 2, tag1);
        W_1_T2 = new FluidStack(FluidRegistry.WATER, 1, tag2);
        W_2_T2 = new FluidStack(FluidRegistry.WATER, 2, tag2);

        L_1 = new FluidStack(FluidRegistry.LAVA, 1);
        L_1_T1 = new FluidStack(FluidRegistry.LAVA, 1, tag1);
    }

    @Test
    public void testInstance() {
        assertThat(M.isInstance("a"), is(false));
        assertThat(M.isInstance(W_1), is(true));
    }

    @Test
    public void testMatches() {
        assertThat(M.matches(null, null, FluidMatch.EXACT), is(true));
        assertThat(M.matches(W_1, null, FluidMatch.EXACT), is(false));
        assertThat(M.matches(null, W_1, FluidMatch.EXACT), is(false));

        assertThat(M.matches(W_1, W_1, FluidMatch.EXACT), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.EXACT), is(false));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.EXACT), is(false));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.EXACT), is(false));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.EXACT), is(false));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.EXACT), is(false));
        assertThat(M.matches(W_1, L_1, FluidMatch.EXACT), is(false));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.EXACT), is(false));

        assertThat(M.matches(W_1, W_1, FluidMatch.ANY), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.ANY), is(true));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.ANY), is(true));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.ANY), is(true));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.ANY), is(true));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.ANY), is(true));
        assertThat(M.matches(W_1, L_1, FluidMatch.ANY), is(false));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.ANY), is(false));

        assertThat(M.matches(W_1, W_1, FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, L_1, FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.AMOUNT), is(false));

        assertThat(M.matches(W_1, W_1, FluidMatch.NBT), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.NBT), is(true));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, L_1, FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.NBT), is(false));
    }

    @Test
    public void testMatchesExactly() {
        assertThat(M.matchesExactly(null, null), is(true));
        assertThat(M.matchesExactly(W_1, null), is(false));
        assertThat(M.matchesExactly(null, W_1), is(false));

        assertThat(M.matchesExactly(W_1, W_1), is(true));
        assertThat(M.matchesExactly(W_1, W_2), is(false));
        assertThat(M.matchesExactly(W_1, W_1_T1), is(false));
        assertThat(M.matchesExactly(W_1, W_2_T1), is(false));
        assertThat(M.matchesExactly(W_1, W_1_T2), is(false));
        assertThat(M.matchesExactly(W_1, W_2_T2), is(false));
        assertThat(M.matchesExactly(W_1, L_1), is(false));
        assertThat(M.matchesExactly(W_1, L_1_T1), is(false));
    }

    @Test
    public void testIsEmpty() {
        assertThat(M.isEmpty(W_1), is(false));
        assertThat(M.isEmpty(null), is(true));
    }

    @Test
    public void testHash() {
        assertThat(M.hash(W_1), is(M.hash(W_1.copy())));
        assertThat(M.hash(null), is(0));
    }

    @Test
    public void testCopy() {
        assertThat(M.matchesExactly(M.copy(W_1), W_1), is(true));
        assertThat(M.matchesExactly(M.copy(null), null), is(true));
    }

    @Test
    public void testCompare() {
        assertThat(M.compare(W_1, null), is(1));
        assertThat(M.compare(null, null), is(0));
        assertThat(M.compare(null, W_1), is(-1));

        assertThat(M.compare(W_1, W_1), is(0));
        assertThat(M.compare(W_1, W_1_T1), is(-1));
        assertThat(M.compare(W_1, W_1_T2), is(-1));
        assertThat(M.compare(W_1, W_2), is(-1));
        assertThat(M.compare(W_1, W_2_T1), is(-1));
        assertThat(M.compare(W_1, W_2_T2), is(-1));
        assertThat(M.compare(W_1, L_1), is(-1));
        assertThat(M.compare(W_1, L_1_T1), is(-1));

        assertThat(M.compare(W_1_T1, W_1), is(1));
        assertThat(M.compare(W_1_T1, W_1_T1), is(0));
        assertThat(M.compare(W_1_T1, W_1_T2), is(-1));
        assertThat(M.compare(W_1_T1, W_2), is(-1));
        assertThat(M.compare(W_1_T1, W_2_T1), is(-1));
        assertThat(M.compare(W_1_T1, W_2_T2), is(-1));
        assertThat(M.compare(W_1_T1, L_1), is(-1));
        assertThat(M.compare(W_1_T1, L_1_T1), is(-1));

        assertThat(M.compare(W_1_T2, W_1), is(1));
        assertThat(M.compare(W_1_T2, W_1_T1), is(1));
        assertThat(M.compare(W_1_T2, W_1_T2), is(0));
        assertThat(M.compare(W_1_T2, W_2), is(-1));
        assertThat(M.compare(W_1_T2, W_2_T1), is(-1));
        assertThat(M.compare(W_1_T2, W_2_T2), is(-1));
        assertThat(M.compare(W_1_T2, L_1), is(-1));
        assertThat(M.compare(W_1_T2, L_1_T1), is(-1));

        assertThat(M.compare(W_2, W_1), is(1));
        assertThat(M.compare(W_2, W_1_T1), is(1));
        assertThat(M.compare(W_2, W_1_T2), is(1));
        assertThat(M.compare(W_2, W_2), is(0));
        assertThat(M.compare(W_2, W_2_T1), is(-1));
        assertThat(M.compare(W_2, W_2_T2), is(-1));
        assertThat(M.compare(W_2, L_1), is(-1));
        assertThat(M.compare(W_2, L_1_T1), is(-1));

        assertThat(M.compare(W_2_T1, W_1), is(1));
        assertThat(M.compare(W_2_T1, W_1_T1), is(1));
        assertThat(M.compare(W_2_T1, W_1_T2), is(1));
        assertThat(M.compare(W_2_T1, W_2), is(1));
        assertThat(M.compare(W_2_T1, W_2_T1), is(0));
        assertThat(M.compare(W_2_T1, W_2_T2), is(-1));
        assertThat(M.compare(W_2_T1, L_1), is(-1));
        assertThat(M.compare(W_2_T1, L_1_T1), is(-1));

        assertThat(M.compare(W_2_T2, W_1), is(1));
        assertThat(M.compare(W_2_T2, W_1_T1), is(1));
        assertThat(M.compare(W_2_T2, W_1_T2), is(1));
        assertThat(M.compare(W_2_T2, W_2), is(1));
        assertThat(M.compare(W_2_T2, W_2_T1), is(1));
        assertThat(M.compare(W_2_T2, W_2_T2), is(0));
        assertThat(M.compare(W_2_T2, L_1), is(-1));
        assertThat(M.compare(W_2_T2, L_1_T1), is(-1));

        assertThat(M.compare(L_1, W_1), is(1));
        assertThat(M.compare(L_1, W_1_T1), is(1));
        assertThat(M.compare(L_1, W_1_T2), is(1));
        assertThat(M.compare(L_1, W_2), is(1));
        assertThat(M.compare(L_1, W_2_T1), is(1));
        assertThat(M.compare(L_1, W_2_T2), is(1));
        assertThat(M.compare(L_1, L_1), is(0));
        assertThat(M.compare(L_1, L_1_T1), is(-1));

        assertThat(M.compare(L_1_T1, W_1), is(1));
        assertThat(M.compare(L_1_T1, W_1_T1), is(1));
        assertThat(M.compare(L_1_T1, W_1_T2), is(1));
        assertThat(M.compare(L_1_T1, W_2), is(1));
        assertThat(M.compare(L_1_T1, W_2_T1), is(1));
        assertThat(M.compare(L_1_T1, W_2_T2), is(1));
        assertThat(M.compare(L_1_T1, L_1), is(1));
        assertThat(M.compare(L_1_T1, L_1_T1), is(0));
    }

}
