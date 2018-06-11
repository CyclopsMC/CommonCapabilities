package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestEnergyEnergyStorageWrapper {

    private IEnergyStorage innerStorage;
    private IIngredientComponentStorage<Integer, Boolean> storage;
    private IngredientComponentStorageWrapperHandlerEnergyStorage.EnergyStorageWrapper wrapper;

    @Before
    public void beforeEach() {
        innerStorage = new EnergyStorage(1000, 10, 10, 100);
        storage = new IngredientComponentStorageWrapperHandlerEnergyStorage.ComponentStorageWrapper(
                IngredientComponents.ENERGY, innerStorage);
        wrapper = new IngredientComponentStorageWrapperHandlerEnergyStorage.EnergyStorageWrapper(storage);
    }

    @Test
    public void testCanReceive() {
        assertThat(wrapper.canReceive(), is(true));
    }

    @Test
    public void testCanExtract() {
        assertThat(wrapper.canExtract(), is(true));
    }

    @Test
    public void testGetEnergyStored() {
        assertThat(wrapper.getEnergyStored(), is(100));
    }

    @Test
    public void testGetMaxEnergyStored() {
        assertThat(wrapper.getMaxEnergyStored(), is(1000));
    }

    @Test
    public void testReceiveEnergy() {
        assertThat(wrapper.receiveEnergy(0, true), is(0));
        assertThat(innerStorage.getEnergyStored(), is(100));
        assertThat(wrapper.receiveEnergy(1, true), is(1));
        assertThat(innerStorage.getEnergyStored(), is(100));
        assertThat(wrapper.receiveEnergy(10, true), is(10));
        assertThat(innerStorage.getEnergyStored(), is(100));
        assertThat(wrapper.receiveEnergy(11, true), is(10));
        assertThat(innerStorage.getEnergyStored(), is(100));

        assertThat(wrapper.receiveEnergy(0, false), is(0));
        assertThat(innerStorage.getEnergyStored(), is(100));
        assertThat(wrapper.receiveEnergy(1, false), is(1));
        assertThat(innerStorage.getEnergyStored(), is(101));
        assertThat(wrapper.receiveEnergy(10, false), is(10));
        assertThat(innerStorage.getEnergyStored(), is(111));
        assertThat(wrapper.receiveEnergy(11, false), is(10));
        assertThat(innerStorage.getEnergyStored(), is(121));
    }

    @Test
    public void testExtractEnergy() {
        assertThat(wrapper.extractEnergy(0, true), is(0));
        assertThat(innerStorage.getEnergyStored(), is(100));
        assertThat(wrapper.extractEnergy(1, true), is(1));
        assertThat(innerStorage.getEnergyStored(), is(100));
        assertThat(wrapper.extractEnergy(10, true), is(10));
        assertThat(innerStorage.getEnergyStored(), is(100));
        assertThat(wrapper.extractEnergy(11, true), is(10));
        assertThat(innerStorage.getEnergyStored(), is(100));

        assertThat(wrapper.extractEnergy(0, false), is(0));
        assertThat(innerStorage.getEnergyStored(), is(100));
        assertThat(wrapper.extractEnergy(1, false), is(1));
        assertThat(innerStorage.getEnergyStored(), is(99));
        assertThat(wrapper.extractEnergy(10, false), is(10));
        assertThat(innerStorage.getEnergyStored(), is(89));
        assertThat(wrapper.extractEnergy(11, false), is(10));
        assertThat(innerStorage.getEnergyStored(), is(79));
    }

}
