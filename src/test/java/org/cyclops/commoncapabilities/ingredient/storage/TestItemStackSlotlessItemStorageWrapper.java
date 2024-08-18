package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.ModBaseMocked;
import org.cyclops.commoncapabilities.api.capability.itemhandler.DefaultSlotlessItemHandlerWrapper;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestItemStackSlotlessItemStorageWrapper {

    private static ItemStack APPLE_1;
    private static ItemStack APPLE_10;

    private static ItemStack APPLE_64;
    private static ItemStack APPLE_8;
    private static ItemStack APPLE_9;
    private static ItemStack APPLE_11;

    private IItemHandler innerStorage;
    private IngredientComponentStorageWrapperHandlerItemStackSlotless.ComponentStorageWrapper storage;
    private IngredientComponentStorageWrapperHandlerItemStackSlotless.ItemStorageWrapper wrapper;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();
        CyclopsCoreInstance.MOD = new ModBaseMocked();
    }

    public static boolean eq(ItemStack a, ItemStack b) {
        return IngredientComponents.ITEMSTACK.getMatcher().matchesExactly(a, b);
    }

    @Before
    public void beforeEach() {
        APPLE_1 = new ItemStack(Items.APPLE, 1);
        APPLE_10 = new ItemStack(Items.APPLE, 10);

        APPLE_64 = new ItemStack(Items.APPLE, 64);
        APPLE_8 = new ItemStack(Items.APPLE, 8);
        APPLE_9 = new ItemStack(Items.APPLE, 9);
        APPLE_11 = new ItemStack(Items.APPLE, 11);

        innerStorage = new ItemStackHandler(10);
        ((ItemStackHandler) innerStorage).setStackInSlot(2, APPLE_1.copy());
        ((ItemStackHandler) innerStorage).setStackInSlot(6, APPLE_10.copy());
        storage = new IngredientComponentStorageWrapperHandlerItemStackSlotless.ComponentStorageWrapper(IngredientComponents.ITEMSTACK, new DefaultSlotlessItemHandlerWrapper(innerStorage));
        wrapper = new IngredientComponentStorageWrapperHandlerItemStackSlotless.ItemStorageWrapper(IngredientComponents.ITEMSTACK, storage);
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
