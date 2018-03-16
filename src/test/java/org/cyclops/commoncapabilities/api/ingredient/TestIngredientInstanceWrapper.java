package org.cyclops.commoncapabilities.api.ingredient;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.ingredient.IngredientMatcherEnergy;
import org.cyclops.commoncapabilities.ingredient.IngredientSerializerEnergy;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TestIngredientInstanceWrapper {

    private static IngredientComponent<Integer, Boolean> C;
    private static IngredientComponent<Integer, Boolean> C_OTHER;

    @BeforeClass
    public static void init() {
        C = IngredientComponents.ENERGY;
        C_OTHER = new IngredientComponent<>("minecraft:energyother", new IngredientMatcherEnergy(),
                new IngredientSerializerEnergy(), Lists.newArrayList(
                new IngredientComponentCategoryType<>(new ResourceLocation("energy/amount"),
                        Integer.class, false, amount -> amount, true)
        ));
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
