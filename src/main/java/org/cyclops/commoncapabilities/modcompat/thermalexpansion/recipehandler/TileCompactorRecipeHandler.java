package org.cyclops.commoncapabilities.modcompat.thermalexpansion.recipehandler;

import cofh.thermalexpansion.block.machine.TileCompactor;
import cofh.thermalexpansion.util.managers.machine.CompactorManager;
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
import org.cyclops.commoncapabilities.modcompat.thermalexpansion.ThermalExpansionHelpers;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Recipe handler for {@link CompactorManager.CompactorRecipe}.
 * @author rubensworks
 */
public class TileCompactorRecipeHandler extends TransformedRecipeHandlerAdapter<CompactorManager.CompactorRecipe> {

    private final TileCompactor tile;

    public TileCompactorRecipeHandler(TileCompactor tile) {
        super(Sets.newHashSet(IngredientComponent.ITEMSTACK),
                Sets.newHashSet(IngredientComponent.ITEMSTACK));
        this.tile = tile;
    }

    protected CompactorManager.Mode getMode() {
        try {
            return (CompactorManager.Mode) ThermalExpansionHelpers.FIELD_TILECOMPACTOR_MODE.get(tile);
        } catch (IllegalAccessException e) {
            return CompactorManager.Mode.ALL;
        }
    }

    @Override
    protected String getRecipeCacheKey() {
        return Reference.MOD_THERMALEXPANSION + ":compactor";
    }

    @Override
    protected IRecipeDefinition transformRecipe(CompactorManager.CompactorRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        inputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(
                Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, recipe.getInput(), ItemMatch.EXACT))
        )));

        outputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(recipe.getOutput()));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected CompactorManager.CompactorRecipe findRecipe(IMixedIngredients input) {
        return CompactorManager.getRecipe(Iterables.getFirst(input.getInstances(IngredientComponent.ITEMSTACK), ItemStack.EMPTY), getMode());
    }

    @Override
    protected Collection<CompactorManager.CompactorRecipe> getRecipesRaw() {
        return Lists.newArrayList(CompactorManager.getRecipeList(getMode()));
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.ITEMSTACK && size == 1;
    }
}
