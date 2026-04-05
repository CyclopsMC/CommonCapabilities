package org.cyclops.commoncapabilities.api.capability.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestFilteredItemHandlerItemStackIterator {

    private static ResourceHandler<ItemResource> HANDLER_EMPTY;
    private static ResourceHandler<ItemResource> HANDLER;

    @BeforeAll
    public static void init() {
        // Bind components so ItemStack construction works in 26.1 (components are normally bound during resource reload)
        DataComponentMap defaultComponents = DataComponentMap.builder().set(DataComponents.MAX_STACK_SIZE, 64).build();
        Items.APPLE.builtInRegistryHolder().bindComponents(defaultComponents);
        Items.LEAD.builtInRegistryHolder().bindComponents(defaultComponents);
        Items.BOWL.builtInRegistryHolder().bindComponents(defaultComponents);

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

    @Test
    public void testEmptyNext() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER_EMPTY, new ItemStack(Items.APPLE), ItemMatch.ITEM);
        Assertions.assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void testNonEmptyApple() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER, new ItemStack(Items.APPLE), ItemMatch.ITEM);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.APPLE));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testNonEmptyAppleOutOfRange() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER, new ItemStack(Items.APPLE), ItemMatch.ITEM);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.APPLE));
        assertThat(it.hasNext(), is(false));

        Assertions.assertThrows(NoSuchElementException.class, it::next);
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

    @Test
    public void testNonEmptyLeadOutOfRange() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER, new ItemStack(Items.LEAD), ItemMatch.ITEM);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(false));

        Assertions.assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void testNonEmptyLeadExact() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER, new ItemStack(Items.LEAD, 10), ItemMatch.EXACT);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testNonEmptyLeadExactOutOfRange() {
        Iterator<ItemStack> it = new FilteredItemHandlerItemStackIterator(HANDLER, new ItemStack(Items.LEAD, 10), ItemMatch.EXACT);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(false));

        Assertions.assertThrows(NoSuchElementException.class, it::next);
    }

}
