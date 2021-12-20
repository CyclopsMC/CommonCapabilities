package org.cyclops.commoncapabilities.api.capability.fluidhandler;

import com.google.common.collect.Lists;
import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestFilteredFluidHandlerFluidStackIterator {

    private static IFluidHandler HANDLER_EMPTY;
    private static IFluidHandler HANDLER;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();

        HANDLER_EMPTY = new ImmutableListFluidHandler(Lists.newArrayList());
        HANDLER = new ImmutableListFluidHandler(Lists.newArrayList(
                new FluidStack(Fluids.WATER, 1000),
                new FluidStack(Fluids.WATER, 123),
                new FluidStack(Fluids.LAVA, 1000)
        ));
    }

    @Test
    public void testEmpty() {
        Iterator<FluidStack> it = new FilteredFluidHandlerFluidStackIterator(HANDLER_EMPTY, new FluidStack(Fluids.WATER, 123), FluidMatch.FLUID);
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyNext() {
        Iterator<FluidStack> it = new FilteredFluidHandlerFluidStackIterator(HANDLER_EMPTY, new FluidStack(Fluids.WATER, 123), FluidMatch.FLUID);
        it.next();
    }

    @Test
    public void testNonEmptyWater() {
        Iterator<FluidStack> it = new FilteredFluidHandlerFluidStackIterator(HANDLER, new FluidStack(Fluids.WATER, 123), FluidMatch.FLUID);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(Fluids.WATER, 1000)));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(Fluids.WATER, 123)));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNonEmptyWaterOutOfRange() {
        Iterator<FluidStack> it = new FilteredFluidHandlerFluidStackIterator(HANDLER, new FluidStack(Fluids.WATER, 123), FluidMatch.FLUID);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(Fluids.WATER, 1000)));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(Fluids.WATER, 123)));
        assertThat(it.hasNext(), is(false));

        it.next();
    }

    @Test
    public void testNonEmptyLava() {
        Iterator<FluidStack> it = new FilteredFluidHandlerFluidStackIterator(HANDLER, new FluidStack(Fluids.LAVA, 123), FluidMatch.FLUID);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(Fluids.LAVA, 1000)));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNonEmptyLavaOutOfRange() {
        Iterator<FluidStack> it = new FilteredFluidHandlerFluidStackIterator(HANDLER, new FluidStack(Fluids.LAVA, 123), FluidMatch.FLUID);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(Fluids.LAVA, 1000)));
        assertThat(it.hasNext(), is(false));

        it.next();
    }

    @Test
    public void testNonEmptyWaterExact() {
        Iterator<FluidStack> it = new FilteredFluidHandlerFluidStackIterator(HANDLER, new FluidStack(Fluids.WATER, 123), FluidMatch.EXACT);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(Fluids.WATER, 123)));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNonEmptyWaterExactOutOfRange() {
        Iterator<FluidStack> it = new FilteredFluidHandlerFluidStackIterator(HANDLER, new FluidStack(Fluids.WATER, 123), FluidMatch.EXACT);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new FluidStack(Fluids.WATER, 123)));
        assertThat(it.hasNext(), is(false));

        it.next();
    }

}
