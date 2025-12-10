package org.cyclops.commoncapabilities.ingredient.storage;

import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestEnergyEnergyStorageWrapper {

    private EnergyHandler innerStorage;
    private IIngredientComponentStorage<Long, Boolean> storage;
    private IngredientComponentStorageWrapperHandlerEnergyHandler.EnergyStorageWrapper wrapper;

    @BeforeEach
    public void beforeEach() {
        innerStorage = new SimpleEnergyHandler(1000, 10, 10, 100);
        storage = new IngredientComponentStorageWrapperHandlerEnergyHandler.ComponentStorageWrapper(
                IngredientComponents.ENERGY, innerStorage);
        wrapper = new IngredientComponentStorageWrapperHandlerEnergyHandler.EnergyStorageWrapper(storage);
    }

    @Test
    public void testGetEnergyStored() {
        assertThat(wrapper.getAmountAsLong(), is(100L));
    }

    @Test
    public void testGetMaxEnergyStored() {
        assertThat(wrapper.getCapacityAsLong(), is(1000L));
    }

    @Test
    public void testReceiveEnergy() {
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(0, tx), is(0));
        }
        assertThat(innerStorage.getAmountAsInt(), is(100));
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(1, tx), is(1));
        }
        assertThat(innerStorage.getAmountAsInt(), is(100));
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(10, tx), is(10));
        }
        assertThat(innerStorage.getAmountAsInt(), is(100));
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(11, tx), is(10));
        }
        assertThat(innerStorage.getAmountAsInt(), is(100));

        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(0, tx), is(0));
            tx.commit();
        }
        assertThat(innerStorage.getAmountAsInt(), is(100));
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(1, tx), is(1));
            tx.commit();
        }
        assertThat(innerStorage.getAmountAsInt(), is(101));
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(10, tx), is(10));
            tx.commit();
        }
        assertThat(innerStorage.getAmountAsInt(), is(111));
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(11, tx), is(10));
            tx.commit();
        }
        assertThat(innerStorage.getAmountAsInt(), is(121));
    }

    @Test
    public void testExtractEnergy() {
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(0, tx), is(0));
        }
        assertThat(innerStorage.getAmountAsInt(), is(100));
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(1, tx), is(1));
        }
        assertThat(innerStorage.getAmountAsInt(), is(100));
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(10, tx), is(10));
        }
        assertThat(innerStorage.getAmountAsInt(), is(100));
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(11, tx), is(10));
        }
        assertThat(innerStorage.getAmountAsInt(), is(100));

        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(0, tx), is(0));
            tx.commit();
        }
        assertThat(innerStorage.getAmountAsInt(), is(100));
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(1, tx), is(1));
            tx.commit();
        }
        assertThat(innerStorage.getAmountAsInt(), is(99));
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(10, tx), is(10));
            tx.commit();
        }
        assertThat(innerStorage.getAmountAsInt(), is(89));
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(11, tx), is(10));
            tx.commit();
        }
        assertThat(innerStorage.getAmountAsInt(), is(79));
    }

}
