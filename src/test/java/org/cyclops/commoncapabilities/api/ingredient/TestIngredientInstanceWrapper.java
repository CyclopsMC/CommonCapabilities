package org.cyclops.commoncapabilities.api.ingredient;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.ingredient.IngredientMatcherEnergy;
import org.cyclops.commoncapabilities.ingredient.IngredientSerializerEnergy;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TestIngredientInstanceWrapper {

    private static IngredientComponent<Long, Boolean> C;
    private static IngredientComponent<Long, Boolean> C_OTHER;

    @BeforeClass
    public static void init() {
        C = IngredientComponents.ENERGY;
        C_OTHER = new IngredientComponent<>("minecraft:energyother", new IngredientMatcherEnergy(),
                new IngredientSerializerEnergy(), Lists.newArrayList(
                new IngredientComponentCategoryType<>(new ResourceLocation("energy/amount"),
                        Long.class, false, amount -> amount, true, true)
        ));
    }

    @Test
    public void testInstance() {
        assertThat(new IngredientInstanceWrapper<>(C, 0L), not(equalTo("abc")));
        assertThat(new IngredientInstanceWrapper<>(C, 0L), not(equalTo(new IngredientInstanceWrapper<>(C_OTHER, 0L))));
        assertThat(new IngredientInstanceWrapper<>(C, 0L), equalTo(new IngredientInstanceWrapper<>(C, 0L)));
        assertThat(new IngredientInstanceWrapper<>(C, 123L), equalTo(new IngredientInstanceWrapper<>(C, 123L)));
    }

    @Test
    public void testHashCode() {
        assertThat(new IngredientInstanceWrapper<>(C, 0L).hashCode(), is(new IngredientInstanceWrapper<>(C, 0L).hashCode()));
        assertThat(new IngredientInstanceWrapper<>(C, 123L).hashCode(), is(new IngredientInstanceWrapper<>(C, 123L).hashCode()));
    }

    @Test
    public void testCompareTo() {
        assertThat(new IngredientInstanceWrapper<>(C, 0L).compareTo(new IngredientInstanceWrapper<>(C, 0L)), is(0));
        assertThat(new IngredientInstanceWrapper<>(C, 0L).compareTo(new IngredientInstanceWrapper<>(C, 123L)), is(-123));
        assertThat(new IngredientInstanceWrapper<>(C, 123L).compareTo(new IngredientInstanceWrapper<>(C, 0L)), is(123));
    }

}
