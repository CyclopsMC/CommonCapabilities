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
    public void testInstance() {
        assertThat(M.isInstance("a"), is(false));
        assertThat(M.isInstance(123), is(true));
    }

    @Test
    public void testMatches() {
        assertThat(M.matches(123, 124, null), is(false));
        assertThat(M.matches(123, 123, null), is(true));
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

}
