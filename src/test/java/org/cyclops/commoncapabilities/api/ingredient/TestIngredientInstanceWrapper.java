package org.cyclops.commoncapabilities.api.ingredient;

import org.cyclops.commoncapabilities.ingredient.IngredientMatcherEnergy;
import org.cyclops.commoncapabilities.ingredient.IngredientSerializerEnergy;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientInstanceWrapper {

    private static IngredientComponent<Integer, Void> C;
    private static IngredientComponent<Integer, Void> C_OTHER;

    @BeforeClass
    public static void init() {
        C = new IngredientComponent<>("minecraft:energy", new IngredientMatcherEnergy(),
                new IngredientSerializerEnergy());
        C_OTHER = new IngredientComponent<>("minecraft:energyother", new IngredientMatcherEnergy(),
                new IngredientSerializerEnergy());
    }

    @Test
    public void testInstance() {
        assertThat(new IngredientInstanceWrapper<>(C, 0), not(equalTo("abc")));
        assertThat(new IngredientInstanceWrapper<>(C, 0), not(equalTo(new IngredientInstanceWrapper<>(C_OTHER, 0))));
        assertThat(new IngredientInstanceWrapper<>(C, 0), equalTo(new IngredientInstanceWrapper<>(C, 0)));
        assertThat(new IngredientInstanceWrapper<>(C, 123), equalTo(new IngredientInstanceWrapper<>(C, 123)));
    }

    @Test
    public void testHashCode() {
        assertThat(new IngredientInstanceWrapper<>(C, 0).hashCode(), is(new IngredientInstanceWrapper<>(C, 0).hashCode()));
        assertThat(new IngredientInstanceWrapper<>(C, 123).hashCode(), is(new IngredientInstanceWrapper<>(C, 123).hashCode()));
    }

    @Test
    public void testCompareTo() {
        assertThat(new IngredientInstanceWrapper<>(C, 0).compareTo(new IngredientInstanceWrapper<>(C, 0)), is(0));
        assertThat(new IngredientInstanceWrapper<>(C, 0).compareTo(new IngredientInstanceWrapper<>(C, 123)), is(-123));
        assertThat(new IngredientInstanceWrapper<>(C, 123).compareTo(new IngredientInstanceWrapper<>(C, 0)), is(123));
    }

}
