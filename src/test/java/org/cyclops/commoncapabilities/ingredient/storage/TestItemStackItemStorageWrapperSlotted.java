package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.ingredient.ItemStacksResourceHandlerTesting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestItemStackItemStorageWrapperSlotted {

    private static ItemStack APPLE_1;
    private static ItemStack APPLE_10;

    private static ItemStack APPLE_64;
    private static ItemStack APPLE_8;
    private static ItemStack APPLE_9;
    private static ItemStack APPLE_11;

    private ItemStacksResourceHandlerTesting innerStorage;
    private IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<ItemResource, ItemStack, Integer> storage;
    private IngredientComponentStorageWrapperHandlerResourceHandler.ResourceStorageWrapperSlotted<ItemResource, ItemStack, Integer> wrapper;

    @BeforeEach
    public void beforeEach() {
        APPLE_1 = new ItemStack(Items.APPLE, 1);
        APPLE_10 = new ItemStack(Items.APPLE, 10);

        APPLE_64 = new ItemStack(Items.APPLE, 64);
        APPLE_8 = new ItemStack(Items.APPLE, 8);
        APPLE_9 = new ItemStack(Items.APPLE, 9);
        APPLE_11 = new ItemStack(Items.APPLE, 11);

        innerStorage = new ItemStacksResourceHandlerTesting(10);
        innerStorage.setStackInSlot(2, APPLE_1.copy());
        innerStorage.setStackInSlot(4, APPLE_1.copy());
        innerStorage.setStackInSlot(6, APPLE_10.copy());
        innerStorage.setStackInSlot(8, APPLE_10.copy());
        storage = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<>(IngredientComponents.ITEMSTACK, innerStorage, IngredientComponent.ITEMSTACK_CONVERTER);
        wrapper = new IngredientComponentStorageWrapperHandlerResourceHandler.ResourceStorageWrapperSlotted<>(storage, IngredientComponent.ITEMSTACK_CONVERTER);
    }

    @Test
    public void testGetSlots() {
        assertThat(wrapper.size(), is(10));
    }

    @Test
    public void testGetStackInSlot() {
        assertThat(wrapper.getResource(0), equalTo(ItemResource.EMPTY));
        assertThat(wrapper.getAmountAsInt(0), equalTo(0));
        assertThat(wrapper.getResource(1), equalTo(ItemResource.EMPTY));
        assertThat(wrapper.getAmountAsInt(1), equalTo(0));
        assertThat(wrapper.getResource(2), equalTo(ItemResource.of(Items.APPLE)));
        assertThat(wrapper.getAmountAsInt(2), equalTo(1));
        assertThat(wrapper.getResource(4), equalTo(ItemResource.of(Items.APPLE)));
        assertThat(wrapper.getAmountAsInt(4), equalTo(1));
        assertThat(wrapper.getResource(6), equalTo(ItemResource.of(Items.APPLE)));
        assertThat(wrapper.getAmountAsInt(6), equalTo(10));
        assertThat(wrapper.getResource(8), equalTo(ItemResource.of(Items.APPLE)));
        assertThat(wrapper.getAmountAsInt(8), equalTo(10));
    }

    @Test
    public void testGetStackInSlotTooSmall() {
        Assertions.assertThrows(RuntimeException.class, () -> wrapper.getResource(-1));
    }

    @Test
    public void testGetStackInSlotTooBig() {
        Assertions.assertThrows(RuntimeException.class, () -> wrapper.getResource(10));
    }

    @Test
    public void testInsertItem() {
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(0, ItemResource.of(APPLE_1), APPLE_1.getCount(), tx), equalTo(1));
        }
    }

    @Test
    public void testExtractItem() {
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(2, ItemResource.of(APPLE_1), 10, tx), equalTo(1));
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(4, ItemResource.of(APPLE_1), 10, tx), equalTo(1));
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(6, ItemResource.of(APPLE_1), 10, tx), equalTo(10));
        }

        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(2, ItemResource.of(APPLE_1), 10, tx), equalTo(1));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(2, ItemResource.of(APPLE_1), 10, tx), equalTo(0));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(4, ItemResource.of(APPLE_1), 10, tx), equalTo(1));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(6, ItemResource.of(APPLE_1), 10, tx), equalTo(10));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(8, ItemResource.of(APPLE_1), 10, tx), equalTo(10));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(8, ItemResource.of(APPLE_1), 10, tx), equalTo(0));
            tx.commit();
        }
    }

    @Test
    public void testGetSlotLimit() {
        assertThat(wrapper.getCapacityAsInt(0, ItemResource.of(APPLE_1)), is(99));
        assertThat(wrapper.getCapacityAsInt(1, ItemResource.of(APPLE_1)), is(99));
        assertThat(wrapper.getCapacityAsInt(2, ItemResource.of(APPLE_1)), is(99));
    }

}
