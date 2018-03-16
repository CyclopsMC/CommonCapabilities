package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.init.Bootstrap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
        assertThat(M.isInstance(null), is(true));
    }

    @Test
    public void testGetAnyMatchCondition() {
        assertThat(M.getAnyMatchCondition(), is(FluidMatch.ANY));
    }

    @Test
    public void testGetExactMatchCondition() {
        assertThat(M.getExactMatchCondition(), is(FluidMatch.EXACT));
    }

    @Test
    public void testWithCondition() {
        assertThat(M.withCondition(M.getAnyMatchCondition(), FluidMatch.ANY), is(FluidMatch.ANY));
        assertThat(M.withCondition(M.getAnyMatchCondition(), FluidMatch.FLUID), is(FluidMatch.FLUID));
        assertThat(M.withCondition(M.getAnyMatchCondition(), FluidMatch.NBT), is(FluidMatch.NBT));
        assertThat(M.withCondition(M.getAnyMatchCondition(), FluidMatch.AMOUNT), is(FluidMatch.AMOUNT));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), FluidMatch.NBT), FluidMatch.AMOUNT), is(FluidMatch.NBT | FluidMatch.AMOUNT));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), FluidMatch.AMOUNT), FluidMatch.NBT), is(FluidMatch.NBT | FluidMatch.AMOUNT));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), FluidMatch.FLUID), FluidMatch.NBT), FluidMatch.AMOUNT), is(FluidMatch.FLUID | FluidMatch.NBT | FluidMatch.AMOUNT));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), FluidMatch.FLUID), FluidMatch.AMOUNT), FluidMatch.NBT), is(FluidMatch.FLUID | FluidMatch.NBT | FluidMatch.AMOUNT));
        assertThat(M.withCondition(M.getAnyMatchCondition(), FluidMatch.EXACT), is(FluidMatch.EXACT));
    }

    @Test
    public void testWithoutCondition() {
        assertThat(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.ANY), is(FluidMatch.EXACT));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.FLUID), is(FluidMatch.NBT | FluidMatch.AMOUNT));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.NBT), is(FluidMatch.FLUID | FluidMatch.AMOUNT));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.AMOUNT), is(FluidMatch.FLUID | FluidMatch.NBT));
        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.NBT), FluidMatch.AMOUNT), is(FluidMatch.FLUID));
        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.AMOUNT), FluidMatch.NBT), is(FluidMatch.FLUID));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.FLUID), FluidMatch.NBT), FluidMatch.AMOUNT), is(FluidMatch.ANY));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.FLUID), FluidMatch.AMOUNT), FluidMatch.NBT), is(FluidMatch.ANY));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.EXACT), is(FluidMatch.ANY));
    }

    @Test
    public void testHasCondition() {
        assertThat(M.hasCondition(M.getExactMatchCondition(), FluidMatch.ANY), is(false));
        assertThat(M.hasCondition(M.getExactMatchCondition(), FluidMatch.NBT), is(true));
        assertThat(M.hasCondition(M.getExactMatchCondition(), FluidMatch.AMOUNT), is(true));
        assertThat(M.hasCondition(M.getExactMatchCondition(), FluidMatch.FLUID), is(true));

        assertThat(M.hasCondition(M.getAnyMatchCondition(), FluidMatch.ANY), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), FluidMatch.NBT), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), FluidMatch.AMOUNT), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), FluidMatch.FLUID), is(false));
    }

    @Test
    public void testMatches() {
        assertThat(M.matches(null, null, FluidMatch.EXACT), is(true));
        assertThat(M.matches(W_1, null, FluidMatch.EXACT), is(false));
        assertThat(M.matches(null, W_1, FluidMatch.EXACT), is(false));

        assertThat(M.matches(null, null, FluidMatch.ANY), is(true));
        assertThat(M.matches(W_1, null, FluidMatch.ANY), is(true));
        assertThat(M.matches(null, W_1, FluidMatch.ANY), is(true));

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
        assertThat(M.matches(W_1, L_1, FluidMatch.ANY), is(true));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.ANY), is(true));

        assertThat(M.matches(W_1, W_1, FluidMatch.FLUID), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.FLUID), is(true));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.FLUID), is(true));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.FLUID), is(true));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.FLUID), is(true));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.FLUID), is(true));
        assertThat(M.matches(W_1, L_1, FluidMatch.FLUID), is(false));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.FLUID), is(false));

        assertThat(M.matches(W_1, W_1, FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, L_1, FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.AMOUNT), is(true));

        assertThat(M.matches(W_1, W_1, FluidMatch.NBT), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.NBT), is(true));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, L_1, FluidMatch.NBT), is(true));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.NBT), is(false));

        assertThat(M.matches(W_1, W_1, FluidMatch.FLUID | FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.FLUID | FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.FLUID | FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.FLUID | FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.FLUID | FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.FLUID | FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, L_1, FluidMatch.FLUID | FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.FLUID | FluidMatch.AMOUNT), is(false));

        assertThat(M.matches(W_1, W_1, FluidMatch.FLUID | FluidMatch.NBT), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.FLUID | FluidMatch.NBT), is(true));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.FLUID | FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.FLUID | FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.FLUID | FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.FLUID | FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, L_1, FluidMatch.FLUID | FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.FLUID | FluidMatch.NBT), is(false));

        assertThat(M.matches(W_1, W_1, FluidMatch.AMOUNT | FluidMatch.NBT), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.AMOUNT | FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.AMOUNT | FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.AMOUNT | FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.AMOUNT | FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.AMOUNT | FluidMatch.NBT), is(false));
        assertThat(M.matches(W_1, L_1, FluidMatch.AMOUNT | FluidMatch.NBT), is(true));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.AMOUNT | FluidMatch.NBT), is(false));
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
    public void testEmpty() {
        assertThat(M.getEmptyInstance(), nullValue());
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
