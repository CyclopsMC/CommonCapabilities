package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Bootstrap;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.capability.itemhandler.DefaultSlotlessItemHandlerWrapper;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientLinkedList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.annotation.Nonnull;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestItemStackSlotlessComponentStorageWrapper {

    private static ItemStack APPLE_1;
    private static ItemStack APPLE_10;

    private static ItemStack APPLE_64;
    private static ItemStack APPLE_2;
    private static ItemStack APPLE_3;
    private static ItemStack APPLE_8;
    private static ItemStack APPLE_9;
    private static ItemStack APPLE_11;

    private IItemHandler storage;
    private IngredientComponentStorageWrapperHandlerItemStackSlotless.ComponentStorageWrapper wrapper;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        Bootstrap.register();
    }

    public static boolean eq(ItemStack a, ItemStack b) {
        return IngredientComponents.ITEMSTACK.getMatcher().matchesExactly(a, b);
    }

    @Before
    public void beforeEach() {
        APPLE_1 = new ItemStack(Items.APPLE, 1);
        APPLE_10 = new ItemStack(Items.APPLE, 10);

        APPLE_64 = new ItemStack(Items.APPLE, 64);
        APPLE_2 = new ItemStack(Items.APPLE, 2);
        APPLE_8 = new ItemStack(Items.APPLE, 8);
        APPLE_9 = new ItemStack(Items.APPLE, 9);
        APPLE_3 = new ItemStack(Items.APPLE, 3);
        APPLE_11 = new ItemStack(Items.APPLE, 11);

        storage = new ItemStackHandler(10);
        ((ItemStackHandler) storage).setStackInSlot(2, APPLE_1.copy());
        ((ItemStackHandler) storage).setStackInSlot(4, APPLE_1.copy());
        ((ItemStackHandler) storage).setStackInSlot(6, APPLE_10.copy());
        ((ItemStackHandler) storage).setStackInSlot(8, APPLE_10.copy());
        wrapper = new IngredientComponentStorageWrapperHandlerItemStackSlotless.ComponentStorageWrapper(IngredientComponents.ITEMSTACK, new DefaultSlotlessItemHandlerWrapper(storage));
    }

    @Test
    public void testGetComponent() {
        assertThat(wrapper.getComponent(), is(IngredientComponents.ITEMSTACK));
    }

    @Test
    public void testGetMaxQuantity() {
        assertThat(wrapper.getMaxQuantity(), is(640L));
    }

    @Test
    public void testIterator() {
        assertThat(new IngredientLinkedList<>(IngredientComponents.ITEMSTACK, wrapper.iterator()),
                is(new IngredientLinkedList<>(IngredientComponents.ITEMSTACK,
                        new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, APPLE_1, ItemStack.EMPTY, APPLE_1,
                                ItemStack.EMPTY, APPLE_10, ItemStack.EMPTY, APPLE_10, ItemStack.EMPTY))));
    }

    @Test
    public void testIteratorMatch() {
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(ItemStack.EMPTY, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_1, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_1, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_10)));

        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(ItemStack.EMPTY, ItemMatch.ITEM)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_1, ItemMatch.ITEM)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1, APPLE_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_1, ItemMatch.ITEM)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1, APPLE_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10, ItemMatch.ITEM)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1, APPLE_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10, ItemMatch.ITEM)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1, APPLE_10)));

        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10, ItemMatch.ANY)), is(
                new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, APPLE_1, ItemStack.EMPTY, APPLE_1,
                        ItemStack.EMPTY, APPLE_10, ItemStack.EMPTY, APPLE_10, ItemStack.EMPTY)));
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
        assertThat(eq(storage.getStackInSlot(4), APPLE_1), is(true));
        assertThat(storage.getStackInSlot(5), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(5), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(storage.getStackInSlot(7), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(5), APPLE_64), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_11), is(true));
        assertThat(storage.getStackInSlot(7), is(ItemStack.EMPTY));
    }

    @Test
    public void testInsertFull() {
        ItemStackHandler storage = new ItemStackHandler(0);
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper wrapper = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper(IngredientComponents.ITEMSTACK, storage);
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
        assertThat(eq(storage.getStackInSlot(4), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, false), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.ITEM, false), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(APPLE_1, ItemMatch.ITEM, false), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_9), is(true));
        assertThat(eq(storage.getStackInSlot(8), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(APPLE_10, ItemMatch.ITEM, false), APPLE_9), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(8), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractExactSplit() {
        IItemHandler storage = new ItemStackHandler(10);
        ((ItemStackHandler) storage).setStackInSlot(2, APPLE_1.copy());
        ((ItemStackHandler) storage).setStackInSlot(4, APPLE_1.copy());
        ((ItemStackHandler) storage).setStackInSlot(6, APPLE_10.copy());
        ((ItemStackHandler) storage).setStackInSlot(8, APPLE_10.copy());
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper wrapper =
                new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper(IngredientComponents.ITEMSTACK, storage);

        assertThat(eq(wrapper.extract(APPLE_11, ItemMatch.EXACT, true), APPLE_11), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(APPLE_11, ItemMatch.EXACT, true), APPLE_11), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10), is(true));

        assertThat(eq(wrapper.extract(APPLE_11, ItemMatch.EXACT, false), APPLE_11), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(APPLE_11, ItemMatch.EXACT, false), APPLE_11), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(8), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractStackSizeSplit() {
        IItemHandler storage = new ItemStackHandler(10);
        ((ItemStackHandler) storage).setStackInSlot(2, APPLE_1.copy());
        ((ItemStackHandler) storage).setStackInSlot(4, APPLE_1.copy());
        ((ItemStackHandler) storage).setStackInSlot(6, APPLE_10.copy());
        ((ItemStackHandler) storage).setStackInSlot(8, APPLE_10.copy());
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper wrapper =
                new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper(IngredientComponents.ITEMSTACK, storage);

        assertThat(eq(wrapper.extract(APPLE_8, ItemMatch.STACKSIZE, true), APPLE_8), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(APPLE_9, ItemMatch.STACKSIZE, true), APPLE_9), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10), is(true));

        assertThat(eq(wrapper.extract(APPLE_8, ItemMatch.STACKSIZE, false), APPLE_8), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_3), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(APPLE_9, ItemMatch.STACKSIZE, false), APPLE_9), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_3), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_2), is(true));
    }

    @Test
    public void testExtractNoExtract() {
        ItemStackHandler storage = new ItemStackHandler(1) {
            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }
        };
        storage.setStackInSlot(0, APPLE_10);
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper wrapper = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper(IngredientComponents.ITEMSTACK, storage);
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
        assertThat(eq(storage.getStackInSlot(4), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(1, false), APPLE_1), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(10, false), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10), is(true));
        assertThat(eq(wrapper.extract(10, false), APPLE_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(8), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(10, false), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(8), ItemStack.EMPTY), is(true));
    }

}
