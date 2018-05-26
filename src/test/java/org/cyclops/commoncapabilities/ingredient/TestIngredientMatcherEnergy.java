package org.cyclops.commoncapabilities.ingredient;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
        assertThat(M.isInstance(123), is(true));
    }

    @Test
    public void testMatches() {
        assertThat(M.matches(123, 124, true), is(false));
        assertThat(M.matches(123, 123, true), is(true));
        assertThat(M.matches(123, 124, false), is(true));
        assertThat(M.matches(123, 123, false), is(true));
    }

    @Test
    public void testMatchesExactly() {
        assertThat(M.matchesExactly(123, 124), is(false));
        assertThat(M.matchesExactly(123, 123), is(true));
    }

    @Test
    public void testEmpty() {
        assertThat(M.getEmptyInstance(), is(0));
    }

    @Test
    public void testIsEmpty() {
        assertThat(M.isEmpty(123), is(false));
        assertThat(M.isEmpty(0), is(true));
    }

    @Test
    public void testHash() {
        assertThat(M.hash(123), is(123));
        assertThat(M.hash(0), is(0));
    }

    @Test
    public void testCopy() {
        assertThat(M.copy(123), is(123));
        assertThat(M.copy(0), is(0));
    }

    @Test
    public void testCompare() {
        assertThat(M.compare(123, 124), is(-1));
        assertThat(M.compare(124, 124), is(0));
        assertThat(M.compare(125, 124), is(1));
    }

    @Test
    public void testGetQuantity() {
        assertThat(M.getQuantity(123), is(123L));
        assertThat(M.getQuantity(124), is(124L));
        assertThat(M.getQuantity(125), is(125L));
    }

    @Test
    public void testSetQuantity() {
        assertThat(M.withQuantity(123, 234L), is(234));
        assertThat(M.withQuantity(124, 235L), is(235));
    }

}
