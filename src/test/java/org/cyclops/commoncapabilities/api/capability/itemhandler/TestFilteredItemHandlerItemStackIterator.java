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

public class TestFilteredItemHandlerItemStackIterator {

    private static IItemHandler HANDLER_EMPTY;
    private static IItemHandler HANDLER;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        Bootstrap.bootStrap();

        HANDLER_EMPTY = new ImmutableListItemHandler(NonNullList.withSize(0, ItemStack.EMPTY));
        HANDLER = new ImmutableListItemHandler(NonNullList.of(ItemStack.EMPTY,
                new ItemStack(Items.APPLE),
                new ItemStack(Items.LEAD),
                new ItemStack(Items.LEAD, 10),
                new ItemStack(Items.BOWL)
        ));
    }

    @Test
    public void testEmpty() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER_EMPTY, new ItemStack(Items.APPLE), ItemMatch.ITEM);
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyNext() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER_EMPTY, new ItemStack(Items.APPLE), ItemMatch.ITEM);
        it.next();
    }

    @Test
    public void testNonEmptyApple() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER, new ItemStack(Items.APPLE), ItemMatch.ITEM);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.APPLE));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNonEmptyAppleOutOfRange() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER, new ItemStack(Items.APPLE), ItemMatch.ITEM);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.APPLE));
        assertThat(it.hasNext(), is(false));

        it.next();
    }

    @Test
    public void testNonEmptyLead() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER, new ItemStack(Items.LEAD), ItemMatch.ITEM);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNonEmptyLeadOutOfRange() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER, new ItemStack(Items.LEAD), ItemMatch.ITEM);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(false));

        it.next();
    }

    @Test
    public void testNonEmptyLeadExact() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER, new ItemStack(Items.LEAD, 10), ItemMatch.EXACT);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNonEmptyLeadExactOutOfRange() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER, new ItemStack(Items.LEAD, 10), ItemMatch.EXACT);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(false));

        it.next();
    }

}
