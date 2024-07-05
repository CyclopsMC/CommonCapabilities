package org.cyclops.commoncapabilities;

import com.google.common.collect.Lists;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
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
import org.cyclops.commoncapabilities.ingredient.storage.IngredientComponentStorageWrapperHandlerEnergyStorage;
import org.cyclops.commoncapabilities.ingredient.storage.IngredientComponentStorageWrapperHandlerFluidStack;
import org.cyclops.commoncapabilities.ingredient.storage.IngredientComponentStorageWrapperHandlerItemStack;
import org.cyclops.commoncapabilities.ingredient.storage.IngredientComponentStorageWrapperHandlerItemStackSlotless;

/**
 * The ingredient components that will be registered by this mod.
 *
 * These should not be used directly, get their instances via the registry instead!
 *
 * @author rubensworks
 */
public class IngredientComponents {

    public static final IngredientComponent<ItemStack, Integer> ITEMSTACK =
            new IngredientComponent<>("minecraft:itemstack", new IngredientMatcherItemStack(),
                    new IngredientSerializerItemStack(), Lists.newArrayList(
                    new IngredientComponentCategoryType<>(ResourceLocation.parse("itemstack/item"),
                            Item.class, true,ItemStack::getItem, ItemMatch.ITEM, false),
                    new IngredientComponentCategoryType<>(ResourceLocation.parse("itemstack/count"),
                            Integer.class, false, ItemStack::getCount, ItemMatch.STACKSIZE, true),
                    new IngredientComponentCategoryType<>(ResourceLocation.parse("itemstack/data"),
                            DataComponentMap.class, false, ItemStack::getComponents, ItemMatch.DATA, false)
            )).setTranslationKey("recipecomponent.minecraft.itemstack");

    public static final IngredientComponent<FluidStack, Integer> FLUIDSTACK =
            new IngredientComponent<>("minecraft:fluidstack", new IngredientMatcherFluidStack(),
                    new IngredientSerializerFluidStack(), Lists.newArrayList(
                    new IngredientComponentCategoryType<>(ResourceLocation.parse("fluidstack/fluid"),
                            Fluid.class, true, FluidStack::getFluid, FluidMatch.FLUID, false),
                    new IngredientComponentCategoryType<>(ResourceLocation.parse("fluidstack/amount"),
                            Integer.class, false, FluidStack::getAmount, FluidMatch.AMOUNT, true),
                    new IngredientComponentCategoryType<>(ResourceLocation.parse("fluidstack/data"),
                            DataComponentMap.class, false, FluidStack::getComponents, FluidMatch.DATA, false)
            )).setTranslationKey("recipecomponent.minecraft.fluidstack");

    public static final IngredientComponent<Long, Boolean> ENERGY =
            new IngredientComponent<>("minecraft:energy", new IngredientMatcherEnergy(),
                    new IngredientSerializerEnergy(), Lists.newArrayList(
                    new IngredientComponentCategoryType<>(ResourceLocation.parse("energy/amount"),
                            Long.class, false, amount -> amount, true, true)
            )).setTranslationKey("recipecomponent.minecraft.energy");

    public static void registerStorageWrapperHandlers() {
        ENERGY.setStorageWrapperHandler(Capabilities.EnergyStorage.BLOCK, new IngredientComponentStorageWrapperHandlerEnergyStorage<>(ENERGY, Capabilities.EnergyStorage.BLOCK));
        ENERGY.setStorageWrapperHandler(Capabilities.EnergyStorage.ITEM, new IngredientComponentStorageWrapperHandlerEnergyStorage<>(ENERGY, Capabilities.EnergyStorage.ITEM));
        ENERGY.setStorageWrapperHandler(Capabilities.EnergyStorage.ENTITY, new IngredientComponentStorageWrapperHandlerEnergyStorage<>(ENERGY, Capabilities.EnergyStorage.ENTITY));

        ITEMSTACK.setStorageWrapperHandler(Capabilities.ItemHandler.BLOCK, new IngredientComponentStorageWrapperHandlerItemStack<>(ITEMSTACK, Capabilities.ItemHandler.BLOCK, org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.BLOCK));
        ITEMSTACK.setStorageWrapperHandler(Capabilities.ItemHandler.ITEM, new IngredientComponentStorageWrapperHandlerItemStack<>(ITEMSTACK, Capabilities.ItemHandler.ITEM, org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.ITEM));
        ITEMSTACK.setStorageWrapperHandler(Capabilities.ItemHandler.ENTITY, new IngredientComponentStorageWrapperHandlerItemStack<>(ITEMSTACK, Capabilities.ItemHandler.ENTITY, org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.ENTITY));

        ITEMSTACK.setStorageWrapperHandler(org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.BLOCK, new IngredientComponentStorageWrapperHandlerItemStackSlotless<>(ITEMSTACK, org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.BLOCK));
        ITEMSTACK.setStorageWrapperHandler(org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.ITEM, new IngredientComponentStorageWrapperHandlerItemStackSlotless<>(ITEMSTACK, org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.ITEM));
        ITEMSTACK.setStorageWrapperHandler(org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.ENTITY, new IngredientComponentStorageWrapperHandlerItemStackSlotless<>(ITEMSTACK, org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.ENTITY));

        FLUIDSTACK.setStorageWrapperHandler(Capabilities.FluidHandler.BLOCK, new IngredientComponentStorageWrapperHandlerFluidStack<>(FLUIDSTACK, Capabilities.FluidHandler.BLOCK));
        FLUIDSTACK.setStorageWrapperHandler(Capabilities.FluidHandler.ITEM, new IngredientComponentStorageWrapperHandlerFluidStack<>(FLUIDSTACK, Capabilities.FluidHandler.ITEM));
        FLUIDSTACK.setStorageWrapperHandler(Capabilities.FluidHandler.ENTITY, new IngredientComponentStorageWrapperHandlerFluidStack<>(FLUIDSTACK, Capabilities.FluidHandler.ENTITY));
    }

}
