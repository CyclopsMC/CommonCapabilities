package org.cyclops.commoncapabilities.modcompat.thermalexpansion.recipehandler;

import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesList;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import org.cyclops.commoncapabilities.capability.recipehandler.TransformedRecipeHandlerAdapter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Recipe handler for {@link PulverizerManager.PulverizerRecipe}.
 * @author rubensworks
 */
public class TilePulverizerRecipeHandler extends TransformedRecipeHandlerAdapter<PulverizerManager.PulverizerRecipe> {

    public TilePulverizerRecipeHandler() {
        super(Sets.newHashSet(IngredientComponent.ITEMSTACK),
                Sets.newHashSet(IngredientComponent.ITEMSTACK));
    }

    @Override
    protected String getRecipeCacheKey() {
        return Reference.MOD_THERMALEXPANSION + ":pulverizer";
    }

    @Override
    protected IRecipeDefinition transformRecipe(PulverizerManager.PulverizerRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        inputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(
                Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, recipe.getInput(), ItemMatch.EXACT))
        )));

        outputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(recipe.getPrimaryOutput()));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected PulverizerManager.PulverizerRecipe findRecipe(IMixedIngredients input) {
        return PulverizerManager.getRecipe(input.getFirstNonEmpty(IngredientComponent.ITEMSTACK));
    }

    @Override
    protected Collection<PulverizerManager.PulverizerRecipe> getRecipesRaw() {
        return Lists.newArrayList(PulverizerManager.getRecipeList());
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.ITEMSTACK && size == 1;
    }
}
