package org.cyclops.commoncapabilities.modcompat.thermalexpansion.recipehandler;

import cofh.thermalexpansion.block.machine.TileFurnace;
import cofh.thermalexpansion.util.managers.machine.FurnaceManager;
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
 * Recipe handler for {@link FurnaceManager.FurnaceRecipe}.
 * @author rubensworks
 */
public class TileFurnaceRecipeHandler extends TransformedRecipeHandlerAdapter<FurnaceManager.FurnaceRecipe> {

    private final TileFurnace tile;

    public TileFurnaceRecipeHandler(TileFurnace tile) {
        super(Sets.newHashSet(IngredientComponent.ITEMSTACK),
                Sets.newHashSet(IngredientComponent.ITEMSTACK));
        this.tile = tile;
    }

    @Override
    protected String getRecipeCacheKey() {
        return Reference.MOD_THERMALEXPANSION + ":furnace:" + tile.augmentPyrolysis();
    }

    @Override
    protected IRecipeDefinition transformRecipe(FurnaceManager.FurnaceRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        inputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(
                Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, recipe.getInput(), ItemMatch.EXACT))
        )));

        outputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(recipe.getOutput()));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected FurnaceManager.FurnaceRecipe findRecipe(IMixedIngredients input) {
        return FurnaceManager.getRecipe(input.getFirstNonEmpty(IngredientComponent.ITEMSTACK), tile.augmentPyrolysis());
    }

    @Override
    protected Collection<FurnaceManager.FurnaceRecipe> getRecipesRaw() {
        return Lists.newArrayList(FurnaceManager.getRecipeList(tile.augmentPyrolysis()));
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.ITEMSTACK && size == 1;
    }
}
