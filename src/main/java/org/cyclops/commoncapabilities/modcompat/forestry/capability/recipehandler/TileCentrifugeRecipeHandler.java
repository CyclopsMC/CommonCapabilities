package org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import forestry.api.recipes.ICentrifugeRecipe;
import forestry.factory.recipes.CentrifugeRecipeManager;
import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
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
 * Recipe handler for {@link ICentrifugeRecipe}.
 * @author rubensworks
 */
public class TileCentrifugeRecipeHandler extends CraftingProviderRecipeHandlerAdapter<ICentrifugeRecipe> {

    public TileCentrifugeRecipeHandler() {
        super(new CentrifugeRecipeManager(),
                Sets.newHashSet(IngredientComponent.ITEMSTACK),
                Sets.newHashSet());
    }

    @Override
    protected IRecipeDefinition transformRecipe(ICentrifugeRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        inputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(
                Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, recipe.getInput(), ItemMatch.EXACT)))
        ));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected ICentrifugeRecipe findRecipe(IMixedIngredients input) {
        return CentrifugeRecipeManager.findMatchingRecipe(Iterables.getFirst(input.getInstances(IngredientComponent.ITEMSTACK), ItemStack.EMPTY));
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.ITEMSTACK && size == 1;
    }
}
