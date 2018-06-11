package org.cyclops.commoncapabilities.api.capability.fluidhandler;

import com.google.common.collect.Lists;
import net.minecraft.init.Bootstrap;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestFluidHandlerFluidStackIterator {

    private static IFluidHandler HANDLER_EMPTY;
    private static IFluidHandler HANDLER;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        Bootstrap.register();

        HANDLER_EMPTY = new ImmutableListFluidHandler(Lists.newArrayList());
        HANDLER = new ImmutableListFluidHandler(Lists.newArrayList(
                new FluidStack(FluidRegistry.WATER, 1000),
                new FluidStack(FluidRegistry.WATER, 123),
                new FluidStack(FluidRegistry.LAVA, 1000)
        ));
    }

    @Test
    public void testEmpty() {
        Iterator<FluidStack> it = new FluidHandlerFluidStackIterator(HANDLER_EMPTY);
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyNext() {
        Iterator<FluidStack> it = new FluidHandlerFluidStackIterator(HANDLER_EMPTY);
        it.next();
    }

    @Test
    public void testNonEmpty() {
        Iterator<FluidStack> it = new FluidHandlerFluidStackIterator(HANDLER);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(FluidRegistry.WATER, 1000)));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(FluidRegistry.WATER, 123)));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(FluidRegistry.LAVA, 1000)));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testNonEmptyOffset() {
        Iterator<FluidStack> it1 = new FluidHandlerFluidStackIterator(HANDLER, 1);
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next(), is(new FluidStack(FluidRegistry.WATER, 123)));
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next(), is(new FluidStack(FluidRegistry.LAVA, 1000)));
        assertThat(it1.hasNext(), is(false));

        Iterator<FluidStack> it2 = new FluidHandlerFluidStackIterator(HANDLER, 2);
        assertThat(it2.hasNext(), is(true));
        assertThat(it2.next(), is(new FluidStack(FluidRegistry.LAVA, 1000)));
        assertThat(it2.hasNext(), is(false));

        Iterator<FluidStack> it3 = new FluidHandlerFluidStackIterator(HANDLER, 3);
        assertThat(it3.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNonEmptyOutOfRange() {
        Iterator<FluidStack> it = new FluidHandlerFluidStackIterator(HANDLER);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(FluidRegistry.WATER, 1000)));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(FluidRegistry.WATER, 123)));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(FluidRegistry.LAVA, 1000)));
        assertThat(it.hasNext(), is(false));

        it.next();
    }

}
