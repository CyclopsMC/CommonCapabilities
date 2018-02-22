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
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IPrototypedIngredient;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.ItemHandlerRecipeTarget;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Recipe handler capability for the vanilla brewing stand.
 * @author rubensworks
 */
public class VanillaBrewingStandRecipeHandler implements IRecipeHandler {

    private static final Set<IngredientComponent<?, ?, ?>> COMPONENTS_INPUT  = Sets.newHashSet(IngredientComponent.ITEMSTACK);
    private static final Set<IngredientComponent<?, ?, ?>> COMPONENTS_OUTPUT = Sets.newHashSet(IngredientComponent.ITEMSTACK);
    private static final int[] OUTPUT_SLOTS = new int[] {1, 2, 3};

    private static List<IRecipeDefinition> VANILLA_RECIPES = null;

    private final TileEntityBrewingStand tile;

    public VanillaBrewingStandRecipeHandler(TileEntityBrewingStand tile) {
        this.tile = tile;
    }

    @Override
    public Set<IngredientComponent<?, ?, ?>> getRecipeInputComponents() {
        return COMPONENTS_INPUT;
    }

    @Override
    public Set<IngredientComponent<?, ?, ?>> getRecipeOutputComponents() {
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
            List<IPrototypedIngredient<ItemStack, ItemHandlerRecipeTarget, Integer>> ingredients = Lists.newArrayList();
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
                    IPrototypedIngredient<ItemStack, ItemHandlerRecipeTarget, Integer> item =
                            new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, inputItem, ItemMatch.DAMAGE | ItemMatch.NBT);
                    for (IPrototypedIngredient<ItemStack, ItemHandlerRecipeTarget, Integer> ingredient : ingredients) {
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

    protected static void addRecipeIfNew(IPrototypedIngredient<ItemStack, ItemHandlerRecipeTarget, Integer> ingredient,
                                         IPrototypedIngredient<ItemStack, ItemHandlerRecipeTarget, Integer> item,
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

    @Nullable
    @Override
    public <R> R[] getInputComponentTargets(IngredientComponent<?, R, ?> component) {
        if (component == IngredientComponent.ITEMSTACK) {
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                    EnumFacing.NORTH);
            return (R[]) new ItemHandlerRecipeTarget[]{
                    new ItemHandlerRecipeTarget(itemHandler, 3),
                    new ItemHandlerRecipeTarget(itemHandler, 0),
                    new ItemHandlerRecipeTarget(itemHandler, 1),
                    new ItemHandlerRecipeTarget(itemHandler, 2)
            };
        }
        return null;
    }

    @Nullable
    @Override
    public <R> R[] getOutputComponentTargets(IngredientComponent<?, R, ?> component) {
        if (component == IngredientComponent.ITEMSTACK) {
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                    EnumFacing.DOWN);
            return (R[]) new ItemHandlerRecipeTarget[]{
                    new ItemHandlerRecipeTarget(itemHandler, 3),
                    new ItemHandlerRecipeTarget(itemHandler, 0),
                    new ItemHandlerRecipeTarget(itemHandler, 1),
                    new ItemHandlerRecipeTarget(itemHandler, 2)
            };
        }
        return null;
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
