package org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IPrototypedIngredient;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Recipe handler capability for the vanilla brewing stand.
 * @author rubensworks
 */
public class VanillaBrewingStandRecipeHandler implements IRecipeHandler {

    private static final VanillaBrewingStandRecipeHandler INSTANCE = new VanillaBrewingStandRecipeHandler();
    private static final Set<IngredientComponent<?, ?>> COMPONENTS_INPUT  = Sets.newHashSet(IngredientComponent.ITEMSTACK);
    private static final Set<IngredientComponent<?, ?>> COMPONENTS_OUTPUT = Sets.newHashSet(IngredientComponent.ITEMSTACK);
    private static final int[] OUTPUT_SLOTS = new int[] {1, 2, 3};

    private static List<IRecipeDefinition> VANILLA_RECIPES = null;

    private VanillaBrewingStandRecipeHandler() {

    }

    public static VanillaBrewingStandRecipeHandler getInstance() {
        return INSTANCE;
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
    public boolean isValidSizeInput(IngredientComponent component, int size) {
        return component == IngredientComponent.ITEMSTACK && (size >= 2 || size <= 4);
    }

    @Override
    public List<IRecipeDefinition> getRecipes() {
        return generateVanillaRecipes();
    }

    protected List<IRecipeDefinition> generateVanillaRecipes() {
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

    protected static boolean isPotionOutputValid(ItemStack input, ItemStack output) {
        return !input.isEmpty() && !output.isEmpty() && (input.getItem() != output.getItem()
                || (PotionUtils.getPotionFromItem(output) != PotionTypes.WATER
                && !Objects.equals(ForgeRegistries.POTION_TYPES.getKey(PotionUtils.getPotionFromItem(output)),
                ForgeRegistries.POTION_TYPES.getKey(PotionUtils.getPotionFromItem(input)))));
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        List<ItemStack> recipeIngredients = input.getInstances(IngredientComponent.ITEMSTACK);
        if (input.getComponents().size() != 1
                || !isValidSizeInput(IngredientComponent.ITEMSTACK, recipeIngredients.size())) {
            return null;
        }

        NonNullList<ItemStack> brewingItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
        for (int i = 0; i < recipeIngredients.size(); i++) {
            brewingItemStacks.set(i, recipeIngredients.get(i).copy());
        }
        BrewingRecipeRegistry.brewPotions(brewingItemStacks, brewingItemStacks.get(0), OUTPUT_SLOTS);
        brewingItemStacks.set(0, ItemStack.EMPTY);

        return MixedIngredients.ofInstances(IngredientComponent.ITEMSTACK, brewingItemStacks);
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
