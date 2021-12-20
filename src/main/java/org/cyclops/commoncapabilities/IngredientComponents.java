package org.cyclops.commoncapabilities;

import com.google.common.collect.Lists;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;
import org.cyclops.commoncapabilities.capability.itemhandler.SlotlessItemHandlerConfig;
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
                    new IngredientComponentCategoryType<>(new ResourceLocation("itemstack/item"),
                            Item.class, true,ItemStack::getItem, ItemMatch.ITEM, false),
                    new IngredientComponentCategoryType<>(new ResourceLocation("itemstack/count"),
                            Integer.class, false, ItemStack::getCount, ItemMatch.STACKSIZE, true),
                    new IngredientComponentCategoryType<>(new ResourceLocation("itemstack/tag"),
                            CompoundTag.class, false, ItemStack::getTag, ItemMatch.TAG, false)
            )).setTranslationKey("recipecomponent.minecraft.itemstack");

    public static final IngredientComponent<FluidStack, Integer> FLUIDSTACK =
            new IngredientComponent<>("minecraft:fluidstack", new IngredientMatcherFluidStack(),
                    new IngredientSerializerFluidStack(), Lists.newArrayList(
                    new IngredientComponentCategoryType<>(new ResourceLocation("fluidstack/fluid"),
                            Fluid.class, true, FluidStack::getFluid, FluidMatch.FLUID, false),
                    new IngredientComponentCategoryType<>(new ResourceLocation("fluidstack/amount"),
                            Integer.class, false, FluidStack::getAmount, FluidMatch.AMOUNT, true),
                    new IngredientComponentCategoryType<>(new ResourceLocation("fluidstack/tag"),
                            CompoundTag.class, false, FluidStack::getTag, FluidMatch.TAG, false)
            )).setTranslationKey("recipecomponent.minecraft.fluidstack");

    public static final IngredientComponent<Long, Boolean> ENERGY =
            new IngredientComponent<>("minecraft:energy", new IngredientMatcherEnergy(),
                    new IngredientSerializerEnergy(), Lists.newArrayList(
                    new IngredientComponentCategoryType<>(new ResourceLocation("energy/amount"),
                            Long.class, false, amount -> amount, true, true)
            )).setTranslationKey("recipecomponent.minecraft.energy");

    public static void registerStorageWrapperHandlers() {
        ENERGY.setStorageWrapperHandler(CapabilityEnergy.ENERGY, new IngredientComponentStorageWrapperHandlerEnergyStorage(ENERGY));
        ITEMSTACK.setStorageWrapperHandler(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, new IngredientComponentStorageWrapperHandlerItemStack(ITEMSTACK));
        ITEMSTACK.setStorageWrapperHandler(SlotlessItemHandlerConfig.CAPABILITY, new IngredientComponentStorageWrapperHandlerItemStackSlotless(ITEMSTACK));
        IngredientComponentStorageWrapperHandlerFluidStack fluidWrapper = new IngredientComponentStorageWrapperHandlerFluidStack(FLUIDSTACK);
        FLUIDSTACK.setStorageWrapperHandler(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, fluidWrapper);
        FLUIDSTACK.setStorageWrapperHandler(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, fluidWrapper);
    }

}
