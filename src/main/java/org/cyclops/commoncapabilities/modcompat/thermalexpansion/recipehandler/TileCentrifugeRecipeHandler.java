package org.cyclops.commoncapabilities.modcompat.thermalexpansion.recipehandler;

import cofh.thermalexpansion.block.machine.TileCentrifuge;
import cofh.thermalexpansion.util.managers.machine.CentrifugeManager;
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
 * Recipe handler for {@link CentrifugeManager.CentrifugeRecipe}.
 * @author rubensworks
 */
public class TileCentrifugeRecipeHandler extends TransformedRecipeHandlerAdapter<CentrifugeManager.CentrifugeRecipe> {

    private final TileCentrifuge tile;

    public TileCentrifugeRecipeHandler(TileCentrifuge tile) {
        super(Sets.newHashSet(IngredientComponent.FLUIDSTACK),
                Sets.newHashSet(IngredientComponent.FLUIDSTACK, IngredientComponent.ITEMSTACK));
        this.tile = tile;
    }

    protected boolean isAugmentMobs() {
        return tile.augmentMobs();
    }

    @Override
    protected String getRecipeCacheKey() {
        return Reference.MOD_THERMALEXPANSION + ":centrifuge:" + isAugmentMobs();
    }

    @Override
    protected IRecipeDefinition transformRecipe(CentrifugeManager.CentrifugeRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        inputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(
                Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, recipe.getInput(), ItemMatch.EXACT))
        )));

        List<ItemStack> outputItems = Lists.newArrayList();
        for (int i = 0; i < recipe.getOutput().size(); i++) {
            if (recipe.getChance().get(i) == 1) {
                outputItems.add(recipe.getOutput().get(i));
            }
        }
        if (!outputItems.isEmpty()) {
            outputs.put(IngredientComponent.ITEMSTACK, outputItems);
        }
        if (recipe.getFluid() != null) {
            outputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(recipe.getFluid()));
        }

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected CentrifugeManager.CentrifugeRecipe findRecipe(IMixedIngredients input) {
        if (isAugmentMobs()) {
            return CentrifugeManager.getRecipeMob(input.getFirstNonEmpty(IngredientComponent.ITEMSTACK));
        } else {
            return CentrifugeManager.getRecipe(input.getFirstNonEmpty(IngredientComponent.ITEMSTACK));
        }
    }

    @Override
    protected Collection<CentrifugeManager.CentrifugeRecipe> getRecipesRaw() {
        return Lists.newArrayList(isAugmentMobs() ? CentrifugeManager.getRecipeListMobs() : CentrifugeManager.getRecipeList());
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.ITEMSTACK && size == 1;
    }
}
