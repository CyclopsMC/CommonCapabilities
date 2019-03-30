package org.cyclops.commoncapabilities.modcompat.thermalexpansion.recipehandler;

import cofh.thermalexpansion.block.machine.TileExtruder;
import cofh.thermalexpansion.util.managers.machine.ExtruderManager;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraftforge.fluids.FluidStack;
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
import org.cyclops.commoncapabilities.modcompat.thermalexpansion.ThermalExpansionHelpers;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Recipe handler for {@link ExtruderManager.ExtruderRecipe}.
 * @author rubensworks
 */
public class TileExtruderRecipeHandler extends TransformedRecipeHandlerAdapter<ExtruderManager.ExtruderRecipe> {

    private final TileExtruder tile;

    public TileExtruderRecipeHandler(TileExtruder tile) {
        super(Sets.newHashSet(IngredientComponent.FLUIDSTACK),
                Sets.newHashSet(IngredientComponent.ITEMSTACK));
        this.tile = tile;
    }

    protected boolean isAugmentSedimentary() {
        try {
            return (boolean) ThermalExpansionHelpers.FIELD_TILEEXTRUDER_AUGMENTSEDIMANTARY.get(tile);
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    @Override
    protected String getRecipeCacheKey() {
        return Reference.MOD_THERMALEXPANSION + ":extruder:" + isAugmentSedimentary();
    }

    @Override
    protected IRecipeDefinition transformRecipe(ExtruderManager.ExtruderRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        inputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(
                new PrototypedIngredientAlternativesList<>(
                        Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.FLUIDSTACK, recipe.getInputCold(), ItemMatch.EXACT))
                ),
                new PrototypedIngredientAlternativesList<>(
                        Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.FLUIDSTACK, recipe.getInputHot(), ItemMatch.EXACT))
                )
        ));

        outputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(recipe.getOutput()));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected ExtruderManager.ExtruderRecipe findRecipe(IMixedIngredients input) {
        FluidStack fluidStackCold = Iterables.get(input.getInstances(IngredientComponent.FLUIDSTACK), 0, null);
        FluidStack fluidStackHot = Iterables.get(input.getInstances(IngredientComponent.FLUIDSTACK), 1, null);

        for (ExtruderManager.ExtruderRecipe recipe : ExtruderManager.getRecipeList(isAugmentSedimentary())) {
            if (recipe.getInputCold().equals(fluidStackCold) && recipe.getInputHot().equals(fluidStackHot)) {
                return recipe;
            }
        }

        return null;
    }

    @Override
    protected Collection<ExtruderManager.ExtruderRecipe> getRecipesRaw() {
        return Lists.newArrayList(ExtruderManager.getRecipeList(isAugmentSedimentary()));
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.FLUIDSTACK && size == 2;
    }
}
