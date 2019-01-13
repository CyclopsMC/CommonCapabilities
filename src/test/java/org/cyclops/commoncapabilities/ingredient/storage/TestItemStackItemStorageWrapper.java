package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientLinkedList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.annotation.Nonnull;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestItemStackItemStorageWrapper {

    private static ItemStack APPLE_1_0;
    private static ItemStack APPLE_1_10;
    private static ItemStack APPLE_10_10;
    private static ItemStack APPLE_10_0;

    private static ItemStack APPLE_64_0;
    private static ItemStack APPLE_64_10;
    private static ItemStack APPLE_8_0;
    private static ItemStack APPLE_9_0;
    private static ItemStack APPLE_11_10;

    private IItemHandler innerStorage;
    private IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper storage;
    private IngredientComponentStorageWrapperHandlerItemStack.ItemStorageWrapper wrapper;

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
        APPLE_8_0 = new ItemStack(Items.APPLE, 8, 0);
        APPLE_9_0 = new ItemStack(Items.APPLE, 9, 0);
        APPLE_11_10 = new ItemStack(Items.APPLE, 11, 10);

        innerStorage = new ItemStackHandler(10);
        ((ItemStackHandler) innerStorage).setStackInSlot(2, APPLE_1_0.copy());
        ((ItemStackHandler) innerStorage).setStackInSlot(4, APPLE_1_10.copy());
        ((ItemStackHandler) innerStorage).setStackInSlot(6, APPLE_10_10.copy());
        ((ItemStackHandler) innerStorage).setStackInSlot(8, APPLE_10_0.copy());
        storage = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper(IngredientComponents.ITEMSTACK, innerStorage);
        wrapper = new IngredientComponentStorageWrapperHandlerItemStack.ItemStorageWrapper(IngredientComponents.ITEMSTACK, storage);
    }

    @Test
    public void testGetSlots() {
        assertThat(wrapper.getSlots(), is(11));
    }

    @Test
    public void testGetStackInSlot() {
        assertThat(eq(wrapper.getStackInSlot(0), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.getStackInSlot(1), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.getStackInSlot(2), APPLE_1_0), is(true));
        assertThat(eq(wrapper.getStackInSlot(4), APPLE_1_10), is(true));
        assertThat(eq(wrapper.getStackInSlot(6), APPLE_10_10), is(true));
        assertThat(eq(wrapper.getStackInSlot(8), APPLE_10_0), is(true));
    }

    @Test
    public void testGetStackInSlotTooBig() {
        assertThat(wrapper.getStackInSlot(11), is(ItemStack.EMPTY));
    }

    @Test
    public void testInsertItem() {
        assertThat(eq(wrapper.insertItem(0, APPLE_1_0, true), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.insertItem(0, APPLE_1_0, false), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractItem() {
        assertThat(eq(wrapper.extractItem(0, 10, true), APPLE_1_0), is(true));
        assertThat(eq(wrapper.extractItem(0, 10, true), APPLE_1_0), is(true));
        assertThat(eq(wrapper.extractItem(0, 10, true), APPLE_1_0), is(true));

        assertThat(eq(wrapper.extractItem(0, 10, false), APPLE_1_0), is(true));
        assertThat(eq(wrapper.extractItem(0, 10, false), APPLE_1_10), is(true));
        assertThat(eq(wrapper.extractItem(0, 10, false), APPLE_10_10), is(true));
        assertThat(eq(wrapper.extractItem(0, 10, false), APPLE_10_0), is(true));
        assertThat(eq(wrapper.extractItem(0, 10, false), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testGetSlotLimit() {
        assertThat(wrapper.getSlotLimit(0), is(Integer.MAX_VALUE));
        assertThat(wrapper.getSlotLimit(1), is(Integer.MAX_VALUE));
        assertThat(wrapper.getSlotLimit(2), is(Integer.MAX_VALUE));
    }

}
