package org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import forestry.api.recipes.IStillRecipe;
import forestry.factory.recipes.StillRecipeManager;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesList;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;

import java.util.List;
import java.util.Map;

/**
 * Recipe handler for {@link IStillRecipe}.
 * @author rubensworks
 */
public class TileStillRecipeHandler extends CraftingProviderRecipeHandlerAdapter<IStillRecipe> {

    public TileStillRecipeHandler() {
        super(new StillRecipeManager(),
                Sets.newHashSet(IngredientComponent.FLUIDSTACK),
                Sets.newHashSet(IngredientComponent.FLUIDSTACK));
    }

    @Override
    protected IRecipeDefinition transformRecipe(IStillRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        inputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                new PrototypedIngredient<>(IngredientComponent.FLUIDSTACK, recipe.getInput(), FluidMatch.EXACT)
        ))));

        outputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(recipe.getOutput()));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected IStillRecipe findRecipe(IMixedIngredients input) {
        return StillRecipeManager.findMatchingRecipe(input.getFirstNonEmpty(IngredientComponent.FLUIDSTACK));
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.FLUIDSTACK && size == 1;
    }
}
