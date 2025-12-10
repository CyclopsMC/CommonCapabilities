package org.cyclops.commoncapabilities.ingredient.storage;

import com.google.common.collect.Lists;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestEnergyComponentStorageWrapper {

    private EnergyHandler storage;
    private IngredientComponentStorageWrapperHandlerEnergyHandler.ComponentStorageWrapper wrapper;

    @BeforeEach
    public void beforeEach() {
        storage = new SimpleEnergyHandler(1000, 10, 10, 100);
        wrapper = new IngredientComponentStorageWrapperHandlerEnergyHandler.ComponentStorageWrapper(IngredientComponents.ENERGY, storage);
    }

    @Test
    public void testGetComponent() {
        assertThat(wrapper.getComponent(), is(IngredientComponents.ENERGY));
    }

    @Test
    public void testGetMaxQuantity() {
        assertThat(wrapper.getMaxQuantity(), is(1000L));
    }

    @Test
    public void testIterator() {
        assertThat(Lists.newArrayList(wrapper.iterator()), is(Lists.newArrayList(100L)));
    }

    @Test
    public void testIteratorMatch() {
        assertThat(Lists.newArrayList(wrapper.iterator(100L, true)), is(Lists.newArrayList(100L)));
        assertThat(Lists.newArrayList(wrapper.iterator(9L, true)), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(wrapper.iterator(0L, true)), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(wrapper.iterator(-1L, true)), is(Lists.newArrayList()));

        assertThat(Lists.newArrayList(wrapper.iterator(10L, false)), is(Lists.newArrayList(100L)));
        assertThat(Lists.newArrayList(wrapper.iterator(9L, false)), is(Lists.newArrayList(100L)));
        assertThat(Lists.newArrayList(wrapper.iterator(0L, false)), is(Lists.newArrayList(100L)));
        assertThat(Lists.newArrayList(wrapper.iterator(-1L, false)), is(Lists.newArrayList(100L)));
    }

    @Test
    public void testInsert() {
        assertThat(wrapper.insert(0L, true), is(0L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.insert(1L, true), is(0L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.insert(10L, true), is(0L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.insert(11L, true), is(1L));
        assertThat(storage.getAmountAsInt(), is(100));

        assertThat(wrapper.insert(0L, false), is(0L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.insert(1L, false), is(0L));
        assertThat(storage.getAmountAsInt(), is(101));
        assertThat(wrapper.insert(10L, false), is(0L));
        assertThat(storage.getAmountAsInt(), is(111));
        assertThat(wrapper.insert(11L, false), is(1L));
        assertThat(storage.getAmountAsInt(), is(121));
    }

    @Test
    public void testExtract() {
        assertThat(wrapper.extract(0L, true, true), is(0L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.extract(1L, true, true), is(1L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.extract(10L, true, true), is(10L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.extract(11L, true, true), is(0L));
        assertThat(storage.getAmountAsInt(), is(100));

        assertThat(wrapper.extract(0L, false, true), is(0L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.extract(1L, false, true), is(1L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.extract(10L, false, true), is(10L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.extract(11L, false, true), is(10L));
        assertThat(storage.getAmountAsInt(), is(100));

        assertThat(wrapper.extract(0L, true, false), is(0L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.extract(1L, true, false), is(1L));
        assertThat(storage.getAmountAsInt(), is(99));
        assertThat(wrapper.extract(10L, true, false), is(10L));
        assertThat(storage.getAmountAsInt(), is(89));
        assertThat(wrapper.extract(11L, true, false), is(0L));
        assertThat(storage.getAmountAsInt(), is(89));

        assertThat(wrapper.extract(0L, false, false), is(0L));
        assertThat(storage.getAmountAsInt(), is(89));
        assertThat(wrapper.extract(1L, false, false), is(1L));
        assertThat(storage.getAmountAsInt(), is(88));
        assertThat(wrapper.extract(10L, false, false), is(10L));
        assertThat(storage.getAmountAsInt(), is(78));
        assertThat(wrapper.extract(11L, false, false), is(10L));
        assertThat(storage.getAmountAsInt(), is(68));
    }

    @Test
    public void testExtractMax() {
        assertThat(wrapper.extract(0L, true), is(0L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.extract(1L, true), is(1L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.extract(10L, true), is(10L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.extract(11L, true), is(10L));
        assertThat(storage.getAmountAsInt(), is(100));

        assertThat(wrapper.extract(0L, false), is(0L));
        assertThat(storage.getAmountAsInt(), is(100));
        assertThat(wrapper.extract(1L, false), is(1L));
        assertThat(storage.getAmountAsInt(), is(99));
        assertThat(wrapper.extract(10L, false), is(10L));
        assertThat(storage.getAmountAsInt(), is(89));
        assertThat(wrapper.extract(11L, false), is(10L));
        assertThat(storage.getAmountAsInt(), is(79));
    }

}
