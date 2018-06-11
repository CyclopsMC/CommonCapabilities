package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.init.Bootstrap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.FluidHandlerConcatenate;
import org.cyclops.commoncapabilities.IngredientComponents;
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
        Bootstrap.register();
    }

    public static boolean eq(FluidStack a, FluidStack b) {
        return IngredientComponents.FLUIDSTACK.getMatcher().matchesExactly(a, b);
    }

    @Before
    public void beforeEach() {
        WATER_1 = new FluidStack(FluidRegistry.WATER, 1);
        LAVA_1_NB = new FluidStack(FluidRegistry.LAVA, 1, new NBTTagCompound());
        LAVA_10 = new FluidStack(FluidRegistry.LAVA, 10);
        WATER_10 = new FluidStack(FluidRegistry.WATER, 10);

        WATER_64 = new FluidStack(FluidRegistry.WATER, 64);
        LAVA_64 = new FluidStack(FluidRegistry.LAVA, 64);
        LAVA_64_NB = new FluidStack(FluidRegistry.LAVA, 64, new NBTTagCompound());
        WATER_11 = new FluidStack(FluidRegistry.WATER, 11);
        LAVA_11_NB = new FluidStack(FluidRegistry.LAVA, 11, new NBTTagCompound());

        t1 = new FluidTank(64);
        t2 = new FluidTank(64);
        t3 = new FluidTank(64);
        t4 = new FluidTank(64);
        innerStorage = new FluidHandlerConcatenate(
                new FluidTank(64),
                new FluidTank(64),
                t1,
                new FluidTank(64),
                t2,
                new FluidTank(64),
                t3,
                new FluidTank(64),
                t4,
                new FluidTank(64)
        );
        t1.fill(WATER_1.copy(), true);
        t2.fill(LAVA_1_NB.copy(), true);
        t3.fill(LAVA_10.copy(), true);
        t4.fill(WATER_10.copy(), true);
        storage = new IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper(IngredientComponents.FLUIDSTACK, innerStorage);
        wrapper = new IngredientComponentStorageWrapperHandlerFluidStack.FluidStorageWrapper(storage);
    }

    @Test
    public void testGetTankProperties() {
        IFluidTankProperties[] props = wrapper.getTankProperties();
        assertThat(props.length, is(10));

        for (IFluidTankProperties prop : props) {
            assertThat(prop.getCapacity(), is(Integer.MAX_VALUE));
            assertThat(prop.canDrain(), is(true));
            assertThat(prop.canDrainFluidType(null), is(true));
            assertThat(prop.canFill(), is(true));
            assertThat(prop.canFillFluidType(null), is(true));
        }

        assertThat(eq(props[0].getContents(), null), is(true));
        assertThat(eq(props[1].getContents(), null), is(true));
        assertThat(eq(props[2].getContents(), WATER_1), is(true));
        assertThat(eq(props[3].getContents(), null), is(true));
        assertThat(eq(props[4].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(props[5].getContents(), null), is(true));
        assertThat(eq(props[6].getContents(), LAVA_10), is(true));
        assertThat(eq(props[7].getContents(), null), is(true));
        assertThat(eq(props[8].getContents(), WATER_10), is(true));
        assertThat(eq(props[9].getContents(), null), is(true));
    }

    @Test
    public void testFill() {
        assertThat(wrapper.fill(WATER_64, false), is(64));
        assertThat(wrapper.fill(WATER_64, false), is(64));
        assertThat(wrapper.fill(WATER_64, false), is(64));
        IFluidTankProperties[] props1 = wrapper.getTankProperties();
        assertThat(eq(props1[0].getContents(), null), is(true));
        assertThat(eq(props1[1].getContents(), null), is(true));
        assertThat(eq(props1[2].getContents(), WATER_1), is(true));

        assertThat(wrapper.fill(WATER_64, true), is(64));
        assertThat(wrapper.fill(WATER_64, true), is(64));
        assertThat(wrapper.fill(WATER_64, true), is(64));
        IFluidTankProperties[] props2 = wrapper.getTankProperties();
        assertThat(eq(props2[0].getContents(), WATER_64), is(true));
        assertThat(eq(props2[1].getContents(), WATER_64), is(true));
        assertThat(eq(props2[2].getContents(), WATER_64), is(true));

        assertThat(wrapper.fill(WATER_64, true), is(64));
        assertThat(wrapper.fill(WATER_64, true), is(64));
        assertThat(wrapper.fill(WATER_64, true), is(64));
        assertThat(wrapper.fill(WATER_64, true), is(64));
        assertThat(wrapper.fill(WATER_64, true), is(53));
    }

    @Test
    public void testDrain() {
        assertThat(eq(wrapper.drain(WATER_10, false), WATER_10), is(true));
        assertThat(eq(wrapper.drain(WATER_10, false), WATER_10), is(true));
        assertThat(eq(wrapper.drain(WATER_10, false), WATER_10), is(true));
        IFluidTankProperties[] props1 = wrapper.getTankProperties();
        assertThat(eq(props1[2].getContents(), WATER_1), is(true));
        assertThat(eq(props1[8].getContents(), WATER_10), is(true));

        assertThat(eq(wrapper.drain(WATER_10, true), WATER_10), is(true));
        assertThat(eq(wrapper.drain(WATER_10, true), WATER_1), is(true));
        assertThat(eq(wrapper.drain(WATER_10, true), null), is(true));
        IFluidTankProperties[] props2 = wrapper.getTankProperties();
        assertThat(eq(props2[2].getContents(), null), is(true));
        assertThat(eq(props2[8].getContents(), null), is(true));
    }

    @Test
    public void testDrainMax() {
        assertThat(eq(wrapper.drain(10, false), WATER_10), is(true));
        assertThat(eq(wrapper.drain(10, false), WATER_10), is(true));
        assertThat(eq(wrapper.drain(10, false), WATER_10), is(true));
        IFluidTankProperties[] props1 = wrapper.getTankProperties();
        assertThat(eq(props1[2].getContents(), WATER_1), is(true));
        assertThat(eq(props1[4].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(props1[6].getContents(), LAVA_10), is(true));
        assertThat(eq(props1[8].getContents(), WATER_10), is(true));

        assertThat(eq(wrapper.drain(10, true), WATER_10), is(true));
        assertThat(eq(wrapper.drain(10, true), LAVA_1_NB), is(true));
        assertThat(eq(wrapper.drain(10, true), LAVA_10), is(true));
        IFluidTankProperties[] props2 = wrapper.getTankProperties();
        assertThat(eq(props2[2].getContents(), null), is(true));
        assertThat(eq(props2[4].getContents(), null), is(true));
        assertThat(eq(props2[6].getContents(), null), is(true));
        assertThat(eq(props2[8].getContents(), WATER_1), is(true));
    }

}
