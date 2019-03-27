package org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import forestry.api.recipes.ISqueezerRecipe;
import forestry.factory.recipes.SqueezerRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
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
import java.util.stream.Collectors;

/**
 * Recipe handler for {@link ISqueezerRecipe}.
 * @author rubensworks
 */
public class TileSqueezerRecipeHandler extends CraftingProviderRecipeHandlerAdapter<ISqueezerRecipe> {

    public TileSqueezerRecipeHandler() {
        super(new SqueezerRecipeManager(),
                Sets.newHashSet(IngredientComponent.ITEMSTACK),
                Sets.newHashSet(IngredientComponent.FLUIDSTACK));
    }

    @Override
    protected IRecipeDefinition transformRecipe(ISqueezerRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        inputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(
                recipe.getResources()
                        .stream()
                        .map(itemStack -> new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, itemStack, ItemMatch.EXACT))
                        .collect(Collectors.toList())
        )));

        outputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(recipe.getFluidOutput()));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected ISqueezerRecipe findRecipe(IMixedIngredients input) {
        NonNullList<ItemStack> elements = NonNullList.create();
        elements.addAll(input.getInstances(IngredientComponent.ITEMSTACK));
        return SqueezerRecipeManager.findMatchingRecipe(elements);
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.ITEMSTACK;
    }
}
