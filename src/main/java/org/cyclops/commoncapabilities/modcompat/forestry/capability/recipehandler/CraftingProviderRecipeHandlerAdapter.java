package org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler;

import forestry.api.recipes.ICraftingProvider;
import forestry.api.recipes.IForestryRecipe;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract recipe handler for {@link ICraftingProvider}.
 * @author rubensworks
 */
public abstract class CraftingProviderRecipeHandlerAdapter<R extends IForestryRecipe> implements IRecipeHandler {

    private final ICraftingProvider<R> craftingProvider;
    private final Set<IngredientComponent<?, ?>> inputComponents;
    private final Set<IngredientComponent<?, ?>> outputComponents;
    private final Collection<IRecipeDefinition> recipes;

    public CraftingProviderRecipeHandlerAdapter(ICraftingProvider<R> craftingProvider,
                                                Set<IngredientComponent<?, ?>> inputComponents,
                                                Set<IngredientComponent<?, ?>> outputComponents) {
        this.craftingProvider = craftingProvider;
        this.inputComponents = inputComponents;
        this.outputComponents = outputComponents;

        this.recipes = this.craftingProvider.recipes().stream()
                .map(this::transformRecipe)
                .collect(Collectors.toList());
    }

    public ICraftingProvider<R> getCraftingProvider() {
        return craftingProvider;
    }

    protected abstract IRecipeDefinition transformRecipe(R recipe);

    protected abstract R findRecipe(IMixedIngredients input);

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
        return this.recipes;
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        R forestryRecipe = findRecipe(input);
        return forestryRecipe == null ? null : transformRecipe(forestryRecipe).getOutput();
    }
}
