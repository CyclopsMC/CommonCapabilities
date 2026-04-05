package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.capability.itemhandler.DefaultSlotlessItemHandlerWrapper;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.ingredient.ItemStacksResourceHandlerTesting;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestItemStackSlotlessItemStorageWrapper {

    private static ItemStack APPLE_1;
    private static ItemStack APPLE_10;

    private static ItemStack APPLE_64;
    private static ItemStack APPLE_8;
    private static ItemStack APPLE_9;
    private static ItemStack APPLE_11;

    private ItemStacksResourceHandlerTesting innerStorage;
    private IngredientComponentStorageWrapperHandlerItemStackSlotless.ComponentStorageWrapper storage;
    private IngredientComponentStorageWrapperHandlerItemStackSlotless.ItemStorageWrapper wrapper;

    static {
        // Bind components so ItemStack construction works in 26.1 (components are normally bound during resource reload)
        Items.APPLE.builtInRegistryHolder().bindComponents(DataComponentMap.builder().set(DataComponents.MAX_STACK_SIZE, 64).build());
    }

    public static boolean eq(ItemStack a, ItemStack b) {
        return IngredientComponents.ITEMSTACK.getMatcher().matchesExactly(a, b);
    }

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
        innerStorage.setStackInSlot(6, APPLE_10.copy());
        storage = new IngredientComponentStorageWrapperHandlerItemStackSlotless.ComponentStorageWrapper(IngredientComponents.ITEMSTACK, new DefaultSlotlessItemHandlerWrapper(innerStorage));
        wrapper = new IngredientComponentStorageWrapperHandlerItemStackSlotless.ItemStorageWrapper(storage);
    }

    @Test
    public void testGetItems() {
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.getItems()),
                is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, APPLE_1, ItemStack.EMPTY, ItemStack.EMPTY,
                        ItemStack.EMPTY, APPLE_10, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)));
    }

    @Test
    public void testFindItems() {
        assertThat(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, wrapper.findItems(ItemStack.EMPTY, ItemMatch.ANY)),
                is(new IngredientArrayList<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemStack.EMPTY, APPLE_1, ItemStack.EMPTY, ItemStack.EMPTY,
                        ItemStack.EMPTY, APPLE_10, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)));
    }

    @Test
    public void testInsertItem() {
        assertThat(eq(wrapper.insertItem(APPLE_1, true), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.insertItem(APPLE_1, false), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractItem() {
        assertThat(eq(wrapper.extractItem(10, true), APPLE_1), is(true));
        assertThat(eq(wrapper.extractItem(10, true), APPLE_1), is(true));
        assertThat(eq(wrapper.extractItem(0, true), ItemStack.EMPTY), is(true));

        assertThat(eq(wrapper.extractItem(10, false), APPLE_1), is(true));
        assertThat(eq(wrapper.extractItem(0, false), ItemStack.EMPTY), is(true));
        assertThat(eq(wrapper.extractItem(10, false), APPLE_10), is(true));
        assertThat(eq(wrapper.extractItem(10, false), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testExtractItemMatch() {
        assertThat(eq(wrapper.extractItem(APPLE_10, ItemMatch.EXACT, true), APPLE_10), is(true));
        assertThat(eq(wrapper.extractItem(APPLE_10, ItemMatch.EXACT, true), APPLE_10), is(true));
        assertThat(eq(wrapper.extractItem(APPLE_10, ItemMatch.EXACT, true), APPLE_10), is(true));

        assertThat(eq(wrapper.extractItem(APPLE_10, ItemMatch.EXACT, false), APPLE_10), is(true));
        assertThat(eq(wrapper.extractItem(APPLE_10, ItemMatch.EXACT, false), APPLE_10), is(false));
        assertThat(eq(wrapper.extractItem(APPLE_1, ItemMatch.EXACT, false), APPLE_1), is(true));
        assertThat(eq(wrapper.extractItem(APPLE_1, ItemMatch.EXACT, false), APPLE_1), is(false));
    }

    @Test
    public void testGetLimit() {
        assertThat(wrapper.getLimit(), is(990));
    }

}
