package org.cyclops.commoncapabilities.modcompat.thermalexpansion.recipehandler;

import cofh.thermalexpansion.block.machine.TileRefinery;
import cofh.thermalexpansion.util.managers.machine.RefineryManager;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesList;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import org.cyclops.commoncapabilities.capability.recipehandler.TransformedRecipeHandlerAdapter;
import org.cyclops.commoncapabilities.modcompat.thermalexpansion.ThermalExpansionHelpers;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Recipe handler for {@link RefineryManager.RefineryRecipe}.
 * @author rubensworks
 */
public class TileRefineryRecipeHandler extends TransformedRecipeHandlerAdapter<RefineryManager.RefineryRecipe> {

    private final TileRefinery tile;

    public TileRefineryRecipeHandler(TileRefinery tile) {
        super(Sets.newHashSet(IngredientComponent.FLUIDSTACK),
                Sets.newHashSet(IngredientComponent.FLUIDSTACK, IngredientComponent.ITEMSTACK));
        this.tile = tile;
    }

    protected boolean isAugmentPotion() {
        try {
            return (boolean) ThermalExpansionHelpers.FIELD_TILEREFINERY_AUGMENTPOTION.get(tile);
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    @Override
    protected String getRecipeCacheKey() {
        return Reference.MOD_THERMALEXPANSION + ":refinery:" + isAugmentPotion();
    }

    @Override
    protected IRecipeDefinition transformRecipe(RefineryManager.RefineryRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        inputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(
                Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.FLUIDSTACK, recipe.getInput(), FluidMatch.EXACT))
        )));

        if (recipe.getChance() == 1) {
            outputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(recipe.getOutputItem()));
        }
        if (recipe.getOutputFluid() != null) {
            outputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(recipe.getOutputFluid()));
        }

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected RefineryManager.RefineryRecipe findRecipe(IMixedIngredients input) {
        if (isAugmentPotion()) {
            return RefineryManager.getRecipePotion(input.getFirstNonEmpty(IngredientComponent.FLUIDSTACK));
        } else {
            return RefineryManager.getRecipe(input.getFirstNonEmpty(IngredientComponent.FLUIDSTACK));
        }
    }

    @Override
    protected Collection<RefineryManager.RefineryRecipe> getRecipesRaw() {
        return Lists.newArrayList(isAugmentPotion() ? RefineryManager.getRecipeListPotion() : RefineryManager.getRecipeList());
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.FLUIDSTACK && size == 1;
    }
}
