package org.cyclops.commoncapabilities.ingredient.storage;

import com.google.common.collect.Lists;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestEnergyComponentStorageWrapper {

    private IEnergyStorage storage;
    private IngredientComponentStorageWrapperHandlerEnergyStorage.ComponentStorageWrapper wrapper;

    @Before
    public void beforeEach() {
        storage = new EnergyStorage(1000, 10, 10, 100);
        wrapper = new IngredientComponentStorageWrapperHandlerEnergyStorage.ComponentStorageWrapper(IngredientComponents.ENERGY, storage);
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
        assertThat(Lists.newArrayList(wrapper.iterator()), is(Lists.newArrayList(100)));
    }

    @Test
    public void testIteratorMatch() {
        assertThat(Lists.newArrayList(wrapper.iterator(100, true)), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(wrapper.iterator(9, true)), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(wrapper.iterator(0, true)), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(wrapper.iterator(-1, true)), is(Lists.newArrayList()));

        assertThat(Lists.newArrayList(wrapper.iterator(10, false)), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(wrapper.iterator(9, false)), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(wrapper.iterator(0, false)), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(wrapper.iterator(-1, false)), is(Lists.newArrayList(100)));
    }

    @Test
    public void testInsert() {
        assertThat(wrapper.insert(0, true), is(0));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.insert(1, true), is(0));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.insert(10, true), is(0));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.insert(11, true), is(1));
        assertThat(storage.getEnergyStored(), is(100));

        assertThat(wrapper.insert(0, false), is(0));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.insert(1, false), is(0));
        assertThat(storage.getEnergyStored(), is(101));
        assertThat(wrapper.insert(10, false), is(0));
        assertThat(storage.getEnergyStored(), is(111));
        assertThat(wrapper.insert(11, false), is(1));
        assertThat(storage.getEnergyStored(), is(121));
    }

    @Test
    public void testExtract() {
        assertThat(wrapper.extract(0, true, true), is(0));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.extract(1, true, true), is(1));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.extract(10, true, true), is(10));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.extract(11, true, true), is(0));
        assertThat(storage.getEnergyStored(), is(100));

        assertThat(wrapper.extract(0, false, true), is(10));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.extract(1, false, true), is(10));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.extract(10, false, true), is(10));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.extract(11, false, true), is(10));
        assertThat(storage.getEnergyStored(), is(100));

        assertThat(wrapper.extract(0, true, false), is(0));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.extract(1, true, false), is(1));
        assertThat(storage.getEnergyStored(), is(99));
        assertThat(wrapper.extract(10, true, false), is(10));
        assertThat(storage.getEnergyStored(), is(89));
        assertThat(wrapper.extract(11, true, false), is(0));
        assertThat(storage.getEnergyStored(), is(89));

        assertThat(wrapper.extract(0, false, false), is(10));
        assertThat(storage.getEnergyStored(), is(79));
        assertThat(wrapper.extract(1, false, false), is(10));
        assertThat(storage.getEnergyStored(), is(69));
        assertThat(wrapper.extract(10, false, false), is(10));
        assertThat(storage.getEnergyStored(), is(59));
        assertThat(wrapper.extract(11, false, false), is(10));
        assertThat(storage.getEnergyStored(), is(49));
    }

    @Test
    public void testExtractMax() {
        assertThat(wrapper.extract(0, true), is(0));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.extract(1, true), is(1));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.extract(10, true), is(10));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.extract(11, true), is(10));
        assertThat(storage.getEnergyStored(), is(100));

        assertThat(wrapper.extract(0, false), is(0));
        assertThat(storage.getEnergyStored(), is(100));
        assertThat(wrapper.extract(1, false), is(1));
        assertThat(storage.getEnergyStored(), is(99));
        assertThat(wrapper.extract(10, false), is(10));
        assertThat(storage.getEnergyStored(), is(89));
        assertThat(wrapper.extract(11, false), is(10));
        assertThat(storage.getEnergyStored(), is(79));
    }

}
