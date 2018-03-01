package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.NBTTagInt;
import org.junit.Test;

import static org.cyclops.commoncapabilities.ingredient.IngredientHelpers.compareTags;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientHelpers {

    @Test
    public void testCompareTags() {
        assertThat(compareTags(null, null), is(0));
        assertThat(compareTags(null, new NBTTagInt(0)), is(-1));
        assertThat(compareTags(new NBTTagInt(0), null), is(1));
        assertThat(compareTags(new NBTTagInt(0), new NBTTagInt(0)), is(0));
    }
}
