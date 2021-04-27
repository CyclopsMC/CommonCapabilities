package org.cyclops.commoncapabilities.ingredient;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientMatcherEnergy {

    private static IngredientMatcherEnergy M;

    @BeforeClass
    public static void init() {
        M = new IngredientMatcherEnergy();
    }

    @Test
    public void testGetAnyMatchCondition() {
        assertThat(M.getAnyMatchCondition(), is(false));
    }

    @Test
    public void testGetExactMatchCondition() {
        assertThat(M.getExactMatchCondition(), is(true));
    }

    @Test
    public void testGetExactNoQuantityMatchCondition() {
        assertThat(M.getExactMatchNoQuantityCondition(), is(false));
    }

    @Test
    public void testWithCondition() {
        assertThat(M.withCondition(M.getAnyMatchCondition(), true), is(true));
        assertThat(M.withCondition(M.getAnyMatchCondition(), false), is(false));
    }

    @Test
    public void testWithoutCondition() {
        assertThat(M.withoutCondition(M.getExactMatchCondition(), true), is(false));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), false), is(true));
    }

    @Test
    public void testHasCondition() {
        assertThat(M.hasCondition(M.getExactMatchCondition(), true), is(true));
        assertThat(M.hasCondition(M.getExactMatchCondition(), false), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), true), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), false), is(true));
    }

    @Test
    public void testInstance() {
        assertThat(M.isInstance("a"), is(false));
        assertThat(M.isInstance(123L), is(true));
    }

    @Test
    public void testMatches() {
        assertThat(M.matches(123L, 124L, true), is(false));
        assertThat(M.matches(123L, 123L, true), is(true));
        assertThat(M.matches(123L, 124L, false), is(true));
        assertThat(M.matches(123L, 123L, false), is(true));
    }

    @Test
    public void testMatchesExactly() {
        assertThat(M.matchesExactly(123L, 124L), is(false));
        assertThat(M.matchesExactly(123L, 123L), is(true));
    }

    @Test
    public void testEmpty() {
        assertThat(M.getEmptyInstance(), is(0L));
    }

    @Test
    public void testIsEmpty() {
        assertThat(M.isEmpty(123L), is(false));
        assertThat(M.isEmpty(0L), is(true));
    }

    @Test
    public void testHash() {
        assertThat(M.hash(123L), is(123));
        assertThat(M.hash(0L), is(0));
    }

    @Test
    public void testCopy() {
        assertThat(M.copy(123L), is(123L));
        assertThat(M.copy(0L), is(0L));
    }

    @Test
    public void testCompare() {
        assertThat(M.compare(123L, 124L), is(-1));
        assertThat(M.compare(124L, 124L), is(0));
        assertThat(M.compare(125L, 124L), is(1));
    }

    @Test
    public void testGetQuantity() {
        assertThat(M.getQuantity(123L), is(123L));
        assertThat(M.getQuantity(124L), is(124L));
        assertThat(M.getQuantity(125L), is(125L));
    }

    @Test
    public void testSetQuantity() {
        assertThat(M.withQuantity(123L, 234L), is(234L));
        assertThat(M.withQuantity(124L, 235L), is(235L));
    }

    @Test
    public void testGetMaximumQuantity() {
        assertThat(M.getMaximumQuantity(), is((long) Long.MAX_VALUE));
    }

    @Test
    public void testConditionCompare() {
        assertThat(M.conditionCompare(true, true), is(0));
        assertThat(M.conditionCompare(true, false), is(1));
        assertThat(M.conditionCompare(false, true), is(-1));
        assertThat(M.conditionCompare(false, false), is(0));
    }

}
