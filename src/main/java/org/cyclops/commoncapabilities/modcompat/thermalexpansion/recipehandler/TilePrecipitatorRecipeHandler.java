package org.cyclops.commoncapabilities.modcompat.thermalexpansion.recipehandler;

import cofh.thermalexpansion.util.managers.machine.PrecipitatorManager;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraftforge.fluids.FluidStack;
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

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Recipe handler for {@link PrecipitatorManager.PrecipitatorRecipe}.
 * @author rubensworks
 */
public class TilePrecipitatorRecipeHandler extends TransformedRecipeHandlerAdapter<PrecipitatorManager.PrecipitatorRecipe> {

    public TilePrecipitatorRecipeHandler() {
        super(Sets.newHashSet(IngredientComponent.FLUIDSTACK),
                Sets.newHashSet(IngredientComponent.ITEMSTACK));
    }

    @Override
    protected String getRecipeCacheKey() {
        return Reference.MOD_THERMALEXPANSION + ":precipitator";
    }

    @Override
    protected IRecipeDefinition transformRecipe(PrecipitatorManager.PrecipitatorRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        inputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(
                        Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.FLUIDSTACK, recipe.getInput(), FluidMatch.EXACT))
                )
        ));

        outputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(recipe.getOutput()));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected PrecipitatorManager.PrecipitatorRecipe findRecipe(IMixedIngredients input) {
        FluidStack fluidStack = input.getFirstNonEmpty(IngredientComponent.FLUIDSTACK);

        for (PrecipitatorManager.PrecipitatorRecipe recipe : PrecipitatorManager.getRecipeList()) {
            if (recipe.getInput().equals(fluidStack)) {
                return recipe;
            }
        }

        return null;
    }

    @Override
    protected Collection<PrecipitatorManager.PrecipitatorRecipe> getRecipesRaw() {
        return Lists.newArrayList(PrecipitatorManager.getRecipeList());
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.FLUIDSTACK && size == 1;
    }
}
