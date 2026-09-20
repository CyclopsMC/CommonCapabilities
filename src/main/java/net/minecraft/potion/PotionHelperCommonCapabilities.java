package net.minecraft.potion;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.PotionsPredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.BrewingInput;
import net.minecraft.world.item.crafting.BrewingRecipe;
import net.minecraft.world.item.crafting.PotionIngredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IPrototypedIngredient;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;

import java.util.List;
import java.util.Optional;

/**
 * @author rubensworks
 */
public class PotionHelperCommonCapabilities {

    private static List<IRecipeDefinition> VANILLA_RECIPES = null;

    public static List<IRecipeDefinition> getVanillaRecipes() {
        if (VANILLA_RECIPES == null) {
            VANILLA_RECIPES = Lists.newArrayList();
            Level level = ServerLifecycleHooks.getCurrentServer().overworld();
            if (level.recipeAccess() instanceof RecipeManager recipeManager) {
                for (RecipeHolder<?> holder : recipeManager.getRecipes()) {
                    if (holder.value() instanceof BrewingRecipe recipe) {
                        addRecipes(recipe);
                    }
                }
            }
        }
        return VANILLA_RECIPES;
    }

    protected static void addRecipes(BrewingRecipe recipe) {
        for (ItemStack reagent : getMatchingStacks(recipe.getReagent())) {
            for (ItemStack input : getMatchingStacks(recipe.getInput())) {
                // Let the recipe assemble the output itself, so subtypes that derive the output
                // from the input container (as the vanilla mixes do) are handled correctly
                ItemStack output = recipe.assemble(new BrewingInput(input, reagent));
                if (!output.isEmpty()) {
                    addRecipe(
                            new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, reagent, ItemMatch.ITEM | ItemMatch.DATA),
                            new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, input, ItemMatch.ITEM | ItemMatch.DATA),
                            output);
                }
            }
        }
    }

    /**
     * Expand a potion ingredient into the concrete stacks it accepts:
     * its container items, each carrying one of the potions its predicate allows.
     */
    protected static List<ItemStack> getMatchingStacks(PotionIngredient ingredient) {
        List<ItemStack> containers = ingredient.ingredient().items()
                .map(ItemStack::new)
                .toList();
        Optional<PotionsPredicate> potionsPredicate = ingredient.potions();
        if (potionsPredicate.isEmpty() || potionsPredicate.get().potions().isEmpty()) {
            return containers;
        }

        List<ItemStack> stacks = Lists.newArrayList();
        for (ItemStack container : containers) {
            for (Holder<Potion> potion : potionsPredicate.get().potions().get()) {
                ItemStack stack = container.copy();
                stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
                stacks.add(stack);
            }
        }
        return stacks;
    }

    protected static void addRecipe(IPrototypedIngredient<ItemStack, Integer> reagent,
                                    IPrototypedIngredient<ItemStack, Integer> input,
                                    ItemStack output) {
        IRecipeDefinition recipe = RecipeDefinition.ofIngredients(IngredientComponent.ITEMSTACK,
                Lists.newArrayList(Lists.newArrayList(reagent), Lists.newArrayList(input), Lists.newArrayList(input), Lists.newArrayList(input)),
                MixedIngredients.ofInstances(IngredientComponent.ITEMSTACK, Lists.newArrayList(ItemStack.EMPTY, output, output, output)));
        if (!VANILLA_RECIPES.contains(recipe)) {
            VANILLA_RECIPES.add(recipe);
        }
    }

}
