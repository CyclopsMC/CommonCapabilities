package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.ingredient.ItemStacksResourceHandlerTesting;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestItemStackComponentStorageWrapper {

    static {
        // Bind components so ItemStack construction works in 26.1 (components are normally bound during resource reload)
        Items.APPLE.builtInRegistryHolder().bindComponents(DataComponentMap.builder().set(DataComponents.MAX_STACK_SIZE, 64).build());
    }

    private static ItemStack APPLE_1;
    private static ItemStack APPLE_10;

    private static ItemStack APPLE_64;
    private static ItemStack APPLE_2;
    private static ItemStack APPLE_3;
    private static ItemStack APPLE_8;
    private static ItemStack APPLE_9;
    private static ItemStack APPLE_11;
    private static ItemStack APPLE_130;
    private static ItemStack APPLE_70;
    private static ItemStack APPLE_60;

    private ItemStacksResourceHandlerTesting storage;
    private IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<ItemResource, ItemStack, Integer> wrapper;

    private ItemStacksResourceHandlerTesting storageLarge;
    private IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<ItemResource, ItemStack, Integer> wrapperLarge;

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
        APPLE_130 = new ItemStack(Items.APPLE, 130);
        APPLE_70 = new ItemStack(Items.APPLE, 70);
        APPLE_60 = new ItemStack(Items.APPLE, 60);

        storage = new ItemStacksResourceHandlerTesting(10);
        storage.set(2, ItemResource.of(APPLE_1), APPLE_1.getCount());
        storage.set(6, ItemResource.of(APPLE_10), APPLE_10.getCount());
        wrapper = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<>(IngredientComponents.ITEMSTACK, storage, IngredientComponent.ITEMSTACK_CONVERTER);

        storageLarge = new ItemStacksResourceHandlerTesting(1) {
            @Override
            public int getCapacity(int slot, ItemResource resource) {
                return 130;
            }
        };
        storageLarge.set(0, ItemResource.of(APPLE_130), APPLE_130.getCount());
        wrapperLarge = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<>(IngredientComponents.ITEMSTACK, storageLarge, IngredientComponent.ITEMSTACK_CONVERTER);
    }

    @Test
    public void testGetComponent() {
        assertThat(wrapper.getComponent(), is(IngredientComponents.ITEMSTACK));
        assertThat(wrapperLarge.getComponent(), is(IngredientComponents.ITEMSTACK));
    }

    @Test
    public void testGetMaxQuantity() {
        assertThat(wrapper.getMaxQuantity(), is(990L));
        assertThat(wrapperLarge.getMaxQuantity(), is(130L));
    }

    @Test
    public void testIterator() {
        assertThat(new IngredientLinkedList<>(IngredientComponents.ITEMSTACK, wrapper.iterator()),
                is(new IngredientLinkedList<>(IngredientComponents.ITEMSTACK,
                        new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, APPLE_1, ItemStack.EMPTY, ItemStack.EMPTY,
                                ItemStack.EMPTY, APPLE_10, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY))));

        assertThat(new IngredientLinkedList<>(IngredientComponents.ITEMSTACK, wrapperLarge.iterator()),
                is(new IngredientLinkedList<>(IngredientComponents.ITEMSTACK,
                        new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_130))));
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

        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapperLarge.iterator(ItemStack.EMPTY, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapperLarge.iterator(APPLE_130, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_130)));
    }

    @Test
    public void testInsert() {
        assertThat(wrapper.insert(APPLE_64, true), is(ItemStack.EMPTY));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64, true), is(ItemStack.EMPTY));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64, true), is(ItemStack.EMPTY));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64, true), is(ItemStack.EMPTY));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64, true), is(ItemStack.EMPTY));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(storage.getStackInSlot(1), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(storage.getStackInSlot(3), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(storage.getStackInSlot(5), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(5), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(storage.getStackInSlot(7), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(5), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(storage.getStackInSlot(7), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(5), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_11), is(true));
        assertThat(storage.getStackInSlot(7), is(ItemStack.EMPTY));
    }

    @Test
    public void testInsertFull() {
        ItemStacksResourceHandlerTesting storage = new ItemStacksResourceHandlerTesting(0);
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<ItemResource, ItemStack, Integer> wrapper = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<>(IngredientComponents.ITEMSTACK, storage, IngredientComponent.ITEMSTACK_CONVERTER);
        assertThat(eq(wrapper.insert(APPLE_64, true), APPLE_64), is(true));

        assertThat(wrapperLarge.insert(APPLE_130, true), is(APPLE_130));
        assertThat(eq(storageLarge.getStackInSlot(0), APPLE_130), is(true));
    }

    @Test
    public void testExtract() {
        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.ITEM, true), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.ITEM, true), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));

        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, false), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, false), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_9), is(true));
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.ITEM, false), APPLE_9), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtract2() {
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.ITEM, true), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.ITEM, true), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));

        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.ITEM, false), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, false), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.ITEM, false), ItemStack.EMPTY), is(true));
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
        assertThat(eq(storage.getStackInSlot(6), APPLE_3), is(true));
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
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<ItemResource, ItemStack, Integer> wrapper =
                new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper<>(IngredientComponents.ITEMSTACK, storage, IngredientComponent.ITEMSTACK_CONVERTER);
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.EXACT, false), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractMax() {
        assertThat(eq(wrapper.extract(10, true), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(1, true), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(10, true), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(wrapper.extract(10, true), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));

        assertThat(eq(wrapper.extract(1, false), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(10, false), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractLarge() {
        assertThat(eq(wrapperLarge.extract(APPLE_130, ItemMatch.ITEM, true), APPLE_130), is(true));
        assertThat(eq(storageLarge.getStackInSlot(0), APPLE_130), is(true));

        assertThat(eq(wrapperLarge.extract(APPLE_130, ItemMatch.ITEM, false), APPLE_130), is(true));
        assertThat(eq(storageLarge.getStackInSlot(0), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractLargePartial() {
        assertThat(eq(wrapperLarge.extract(APPLE_70, ItemMatch.ITEM, true), APPLE_70), is(true));
        assertThat(eq(storageLarge.getStackInSlot(0), APPLE_130), is(true));

        assertThat(eq(wrapperLarge.extract(APPLE_70, ItemMatch.ITEM, false), APPLE_70), is(true));
        assertThat(eq(storageLarge.getStackInSlot(0), APPLE_60), is(true));
    }

}
