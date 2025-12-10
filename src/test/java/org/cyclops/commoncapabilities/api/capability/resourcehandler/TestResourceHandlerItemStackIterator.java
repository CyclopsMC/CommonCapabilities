package org.cyclops.commoncapabilities.api.capability.resourcehandler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ImmutableListItemHandler;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestResourceHandlerItemStackIterator {

    private static ResourceHandler<ItemResource> HANDLER_EMPTY;
    private static ResourceHandler<ItemResource> HANDLER;

    @BeforeAll
    public static void init() {
        HANDLER_EMPTY = new ImmutableListItemHandler(NonNullList.withSize(0, ItemStack.EMPTY));
        HANDLER = new ImmutableListItemHandler(NonNullList.of(ItemStack.EMPTY,
                new ItemStack(Items.APPLE),
                new ItemStack(Items.LEAD),
                new ItemStack(Items.BOWL)
        ));
    }

    @Test
    public void testEmpty() {
        Iterator<ItemStack> it = new ResourceHandlerIngredientIterator<>(HANDLER_EMPTY, IngredientComponent.ITEMSTACK_CONVERTER);
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testEmptyNext() {
        Iterator<ItemStack> it = new ResourceHandlerIngredientIterator<>(HANDLER_EMPTY, IngredientComponent.ITEMSTACK_CONVERTER);
        Assertions.assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void testNonEmpty() {
        Iterator<ItemStack> it = new ResourceHandlerIngredientIterator<>(HANDLER, IngredientComponent.ITEMSTACK_CONVERTER);
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
        Iterator<ItemStack> it1 = new ResourceHandlerIngredientIterator<>(HANDLER, IngredientComponent.ITEMSTACK_CONVERTER, 1);
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next().getItem(), is(Items.LEAD));
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next().getItem(), is(Items.BOWL));
        assertThat(it1.hasNext(), is(false));

        Iterator<ItemStack> it2 = new ResourceHandlerIngredientIterator<>(HANDLER, IngredientComponent.ITEMSTACK_CONVERTER, 2);
        assertThat(it2.hasNext(), is(true));
        assertThat(it2.next().getItem(), is(Items.BOWL));
        assertThat(it2.hasNext(), is(false));

        Iterator<ItemStack> it3 = new ResourceHandlerIngredientIterator<>(HANDLER, IngredientComponent.ITEMSTACK_CONVERTER, 3);
        assertThat(it3.hasNext(), is(false));
    }

    @Test
    public void testNonEmptyOutOfRange() {
        Iterator<ItemStack> it = new ResourceHandlerIngredientIterator<>(HANDLER, IngredientComponent.ITEMSTACK_CONVERTER);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.APPLE));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.LEAD));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getItem(), is(Items.BOWL));
        assertThat(it.hasNext(), is(false));

        Assertions.assertThrows(NoSuchElementException.class, it::next);
    }

}
