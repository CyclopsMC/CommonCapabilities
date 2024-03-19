package org.cyclops.commoncapabilities.modcompat.thermalexpansion.recipehandler;

import cofh.thermalexpansion.util.managers.machine.BrewerManager;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.Reference;
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
import org.cyclops.commoncapabilities.capability.recipehandler.TransformedRecipeHandlerAdapter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Recipe handler for {@link BrewerManager.BrewerRecipe}.
 * @author rubensworks
 */
public class TileBrewerRecipeHandler extends TransformedRecipeHandlerAdapter<BrewerManager.BrewerRecipe> {

    public TileBrewerRecipeHandler() {
        super(Sets.newHashSet(IngredientComponent.ITEMSTACK, IngredientComponent.FLUIDSTACK),
                Sets.newHashSet(IngredientComponent.ITEMSTACK));
    }

    @Override
    protected String getRecipeCacheKey() {
        return Reference.MOD_THERMALEXPANSION + ":brewer";
    }

    @Override
    protected IRecipeDefinition transformRecipe(BrewerManager.BrewerRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        inputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(
                        Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, recipe.getInput(), ItemMatch.EXACT)))
        ));
        inputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(
                Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.FLUIDSTACK, recipe.getInputFluid(), FluidMatch.EXACT)))
        ));

        outputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(recipe.getOutputFluid()));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected BrewerManager.BrewerRecipe findRecipe(IMixedIngredients input) {
        return BrewerManager.getRecipe(
                input.getFirstNonEmpty(IngredientComponent.ITEMSTACK),
                input.getFirstNonEmpty(IngredientComponent.FLUIDSTACK)
        );
    }

    @Override
    protected Collection<BrewerManager.BrewerRecipe> getRecipesRaw() {
        return Lists.newArrayList(BrewerManager.getRecipeList());
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return (component == IngredientComponent.ITEMSTACK || component == IngredientComponent.FLUIDSTACK) && size == 1;
    }
}
