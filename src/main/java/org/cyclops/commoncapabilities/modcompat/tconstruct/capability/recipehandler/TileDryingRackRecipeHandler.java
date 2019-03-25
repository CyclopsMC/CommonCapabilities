package org.cyclops.commoncapabilities.modcompat.tconstruct.capability.recipehandler;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesList;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import slimeknights.tconstruct.library.TinkerRegistry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Recipe handler capability for the drying rack.
 * @author rubensworks
 */
public class TileDryingRackRecipeHandler implements IRecipeHandler {

    private static final TileDryingRackRecipeHandler INSTANCE = new TileDryingRackRecipeHandler();
    private static final Set<IngredientComponent<?, ?>> COMPONENTS_INPUT  = Sets.newHashSet(IngredientComponent.ITEMSTACK);
    private static final Set<IngredientComponent<?, ?>> COMPONENTS_OUTPUT = Sets.newHashSet(IngredientComponent.ITEMSTACK);

    public static TileDryingRackRecipeHandler getInstance() {
        return INSTANCE;
    }

    private TileDryingRackRecipeHandler() {

    }

    @Override
    public Set<IngredientComponent<?, ?>> getRecipeInputComponents() {
        return COMPONENTS_INPUT;
    }

    @Override
    public Set<IngredientComponent<?, ?>> getRecipeOutputComponents() {
        return COMPONENTS_OUTPUT;
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.ITEMSTACK && size == 1;
    }

    @Override
    public Collection<IRecipeDefinition> getRecipes() {
        // Based on the TCon JEI code
        return TinkerRegistry.getAllDryingRecipes().stream()
                    .map(recipe -> {
                        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
                        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

                        inputs.put(IngredientComponent.ITEMSTACK, recipe.input.getInputs().stream()
                                .map(input -> new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                                        new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, input, ItemMatch.EXACT)
                                )))
                                .collect(Collectors.toList()));

                        outputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(recipe.getResult()));

                        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
                    })
                    .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        ItemStack itemStack = Iterables.getFirst(input.getInstances(IngredientComponent.ITEMSTACK), ItemStack.EMPTY);

        ItemStack result = TinkerRegistry.getDryingResult(itemStack);
        if (result.isEmpty()) {
            return null;
        }

        Map<IngredientComponent<?, ?>, List<?>> ingredientsMap = Maps.newIdentityHashMap();
        ingredientsMap.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(result));
        return new MixedIngredients(ingredientsMap);
    }
}
