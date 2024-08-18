package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.ModBaseMocked;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientMatcherFluidStack {

    private static IngredientMatcherFluidStack M;

    private static FluidStack W_1;
    private static FluidStack W_100;
    private static FluidStack W_123;
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
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();
        CyclopsCoreInstance.MOD = new ModBaseMocked();

        M = new IngredientMatcherFluidStack();

        DataComponentPatch tag1 = DataComponentPatch.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .build();

        DataComponentPatch tag2 = DataComponentPatch.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
                .build();

        W_1 = new FluidStack(Fluids.WATER, 1);
        W_100 = new FluidStack(Fluids.WATER, 100);
        W_123 = new FluidStack(Fluids.WATER, 123);
        W_2 = new FluidStack(Fluids.WATER, 2);
        W_1_T1 = new FluidStack(Holder.direct(Fluids.WATER), 1, tag1);
        W_2_T1 = new FluidStack(Holder.direct(Fluids.WATER), 2, tag1);
        W_1_T2 = new FluidStack(Holder.direct(Fluids.WATER), 1, tag2);
        W_2_T2 = new FluidStack(Holder.direct(Fluids.WATER), 2, tag2);

        L_1 = new FluidStack(Fluids.LAVA, 1);
        L_1_T1 = new FluidStack(Holder.direct(Fluids.LAVA), 1, tag1);
    }

    @Test
    public void testInstance() {
        assertThat(M.isInstance("a"), is(false));
        assertThat(M.isInstance(W_1), is(true));
        assertThat(M.isInstance(FluidStack.EMPTY), is(true));
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
    public void testGetExactNoQuantityMatchCondition() {
        assertThat(M.getExactMatchNoQuantityCondition(), is(FluidMatch.FLUID | FluidMatch.DATA));
    }

    @Test
    public void testWithCondition() {
        assertThat(M.withCondition(M.getAnyMatchCondition(), FluidMatch.ANY), is(FluidMatch.ANY));
        assertThat(M.withCondition(M.getAnyMatchCondition(), FluidMatch.FLUID), is(FluidMatch.FLUID));
        assertThat(M.withCondition(M.getAnyMatchCondition(), FluidMatch.DATA), is(FluidMatch.DATA));
        assertThat(M.withCondition(M.getAnyMatchCondition(), FluidMatch.AMOUNT), is(FluidMatch.AMOUNT));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), FluidMatch.DATA), FluidMatch.AMOUNT), is(FluidMatch.DATA | FluidMatch.AMOUNT));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), FluidMatch.AMOUNT), FluidMatch.DATA), is(FluidMatch.DATA | FluidMatch.AMOUNT));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), FluidMatch.FLUID), FluidMatch.DATA), FluidMatch.AMOUNT), is(FluidMatch.FLUID | FluidMatch.DATA | FluidMatch.AMOUNT));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), FluidMatch.FLUID), FluidMatch.AMOUNT), FluidMatch.DATA), is(FluidMatch.FLUID | FluidMatch.DATA | FluidMatch.AMOUNT));
        assertThat(M.withCondition(M.getAnyMatchCondition(), FluidMatch.EXACT), is(FluidMatch.EXACT));
    }

    @Test
    public void testWithoutCondition() {
        assertThat(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.ANY), is(FluidMatch.EXACT));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.FLUID), is(FluidMatch.DATA | FluidMatch.AMOUNT));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.DATA), is(FluidMatch.FLUID | FluidMatch.AMOUNT));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.AMOUNT), is(FluidMatch.FLUID | FluidMatch.DATA));
        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.DATA), FluidMatch.AMOUNT), is(FluidMatch.FLUID));
        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.AMOUNT), FluidMatch.DATA), is(FluidMatch.FLUID));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.FLUID), FluidMatch.DATA), FluidMatch.AMOUNT), is(FluidMatch.ANY));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.FLUID), FluidMatch.AMOUNT), FluidMatch.DATA), is(FluidMatch.ANY));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), FluidMatch.EXACT), is(FluidMatch.ANY));
    }

    @Test
    public void testHasCondition() {
        assertThat(M.hasCondition(M.getExactMatchCondition(), FluidMatch.ANY), is(false));
        assertThat(M.hasCondition(M.getExactMatchCondition(), FluidMatch.DATA), is(true));
        assertThat(M.hasCondition(M.getExactMatchCondition(), FluidMatch.AMOUNT), is(true));
        assertThat(M.hasCondition(M.getExactMatchCondition(), FluidMatch.FLUID), is(true));

        assertThat(M.hasCondition(M.getAnyMatchCondition(), FluidMatch.ANY), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), FluidMatch.DATA), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), FluidMatch.AMOUNT), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), FluidMatch.FLUID), is(false));
    }

    @Test
    public void testMatches() {
        assertThat(M.matches(FluidStack.EMPTY, FluidStack.EMPTY, FluidMatch.EXACT), is(true));
        assertThat(M.matches(W_1, FluidStack.EMPTY, FluidMatch.EXACT), is(false));
        assertThat(M.matches(FluidStack.EMPTY, W_1, FluidMatch.EXACT), is(false));

        assertThat(M.matches(FluidStack.EMPTY, FluidStack.EMPTY, FluidMatch.ANY), is(true));
        assertThat(M.matches(W_1, FluidStack.EMPTY, FluidMatch.ANY), is(true));
        assertThat(M.matches(FluidStack.EMPTY, W_1, FluidMatch.ANY), is(true));

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

        assertThat(M.matches(W_1, W_1, FluidMatch.DATA), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.DATA), is(true));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, L_1, FluidMatch.DATA), is(true));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.DATA), is(false));

        assertThat(M.matches(W_1, W_1, FluidMatch.FLUID | FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.FLUID | FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.FLUID | FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.FLUID | FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.FLUID | FluidMatch.AMOUNT), is(true));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.FLUID | FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, L_1, FluidMatch.FLUID | FluidMatch.AMOUNT), is(false));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.FLUID | FluidMatch.AMOUNT), is(false));

        assertThat(M.matches(W_1, W_1, FluidMatch.FLUID | FluidMatch.DATA), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.FLUID | FluidMatch.DATA), is(true));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.FLUID | FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.FLUID | FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.FLUID | FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.FLUID | FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, L_1, FluidMatch.FLUID | FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.FLUID | FluidMatch.DATA), is(false));

        assertThat(M.matches(W_1, W_1, FluidMatch.AMOUNT | FluidMatch.DATA), is(true));
        assertThat(M.matches(W_1, W_2, FluidMatch.AMOUNT | FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_1_T1, FluidMatch.AMOUNT | FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T1, FluidMatch.AMOUNT | FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_1_T2, FluidMatch.AMOUNT | FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T2, FluidMatch.AMOUNT | FluidMatch.DATA), is(false));
        assertThat(M.matches(W_1, L_1, FluidMatch.AMOUNT | FluidMatch.DATA), is(true));
        assertThat(M.matches(W_1, L_1_T1, FluidMatch.AMOUNT | FluidMatch.DATA), is(false));
    }

    @Test
    public void testMatchesExactly() {
        assertThat(M.matchesExactly(FluidStack.EMPTY, FluidStack.EMPTY), is(true));
        assertThat(M.matchesExactly(W_1, FluidStack.EMPTY), is(false));
        assertThat(M.matchesExactly(FluidStack.EMPTY, W_1), is(false));

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
        assertThat(M.getEmptyInstance(), is(FluidStack.EMPTY));
    }

    @Test
    public void testIsEmpty() {
        assertThat(M.isEmpty(W_1), is(false));
        assertThat(M.isEmpty(FluidStack.EMPTY), is(true));
    }

    @Test
    public void testHash() {
        assertThat(M.hash(W_1), is(M.hash(W_1.copy())));
        assertThat(M.hash(FluidStack.EMPTY), is(0));
    }

    @Test
    public void testCopy() {
        assertThat(M.matchesExactly(M.copy(W_1), W_1), is(true));
        assertThat(M.matchesExactly(M.copy(FluidStack.EMPTY), FluidStack.EMPTY), is(true));
    }

    @Test
    public void testCompare() {
        assertThat(M.compare(W_1, FluidStack.EMPTY), is(1));
        assertThat(M.compare(FluidStack.EMPTY, FluidStack.EMPTY), is(0));
        assertThat(M.compare(FluidStack.EMPTY, W_1), is(-1));

        assertThat(M.compare(W_1, W_1), is(0));
        assertThat(M.compare(W_1, W_1_T1), is(-1));
        assertThat(M.compare(W_1, W_1_T2), is(-1));
        assertThat(M.compare(W_1, W_2), is(-1));
        assertThat(M.compare(W_1, W_2_T1), is(-1));
        assertThat(M.compare(W_1, W_2_T2), is(-1));
        assertThat(M.compare(W_1, L_1), is(11));
        assertThat(M.compare(W_1, L_1_T1), is(11));

        assertThat(M.compare(W_1_T1, W_1), is(1));
        assertThat(M.compare(W_1_T1, W_1_T1), is(0));
        assertThat(M.compare(W_1_T1, W_1_T2), is(1));
        assertThat(M.compare(W_1_T1, W_2), is(-1));
        assertThat(M.compare(W_1_T1, W_2_T1), is(-1));
        assertThat(M.compare(W_1_T1, W_2_T2), is(-1));
        assertThat(M.compare(W_1_T1, L_1), is(11));
        assertThat(M.compare(W_1_T1, L_1_T1), is(11));

        assertThat(M.compare(W_1_T2, W_1), is(1));
        assertThat(M.compare(W_1_T2, W_1_T1), is(-1));
        assertThat(M.compare(W_1_T2, W_1_T2), is(0));
        assertThat(M.compare(W_1_T2, W_2), is(-1));
        assertThat(M.compare(W_1_T2, W_2_T1), is(-1));
        assertThat(M.compare(W_1_T2, W_2_T2), is(-1));
        assertThat(M.compare(W_1_T2, L_1), is(11));
        assertThat(M.compare(W_1_T2, L_1_T1), is(11));

        assertThat(M.compare(W_2, W_1), is(1));
        assertThat(M.compare(W_2, W_1_T1), is(1));
        assertThat(M.compare(W_2, W_1_T2), is(1));
        assertThat(M.compare(W_2, W_2), is(0));
        assertThat(M.compare(W_2, W_2_T1), is(-1));
        assertThat(M.compare(W_2, W_2_T2), is(-1));
        assertThat(M.compare(W_2, L_1), is(11));
        assertThat(M.compare(W_2, L_1_T1), is(11));

        assertThat(M.compare(W_2_T1, W_1), is(1));
        assertThat(M.compare(W_2_T1, W_1_T1), is(1));
        assertThat(M.compare(W_2_T1, W_1_T2), is(1));
        assertThat(M.compare(W_2_T1, W_2), is(1));
        assertThat(M.compare(W_2_T1, W_2_T1), is(0));
        assertThat(M.compare(W_2_T1, W_2_T2), is(1));
        assertThat(M.compare(W_2_T1, L_1), is(11));
        assertThat(M.compare(W_2_T1, L_1_T1), is(11));

        assertThat(M.compare(W_2_T2, W_1), is(1));
        assertThat(M.compare(W_2_T2, W_1_T1), is(1));
        assertThat(M.compare(W_2_T2, W_1_T2), is(1));
        assertThat(M.compare(W_2_T2, W_2), is(1));
        assertThat(M.compare(W_2_T2, W_2_T1), is(-1));
        assertThat(M.compare(W_2_T2, W_2_T2), is(0));
        assertThat(M.compare(W_2_T2, L_1), is(11));
        assertThat(M.compare(W_2_T2, L_1_T1), is(11));

        assertThat(M.compare(L_1, W_1), is(-11));
        assertThat(M.compare(L_1, W_1_T1), is(-11));
        assertThat(M.compare(L_1, W_1_T2), is(-11));
        assertThat(M.compare(L_1, W_2), is(-11));
        assertThat(M.compare(L_1, W_2_T1), is(-11));
        assertThat(M.compare(L_1, W_2_T2), is(-11));
        assertThat(M.compare(L_1, L_1), is(0));
        assertThat(M.compare(L_1, L_1_T1), is(-1));

        assertThat(M.compare(L_1_T1, W_1), is(-11));
        assertThat(M.compare(L_1_T1, W_1_T1), is(-11));
        assertThat(M.compare(L_1_T1, W_1_T2), is(-11));
        assertThat(M.compare(L_1_T1, W_2), is(-11));
        assertThat(M.compare(L_1_T1, W_2_T1), is(-11));
        assertThat(M.compare(L_1_T1, W_2_T2), is(-11));
        assertThat(M.compare(L_1_T1, L_1), is(1));
        assertThat(M.compare(L_1_T1, L_1_T1), is(0));
    }

    @Test
    public void testGetQuantity() {
        assertThat(M.getQuantity(W_1), is(1L));
        assertThat(M.getQuantity(W_2), is(2L));
        assertThat(M.getQuantity(FluidStack.EMPTY), is(0L));
    }

    @Test
    public void testSetQuantity() {
        assertThat(M.matchesExactly(M.withQuantity(W_1, 100L), W_100), is(true));
        assertThat(M.matchesExactly(M.withQuantity(W_1, 123L), W_123), is(true));
        assertThat(M.matchesExactly(M.withQuantity(FluidStack.EMPTY, 123L), W_123), is(true));
        assertThat(M.matchesExactly(M.withQuantity(FluidStack.EMPTY, 0L), FluidStack.EMPTY), is(true));
        assertThat(M.matchesExactly(M.withQuantity(W_1, 0L), FluidStack.EMPTY), is(true));
    }

    @Test
    public void testGetMaximumQuantity() {
        assertThat(M.getMaximumQuantity(), is((long) Integer.MAX_VALUE));
    }

    @Test
    public void testConditionCompare() {
        assertThat(M.conditionCompare(0, 0), is(0));
        assertThat(M.conditionCompare(10, 2), is(1));
        assertThat(M.conditionCompare(2, 10), is(-1));
    }

    @Test
    public void testToString() {
        assertThat(M.toString(W_1), is("minecraft:water 1 {}"));
        assertThat(M.toString(W_1_T1), is("minecraft:water 1 {minecraft:enchantment_glint_override=>true}"));
    }

}
