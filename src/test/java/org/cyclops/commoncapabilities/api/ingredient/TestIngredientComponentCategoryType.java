package org.cyclops.commoncapabilities.api.ingredient;

import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TestIngredientComponentCategoryType {

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        Bootstrap.register();
    }

    @Test
    public void testItemStack() {
        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().size(), is(4));

        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(0).getCategoryType(), equalTo(Item.class));
        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(0).isReferenceEqual(), is(true));
        Function<ItemStack, ?> classifier0 = IngredientComponents.ITEMSTACK.getCategoryTypes().get(0).getClassifier();
        assertThat(classifier0.apply(new ItemStack(Items.APPLE)), is(Items.APPLE));
        assertThat(classifier0.apply(new ItemStack(Blocks.DIRT)), is(Item.getItemFromBlock(Blocks.DIRT)));

        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(1).getCategoryType(), equalTo(Integer.class));
        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(1).isReferenceEqual(), is(false));
        Function<ItemStack, ?> classifier1 = IngredientComponents.ITEMSTACK.getCategoryTypes().get(1).getClassifier();
        assertThat(classifier1.apply(new ItemStack(Items.APPLE, 1, 0)), is(0));
        assertThat(classifier1.apply(new ItemStack(Items.APPLE, 1, 1)), is(1));
        assertThat(classifier1.apply(new ItemStack(Items.APPLE, 1, 2)), is(2));

        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(2).getCategoryType(), equalTo(Integer.class));
        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(2).isReferenceEqual(), is(false));
        Function<ItemStack, ?> classifier2 = IngredientComponents.ITEMSTACK.getCategoryTypes().get(2).getClassifier();
        assertThat(classifier2.apply(new ItemStack(Items.APPLE, 0)), is(0));
        assertThat(classifier2.apply(new ItemStack(Items.APPLE, 1)), is(1));
        assertThat(classifier2.apply(new ItemStack(Items.APPLE, 2)), is(2));

        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(3).getCategoryType(), equalTo(NBTTagCompound.class));
        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(3).isReferenceEqual(), is(false));
        Function<ItemStack, ?> classifier3 = IngredientComponents.ITEMSTACK.getCategoryTypes().get(3).getClassifier();
        assertThat(classifier3.apply(new ItemStack(Items.APPLE)), nullValue());
        ItemStack itemStack = new ItemStack(Items.APPLE);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("a", true);
        itemStack.setTagCompound(tag);
        assertThat(classifier3.apply(itemStack), is(tag));
    }

    @Test
    public void testFluidStack() {
        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().size(), is(3));

        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(0).getCategoryType(), equalTo(Fluid.class));
        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(0).isReferenceEqual(), is(true));
        Function<FluidStack, ?> classifier0 = IngredientComponents.FLUIDSTACK.getCategoryTypes().get(0).getClassifier();
        assertThat(classifier0.apply(new FluidStack(FluidRegistry.WATER, 1)), is(FluidRegistry.WATER));
        assertThat(classifier0.apply(new FluidStack(FluidRegistry.LAVA, 2)), is(FluidRegistry.LAVA));

        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(1).getCategoryType(), equalTo(Integer.class));
        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(1).isReferenceEqual(), is(false));
        Function<FluidStack, ?> classifier1 = IngredientComponents.FLUIDSTACK.getCategoryTypes().get(1).getClassifier();
        assertThat(classifier1.apply(new FluidStack(FluidRegistry.WATER, 0)), is(0));
        assertThat(classifier1.apply(new FluidStack(FluidRegistry.WATER, 1)), is(1));
        assertThat(classifier1.apply(new FluidStack(FluidRegistry.WATER, 2)), is(2));

        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(2).getCategoryType(), equalTo(NBTTagCompound.class));
        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(2).isReferenceEqual(), is(false));
        Function<FluidStack, ?> classifier2 = IngredientComponents.FLUIDSTACK.getCategoryTypes().get(2).getClassifier();
        assertThat(classifier2.apply(new FluidStack(FluidRegistry.WATER, 1)), nullValue());
        FluidStack fluidStack = new FluidStack(FluidRegistry.WATER, 1);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("a", true);
        fluidStack.tag = tag;
        assertThat(classifier2.apply(fluidStack), is(tag));
    }

    @Test
    public void testEnergy() {
        assertThat(IngredientComponents.ENERGY.getCategoryTypes().size(), is(1));

        assertThat(IngredientComponents.ENERGY.getCategoryTypes().get(0).getCategoryType(), equalTo(Integer.class));
        assertThat(IngredientComponents.ENERGY.getCategoryTypes().get(0).isReferenceEqual(), is(false));
        Function<Integer, ?> classifier1 = IngredientComponents.ENERGY.getCategoryTypes().get(0).getClassifier();
        assertThat(classifier1.apply(0), is(0));
        assertThat(classifier1.apply(1), is(1));
        assertThat(classifier1.apply(2), is(2));
    }

}
