package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidHandlerConcatenate;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidTankFixed;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

    private IFluidHandler innerStorage;
    private IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper storage;
    private IngredientComponentStorageWrapperHandlerFluidStack.FluidStorageWrapper wrapper;
    private FluidTank t1;
    private FluidTank t2;
    private FluidTank t3;
    private FluidTank t4;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();
    }

    public static boolean eq(FluidStack a, FluidStack b) {
        return IngredientComponents.FLUIDSTACK.getMatcher().matchesExactly(a, b);
    }

    @Before
    public void beforeEach() {
        WATER_1 = new FluidStack(Fluids.WATER, 1);
        LAVA_1_NB = new FluidStack(Fluids.LAVA, 1, new CompoundTag());
        LAVA_10 = new FluidStack(Fluids.LAVA, 10);
        WATER_10 = new FluidStack(Fluids.WATER, 10);

        WATER_64 = new FluidStack(Fluids.WATER, 64);
        LAVA_64 = new FluidStack(Fluids.LAVA, 64);
        LAVA_64_NB = new FluidStack(Fluids.LAVA, 64, new CompoundTag());
        WATER_11 = new FluidStack(Fluids.WATER, 11);
        LAVA_11_NB = new FluidStack(Fluids.LAVA, 11, new CompoundTag());

        t1 = new FluidTankFixed(64);
        t2 = new FluidTankFixed(64);
        t3 = new FluidTankFixed(64);
        t4 = new FluidTankFixed(64);
        innerStorage = new FluidHandlerConcatenate(
                new FluidTankFixed(64),
                new FluidTankFixed(64),
                t1,
                new FluidTankFixed(64),
                t2,
                new FluidTankFixed(64),
                t3,
                new FluidTankFixed(64),
                t4,
                new FluidTankFixed(64)
        );
        t1.fill(WATER_1.copy(), IFluidHandler.FluidAction.EXECUTE);
        t2.fill(LAVA_1_NB.copy(), IFluidHandler.FluidAction.EXECUTE);
        t3.fill(LAVA_10.copy(), IFluidHandler.FluidAction.EXECUTE);
        t4.fill(WATER_10.copy(), IFluidHandler.FluidAction.EXECUTE);
        storage = new IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper(IngredientComponents.FLUIDSTACK, innerStorage);
        wrapper = new IngredientComponentStorageWrapperHandlerFluidStack.FluidStorageWrapper(storage);
    }

    @Test
    public void testGetTankProperties() {
        assertThat(wrapper.getTanks(), is(11));

        for (int i = 0; i < wrapper.getTanks(); i++) {
            assertThat(wrapper.getTankCapacity(i), is(640));
        }

        assertThat(eq(wrapper.getFluidInTank(0), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getFluidInTank(1), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.getFluidInTank(3), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(eq(wrapper.getFluidInTank(5), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(wrapper.getFluidInTank(7), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.getFluidInTank(9), FluidStack.EMPTY), is(true));
    }

    @Test
    public void testFill() {
        assertThat(wrapper.fill(WATER_64, IFluidHandler.FluidAction.SIMULATE), is(64));
        assertThat(wrapper.fill(WATER_64, IFluidHandler.FluidAction.SIMULATE), is(64));
        assertThat(wrapper.fill(WATER_64, IFluidHandler.FluidAction.SIMULATE), is(64));
        assertThat(eq(wrapper.getFluidInTank(0), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getFluidInTank(1), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getFluidInTank(2), WATER_1), is(true));

        assertThat(wrapper.fill(WATER_64, IFluidHandler.FluidAction.EXECUTE), is(64));
        assertThat(wrapper.fill(WATER_64, IFluidHandler.FluidAction.EXECUTE), is(64));
        assertThat(wrapper.fill(WATER_64, IFluidHandler.FluidAction.EXECUTE), is(64));
        assertThat(eq(wrapper.getFluidInTank(0), WATER_64), is(true));
        assertThat(eq(wrapper.getFluidInTank(1), WATER_64), is(true));
        assertThat(eq(wrapper.getFluidInTank(2), WATER_64), is(true));

        assertThat(wrapper.fill(WATER_64, IFluidHandler.FluidAction.EXECUTE), is(64));
        assertThat(wrapper.fill(WATER_64, IFluidHandler.FluidAction.EXECUTE), is(64));
        assertThat(wrapper.fill(WATER_64, IFluidHandler.FluidAction.EXECUTE), is(64));
        assertThat(wrapper.fill(WATER_64, IFluidHandler.FluidAction.EXECUTE), is(64));
        assertThat(wrapper.fill(WATER_64, IFluidHandler.FluidAction.EXECUTE), is(53));
    }

    @Test
    public void testDrain() {
        assertThat(eq(wrapper.drain(WATER_10, IFluidHandler.FluidAction.SIMULATE), WATER_10), is(true));
        assertThat(eq(wrapper.drain(WATER_10, IFluidHandler.FluidAction.SIMULATE), WATER_10), is(true));
        assertThat(eq(wrapper.drain(WATER_10, IFluidHandler.FluidAction.SIMULATE), WATER_10), is(true));
        assertThat(eq(wrapper.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.getFluidInTank(8), WATER_10), is(true));

        assertThat(eq(wrapper.drain(WATER_10, IFluidHandler.FluidAction.EXECUTE), WATER_10), is(true));
        assertThat(eq(wrapper.drain(WATER_10, IFluidHandler.FluidAction.EXECUTE), WATER_1), is(true));
        assertThat(eq(wrapper.drain(WATER_10, IFluidHandler.FluidAction.EXECUTE), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getFluidInTank(8), FluidStack.EMPTY), is(true));
    }

    @Test
    public void testDrainMax() {
        assertThat(eq(wrapper.drain(10, IFluidHandler.FluidAction.SIMULATE), WATER_10), is(true));
        assertThat(eq(wrapper.drain(10, IFluidHandler.FluidAction.SIMULATE), WATER_10), is(true));
        assertThat(eq(wrapper.drain(10, IFluidHandler.FluidAction.SIMULATE), WATER_10), is(true));
        assertThat(eq(wrapper.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(eq(wrapper.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(wrapper.getFluidInTank(8), WATER_10), is(true));

        assertThat(eq(wrapper.drain(10, IFluidHandler.FluidAction.EXECUTE), WATER_10), is(true));
        assertThat(eq(wrapper.drain(10, IFluidHandler.FluidAction.EXECUTE), LAVA_1_NB), is(true));
        assertThat(eq(wrapper.drain(10, IFluidHandler.FluidAction.EXECUTE), LAVA_10), is(true));
        assertThat(eq(wrapper.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getFluidInTank(8), WATER_1), is(true));
    }

}
