package org.cyclops.commoncapabilities.modcompat.thermalexpansion.recipehandler;

import cofh.thermalexpansion.util.managers.machine.CrucibleManager;
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
 * Recipe handler for {@link CrucibleManager.CrucibleRecipe}.
 * @author rubensworks
 */
public class TileCrucibleRecipeHandler extends TransformedRecipeHandlerAdapter<CrucibleManager.CrucibleRecipe> {

    public TileCrucibleRecipeHandler() {
        super(Sets.newHashSet(IngredientComponent.ITEMSTACK),
                Sets.newHashSet(IngredientComponent.FLUIDSTACK));
    }

    @Override
    protected String getRecipeCacheKey() {
        return Reference.MOD_THERMALEXPANSION + ":crucible";
    }

    @Override
    protected IRecipeDefinition transformRecipe(CrucibleManager.CrucibleRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        inputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(
                Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, recipe.getInput(), ItemMatch.EXACT))
        )));

        outputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(recipe.getOutput()));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected CrucibleManager.CrucibleRecipe findRecipe(IMixedIngredients input) {
        return CrucibleManager.getRecipe(Iterables.getFirst(input.getInstances(IngredientComponent.ITEMSTACK), ItemStack.EMPTY));
    }

    @Override
    protected Collection<CrucibleManager.CrucibleRecipe> getRecipesRaw() {
        return Lists.newArrayList(CrucibleManager.getRecipeList());
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.ITEMSTACK && size == 1;
    }
}
