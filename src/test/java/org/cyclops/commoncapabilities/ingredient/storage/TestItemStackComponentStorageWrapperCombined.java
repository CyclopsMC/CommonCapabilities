package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.capability.itemhandler.DefaultSlotlessItemHandlerWrapper;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.ingredient.ItemStacksResourceHandlerTesting;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestItemStackComponentStorageWrapperCombined {

    private static ItemStack APPLE_1;
    private static ItemStack APPLE_10;

    private static ItemStack APPLE_64;
    private static ItemStack APPLE_2;
    private static ItemStack APPLE_3;
    private static ItemStack APPLE_8;
    private static ItemStack APPLE_9;
    private static ItemStack APPLE_11;

    private ItemStacksResourceHandlerTesting storage;
    private IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapperCombined wrapper;

    public static boolean eq(ItemStack a, ItemStack b) {
        return IngredientComponents.ITEMSTACK.getMatcher().matchesExactly(a, b);
    }

    @BeforeEach
    public void beforeEach() {
        APPLE_1 = new ItemStack(Items.APPLE, 1);
        APPLE_10 = new ItemStack(Items.APPLE, 10);

        APPLE_64 = new ItemStack(Items.APPLE, 64);
        APPLE_2 = new ItemStack(Items.APPLE, 2);
        APPLE_8 = new ItemStack(Items.APPLE, 8);
        APPLE_9 = new ItemStack(Items.APPLE, 9);
        APPLE_3 = new ItemStack(Items.APPLE, 3);
        APPLE_11 = new ItemStack(Items.APPLE, 11);

        storage = new ItemStacksResourceHandlerTesting(10);
        storage.setStackInSlot(2, APPLE_1.copy());
        storage.setStackInSlot(6, APPLE_10.copy());
        wrapper = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapperCombined(IngredientComponents.ITEMSTACK, storage, IngredientComponent.ITEMSTACK_CONVERTER, new DefaultSlotlessItemHandlerWrapper(storage));
    }

    @Test
    public void testGetComponent() {
        assertThat(wrapper.getComponent(), is(IngredientComponents.ITEMSTACK));
    }

    @Test
    public void testGetMaxQuantity() {
        assertThat(wrapper.getMaxQuantity(), is(990L));
    }

    @Test
    public void testIterator() {
        assertThat(new IngredientLinkedList<>(IngredientComponents.ITEMSTACK, wrapper.iterator()),
                is(new IngredientLinkedList<>(IngredientComponents.ITEMSTACK,
                        new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, APPLE_1, ItemStack.EMPTY, ItemStack.EMPTY,
                                ItemStack.EMPTY, APPLE_10, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY))));
    }

    @Test
    public void testIteratorMatch() {
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(ItemStack.EMPTY, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_1, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_10)));

        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(ItemStack.EMPTY, ItemMatch.ITEM)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_1, ItemMatch.ITEM)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1, APPLE_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10, ItemMatch.ITEM)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1, APPLE_10)));

        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10, ItemMatch.ANY)), is(
                new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, APPLE_1, ItemStack.EMPTY, ItemStack.EMPTY,
                        ItemStack.EMPTY, APPLE_10, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)));
    }

    @Test
    public void testInsert() {
        assertThat(eq(wrapper.insert(APPLE_64, true), ItemStack.EMPTY), is(true));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(eq(wrapper.insert(APPLE_64, true), ItemStack.EMPTY), is(true));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(eq(wrapper.insert(APPLE_64, true), ItemStack.EMPTY), is(true));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(eq(wrapper.insert(APPLE_64, true), ItemStack.EMPTY), is(true));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(eq(wrapper.insert(APPLE_64, true), ItemStack.EMPTY), is(true));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(eq(wrapper.insert(APPLE_64, false), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(storage.getStackInSlot(1), is(ItemStack.EMPTY));
        assertThat(eq(wrapper.insert(APPLE_64, false), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(storage.getStackInSlot(3), is(ItemStack.EMPTY));
        assertThat(eq(wrapper.insert(APPLE_64, false), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(storage.getStackInSlot(5), is(ItemStack.EMPTY));
        assertThat(eq(wrapper.insert(APPLE_64, false), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(5), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(storage.getStackInSlot(7), is(ItemStack.EMPTY));
        assertThat(eq(wrapper.insert(APPLE_64, false), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(5), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(storage.getStackInSlot(7), is(ItemStack.EMPTY));
        assertThat(eq(wrapper.insert(APPLE_64, false), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(5), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_11), is(true));
        assertThat(eq(storage.getStackInSlot(7), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testInsertFull() {
        ItemStacksResourceHandlerTesting storage = new ItemStacksResourceHandlerTesting(0);
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<ItemResource, ItemStack, Integer> wrapper = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<>(IngredientComponents.ITEMSTACK, storage, IngredientComponent.ITEMSTACK_CONVERTER);
        assertThat(eq(wrapper.insert(APPLE_64, true), APPLE_64), is(true));
    }

    @Test
    public void testExtract() {
        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.ITEM, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.ITEM, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));

        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, false), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.ITEM, false), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, false), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractExactSplit() {
        ItemStacksResourceHandlerTesting storage = new ItemStacksResourceHandlerTesting(10);
        storage.setStackInSlot(2, APPLE_1.copy());
        storage.setStackInSlot(6, APPLE_10.copy());
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<ItemResource, ItemStack, Integer> wrapper =
                new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<>(IngredientComponents.ITEMSTACK, storage, IngredientComponent.ITEMSTACK_CONVERTER);

        assertThat(eq(wrapper.extract(APPLE_11, ItemMatch.EXACT, true), APPLE_11), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(APPLE_11, ItemMatch.EXACT, true), APPLE_11), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));

        assertThat(eq(wrapper.extract(APPLE_11, ItemMatch.EXACT, false), APPLE_11), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(APPLE_11, ItemMatch.EXACT, false), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractStackSizeSplit() {
        ItemStacksResourceHandlerTesting storage = new ItemStacksResourceHandlerTesting(10);
        storage.setStackInSlot(2, APPLE_1.copy());
        storage.setStackInSlot(6, APPLE_10.copy());
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<ItemResource, ItemStack, Integer> wrapper =
                new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<>(IngredientComponents.ITEMSTACK, storage, IngredientComponent.ITEMSTACK_CONVERTER);

        assertThat(eq(wrapper.extract(APPLE_8, ItemMatch.STACKSIZE, true), APPLE_8), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(APPLE_3, ItemMatch.STACKSIZE, true), APPLE_3), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));

        assertThat(eq(wrapper.extract(APPLE_8, ItemMatch.STACKSIZE, false), APPLE_8), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_3), is(true));;
        assertThat(eq(wrapper.extract(APPLE_3, ItemMatch.STACKSIZE, false), APPLE_3), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractNoExtract() {
        ItemStacksResourceHandlerTesting storage = new ItemStacksResourceHandlerTesting(1) {
            @Override
            public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
                return 0;
            }
        };
        storage.setStackInSlot(0, APPLE_10);
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<ItemResource, ItemStack, Integer> wrapper = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<>(IngredientComponents.ITEMSTACK, storage, IngredientComponent.ITEMSTACK_CONVERTER);
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.EXACT, false), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractMax() {
        assertThat(eq(wrapper.extract(10, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(1, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(10, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(10, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));

        assertThat(eq(wrapper.extract(10, false), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(10, false), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(10, false), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
    }

}
