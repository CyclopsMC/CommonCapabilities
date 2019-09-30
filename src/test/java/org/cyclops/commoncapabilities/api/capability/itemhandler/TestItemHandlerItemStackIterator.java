package org.cyclops.commoncapabilities.api.capability.itemhandler;

import net.minecraft.item.Items;
import net.minecraft.util.registry.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestItemHandlerItemStackIterator {

    private static IItemHandler HANDLER_EMPTY;
    private static IItemHandler HANDLER;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        Bootstrap.register();

        HANDLER_EMPTY = new ImmutableListItemHandler(NonNullList.withSize(0, ItemStack.EMPTY));
        HANDLER = new ImmutableListItemHandler(NonNullList.from(ItemStack.EMPTY,
                new ItemStack(Items.APPLE),
                new ItemStack(Items.LEAD),
                new ItemStack(Items.BOWL)
        ));
    }

    @Test
    public void testEmpty() {
        Iterator<ItemStack> it = new ItemHandlerItemStackIterator(HANDLER_EMPTY);
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyNext() {
        Iterator<ItemStack> it = new ItemHandlerItemStackIterator(HANDLER_EMPTY);
        it.next();
    }

    @Test
    public void testNonEmpty() {
        Iterator<ItemStack> it = new ItemHandlerItemStackIterator(HANDLER);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.APPLE));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.BOWL));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testNonEmptyOffset() {
        Iterator<ItemStack> it1 = new ItemHandlerItemStackIterator(HANDLER, 1);
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next().getItem(), is(Items.LEAD));
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next().getItem(), is(Items.BOWL));
        assertThat(it1.hasNext(), is(false));

        Iterator<ItemStack> it2 = new ItemHandlerItemStackIterator(HANDLER, 2);
        assertThat(it2.hasNext(), is(true));
        assertThat(it2.next().getItem(), is(Items.BOWL));
        assertThat(it2.hasNext(), is(false));

        Iterator<ItemStack> it3 = new ItemHandlerItemStackIterator(HANDLER, 3);
        assertThat(it3.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNonEmptyOutOfRange() {
        Iterator<ItemStack> it = new ItemHandlerItemStackIterator(HANDLER);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.APPLE));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.BOWL));
        assertThat(it.hasNext(), is(false));

        it.next();
    }

}
