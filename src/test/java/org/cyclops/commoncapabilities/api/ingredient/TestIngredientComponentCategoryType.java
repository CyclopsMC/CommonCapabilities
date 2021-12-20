package org.cyclops.commoncapabilities.api.ingredient;

import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.registry.Bootstrap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        Bootstrap.bootStrap();
    }

    @Test
    public void testItemStack() {
        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().size(), is(3));

        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(0).getCategoryType(), equalTo(Item.class));
        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(0).isReferenceEqual(), is(true));
        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(0).isPrimaryQuantifier(), is(false));
        Function<ItemStack, ?> classifier0 = IngredientComponents.ITEMSTACK.getCategoryTypes().get(0).getClassifier();
        assertThat(classifier0.apply(new ItemStack(Items.APPLE)), is(Items.APPLE));
        assertThat(classifier0.apply(new ItemStack(Blocks.DIRT)), is(Item.byBlock(Blocks.DIRT)));

        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(1).getCategoryType(), equalTo(Integer.class));
        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(1).isReferenceEqual(), is(false));
        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(1).isPrimaryQuantifier(), is(true));
        Function<ItemStack, ?> classifier2 = IngredientComponents.ITEMSTACK.getCategoryTypes().get(1).getClassifier();
        assertThat(classifier2.apply(new ItemStack(Items.APPLE, 0)), is(0));
        assertThat(classifier2.apply(new ItemStack(Items.APPLE, 1)), is(1));
        assertThat(classifier2.apply(new ItemStack(Items.APPLE, 2)), is(2));

        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(2).getCategoryType(), equalTo(CompoundNBT.class));
        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(2).isReferenceEqual(), is(false));
        assertThat(IngredientComponents.ITEMSTACK.getCategoryTypes().get(2).isPrimaryQuantifier(), is(false));
        Function<ItemStack, ?> classifier3 = IngredientComponents.ITEMSTACK.getCategoryTypes().get(2).getClassifier();
        assertThat(classifier3.apply(new ItemStack(Items.APPLE)), nullValue());
        ItemStack itemStack = new ItemStack(Items.APPLE);
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("a", true);
        itemStack.setTag(tag);
        assertThat(classifier3.apply(itemStack), is(tag));
    }

    @Test
    public void testFluidStack() {
        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().size(), is(3));

        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(0).getCategoryType(), equalTo(Fluid.class));
        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(0).isReferenceEqual(), is(true));
        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(0).isPrimaryQuantifier(), is(false));
        Function<FluidStack, ?> classifier0 = IngredientComponents.FLUIDSTACK.getCategoryTypes().get(0).getClassifier();
        assertThat(classifier0.apply(new FluidStack(Fluids.WATER, 1)), is(Fluids.WATER));
        assertThat(classifier0.apply(new FluidStack(Fluids.LAVA, 2)), is(Fluids.LAVA));

        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(1).getCategoryType(), equalTo(Integer.class));
        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(1).isReferenceEqual(), is(false));
        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(1).isPrimaryQuantifier(), is(true));
        Function<FluidStack, ?> classifier1 = IngredientComponents.FLUIDSTACK.getCategoryTypes().get(1).getClassifier();
        assertThat(classifier1.apply(new FluidStack(Fluids.WATER, 0)), is(0));
        assertThat(classifier1.apply(new FluidStack(Fluids.WATER, 1)), is(1));
        assertThat(classifier1.apply(new FluidStack(Fluids.WATER, 2)), is(2));

        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(2).getCategoryType(), equalTo(CompoundNBT.class));
        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(2).isReferenceEqual(), is(false));
        assertThat(IngredientComponents.FLUIDSTACK.getCategoryTypes().get(2).isPrimaryQuantifier(), is(false));
        Function<FluidStack, ?> classifier2 = IngredientComponents.FLUIDSTACK.getCategoryTypes().get(2).getClassifier();
        assertThat(classifier2.apply(new FluidStack(Fluids.WATER, 1)), nullValue());
        FluidStack fluidStack = new FluidStack(Fluids.WATER, 1);
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("a", true);
        fluidStack.setTag(tag);
        assertThat(classifier2.apply(fluidStack), is(tag));
    }

    @Test
    public void testEnergy() {
        assertThat(IngredientComponents.ENERGY.getCategoryTypes().size(), is(1));

        assertThat(IngredientComponents.ENERGY.getCategoryTypes().get(0).getCategoryType(), equalTo(Long.class));
        assertThat(IngredientComponents.ENERGY.getCategoryTypes().get(0).isReferenceEqual(), is(false));
        assertThat(IngredientComponents.ENERGY.getCategoryTypes().get(0).isPrimaryQuantifier(), is(true));
        Function<Long, ?> classifier1 = IngredientComponents.ENERGY.getCategoryTypes().get(0).getClassifier();
        assertThat(classifier1.apply(0L), is(0L));
        assertThat(classifier1.apply(1L), is(1L));
        assertThat(classifier1.apply(2L), is(2L));
    }

}
