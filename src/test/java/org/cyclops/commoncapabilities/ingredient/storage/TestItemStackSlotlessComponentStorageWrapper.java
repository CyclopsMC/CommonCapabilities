package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.capability.itemhandler.DefaultSlotlessItemHandlerWrapper;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ISlotlessItemHandler;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.itemhandler.SlotlessItemHandlerWrapper;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientLinkedList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.annotation.Nonnull;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestItemStackSlotlessComponentStorageWrapper {

    private static ItemStack APPLE_1_0;
    private static ItemStack APPLE_1_10;
    private static ItemStack APPLE_10_10;
    private static ItemStack APPLE_10_0;

    private static ItemStack APPLE_64_0;
    private static ItemStack APPLE_64_10;
    private static ItemStack APPLE_2_0;
    private static ItemStack APPLE_8_0;
    private static ItemStack APPLE_9_0;
    private static ItemStack APPLE_3_10;
    private static ItemStack APPLE_8_10;
    private static ItemStack APPLE_9_10;
    private static ItemStack APPLE_11_0;
    private static ItemStack APPLE_11_10;

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
        APPLE_1_0 = new ItemStack(Items.APPLE, 1, 0);
        APPLE_1_10 = new ItemStack(Items.APPLE, 1, 10);
        APPLE_10_10 = new ItemStack(Items.APPLE, 10, 10);
        APPLE_10_0 = new ItemStack(Items.APPLE, 10, 0);

        APPLE_64_0 = new ItemStack(Items.APPLE, 64, 0);
        APPLE_64_10 = new ItemStack(Items.APPLE, 64, 10);
        APPLE_2_0 = new ItemStack(Items.APPLE, 2, 0);
        APPLE_8_0 = new ItemStack(Items.APPLE, 8, 0);
        APPLE_9_0 = new ItemStack(Items.APPLE, 9, 0);
        APPLE_3_10 = new ItemStack(Items.APPLE, 3, 10);
        APPLE_8_10 = new ItemStack(Items.APPLE, 8, 10);
        APPLE_9_10 = new ItemStack(Items.APPLE, 9, 10);
        APPLE_11_0 = new ItemStack(Items.APPLE, 11, 0);
        APPLE_11_10 = new ItemStack(Items.APPLE, 11, 10);

        storage = new ItemStackHandler(10);
        ((ItemStackHandler) storage).setStackInSlot(2, APPLE_1_0.copy());
        ((ItemStackHandler) storage).setStackInSlot(4, APPLE_1_10.copy());
        ((ItemStackHandler) storage).setStackInSlot(6, APPLE_10_10.copy());
        ((ItemStackHandler) storage).setStackInSlot(8, APPLE_10_0.copy());
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
                        new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, APPLE_1_0, ItemStack.EMPTY, APPLE_1_10,
                                ItemStack.EMPTY, APPLE_10_10, ItemStack.EMPTY, APPLE_10_0, ItemStack.EMPTY))));
    }

    @Test
    public void testIteratorMatch() {
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(ItemStack.EMPTY, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_1_0, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1_0)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_1_10, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10_10, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_10_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10_0, ItemMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_10_0)));

        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(ItemStack.EMPTY, ItemMatch.ITEM | ItemMatch.DAMAGE)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_1_0, ItemMatch.ITEM | ItemMatch.DAMAGE)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1_0, APPLE_10_0)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_1_10, ItemMatch.ITEM | ItemMatch.DAMAGE)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1_10, APPLE_10_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10_10, ItemMatch.ITEM | ItemMatch.DAMAGE)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1_10, APPLE_10_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10_0, ItemMatch.ITEM | ItemMatch.DAMAGE)), is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, APPLE_1_0, APPLE_10_0)));

        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.iterator(APPLE_10_0, ItemMatch.ANY)), is(
                new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, APPLE_1_0, ItemStack.EMPTY, APPLE_1_10,
                        ItemStack.EMPTY, APPLE_10_10, ItemStack.EMPTY, APPLE_10_0, ItemStack.EMPTY)));
    }

    @Test
    public void testInsert() {
        assertThat(wrapper.insert(APPLE_64_0, true), is(ItemStack.EMPTY));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64_0, true), is(ItemStack.EMPTY));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64_0, true), is(ItemStack.EMPTY));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64_10, true), is(ItemStack.EMPTY));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64_10, true), is(ItemStack.EMPTY));
        assertThat(storage.getStackInSlot(0), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64_0, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64_0), is(true));
        assertThat(storage.getStackInSlot(1), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64_0, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64_0), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(storage.getStackInSlot(3), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64_0, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64_0), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64_0), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1_10), is(true));
        assertThat(storage.getStackInSlot(5), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64_10, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64_0), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64_0), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_64_10), is(true));
        assertThat(eq(storage.getStackInSlot(5), APPLE_1_10), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10_10), is(true));
        assertThat(storage.getStackInSlot(7), is(ItemStack.EMPTY));
        assertThat(wrapper.insert(APPLE_64_10, false), is(ItemStack.EMPTY));
        assertThat(eq(storage.getStackInSlot(0), APPLE_64_0), is(true));
        assertThat(eq(storage.getStackInSlot(1), APPLE_64_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_64_0), is(true));
        assertThat(eq(storage.getStackInSlot(3), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_64_10), is(true));
        assertThat(eq(storage.getStackInSlot(5), APPLE_64_10), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_11_10), is(true));
        assertThat(storage.getStackInSlot(7), is(ItemStack.EMPTY));
    }

    @Test
    public void testInsertFull() {
        ItemStackHandler storage = new ItemStackHandler(0);
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper wrapper = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper(IngredientComponents.ITEMSTACK, storage);
        assertThat(eq(wrapper.insert(APPLE_64_0, true), APPLE_64_0), is(true));
    }

    @Test
    public void testExtract() {
        assertThat(eq(wrapper.extract(APPLE_1_0, ItemMatch.ITEM | ItemMatch.DAMAGE, true), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(eq(wrapper.extract(APPLE_1_10, ItemMatch.ITEM | ItemMatch.DAMAGE, true), APPLE_1_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(eq(wrapper.extract(APPLE_10_0, ItemMatch.ITEM | ItemMatch.DAMAGE, true), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(eq(wrapper.extract(APPLE_10_10, ItemMatch.ITEM | ItemMatch.DAMAGE, true), APPLE_1_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));

        assertThat(eq(wrapper.extract(APPLE_1_0, ItemMatch.ITEM | ItemMatch.DAMAGE, false), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1_10), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10_0), is(true));
        assertThat(eq(wrapper.extract(APPLE_1_10, ItemMatch.ITEM | ItemMatch.DAMAGE, false), APPLE_1_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10_0), is(true));
        assertThat(eq(wrapper.extract(APPLE_10_0, ItemMatch.ITEM | ItemMatch.DAMAGE, false), APPLE_10_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(APPLE_10_10, ItemMatch.ITEM | ItemMatch.DAMAGE, false), APPLE_10_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(8), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(APPLE_10_10, ItemMatch.ITEM | ItemMatch.DAMAGE, false), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(8), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractExactSplit() {
        IItemHandler storage = new ItemStackHandler(10);
        ((ItemStackHandler) storage).setStackInSlot(2, APPLE_1_0.copy());
        ((ItemStackHandler) storage).setStackInSlot(4, APPLE_1_10.copy());
        ((ItemStackHandler) storage).setStackInSlot(6, APPLE_10_10.copy());
        ((ItemStackHandler) storage).setStackInSlot(8, APPLE_10_0.copy());
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper wrapper =
                new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper(IngredientComponents.ITEMSTACK, storage);

        assertThat(eq(wrapper.extract(APPLE_11_0, ItemMatch.EXACT, true), APPLE_11_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1_10), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10_0), is(true));
        assertThat(eq(wrapper.extract(APPLE_11_10, ItemMatch.EXACT, true), APPLE_11_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1_10), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10_0), is(true));

        assertThat(eq(wrapper.extract(APPLE_11_0, ItemMatch.EXACT, false), APPLE_11_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1_10), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(APPLE_11_10, ItemMatch.EXACT, false), APPLE_11_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(8), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractStackSizeSplit() {
        IItemHandler storage = new ItemStackHandler(10);
        ((ItemStackHandler) storage).setStackInSlot(2, APPLE_1_0.copy());
        ((ItemStackHandler) storage).setStackInSlot(4, APPLE_1_10.copy());
        ((ItemStackHandler) storage).setStackInSlot(6, APPLE_10_10.copy());
        ((ItemStackHandler) storage).setStackInSlot(8, APPLE_10_0.copy());
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper wrapper =
                new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper(IngredientComponents.ITEMSTACK, storage);

        assertThat(eq(wrapper.extract(APPLE_8_0, ItemMatch.STACKSIZE, true), APPLE_8_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1_10), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10_0), is(true));
        assertThat(eq(wrapper.extract(APPLE_9_0, ItemMatch.STACKSIZE, true), APPLE_9_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1_10), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10_0), is(true));

        assertThat(eq(wrapper.extract(APPLE_8_0, ItemMatch.STACKSIZE, false), APPLE_8_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_3_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10_0), is(true));
        assertThat(eq(wrapper.extract(APPLE_9_0, ItemMatch.STACKSIZE, false), APPLE_9_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_3_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_2_0), is(true));
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
        storage.setStackInSlot(0, APPLE_10_10);
        IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper wrapper = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper(IngredientComponents.ITEMSTACK, storage);
        assertThat(eq(wrapper.extract(APPLE_10_10, ItemMatch.EXACT, false), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractMax() {
        assertThat(eq(wrapper.extract(10, true), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(eq(wrapper.extract(1, true), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(eq(wrapper.extract(10, true), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(eq(wrapper.extract(10, true), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), APPLE_1_0), is(true));

        assertThat(eq(wrapper.extract(10, false), APPLE_1_0), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), APPLE_1_10), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10_0), is(true));
        assertThat(eq(wrapper.extract(1, false), APPLE_1_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), APPLE_10_10), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10_0), is(true));
        assertThat(eq(wrapper.extract(10, false), APPLE_10_10), is(true));
        assertThat(eq(storage.getStackInSlot(2), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(4), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(6), ItemStack.EMPTY), is(true));
        assertThat(eq(storage.getStackInSlot(8), APPLE_10_0), is(true));
        assertThat(eq(wrapper.extract(10, false), APPLE_10_0), is(true));
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
