package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidHandlerConcatenate;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestFluidStackFluidStorageWrapper {

    private static FluidStack WATER_1;
    private static FluidStack LAVA_1_NB;
    private static FluidStack LAVA_10;
    private static FluidStack WATER_10;

    private static FluidStack WATER_64;
    private static FluidStack LAVA_64;
    private static FluidStack LAVA_64_NB;
    private static FluidStack WATER_8;
    private static FluidStack WATER_9;
    private static FluidStack WATER_11;
    private static FluidStack LAVA_11_NB;

    private ResourceHandler<FluidResource> innerStorage;
    private IngredientComponentStorageWrapperHandlerResourceHandler.ComponentStorageWrapper<FluidResource, FluidStack, Integer> storage;
    private IngredientComponentStorageWrapperHandlerResourceHandler.ResourceStorageWrapper<FluidResource, FluidStack, Integer> wrapper;
    private SingleUseTank t1;
    private SingleUseTank t2;
    private SingleUseTank t3;
    private SingleUseTank t4;

    @BeforeEach
    public void beforeEach() {
        WATER_1 = new FluidStack(Fluids.WATER, 1);
        LAVA_1_NB = new FluidStack(Holder.direct(Fluids.LAVA), 1, DataComponentPatch.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .build());
        LAVA_10 = new FluidStack(Fluids.LAVA, 10);
        WATER_10 = new FluidStack(Fluids.WATER, 10);

        WATER_64 = new FluidStack(Fluids.WATER, 64);
        LAVA_64 = new FluidStack(Fluids.LAVA, 64);
        LAVA_64_NB = new FluidStack(Holder.direct(Fluids.LAVA), 64, DataComponentPatch.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .build());
        WATER_11 = new FluidStack(Fluids.WATER, 11);
        LAVA_11_NB = new FluidStack(Holder.direct(Fluids.LAVA), 11, DataComponentPatch.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .build());

        t1 = new SingleUseTank(64);
        t2 = new SingleUseTank(64);
        t3 = new SingleUseTank(64);
        t4 = new SingleUseTank(64);
        innerStorage = new FluidHandlerConcatenate(
                new SingleUseTank(64),
                new SingleUseTank(64),
                t1,
                new SingleUseTank(64),
                t2,
                new SingleUseTank(64),
                t3,
                new SingleUseTank(64),
                t4,
                new SingleUseTank(64)
        );
        try (var tx = Transaction.openRoot()) {
            t1.insert(FluidResource.of(WATER_1), WATER_1.getAmount(), tx);
            t2.insert(FluidResource.of(LAVA_1_NB), LAVA_1_NB.getAmount(), tx);
            t3.insert(FluidResource.of(LAVA_10), LAVA_10.getAmount(), tx);
            t4.insert(FluidResource.of(WATER_10), WATER_10.getAmount(), tx);
            tx.commit();
        }
        storage = new IngredientComponentStorageWrapperHandlerResourceHandler.ComponentStorageWrapper<>(IngredientComponents.FLUIDSTACK, innerStorage, IngredientComponent.FLUIDSTACK_CONVERTER);
        wrapper = new IngredientComponentStorageWrapperHandlerResourceHandler.ResourceStorageWrapper<>(storage, IngredientComponent.FLUIDSTACK_CONVERTER);
    }

    @Test
    public void testGetTankProperties() {
        assertThat(wrapper.size(), is(11));

        for (int i = 0; i < wrapper.size(); i++) {
            assertThat(wrapper.getCapacityAsInt(i, FluidResource.of(Fluids.WATER)), is(640));
        }

        assertThat(wrapper.getResource(0), equalTo(FluidResource.EMPTY));
        assertThat(wrapper.getAmountAsInt(0), equalTo(0));
        assertThat(wrapper.getResource(1), equalTo(FluidResource.EMPTY));
        assertThat(wrapper.getAmountAsInt(1), equalTo(0));
        assertThat(wrapper.getResource(2), equalTo(FluidResource.of(WATER_1)));
        assertThat(wrapper.getAmountAsInt(2), equalTo(1));
        assertThat(wrapper.getResource(3), equalTo(FluidResource.EMPTY));
        assertThat(wrapper.getAmountAsInt(3), equalTo(0));
        assertThat(wrapper.getResource(4), equalTo(FluidResource.of(LAVA_1_NB)));
        assertThat(wrapper.getAmountAsInt(4), equalTo(1));
        assertThat(wrapper.getResource(5), equalTo(FluidResource.EMPTY));
        assertThat(wrapper.getAmountAsInt(5), equalTo(0));
        assertThat(wrapper.getResource(6), equalTo(FluidResource.of(LAVA_10)));
        assertThat(wrapper.getAmountAsInt(6), equalTo(10));
        assertThat(wrapper.getResource(7), equalTo(FluidResource.EMPTY));
        assertThat(wrapper.getAmountAsInt(7), equalTo(0));
        assertThat(wrapper.getResource(8), equalTo(FluidResource.of(WATER_10)));
        assertThat(wrapper.getAmountAsInt(8), equalTo(10));
        assertThat(wrapper.getResource(9), equalTo(FluidResource.EMPTY));
        assertThat(wrapper.getAmountAsInt(9), equalTo(0));
    }

    @Test
    public void testFill() {
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(FluidResource.of(WATER_64), WATER_64.getAmount(), tx), is(64));
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(FluidResource.of(WATER_64), WATER_64.getAmount(), tx), is(64));
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(FluidResource.of(WATER_64), WATER_64.getAmount(), tx), is(64));
        }
        assertThat(wrapper.getResource(0), equalTo(FluidResource.EMPTY));
        assertThat(wrapper.getAmountAsInt(0), equalTo(0));
        assertThat(wrapper.getResource(1), equalTo(FluidResource.EMPTY));
        assertThat(wrapper.getAmountAsInt(1), equalTo(0));
        assertThat(wrapper.getResource(2), equalTo(FluidResource.of(WATER_1)));
        assertThat(wrapper.getAmountAsInt(2), equalTo(1));

        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(FluidResource.of(WATER_64), WATER_64.getAmount(), tx), is(64));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(FluidResource.of(WATER_64), WATER_64.getAmount(), tx), is(64));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(FluidResource.of(WATER_64), WATER_64.getAmount(), tx), is(64));
            tx.commit();
        }
        assertThat(wrapper.getResource(0), equalTo(FluidResource.of(WATER_64)));
        assertThat(wrapper.getAmountAsInt(0), equalTo(64));
        assertThat(wrapper.getResource(1), equalTo(FluidResource.of(WATER_64)));
        assertThat(wrapper.getAmountAsInt(1), equalTo(64));
        assertThat(wrapper.getResource(2), equalTo(FluidResource.of(WATER_64)));
        assertThat(wrapper.getAmountAsInt(2), equalTo(64));

        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(FluidResource.of(WATER_64), WATER_64.getAmount(), tx), is(64));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(FluidResource.of(WATER_64), WATER_64.getAmount(), tx), is(64));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(FluidResource.of(WATER_64), WATER_64.getAmount(), tx), is(64));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(FluidResource.of(WATER_64), WATER_64.getAmount(), tx), is(64));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.insert(FluidResource.of(WATER_64), WATER_64.getAmount(), tx), is(53));
            tx.commit();
        }
    }

    @Test
    public void testDrain() {
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(FluidResource.of(WATER_10), WATER_10.getAmount(), tx), is(10));
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(FluidResource.of(WATER_10), WATER_10.getAmount(), tx), is(10));
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(FluidResource.of(WATER_10), WATER_10.getAmount(), tx), is(10));
        }
        assertThat(wrapper.getResource(2), equalTo(FluidResource.of(WATER_1)));
        assertThat(wrapper.getAmountAsInt(2), equalTo(1));
        assertThat(wrapper.getResource(8), equalTo(FluidResource.of(WATER_10)));
        assertThat(wrapper.getAmountAsInt(8), equalTo(10));

        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(FluidResource.of(WATER_10), WATER_10.getAmount(), tx), is(10));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(FluidResource.of(WATER_10), WATER_10.getAmount(), tx), is(1));
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            assertThat(wrapper.extract(FluidResource.of(WATER_10), WATER_10.getAmount(), tx), is(0));
            tx.commit();
        }
        assertThat(wrapper.getResource(2), equalTo(FluidResource.EMPTY));
        assertThat(wrapper.getAmountAsInt(2), equalTo(0));
        assertThat(wrapper.getResource(8), equalTo(FluidResource.EMPTY));
        assertThat(wrapper.getAmountAsInt(8), equalTo(0));
    }

}
