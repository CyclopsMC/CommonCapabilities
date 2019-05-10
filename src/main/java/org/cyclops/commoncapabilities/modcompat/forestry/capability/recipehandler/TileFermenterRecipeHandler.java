package org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import forestry.api.recipes.IFermenterRecipe;
import forestry.factory.recipes.FermenterRecipeManager;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesItemStackOredictionary;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesList;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;

import java.util.List;
import java.util.Map;

/**
 * Recipe handler for {@link IFermenterRecipe}.
 * @author rubensworks
 */
public class TileFermenterRecipeHandler extends CraftingProviderRecipeHandlerAdapter<IFermenterRecipe> {

    public TileFermenterRecipeHandler() {
        super(new FermenterRecipeManager(),
                Sets.newHashSet(IngredientComponent.ITEMSTACK, IngredientComponent.FLUIDSTACK),
                Sets.newHashSet(IngredientComponent.FLUIDSTACK));
    }

    @Override
    protected IRecipeDefinition transformRecipe(IFermenterRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        if (recipe.getResourceOreName() == null) {
            inputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                    new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, recipe.getResource(), ItemMatch.EXACT)
            ))));
        } else {
            inputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesItemStackOredictionary(
                    Lists.newArrayList(recipe.getResourceOreName()), ItemMatch.EXACT
            )));
        }
        inputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                new PrototypedIngredient<>(IngredientComponent.FLUIDSTACK, new FluidStack(recipe.getFluidResource(), recipe.getFermentationValue()), FluidMatch.EXACT)
        ))));

        outputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(
                new FluidStack(recipe.getOutput(), (int) (recipe.getModifier() * recipe.getFermentationValue()))
        ));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected IFermenterRecipe findRecipe(IMixedIngredients input) {
        return FermenterRecipeManager.findMatchingRecipe(
                input.getFirstNonEmpty(IngredientComponent.ITEMSTACK),
                input.getFirstNonEmpty(IngredientComponent.FLUIDSTACK)
        );
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return (component == IngredientComponent.FLUIDSTACK && size == 1)
                || (component == IngredientComponent.ITEMSTACK && size <= 1);
    }
}
