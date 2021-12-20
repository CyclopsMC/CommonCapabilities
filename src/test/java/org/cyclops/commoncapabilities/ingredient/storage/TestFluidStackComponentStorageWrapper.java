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
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidTankFixed;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientLinkedList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestFluidStackComponentStorageWrapper {

    private static FluidStack WATER_1;
    private static FluidStack LAVA_1_NB;
    private static FluidStack LAVA_1;
    private static FluidStack LAVA_10;
    private static FluidStack LAVA_9;
    private static FluidStack WATER_10;

    private static FluidStack WATER_64;
    private static FluidStack LAVA_64;
    private static FluidStack LAVA_64_NB;
    private static FluidStack WATER_11;
    private static FluidStack LAVA_11_NB;
    private static FluidStack WATER_2;
    private static FluidStack WATER_9;

    private IFluidHandler storage;
    private IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper wrapper;
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
        LAVA_1 = new FluidStack(Fluids.LAVA, 1);
        LAVA_10 = new FluidStack(Fluids.LAVA, 10);
        LAVA_9 = new FluidStack(Fluids.LAVA, 9);
        WATER_10 = new FluidStack(Fluids.WATER, 10);

        WATER_64 = new FluidStack(Fluids.WATER, 64);
        LAVA_64 = new FluidStack(Fluids.LAVA, 64);
        LAVA_64_NB = new FluidStack(Fluids.LAVA, 64, new CompoundTag());
        WATER_11 = new FluidStack(Fluids.WATER, 11);
        LAVA_11_NB = new FluidStack(Fluids.LAVA, 11, new CompoundTag());
        WATER_2 = new FluidStack(Fluids.WATER, 2);
        WATER_9 = new FluidStack(Fluids.WATER, 9);

        t1 = new FluidTankFixed(64);
        t2 = new FluidTankFixed(64);
        t3 = new FluidTankFixed(64);
        t4 = new FluidTankFixed(64);
        storage = new FluidHandlerConcatenate(
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
        wrapper = new IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper(IngredientComponents.FLUIDSTACK, storage);
    }

    @Test
    public void testGetComponent() {
        assertThat(wrapper.getComponent(), is(IngredientComponents.FLUIDSTACK));
    }

    @Test
    public void testGetMaxQuantity() {
        assertThat(wrapper.getMaxQuantity(), is(640L));
    }

    @Test
    public void testIterator() {
        assertThat(new IngredientLinkedList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator()),
                is(new IngredientLinkedList<>(IngredientComponents.FLUIDSTACK,
                        new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, FluidStack.EMPTY, FluidStack.EMPTY, WATER_1, FluidStack.EMPTY, LAVA_1_NB,
                                FluidStack.EMPTY, LAVA_10, FluidStack.EMPTY, WATER_10, FluidStack.EMPTY))));
    }

    @Test
    public void testIteratorMatch() {
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(WATER_1, FluidMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, WATER_1)));
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(LAVA_1_NB, FluidMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, LAVA_1_NB)));
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(LAVA_10, FluidMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, LAVA_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(WATER_10, FluidMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, WATER_10)));

        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(WATER_1, FluidMatch.FLUID)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, WATER_1, WATER_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(LAVA_1_NB, FluidMatch.FLUID)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, LAVA_1_NB, LAVA_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(LAVA_10, FluidMatch.FLUID)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, LAVA_1_NB, LAVA_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(WATER_10, FluidMatch.FLUID)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, WATER_1, WATER_10)));

        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(WATER_10, FluidMatch.ANY)), is(
                new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, FluidStack.EMPTY, FluidStack.EMPTY, WATER_1, FluidStack.EMPTY, LAVA_1_NB,
                        FluidStack.EMPTY, LAVA_10, FluidStack.EMPTY, WATER_10, FluidStack.EMPTY)));
    }

    @Test
    public void testInsert() {
        assertThat(wrapper.insert(WATER_64, true), is(FluidStack.EMPTY));
        assertThat(storage.getFluidInTank(0), is(FluidStack.EMPTY));
        assertThat(wrapper.insert(WATER_64, true), is(FluidStack.EMPTY));
        assertThat(storage.getFluidInTank(0), is(FluidStack.EMPTY));
        assertThat(wrapper.insert(WATER_64, true), is(FluidStack.EMPTY));
        assertThat(storage.getFluidInTank(0), is(FluidStack.EMPTY));
        assertThat(wrapper.insert(LAVA_64, true), is(FluidStack.EMPTY));
        assertThat(storage.getFluidInTank(0), is(FluidStack.EMPTY));
        assertThat(wrapper.insert(LAVA_64, true), is(FluidStack.EMPTY));
        assertThat(storage.getFluidInTank(0), is(FluidStack.EMPTY));
        assertThat(wrapper.insert(WATER_64, false), is(FluidStack.EMPTY));
        assertThat(eq(storage.getFluidInTank(0), WATER_64), is(true));
        assertThat(storage.getFluidInTank(1), is(FluidStack.EMPTY));
        assertThat(wrapper.insert(WATER_64, false), is(FluidStack.EMPTY));
        assertThat(eq(storage.getFluidInTank(0), WATER_64), is(true));
        assertThat(eq(storage.getFluidInTank(1), WATER_64), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(storage.getFluidInTank(3), is(FluidStack.EMPTY));
        assertThat(wrapper.insert(WATER_64, false), is(FluidStack.EMPTY));
        assertThat(eq(storage.getFluidInTank(0), WATER_64), is(true));
        assertThat(eq(storage.getFluidInTank(1), WATER_64), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_64), is(true));
        assertThat(eq(storage.getFluidInTank(3), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(storage.getFluidInTank(5), is(FluidStack.EMPTY));
        assertThat(wrapper.insert(LAVA_64_NB, false), is(FluidStack.EMPTY));
        assertThat(eq(storage.getFluidInTank(0), WATER_64), is(true));
        assertThat(eq(storage.getFluidInTank(1), WATER_64), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_64), is(true));
        assertThat(eq(storage.getFluidInTank(3), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_64_NB), is(true));
        assertThat(eq(storage.getFluidInTank(5), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(storage.getFluidInTank(7), is(FluidStack.EMPTY));
        assertThat(wrapper.insert(LAVA_64_NB, false), is(FluidStack.EMPTY));
        assertThat(eq(storage.getFluidInTank(0), WATER_64), is(true));
        assertThat(eq(storage.getFluidInTank(1), WATER_64), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_64), is(true));
        assertThat(eq(storage.getFluidInTank(3), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_64_NB), is(true));
        assertThat(eq(storage.getFluidInTank(5), LAVA_64_NB), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(7), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(storage.getFluidInTank(9), is(FluidStack.EMPTY));
    }

    @Test
    public void testInsertFull() {
        IFluidHandler storage = new FluidTank(0);
        IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper wrapper = new IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper(IngredientComponents.FLUIDSTACK, storage);
        assertThat(eq(wrapper.insert(WATER_64, true), WATER_64), is(true));
    }

    @Test
    public void testExtractAny() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.ANY, true), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.ANY, true), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.ANY, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.ANY, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.ANY, false), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.ANY, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.ANY, false), LAVA_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_9), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
    }

    @Test
    public void testExtractAmount() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.AMOUNT, true), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.AMOUNT, true), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.AMOUNT, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.AMOUNT, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.AMOUNT, false), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.AMOUNT, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.AMOUNT, false), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(WATER_11, FluidMatch.AMOUNT, false), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.AMOUNT, false), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(8), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.AMOUNT, false), FluidStack.EMPTY), is(true));
    }

    @Test
    public void testExtractFluid() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.FLUID, true), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.FLUID, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.FLUID, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.FLUID, false), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID, false), LAVA_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_9), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
    }

    @Test
    public void testExtractNbt() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.TAG, true), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.TAG, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.TAG, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.TAG, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.TAG, false), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.TAG, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.TAG, false), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
    }

    @Test
    public void testExtractFluidAmount() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.FLUID | FluidMatch.AMOUNT, true), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID | FluidMatch.AMOUNT, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.FLUID | FluidMatch.AMOUNT, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.FLUID | FluidMatch.AMOUNT, true), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.FLUID | FluidMatch.AMOUNT, false), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID | FluidMatch.AMOUNT, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.FLUID | FluidMatch.AMOUNT, false), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.FLUID | FluidMatch.AMOUNT, false), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(8), FluidStack.EMPTY), is(true));
    }

    @Test
    public void testExtractFluidNbt() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.FLUID | FluidMatch.TAG, true), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID | FluidMatch.TAG, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.FLUID | FluidMatch.TAG, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.FLUID | FluidMatch.TAG, true), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.FLUID | FluidMatch.TAG, false), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID | FluidMatch.TAG, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.FLUID | FluidMatch.TAG, false), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
    }

    @Test
    public void testExtractAmountNbt() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.AMOUNT | FluidMatch.TAG, true), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.AMOUNT | FluidMatch.TAG, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.AMOUNT | FluidMatch.TAG, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.AMOUNT | FluidMatch.TAG, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.AMOUNT | FluidMatch.TAG, false), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.AMOUNT | FluidMatch.TAG, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.AMOUNT | FluidMatch.TAG, false), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.AMOUNT | FluidMatch.TAG, false), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(8), FluidStack.EMPTY), is(true));
    }

    @Test
    public void testExtractExact() {
        assertThat(eq(wrapper.extract(WATER_9, FluidMatch.EXACT, true), WATER_9), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_2, FluidMatch.EXACT, true), WATER_2), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.EXACT, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.EXACT, true), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_9, FluidMatch.EXACT, false), WATER_9), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_2), is(true));
        assertThat(eq(wrapper.extract(WATER_2, FluidMatch.EXACT, false), WATER_2), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.EXACT, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.EXACT, false), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(8), FluidStack.EMPTY), is(true));
    }

    @Test
    public void testExtractNoExtract() {
        IFluidHandler storage = new FluidTank(1) {
            @Override
            public FluidStack drain(int maxDrain, FluidAction action) {
                return FluidStack.EMPTY;
            }
        };
        storage.fill(LAVA_10, IFluidHandler.FluidAction.EXECUTE);
        IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper wrapper = new IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper(IngredientComponents.FLUIDSTACK, storage);
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.EXACT, false), FluidStack.EMPTY), is(true));
    }

    @Test
    public void testExtractMax() {
        assertThat(eq(wrapper.extract(10, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(1, true), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(10, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));
        assertThat(eq(wrapper.extract(10, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));

        assertThat(eq(wrapper.extract(10, false), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_1), is(true));
        assertThat(eq(wrapper.extract(1, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_1), is(true));
        assertThat(eq(wrapper.extract(10, false), LAVA_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(8), WATER_1), is(true));
        assertThat(eq(wrapper.extract(10, false), WATER_1), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(8), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.extract(10, false), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(4), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(6), FluidStack.EMPTY), is(true));
        assertThat(eq(storage.getFluidInTank(8), FluidStack.EMPTY), is(true));
    }

    @Test
    public void testGetSlots() {
        assertThat(wrapper.getSlots(), is(10));
    }

    @Test
    public void testGetSlotContents() {
        assertThat(eq(wrapper.getSlotContents(0), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getSlotContents(1), FluidStack.EMPTY), is(true));
        assertThat(eq(wrapper.getSlotContents(2), WATER_1), is(true));
    }

    @Test
    public void testGetMaxQuantitySlot() {
        assertThat(wrapper.getMaxQuantity(0), is(64L));
        assertThat(wrapper.getMaxQuantity(1), is(64L));
        assertThat(wrapper.getMaxQuantity(2), is(64L));
    }

    @Test
    public void testInsertSlot() {
        assertThat(wrapper.insert(0, WATER_64, true), is(FluidStack.EMPTY));
        assertThat(storage.getFluidInTank(0), is(FluidStack.EMPTY));

        assertThat(wrapper.insert(0, WATER_64, false), is(FluidStack.EMPTY));
        assertThat(eq(storage.getFluidInTank(0), WATER_64), is(true));
    }

    @Test
    public void testExtractAmountSlot() {
        assertThat(eq(wrapper.extract(0, 10, true), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), WATER_1), is(true));

        assertThat(eq(wrapper.extract(0, 10, false), WATER_10), is(true));
        assertThat(eq(storage.getFluidInTank(2), FluidStack.EMPTY), is(true));
    }
}
