package org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import forestry.api.recipes.ICarpenterRecipe;
import forestry.api.recipes.IDescriptiveRecipe;
import forestry.core.recipes.RecipePair;
import forestry.factory.recipes.CarpenterRecipeManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesList;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler.VanillaCraftingTableRecipeHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Recipe handler for {@link ICarpenterRecipe}.
 * @author rubensworks
 */
public class TileCarpenterRecipeHandler extends CraftingProviderRecipeHandlerAdapter<ICarpenterRecipe> {

    public TileCarpenterRecipeHandler() {
        super(new CarpenterRecipeManager(),
                Sets.newHashSet(IngredientComponent.ITEMSTACK, IngredientComponent.FLUIDSTACK),
                Sets.newHashSet(IngredientComponent.ITEMSTACK));
    }

    @Override
    protected IRecipeDefinition transformRecipe(ICarpenterRecipe recipe) {
        IDescriptiveRecipe description = recipe.getCraftingGridRecipe();

        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        List<IPrototypedIngredientAlternatives<?, ?>> itemStackInputs = Lists.newArrayList();
        itemStackInputs.add(new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, recipe.getBox(), ItemMatch.EXACT)
        )));
        itemStackInputs.addAll(description.getRawIngredients()
                .stream()
                .map(alternatives -> new PrototypedIngredientAlternativesList<>(alternatives
                        .stream()
                        .map(itemStack -> new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, itemStack, ItemMatch.EXACT))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList()));
        inputs.put(IngredientComponent.ITEMSTACK, itemStackInputs);
        inputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                new PrototypedIngredient<>(IngredientComponent.FLUIDSTACK, recipe.getFluidResource(), FluidMatch.EXACT)
        ))));

        outputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(description.getOutput()));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected ICarpenterRecipe findRecipe(IMixedIngredients input) {
        List<ItemStack> itemStacks = input.getInstances(IngredientComponent.ITEMSTACK);

        ItemStack box = ItemStack.EMPTY;
        int dimension = (int) Math.ceil(Math.sqrt((float) itemStacks.size() - 1));
        InventoryCrafting grid = new InventoryCrafting(VanillaCraftingTableRecipeHandler.DUMMY_CONTAINTER, dimension, dimension);
        for (int i = 0; i < itemStacks.size(); i++) {
            if (i == 0) {
                box = itemStacks.get(i);
            } else {
                grid.setInventorySlotContents(i - 1, itemStacks.get(i));
            }
        }

        FluidStack fluidStack = input.getFirstNonEmpty(IngredientComponent.FLUIDSTACK);

        RecipePair<ICarpenterRecipe> recipePair = CarpenterRecipeManager.findMatchingRecipe(fluidStack, box, grid);
        return recipePair == null ? null : recipePair.getRecipe();
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return (component == IngredientComponent.FLUIDSTACK && size <= 1)
                || (component == IngredientComponent.ITEMSTACK && (size <= 10));
    }
}
