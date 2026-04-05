package org.cyclops.commoncapabilities.api.capability.resourcehandler;

import com.google.common.collect.Lists;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.ImmutableListFluidHandler;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestResourceHandlerFluidStackIterator {

    private static ResourceHandler<FluidResource> HANDLER_EMPTY;
    private static ResourceHandler<FluidResource> HANDLER;

    @BeforeAll
    public static void init() {
        // Bind empty components so FluidStack construction works in 26.1 (components are normally bound during resource reload)
        Fluids.WATER.builtInRegistryHolder().bindComponents(DataComponentMap.EMPTY);
        Fluids.LAVA.builtInRegistryHolder().bindComponents(DataComponentMap.EMPTY);

        HANDLER_EMPTY = new ImmutableListFluidHandler(Lists.newArrayList());
        HANDLER = new ImmutableListFluidHandler(Lists.newArrayList(
                new FluidStack(Fluids.WATER, 1000),
                new FluidStack(Fluids.WATER, 123),
                new FluidStack(Fluids.LAVA, 1000)
        ));
    }

    @Test
    public void testEmpty() {
        Iterator<FluidStack> it = new ResourceHandlerIngredientIterator<>(HANDLER_EMPTY, IngredientComponent.FLUIDSTACK_CONVERTER);
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testEmptyNext() {
        Iterator<FluidStack> it = new ResourceHandlerIngredientIterator<>(HANDLER_EMPTY, IngredientComponent.FLUIDSTACK_CONVERTER);
        Assertions.assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void testNonEmpty() {
        Iterator<FluidStack> it = new ResourceHandlerIngredientIterator<>(HANDLER, IngredientComponent.FLUIDSTACK_CONVERTER);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getFluid(), is(Fluids.WATER));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getFluid(), is(Fluids.WATER));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getFluid(), is(Fluids.LAVA));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testNonEmptyOffset() {
        Iterator<FluidStack> it1 = new ResourceHandlerIngredientIterator<>(HANDLER, IngredientComponent.FLUIDSTACK_CONVERTER, 1);
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next().getFluid(), is(Fluids.WATER));
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next().getFluid(), is(Fluids.LAVA));
        assertThat(it1.hasNext(), is(false));

        Iterator<FluidStack> it2 = new ResourceHandlerIngredientIterator<>(HANDLER, IngredientComponent.FLUIDSTACK_CONVERTER, 2);
        assertThat(it2.hasNext(), is(true));
        assertThat(it2.next().getFluid(), is(Fluids.LAVA));
        assertThat(it2.hasNext(), is(false));

        Iterator<FluidStack> it3 = new ResourceHandlerIngredientIterator<>(HANDLER, IngredientComponent.FLUIDSTACK_CONVERTER, 3);
        assertThat(it3.hasNext(), is(false));
    }

    @Test
    public void testNonEmptyOutOfRange() {
        Iterator<FluidStack> it = new ResourceHandlerIngredientIterator<>(HANDLER, IngredientComponent.FLUIDSTACK_CONVERTER);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getFluid(), is(Fluids.WATER));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getFluid(), is(Fluids.WATER));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next().getFluid(), is(Fluids.LAVA));
        assertThat(it.hasNext(), is(false));

        Assertions.assertThrows(NoSuchElementException.class, it::next);
    }

}
