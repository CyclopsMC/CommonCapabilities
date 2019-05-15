package org.cyclops.commoncapabilities.api.capability.itemhandler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.PrimitiveIterator;
import java.util.function.Supplier;

/**
 * A dummy implementation of {@link SlotlessItemHandlerWrapper} with hardcoded iterators.
 * @author rubensworks
 */
public class SlotlessItemHandlerWrapperDummy extends SlotlessItemHandlerWrapper {

    private final Supplier<PrimitiveIterator.OfInt> nonFullSlotsWithItemStack;
    private final Supplier<PrimitiveIterator.OfInt> nonEmptySlotsWithItemStack;
    private final Supplier<PrimitiveIterator.OfInt> slotsWithItemStack;
    private final Supplier<PrimitiveIterator.OfInt> emptySlots;
    private final Supplier<PrimitiveIterator.OfInt> nonEmptySlots;

    public SlotlessItemHandlerWrapperDummy(IItemHandler itemHandler,
                                           Supplier<PrimitiveIterator.OfInt> nonFullSlotsWithItemStack,
                                           Supplier<PrimitiveIterator.OfInt> nonEmptySlotsWithItemStack,
                                           Supplier<PrimitiveIterator.OfInt> slotsWithItemStack,
                                           Supplier<PrimitiveIterator.OfInt> emptySlots,
                                           Supplier<PrimitiveIterator.OfInt> nonEmptySlots) {
        super(itemHandler);
        this.nonFullSlotsWithItemStack = nonFullSlotsWithItemStack;
        this.nonEmptySlotsWithItemStack = nonEmptySlotsWithItemStack;
        this.slotsWithItemStack = slotsWithItemStack;
        this.emptySlots = emptySlots;
        this.nonEmptySlots = nonEmptySlots;
    }

    @Override
    protected PrimitiveIterator.OfInt getNonFullSlotsWithItemStack(@Nonnull ItemStack itemStack, int matchFlags) {
        return nonFullSlotsWithItemStack.get();
    }

    @Override
    protected PrimitiveIterator.OfInt getNonEmptySlotsWithItemStack(@Nonnull ItemStack itemStack, int matchFlags) {
        return nonEmptySlotsWithItemStack.get();
    }

    @Override
    protected PrimitiveIterator.OfInt getSlotsWithItemStack(@Nonnull ItemStack itemStack, int matchFlags) {
        return slotsWithItemStack.get();
    }

    @Override
    protected PrimitiveIterator.OfInt getEmptySlots() {
        return emptySlots.get();
    }

    @Override
    protected PrimitiveIterator.OfInt getNonEmptySlots() {
        return nonEmptySlots.get();
    }
}
