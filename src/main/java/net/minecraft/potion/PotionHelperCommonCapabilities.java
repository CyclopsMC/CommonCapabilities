package net.minecraft.potion;

import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IPrototypedIngredient;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler.VanillaCraftingTableRecipeHandler;

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
            List<ItemStack> inputItems = Lists.newArrayList(PotionUtils.addPotionToItemStack(
                    new ItemStack(Items.POTIONITEM), PotionTypes.WATER));
            List<IPrototypedIngredient<ItemStack, Integer>> ingredients = Lists.newArrayList();
            for (PotionHelper.MixPredicate<Item> mixPredicate : getPotionItems()) {
                ingredients.addAll(VanillaCraftingTableRecipeHandler.getPrototypesFromIngredient(getMixReagent(mixPredicate)));
            }
            for (PotionHelper.MixPredicate<PotionType> mixPredicate : getPotionTypes()) {
                ingredients.addAll(VanillaCraftingTableRecipeHandler.getPrototypesFromIngredient(getMixReagent(mixPredicate)));
            }

            List<ItemStack> checkInputItems = Lists.newArrayList(inputItems);
            while (!checkInputItems.isEmpty()) {
                List<ItemStack> newItems = Lists.newArrayList();
                for (ItemStack inputItem : checkInputItems) {
                    IPrototypedIngredient<ItemStack, Integer> item =
                            new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, inputItem, ItemMatch.ITEM | ItemMatch.DAMAGE | ItemMatch.NBT);
                    for (IPrototypedIngredient<ItemStack, Integer> ingredient : ingredients) {
                        ItemStack output = PotionHelper.doReaction(ingredient.getPrototype().copy(), inputItem.copy());
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
                || (PotionUtils.getPotionFromItem(output) != PotionTypes.WATER
                && !Objects.equals(ForgeRegistries.POTION_TYPES.getKey(PotionUtils.getPotionFromItem(output)),
                ForgeRegistries.POTION_TYPES.getKey(PotionUtils.getPotionFromItem(input)))));
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

    private static List<PotionHelper.MixPredicate<PotionType>> getPotionTypes() {
        return ReflectionHelper.getPrivateValue(PotionHelper.class, null, "field_185213_a", "POTION_TYPE_CONVERSIONS");
    }

    private static List<PotionHelper.MixPredicate<Item>> getPotionItems() {
        return ReflectionHelper.getPrivateValue(PotionHelper.class, null, "field_185214_b", "POTION_ITEM_CONVERSIONS");
    }

    private static Ingredient getMixReagent(PotionHelper.MixPredicate<?> mixPredicate) {
        return ReflectionHelper.getPrivateValue(PotionHelper.MixPredicate.class, mixPredicate, "field_185199_b", "reagent");
    }

}
