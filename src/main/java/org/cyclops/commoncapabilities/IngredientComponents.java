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
import org.cyclops.commoncapabilities.api.ingredient.*;
import org.cyclops.commoncapabilities.ingredient.*;
import org.cyclops.commoncapabilities.ingredient.storage.IngredientComponentStorageWrapperHandlerEnergyHandler;
import org.cyclops.commoncapabilities.ingredient.storage.IngredientComponentStorageWrapperHandlerItemStack;
import org.cyclops.commoncapabilities.ingredient.storage.IngredientComponentStorageWrapperHandlerItemStackSlotless;
import org.cyclops.commoncapabilities.ingredient.storage.IngredientComponentStorageWrapperHandlerResourceHandler;

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
        ENERGY.setStorageWrapperHandler(Capabilities.Energy.BLOCK, new IngredientComponentStorageWrapperHandlerEnergyHandler<>(ENERGY, Capabilities.Energy.BLOCK));
        ENERGY.setStorageWrapperHandler(Capabilities.Energy.ITEM, new IngredientComponentStorageWrapperHandlerEnergyHandler<>(ENERGY, Capabilities.Energy.ITEM));
        ENERGY.setStorageWrapperHandler(Capabilities.Energy.ENTITY, new IngredientComponentStorageWrapperHandlerEnergyHandler<>(ENERGY, Capabilities.Energy.ENTITY));

        ITEMSTACK.setStorageWrapperHandler(Capabilities.Item.BLOCK, new IngredientComponentStorageWrapperHandlerItemStack<>(ITEMSTACK, Capabilities.Item.BLOCK, org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.BLOCK));
        ITEMSTACK.setStorageWrapperHandler(Capabilities.Item.ITEM, new IngredientComponentStorageWrapperHandlerItemStack<>(ITEMSTACK, Capabilities.Item.ITEM, org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.ITEM));
        ITEMSTACK.setStorageWrapperHandler(Capabilities.Item.ENTITY, new IngredientComponentStorageWrapperHandlerItemStack<>(ITEMSTACK, Capabilities.Item.ENTITY, org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.ENTITY));

        ITEMSTACK.setStorageWrapperHandler(org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.BLOCK, new IngredientComponentStorageWrapperHandlerItemStackSlotless<>(ITEMSTACK, org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.BLOCK));
        ITEMSTACK.setStorageWrapperHandler(org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.ITEM, new IngredientComponentStorageWrapperHandlerItemStackSlotless<>(ITEMSTACK, org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.ITEM));
        ITEMSTACK.setStorageWrapperHandler(org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.ENTITY, new IngredientComponentStorageWrapperHandlerItemStackSlotless<>(ITEMSTACK, org.cyclops.commoncapabilities.api.capability.Capabilities.SlotlessItemHandler.ENTITY));

        FLUIDSTACK.setStorageWrapperHandler(Capabilities.Fluid.BLOCK, new IngredientComponentStorageWrapperHandlerResourceHandler<>(FLUIDSTACK, Capabilities.Fluid.BLOCK, IngredientComponent.FLUIDSTACK_CONVERTER));
        FLUIDSTACK.setStorageWrapperHandler(Capabilities.Fluid.ITEM, new IngredientComponentStorageWrapperHandlerResourceHandler<>(FLUIDSTACK, Capabilities.Fluid.ITEM, IngredientComponent.FLUIDSTACK_CONVERTER));
        FLUIDSTACK.setStorageWrapperHandler(Capabilities.Fluid.ENTITY, new IngredientComponentStorageWrapperHandlerResourceHandler<>(FLUIDSTACK, Capabilities.Fluid.ENTITY, IngredientComponent.FLUIDSTACK_CONVERTER));
    }

}
