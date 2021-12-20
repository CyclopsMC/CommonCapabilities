package net.minecraft.potion;

import com.google.common.collect.Lists;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IPrototypedIngredient;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler.VanillaRecipeTypeRecipeHandler;

import java.util.List;
import java.util.Objects;

/**
 * @author rubensworks
 */
public class PotionHelperCommonCapabilities {

    private static List<IRecipeDefinition> VANILLA_RECIPES = null;

    public static List<IRecipeDefinition> getVanillaRecipes() {
        if (VANILLA_RECIPES == null) {
            VANILLA_RECIPES = Lists.newArrayList();
            List<ItemStack> inputItems = Lists.newArrayList(PotionUtils.setPotion(
                    new ItemStack(Items.POTION), Potions.WATER));
            List<IPrototypedIngredient<ItemStack, Integer>> ingredients = Lists.newArrayList();
            for (PotionBrewing.Mix<Item> mixPredicate : PotionBrewing.CONTAINER_MIXES) {
                ingredients.addAll(VanillaRecipeTypeRecipeHandler.getPrototypesFromIngredient(mixPredicate.ingredient));
            }
            for (PotionBrewing.Mix<Potion> mixPredicate : PotionBrewing.POTION_MIXES) {
                ingredients.addAll(VanillaRecipeTypeRecipeHandler.getPrototypesFromIngredient(mixPredicate.ingredient));
            }

            List<ItemStack> checkInputItems = Lists.newArrayList(inputItems);
            while (!checkInputItems.isEmpty()) {
                List<ItemStack> newItems = Lists.newArrayList();
                for (ItemStack inputItem : checkInputItems) {
                    IPrototypedIngredient<ItemStack, Integer> item =
                            new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, inputItem, ItemMatch.ITEM | ItemMatch.TAG);
                    for (IPrototypedIngredient<ItemStack, Integer> ingredient : ingredients) {
                        ItemStack output = PotionBrewing.mix(ingredient.getPrototype().copy(), inputItem.copy());
                        if (isPotionOutputValid(inputItem, output)) {
                            addRecipeIfNew(ingredient, item, output, newItems);
                        }
                    }

                    // Throw error if infinite loop detected
                    if (VANILLA_RECIPES.size() > 7500) {
                        throw new RuntimeException("Infinite loop detected! Please report this to the Common " +
                                "Capabilities issue tracker with a list of (potion-changing) mods installed");
                    }
                }
                checkInputItems = newItems;
            }

        }
        return VANILLA_RECIPES;
    }

    protected static boolean isPotionOutputValid(ItemStack input, ItemStack output) {
        return !input.isEmpty() && !output.isEmpty() && (input.getItem() != output.getItem()
                || (PotionUtils.getPotion(output) != Potions.WATER
                && !Objects.equals(ForgeRegistries.POTIONS.getKey(PotionUtils.getPotion(output)),
                ForgeRegistries.POTIONS.getKey(PotionUtils.getPotion(input)))));
    }

    protected static void addRecipeIfNew(IPrototypedIngredient<ItemStack, Integer> ingredient,
                                         IPrototypedIngredient<ItemStack, Integer> item,
                                         ItemStack output,
                                         List<ItemStack> newItems) {
        IRecipeDefinition recipe = RecipeDefinition.ofIngredients(IngredientComponent.ITEMSTACK,
                Lists.newArrayList(Lists.newArrayList(ingredient), Lists.newArrayList(item), Lists.newArrayList(item), Lists.newArrayList(item)),
                MixedIngredients.ofInstances(IngredientComponent.ITEMSTACK, Lists.newArrayList(ItemStack.EMPTY, output, output, output)));
        if (!VANILLA_RECIPES.contains(recipe)) {
            VANILLA_RECIPES.add(recipe);
            newItems.add(output);
        }
    }

}
