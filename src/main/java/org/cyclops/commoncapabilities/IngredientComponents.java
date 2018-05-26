package org.cyclops.commoncapabilities;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;
import org.cyclops.commoncapabilities.ingredient.IngredientMatcherEnergy;
import org.cyclops.commoncapabilities.ingredient.IngredientMatcherFluidStack;
import org.cyclops.commoncapabilities.ingredient.IngredientMatcherItemStack;
import org.cyclops.commoncapabilities.ingredient.IngredientSerializerEnergy;
import org.cyclops.commoncapabilities.ingredient.IngredientSerializerFluidStack;
import org.cyclops.commoncapabilities.ingredient.IngredientSerializerItemStack;

/**
 * The ingredient components that will be registered by this mod.
 * @author rubensworks
 */
public class IngredientComponents {

    public static final IngredientComponent<ItemStack, Integer> ITEMSTACK =
            new IngredientComponent<>("minecraft:itemstack", new IngredientMatcherItemStack(),
                    new IngredientSerializerItemStack(), Lists.newArrayList(
                    new IngredientComponentCategoryType<>(new ResourceLocation("itemstack/item"),
                            Item.class, true,ItemStack::getItem, ItemMatch.ITEM, false),
                    new IngredientComponentCategoryType<>(new ResourceLocation("itemstack/metadata"),
                            Integer.class, false, ItemStack::getMetadata, ItemMatch.DAMAGE, false),
                    new IngredientComponentCategoryType<>(new ResourceLocation("itemstack/count"),
                            Integer.class, false, ItemStack::getCount, ItemMatch.STACKSIZE, true),
                    new IngredientComponentCategoryType<>(new ResourceLocation("itemstack/tag"),
                            NBTTagCompound.class, false, ItemStack::getTagCompound, ItemMatch.NBT, false)
            )).setUnlocalizedName("recipecomponent.minecraft.itemstack");

    public static final IngredientComponent<FluidStack, Integer> FLUIDSTACK =
            new IngredientComponent<>("minecraft:fluidstack", new IngredientMatcherFluidStack(),
                    new IngredientSerializerFluidStack(), Lists.newArrayList(
                    new IngredientComponentCategoryType<>(new ResourceLocation("fluidstack/fluid"),
                            Fluid.class, true, FluidStack::getFluid, FluidMatch.FLUID, false),
                    new IngredientComponentCategoryType<>(new ResourceLocation("fluidstack/amount"),
                            Integer.class, false, fluidStack -> fluidStack.amount, FluidMatch.AMOUNT, true),
                    new IngredientComponentCategoryType<>(new ResourceLocation("fluidstack/tag"),
                            NBTTagCompound.class, false, fluidStack -> fluidStack.tag, FluidMatch.NBT, false)
            )).setUnlocalizedName("recipecomponent.minecraft.fluidstack");

    public static final IngredientComponent<Integer, Boolean> ENERGY =
            new IngredientComponent<>("minecraft:energy", new IngredientMatcherEnergy(),
                    new IngredientSerializerEnergy(), Lists.newArrayList(
                    new IngredientComponentCategoryType<>(new ResourceLocation("energy/amount"),
                            Integer.class, false, amount -> amount, true, true)
            )).setUnlocalizedName("recipecomponent.minecraft.energy");

}
