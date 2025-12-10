package org.cyclops.commoncapabilities.api.capability.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.cyclops.commoncapabilities.ingredient.DataComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.PrimitiveIterator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.cyclops.commoncapabilities.api.ingredient.IngredientTestMatcher.isItemStack;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author rubensworks
 */
public class TestSlotlessItemHandlerWrapper {

    private ResourceHandler<ItemResource> handlerEmpty;
    private ResourceHandler<ItemResource> handler;
    private Supplier<PrimitiveIterator.OfInt> itEmpty;
    private Supplier<PrimitiveIterator.OfInt> it0;
    private Supplier<PrimitiveIterator.OfInt> it9;
    private Supplier<PrimitiveIterator.OfInt> itAll;

    @BeforeEach
    public void init() {
        handlerEmpty = new ItemStacksResourceHandler(NonNullList.withSize(10, ItemStack.EMPTY));
        handler = new ItemStacksResourceHandler(NonNullList.of(ItemStack.EMPTY,
                new ItemStack(Items.APPLE, 5),
                new ItemStack(Items.LEAD, 11),
                new ItemStack(Items.BOWL, 64),
                new ItemStack(Items.APPLE, 60)
        ));

        itEmpty = () -> IntStream.of().iterator();
        it0 = () -> IntStream.of(0).iterator();
        it9 = () -> IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).iterator();
        itAll = () -> IntStream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).iterator();

        ItemMatch.DATA_COMPARATOR = DataComparator.INSTANCE = new DataComparator(null);
    }

    @Test
    public void testInsertEmpty() {
        SlotlessItemHandlerWrapper wrapper = new SlotlessItemHandlerWrapperDummy(handlerEmpty,
                itEmpty,
                itEmpty,
                itEmpty,
                itEmpty,
                itEmpty);

        ItemStack inserting = new ItemStack(Items.DIAMOND, 10);
        assertThat(wrapper.insertItem(inserting, true), isItemStack(inserting));
        assertThat(wrapper.insertItem(inserting, false), isItemStack(inserting));
    }

    @Test
    public void testInsertFillable() {
        SlotlessItemHandlerWrapper wrapper = new SlotlessItemHandlerWrapperDummy(handlerEmpty,
                it0,
                itEmpty,
                itEmpty,
                it9,
                itEmpty);

        ItemStack inserting0 = new ItemStack(Items.DIAMOND, 10);
        assertThat(wrapper.insertItem(inserting0, true), isItemStack(ItemStack.EMPTY));
        assertThat(wrapper.insertItem(inserting0, false), isItemStack(ItemStack.EMPTY));
        assertThat(handlerEmpty.getResource(0), equalTo(ItemResource.of(Items.DIAMOND)));
        assertThat(handlerEmpty.getAmountAsInt(0), equalTo(10));

        ItemStack inserting1 = new ItemStack(Items.APPLE, 10);
        assertThat(wrapper.insertItem(inserting1, true), isItemStack(ItemStack.EMPTY));
        assertThat(wrapper.insertItem(inserting1, false), isItemStack(ItemStack.EMPTY));
        assertThat(handlerEmpty.getResource(1), equalTo(ItemResource.of(Items.APPLE)));
        assertThat(handlerEmpty.getAmountAsInt(1), equalTo(10));
    }

    @Test
    public void testInsertFillableLoop() {
        SlotlessItemHandlerWrapper wrapper = new SlotlessItemHandlerWrapperDummy(handlerEmpty,
                it0,
                itEmpty,
                itEmpty,
                it9,
                itEmpty);

        ItemStack inserting0 = new ItemStack(Items.DIAMOND, 10);
        assertThat(wrapper.insertItem(inserting0, true), isItemStack(ItemStack.EMPTY));
        assertThat(wrapper.insertItem(inserting0, false), isItemStack(ItemStack.EMPTY));
        assertThat(handlerEmpty.getResource(0), equalTo(ItemResource.of(Items.DIAMOND)));
        assertThat(handlerEmpty.getAmountAsInt(0), equalTo(10));

        ItemStack inserting1 = new ItemStack(Items.APPLE, 10);
        assertThat(wrapper.insertItem(inserting1, true), isItemStack(ItemStack.EMPTY));
        assertThat(wrapper.insertItem(inserting1, false), isItemStack(ItemStack.EMPTY));
        assertThat(handlerEmpty.getResource(1), equalTo(ItemResource.of(Items.APPLE)));
        assertThat(handlerEmpty.getAmountAsInt(1), equalTo(10));

        ItemStack inserting2 = new ItemStack(Items.DIAMOND, 64);
        assertThat(wrapper.insertItem(inserting2, true), isItemStack(ItemStack.EMPTY));
        assertThat(wrapper.insertItem(inserting2, false), isItemStack(ItemStack.EMPTY));
        assertThat(handlerEmpty.getResource(0), equalTo(ItemResource.of(Items.DIAMOND)));
        assertThat(handlerEmpty.getAmountAsInt(0), equalTo(64));
        assertThat(handlerEmpty.getResource(2), equalTo(ItemResource.of(Items.DIAMOND)));
        assertThat(handlerEmpty.getAmountAsInt(2), equalTo(10));

        ItemStack inserting3 = new ItemStack(Items.DIAMOND, 100);
        assertThat(wrapper.insertItem(inserting3, true), isItemStack(ItemStack.EMPTY));
        assertThat(wrapper.insertItem(inserting3, false), isItemStack(ItemStack.EMPTY));
        assertThat(handlerEmpty.getResource(0), equalTo(ItemResource.of(Items.DIAMOND)));
        assertThat(handlerEmpty.getAmountAsInt(0), equalTo(64));
        assertThat(handlerEmpty.getResource(2), equalTo(ItemResource.of(Items.DIAMOND)));
        assertThat(handlerEmpty.getAmountAsInt(2), equalTo(64));
        assertThat(handlerEmpty.getResource(3), equalTo(ItemResource.of(Items.DIAMOND)));
        assertThat(handlerEmpty.getAmountAsInt(3), equalTo(46));
    }

    @Test
    public void testExtractAmountEmpty() {
        SlotlessItemHandlerWrapper wrapper = new SlotlessItemHandlerWrapperDummy(handlerEmpty,
                itEmpty,
                itEmpty,
                itEmpty,
                itEmpty,
                itEmpty);

        assertThat(wrapper.extractItem(10, true), isItemStack(ItemStack.EMPTY));
        assertThat(wrapper.extractItem(10, false), isItemStack(ItemStack.EMPTY));
    }

    @Test
    public void testExtractAmountExtractable0() {
        SlotlessItemHandlerWrapper wrapper = new SlotlessItemHandlerWrapperDummy(handler,
                itEmpty,
                itEmpty,
                itEmpty,
                itEmpty,
                it0);

        assertThat(wrapper.extractItem(10, true), isItemStack(new ItemStack(Items.APPLE, 5)));
        assertThat(wrapper.extractItem(10, false), isItemStack(new ItemStack(Items.APPLE, 5)));
        assertThat(handler.getResource(0), equalTo(ItemResource.EMPTY));
        assertThat(handler.getAmountAsInt(0), equalTo(0));
        assertThat(handler.getResource(1), equalTo(ItemResource.of(Items.LEAD)));
        assertThat(handler.getAmountAsInt(1), equalTo(11));
        assertThat(handler.getResource(3), equalTo(ItemResource.of(Items.APPLE)));
        assertThat(handler.getAmountAsInt(3), equalTo(60));
    }

    @Test
    public void testExtractAmountExtractableAll() {
        SlotlessItemHandlerWrapper wrapper = new SlotlessItemHandlerWrapperDummy(handler,
                itEmpty,
                itEmpty,
                itEmpty,
                itEmpty,
                itAll);

        assertThat(wrapper.extractItem(10, true), isItemStack(new ItemStack(Items.APPLE, 10)));
        assertThat(wrapper.extractItem(10, false), isItemStack(new ItemStack(Items.APPLE, 10)));
        assertThat(handler.getResource(0), equalTo(ItemResource.EMPTY));
        assertThat(handler.getAmountAsInt(0), equalTo(0));
        assertThat(handler.getResource(1), equalTo(ItemResource.of(Items.LEAD)));
        assertThat(handler.getAmountAsInt(1), equalTo(11));
        assertThat(handler.getResource(3), equalTo(ItemResource.of(Items.APPLE)));
        assertThat(handler.getAmountAsInt(3), equalTo(55));
    }

    @Test
    public void testExtractAmountExtractableAllMany() {
        SlotlessItemHandlerWrapper wrapper = new SlotlessItemHandlerWrapperDummy(handler,
                itEmpty,
                itEmpty,
                itEmpty,
                itEmpty,
                itAll);

        assertThat(wrapper.extractItem(65, true), isItemStack(new ItemStack(Items.APPLE, 65)));
        assertThat(wrapper.extractItem(65, false), isItemStack(new ItemStack(Items.APPLE, 65)));
        assertThat(handler.getResource(0), equalTo(ItemResource.EMPTY));
        assertThat(handler.getAmountAsInt(0), equalTo(0));
        assertThat(handler.getResource(1), equalTo(ItemResource.of(Items.LEAD)));
        assertThat(handler.getAmountAsInt(1), equalTo(11));
        assertThat(handler.getResource(3), equalTo(ItemResource.EMPTY));
        assertThat(handler.getAmountAsInt(3), equalTo(0));
    }

    @Test
    public void testExtractItemEmpty() {
        SlotlessItemHandlerWrapper wrapper = new SlotlessItemHandlerWrapperDummy(handlerEmpty,
                itEmpty,
                itEmpty,
                itEmpty,
                itEmpty,
                itEmpty);

        ItemStack extracting0 = new ItemStack(Items.APPLE, 10);
        assertThat(wrapper.extractItem(extracting0, ItemMatch.EXACT, true), isItemStack(ItemStack.EMPTY));
        assertThat(wrapper.extractItem(extracting0, ItemMatch.EXACT, false), isItemStack(ItemStack.EMPTY));
        assertThat(handlerEmpty.getResource(0), equalTo(ItemResource.EMPTY));
        assertThat(handlerEmpty.getAmountAsInt(0), equalTo(0));
    }

    @Test
    public void testExtractItemExtractable0() {
        SlotlessItemHandlerWrapper wrapper = new SlotlessItemHandlerWrapperDummy(handler,
                itEmpty,
                it0,
                itEmpty,
                itEmpty,
                itEmpty);

        ItemStack extracting0 = new ItemStack(Items.APPLE, 10);
        assertThat(wrapper.extractItem(extracting0, ItemMatch.EXACT, true), isItemStack(new ItemStack(Items.APPLE, 5)));
        assertThat(wrapper.extractItem(extracting0, ItemMatch.EXACT, false), isItemStack(new ItemStack(Items.APPLE, 5)));
        assertThat(handler.getResource(0), equalTo(ItemResource.EMPTY));
        assertThat(handler.getAmountAsInt(0), equalTo(0));
        assertThat(handler.getResource(1), equalTo(ItemResource.of(Items.LEAD)));
        assertThat(handler.getAmountAsInt(1), equalTo(11));
    }

    @Test
    public void testExtractItemExtractableAll() {
        SlotlessItemHandlerWrapper wrapper = new SlotlessItemHandlerWrapperDummy(handler,
                itEmpty,
                itAll,
                itEmpty,
                itEmpty,
                itEmpty);

        ItemStack extracting0 = new ItemStack(Items.APPLE, 10);
        assertThat(wrapper.extractItem(extracting0, ItemMatch.EXACT, true), isItemStack(extracting0));
        assertThat(wrapper.extractItem(extracting0, ItemMatch.EXACT, false), isItemStack(extracting0));
        assertThat(handler.getResource(0), equalTo(ItemResource.EMPTY));
        assertThat(handler.getAmountAsInt(0), equalTo(0));
        assertThat(handler.getResource(1), equalTo(ItemResource.of(Items.LEAD)));
        assertThat(handler.getAmountAsInt(1), equalTo(11));
        assertThat(handler.getResource(3), equalTo(ItemResource.of(Items.APPLE)));
        assertThat(handler.getAmountAsInt(3), equalTo(55));
    }

    @Test
    public void testExtractItemExtractableAllMany() {
        SlotlessItemHandlerWrapper wrapper = new SlotlessItemHandlerWrapperDummy(handler,
                itEmpty,
                itAll,
                itEmpty,
                itEmpty,
                itEmpty);

        ItemStack extracting0 = new ItemStack(Items.APPLE, 65);
        assertThat(wrapper.extractItem(extracting0, ItemMatch.EXACT, true), isItemStack(extracting0));
        assertThat(wrapper.extractItem(extracting0, ItemMatch.EXACT, false), isItemStack(extracting0));
        assertThat(handler.getResource(0), equalTo(ItemResource.EMPTY));
        assertThat(handler.getAmountAsInt(0), equalTo(0));
        assertThat(handler.getResource(1), equalTo(ItemResource.of(Items.LEAD)));
        assertThat(handler.getAmountAsInt(1), equalTo(11));
        assertThat(handler.getResource(3), equalTo(ItemResource.EMPTY));
        assertThat(handler.getAmountAsInt(3), equalTo(0));
    }

}
