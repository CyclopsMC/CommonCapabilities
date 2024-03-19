package org.cyclops.commoncapabilities.modcompat.tconstruct.capability.recipehandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import net.neoforged.neoforge.fluids.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
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
import org.cyclops.cyclopscore.helper.FluidHelpers;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Recipe handler capability for the casting.
 * @author rubensworks
 */
public abstract class TileCastingRecipeHandler implements IRecipeHandler {

    private static final Set<IngredientComponent<?, ?>> COMPONENTS_INPUT  = Sets.newHashSet(IngredientComponent.ITEMSTACK, IngredientComponent.FLUIDSTACK);
    private static final Set<IngredientComponent<?, ?>> COMPONENTS_OUTPUT = Sets.newHashSet(IngredientComponent.ITEMSTACK);

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
        return (component == IngredientComponent.ITEMSTACK || component == IngredientComponent.FLUIDSTACK) && size <= 1;
    }

    protected void putRecipeItemStackInputs(Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs,
                                            CastingRecipe recipeCasting) {
        if (recipeCasting.cast != null) {
            List<ItemStack> inputItems = recipeCasting.cast.getInputs();
            if (!inputItems.isEmpty()) {
                inputs.put(IngredientComponent.ITEMSTACK, inputItems.stream()
                        .map(input -> new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                                new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, input, ItemMatch.EXACT)
                        )))
                        .collect(Collectors.toList()));
            }
        }
    }

    protected abstract List<ICastingRecipe> getCastingRecipes();

    @Override
    public Collection<IRecipeDefinition> getRecipes() {
        // Based on the TCon JEI code
        return getCastingRecipes().stream()
                    .filter(recipe -> recipe instanceof CastingRecipe)
                    .map(recipe -> {
                        CastingRecipe recipeCasting = (CastingRecipe) recipe;
                        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
                        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

                        putRecipeItemStackInputs(inputs, recipeCasting);
                        inputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                                new PrototypedIngredient<>(IngredientComponent.FLUIDSTACK, recipeCasting.getFluid(), FluidMatch.EXACT)
                        ))));

                        outputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(recipeCasting.getResult()));

                        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
                    })
                    .collect(Collectors.toList());
    }

    protected abstract ICastingRecipe getCastingRecipe(ItemStack itemStack, Fluid fluid);

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        ItemStack itemStack = input.getFirstNonEmpty(IngredientComponent.ITEMSTACK);
        FluidStack fluidStack = input.getFirstNonEmpty(IngredientComponent.FLUIDSTACK);
        Fluid fluid = fluidStack != null ? fluidStack.getFluid() : null;

        ICastingRecipe recipe = getCastingRecipe(itemStack, fluid);
        if (recipe == null || recipe.getFluidAmount() < FluidHelpers.getAmount(fluidStack)) {
            return null;
        }

        Map<IngredientComponent<?, ?>, List<?>> ingredientsMap = Maps.newIdentityHashMap();
        ingredientsMap.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(recipe.getResult(itemStack, fluid)));
        return new MixedIngredients(ingredientsMap);
    }
}
