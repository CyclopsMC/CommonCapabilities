package org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import forestry.api.recipes.IFabricatorRecipe;
import forestry.core.recipes.RecipePair;
import forestry.factory.recipes.FabricatorRecipeManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesList;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler.VanillaCraftingTableRecipeHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Recipe handler for {@link IFabricatorRecipe}.
 * @author rubensworks
 */
public class TileFabricatorRecipeHandler extends CraftingProviderRecipeHandlerAdapter<IFabricatorRecipe> {

    public TileFabricatorRecipeHandler() {
        super(new FabricatorRecipeManager(),
                Sets.newHashSet(IngredientComponent.ITEMSTACK, IngredientComponent.FLUIDSTACK),
                Sets.newHashSet(IngredientComponent.ITEMSTACK));
    }

    @Override
    protected IRecipeDefinition transformRecipe(IFabricatorRecipe recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        List<IPrototypedIngredientAlternatives<?, ?>> itemStackInputs = Lists.newArrayList();
        itemStackInputs.add(new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, recipe.getPlan(), ItemMatch.EXACT)
        )));
        itemStackInputs.addAll(recipe.getIngredients()
                .stream()
                .map(alternatives -> new PrototypedIngredientAlternativesList<>(alternatives
                        .stream()
                        .map(itemStack -> new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, itemStack, ItemMatch.EXACT))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList()));
        inputs.put(IngredientComponent.ITEMSTACK, itemStackInputs);
        inputs.put(IngredientComponent.FLUIDSTACK, Lists.newArrayList(new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                new PrototypedIngredient<>(IngredientComponent.FLUIDSTACK, recipe.getLiquid(), FluidMatch.EXACT)
        ))));

        outputs.put(IngredientComponent.ITEMSTACK, Lists.newArrayList(recipe.getRecipeOutput()));

        return new RecipeDefinition(inputs, new MixedIngredients(outputs));
    }

    @Override
    protected IFabricatorRecipe findRecipe(IMixedIngredients input) {
        List<ItemStack> itemStacks = input.getInstances(IngredientComponent.ITEMSTACK);

        ItemStack plan = ItemStack.EMPTY;
        int dimension = (int) Math.ceil(Math.sqrt((float) itemStacks.size() - 1));
        InventoryCrafting grid = new InventoryCrafting(VanillaCraftingTableRecipeHandler.DUMMY_CONTAINTER, dimension, dimension);
        for (int i = 0; i < itemStacks.size(); i++) {
            if (i == 0) {
                plan = itemStacks.get(i);
            } else {
                grid.setInventorySlotContents(i - 1, itemStacks.get(i));
            }
        }

        RecipePair<IFabricatorRecipe> recipePair = FabricatorRecipeManager.findMatchingRecipe(plan, grid);
        return recipePair == null ? null : recipePair.getRecipe();
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size) {
        return component == IngredientComponent.ITEMSTACK
                || (component == IngredientComponent.FLUIDSTACK && size == 1);
    }
}
