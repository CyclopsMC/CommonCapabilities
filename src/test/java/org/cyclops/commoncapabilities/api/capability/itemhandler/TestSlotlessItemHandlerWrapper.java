package org.cyclops.commoncapabilities.api.capability.itemhandler;

import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.core.NonNullList;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.PrimitiveIterator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.cyclops.commoncapabilities.api.ingredient.IngredientTestMatcher.isItemStack;
import static org.junit.Assert.assertThat;

/**
 * @author rubensworks
 */
public class TestSlotlessItemHandlerWrapper {

    private IItemHandler handlerEmpty;
    private IItemHandler handler;
    private Supplier<PrimitiveIterator.OfInt> itEmpty;
    private Supplier<PrimitiveIterator.OfInt> it0;
    private Supplier<PrimitiveIterator.OfInt> it9;
    private Supplier<PrimitiveIterator.OfInt> itAll;

    @Before
    public void init() {
        // We need the Minecraft registries to be filled
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();

        handlerEmpty = new ItemStackHandler(NonNullList.withSize(10, ItemStack.EMPTY));
        handler = new ItemStackHandler(NonNullList.of(ItemStack.EMPTY,
                new ItemStack(Items.APPLE, 5),
                new ItemStack(Items.LEAD, 11),
                new ItemStack(Items.BOWL, 64),
                new ItemStack(Items.APPLE, 60)
        ));

        itEmpty = () -> IntStream.of().iterator();
        it0 = () -> IntStream.of(0).iterator();
        it9 = () -> IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).iterator();
        itAll = () -> IntStream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).iterator();
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
        assertThat(handlerEmpty.getStackInSlot(0), isItemStack(inserting0));

        ItemStack inserting1 = new ItemStack(Items.APPLE, 10);
        assertThat(wrapper.insertItem(inserting1, true), isItemStack(ItemStack.EMPTY));
        assertThat(wrapper.insertItem(inserting1, false), isItemStack(ItemStack.EMPTY));
        assertThat(handlerEmpty.getStackInSlot(1), isItemStack(inserting1));
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
        assertThat(handlerEmpty.getStackInSlot(0), isItemStack(inserting0));

        ItemStack inserting1 = new ItemStack(Items.APPLE, 10);
        assertThat(wrapper.insertItem(inserting1, true), isItemStack(ItemStack.EMPTY));
        assertThat(wrapper.insertItem(inserting1, false), isItemStack(ItemStack.EMPTY));
        assertThat(handlerEmpty.getStackInSlot(1), isItemStack(inserting1));

        ItemStack inserting2 = new ItemStack(Items.DIAMOND, 64);
        assertThat(wrapper.insertItem(inserting2, true), isItemStack(ItemStack.EMPTY));
        assertThat(wrapper.insertItem(inserting2, false), isItemStack(ItemStack.EMPTY));
        assertThat(handlerEmpty.getStackInSlot(0), isItemStack(new ItemStack(Items.DIAMOND, 64)));
        assertThat(handlerEmpty.getStackInSlot(2), isItemStack(new ItemStack(Items.DIAMOND, 10)));

        ItemStack inserting3 = new ItemStack(Items.DIAMOND, 100);
        assertThat(wrapper.insertItem(inserting3, true), isItemStack(ItemStack.EMPTY));
        assertThat(wrapper.insertItem(inserting3, false), isItemStack(ItemStack.EMPTY));
        assertThat(handlerEmpty.getStackInSlot(0), isItemStack(new ItemStack(Items.DIAMOND, 64)));
        assertThat(handlerEmpty.getStackInSlot(2), isItemStack(new ItemStack(Items.DIAMOND, 64)));
        assertThat(handlerEmpty.getStackInSlot(3), isItemStack(new ItemStack(Items.DIAMOND, 46)));
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
        assertThat(handler.getStackInSlot(0), isItemStack(ItemStack.EMPTY));
        assertThat(handler.getStackInSlot(1), isItemStack(new ItemStack(Items.LEAD, 11)));
        assertThat(handler.getStackInSlot(3), isItemStack(new ItemStack(Items.APPLE, 60)));
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
        assertThat(handler.getStackInSlot(0), isItemStack(ItemStack.EMPTY));
        assertThat(handler.getStackInSlot(1), isItemStack(new ItemStack(Items.LEAD, 11)));
        assertThat(handler.getStackInSlot(3), isItemStack(new ItemStack(Items.APPLE, 55)));
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
        assertThat(handler.getStackInSlot(0), isItemStack(ItemStack.EMPTY));
        assertThat(handler.getStackInSlot(1), isItemStack(new ItemStack(Items.LEAD, 11)));
        assertThat(handler.getStackInSlot(3), isItemStack(ItemStack.EMPTY));
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
        assertThat(handler.getStackInSlot(0), isItemStack(new ItemStack(Items.APPLE, 5)));
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
        assertThat(handler.getStackInSlot(0), isItemStack(ItemStack.EMPTY));
        assertThat(handler.getStackInSlot(1), isItemStack(new ItemStack(Items.LEAD, 11)));
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
        assertThat(handler.getStackInSlot(0), isItemStack(ItemStack.EMPTY));
        assertThat(handler.getStackInSlot(1), isItemStack(new ItemStack(Items.LEAD, 11)));
        assertThat(handler.getStackInSlot(3), isItemStack(new ItemStack(Items.APPLE, 55)));
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
        assertThat(handler.getStackInSlot(0), isItemStack(ItemStack.EMPTY));
        assertThat(handler.getStackInSlot(1), isItemStack(new ItemStack(Items.LEAD, 11)));
        assertThat(handler.getStackInSlot(3), isItemStack(ItemStack.EMPTY));
    }

}
