package org.cyclops.commoncapabilities.ingredient;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.nbt.IntTag;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;

import static org.cyclops.commoncapabilities.api.ingredient.MixedIngredients.compareCollection;
import static org.cyclops.commoncapabilities.ingredient.IngredientHelpers.compareTags;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientHelpers {

    @Test
    public void testCompareTags() {
        assertThat(compareTags(null, null), is(0));
        assertThat(compareTags(null, IntTag.valueOf(0)), is(-1));
        assertThat(compareTags(IntTag.valueOf(0), null), is(1));
        assertThat(compareTags(IntTag.valueOf(0), IntTag.valueOf(0)), is(0));
    }

    @Test
    public void testCompareCollectionEmpty() {
        assertThat(compareCollection(Collections.emptyList(), Collections.emptyList()), is(0));
    }

    @Test
    public void testCompareCollectionEqual() {
        assertThat(compareCollection(
                Lists.newArrayList(1, 2, 3),
                Lists.newArrayList(1, 2, 3)
        ), is(0));

        assertThat(compareCollection(
                Lists.newArrayList(1, 2, 3, 4, 5),
                Lists.newArrayList(1, 2, 3, 4, 5)
        ), is(0));

        assertThat(compareCollection(
                Sets.newTreeSet(Lists.newArrayList(1, 2, 3, 4, 5)),
                Sets.newTreeSet(Lists.newArrayList(5, 2, 3, 1, 4))
        ), is(0));
    }

    @Test
    public void testCompareCollectionNonEqual() {
        assertThat(compareCollection(
                Lists.newArrayList(1, 2),
                Lists.newArrayList(1, 2, 3)
        ), is(-1));

        assertThat(compareCollection(
                Lists.newArrayList(1, 2, 3),
                Lists.newArrayList(1, 2)
        ), is(1));

        assertThat(compareCollection(
                Lists.newArrayList(2),
                Lists.newArrayList(3)
        ), is(-1));

        assertThat(compareCollection(
                Lists.newArrayList(3),
                Lists.newArrayList(2)
        ), is(1));
    }

    @Test
    public void testCompareCollectionComparatorEmpty() {
        assertThat(compareCollection(Collections.emptyList(), Collections.emptyList(), Comparator.<Integer>reverseOrder()), is(0));
    }

    @Test
    public void testCompareCollectionComparatorEqual() {
        assertThat(compareCollection(
                Lists.newArrayList(1, 2, 3),
                Lists.newArrayList(1, 2, 3),
                Comparator.<Integer>reverseOrder()
        ), is(0));

        assertThat(compareCollection(
                Lists.newArrayList(1, 2, 3, 4, 5),
                Lists.newArrayList(1, 2, 3, 4, 5),
                Comparator.<Integer>reverseOrder()
        ), is(0));

        assertThat(compareCollection(
                Sets.newTreeSet(Lists.newArrayList(1, 2, 3, 4, 5)),
                Sets.newTreeSet(Lists.newArrayList(5, 2, 3, 1, 4)),
                Comparator.<Integer>reverseOrder()
        ), is(0));
    }

    @Test
    public void testCompareCollectionComparatorNonEqual() {
        assertThat(compareCollection(
                Lists.newArrayList(1, 2),
                Lists.newArrayList(1, 2, 3),
                Comparator.<Integer>reverseOrder()
        ), is(-1));

        assertThat(compareCollection(
                Lists.newArrayList(1, 2, 3),
                Lists.newArrayList(1, 2),
                Comparator.<Integer>reverseOrder()
        ), is(1));

        assertThat(compareCollection(
                Lists.newArrayList(2),
                Lists.newArrayList(3),
                Comparator.<Integer>reverseOrder()
        ), is(1));

        assertThat(compareCollection(
                Lists.newArrayList(3),
                Lists.newArrayList(2),
                Comparator.<Integer>reverseOrder()
        ), is(-1));
    }
}
