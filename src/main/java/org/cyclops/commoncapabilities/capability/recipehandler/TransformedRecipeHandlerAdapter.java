package org.cyclops.commoncapabilities.capability.recipehandler;

import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract recipe handler based on transforming a recipe collection.
 *
 * It will also attempt to cache the transformed recipes.
 *
 * @author rubensworks
 */
public abstract class TransformedRecipeHandlerAdapter<R> implements IRecipeHandler {

    private static final Map<String, Collection<IRecipeDefinition>> CACHED_RECIPES = Maps.newHashMap();

    private final Set<IngredientComponent<?, ?>> inputComponents;
    private final Set<IngredientComponent<?, ?>> outputComponents;

    private Collection<IRecipeDefinition> recipes;

    public TransformedRecipeHandlerAdapter(Set<IngredientComponent<?, ?>> inputComponents,
                                           Set<IngredientComponent<?, ?>> outputComponents) {
        this.inputComponents = inputComponents;
        this.outputComponents = outputComponents;
    }

    protected boolean isRecipeCacheEnabled() {
        return true;
    }

    protected abstract String getRecipeCacheKey();

    protected abstract IRecipeDefinition transformRecipe(R recipe);

    protected abstract R findRecipe(IMixedIngredients input);

    protected abstract Collection<R> getRecipesRaw();

    @Override
    public Set<IngredientComponent<?, ?>> getRecipeInputComponents() {
        return inputComponents;
    }

    @Override
    public Set<IngredientComponent<?, ?>> getRecipeOutputComponents() {
        return outputComponents;
    }

    @Override
    public Collection<IRecipeDefinition> getRecipes() {
        if (this.recipes == null) {
            if (isRecipeCacheEnabled() && CACHED_RECIPES.containsKey(getRecipeCacheKey())) {
                this.recipes = CACHED_RECIPES.get(getRecipeCacheKey());
            } else {
                this.recipes = getRecipesRaw().stream()
                        .map(this::transformRecipe)
                        .collect(Collectors.toList());
                CACHED_RECIPES.put(getRecipeCacheKey(), this.recipes);
            }
        }

        return this.recipes;
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        R teRecipe = findRecipe(input);
        return teRecipe == null ? null : transformRecipe(teRecipe).getOutput();
    }
}
