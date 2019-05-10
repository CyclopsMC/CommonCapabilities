package org.cyclops.commoncapabilities.modcompat.tconstruct.capability.recipehandler;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
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
import scala.xml.dtd.EMPTY;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Recipe handler capability for the smeltery.
 * @author rubensworks
 */
public class TileSmelteryRecipeHandler implements IRecipeHandler {

    private static final TileSmelteryRecipeHandler INSTANCE = new TileSmelteryRecipeHandler();
    private static final Set<IngredientComponent<?, ?>> COMPONENTS_INPUT  = Sets.newHashSet(IngredientComponent.ITEMSTACK, IngredientComponent.FLUIDSTACK);
    private static final Set<IngredientComponent<?, ?>> COMPONENTS_OUTPUT = Sets.newHashSet(IngredientComponent.FLUIDSTACK);

    private static List<IRecipeDefinition> RECIPES = null;

    public static TileSmelteryRecipeHandler getInstance() {
        return INSTANCE;
    }

    private TileSmelteryRecipeHandler() {

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
        return (component == IngredientComponent.ITEMSTACK && size == 1)
                || (component == IngredientComponent.FLUIDSTACK && size > 1);
    }

    @Override
    public Collection<IRecipeDefinition> getRecipes() {
        if (RECIPES == null) {
            RECIPES = TinkerRegistry.getAllMeltingRecipies().stream()
                    .map(recipe -> {
                        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
                        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

                        if (recipe.input.getInputs().isEmpty()) {
                            return null;
                        }

                        inputs.put(IngredientComponent.ITEMSTACK, recipe.input.getInputs().stream()
                                .map(input -> new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                                        new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, input, ItemMatch.EXACT)
                                )))
                                .collect(Collectors.toList()));

                        outputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(recipe.getResult()));

                        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            RECIPES.addAll(TinkerRegistry.getAlloys().stream()
                    .map(recipe -> {
                        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
                        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

                        if (recipe.getFluids().size() == 0) {
                            return null;
                        }
                        inputs.put(IngredientComponent.FLUIDSTACK, recipe.getFluids().stream()
                                .map(input -> new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                                        new PrototypedIngredient<>(IngredientComponent.FLUIDSTACK, input, FluidMatch.EXACT)
                                )))
                                .collect(Collectors.toList()));

                        outputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(recipe.getResult()));

                        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        }
        return RECIPES;
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        if (input.getComponents().contains(IngredientComponent.ITEMSTACK)) {
            ItemStack itemStack = input.getFirstNonEmpty(IngredientComponent.ITEMSTACK);

            MeltingRecipe recipe = TinkerRegistry.getMelting(itemStack);
            if (recipe == null) {
                return null;
            }

            Map<IngredientComponent<?, ?>, List<?>> ingredientsMap = Maps.newIdentityHashMap();
            ingredientsMap.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(recipe.getResult()));
            return new MixedIngredients(ingredientsMap);
        } else if (input.getComponents().contains(IngredientComponent.FLUIDSTACK)) {
            List<FluidStack> fluidStacks = input.getInstances(IngredientComponent.FLUIDSTACK);

            for (AlloyRecipe recipe : TinkerRegistry.getAlloys()) {
                int matches = recipe.matches(fluidStacks);
                if (matches > 0) {
                    FluidStack result = recipe.getResult().copy();
                    result.amount *= matches;

                    Map<IngredientComponent<?, ?>, List<?>> ingredientsMap = Maps.newIdentityHashMap();
                    ingredientsMap.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(result));
                    return new MixedIngredients(ingredientsMap);
                }
            }

        }
        return null;
    }
}
