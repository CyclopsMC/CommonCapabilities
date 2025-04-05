package org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.recipebook.PlaceRecipeHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.*;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import org.cyclops.cyclopscore.helper.IModHelpers;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Recipe handler capability for recipe types.
 * @author rubensworks
 */
public class VanillaRecipeTypeRecipeHandler<C extends RecipeInput, T extends Recipe<C>> implements IRecipeHandler {

    private static final Set<IngredientComponent<?, ?>> COMPONENTS_INPUT  = Sets.newHashSet(IngredientComponent.ITEMSTACK);
    private static final Set<IngredientComponent<?, ?>> COMPONENTS_OUTPUT = Sets.newHashSet(IngredientComponent.ITEMSTACK);

    public static final AbstractContainerMenu DUMMY_CONTAINTER = new AbstractContainerMenu(MenuType.CRAFTING, 0) {
        @Override
        public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
            return ItemStack.EMPTY;
        }

        @Override
        public boolean stillValid(Player playerIn) {
            return true;
        }
    };

    private final Supplier<Level> worldSupplier;
    private final RecipeType<T> recipeType;
    private final Predicate<Integer> inputSizePredicate;
    private final Function<CraftingContainer, C> createRecipeInput;
    private final boolean ignoreEmptySlots;

    private static Map<Pair<RecipeType<?>, ResourceLocation>, Collection<IRecipeDefinition>> CACHED_RECIPES = Maps.newHashMap();

    public VanillaRecipeTypeRecipeHandler(Supplier<Level> worldSupplier, RecipeType<T> recipeType, Predicate<Integer> inputSizePredicate, Function<CraftingContainer, C> createRecipeInput, boolean ignoreEmptySlots) {
        this.worldSupplier = worldSupplier;
        this.recipeType = recipeType;
        this.inputSizePredicate = inputSizePredicate;
        this.createRecipeInput = createRecipeInput;
        this.ignoreEmptySlots = ignoreEmptySlots;
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
        return component == IngredientComponent.ITEMSTACK && this.inputSizePredicate.test(size);
    }

    /**
     * A heuristical method for converting a display slot to a list of prototyped ingredients.
     * @param display A display slot.
     * @return A list of prototyped ingredients.
     */
    public static IPrototypedIngredientAlternatives<ItemStack, Integer> getPrototypesFromDisplay(SlotDisplay display) {
        if (display instanceof SlotDisplay.TagSlotDisplay(net.minecraft.tags.TagKey<net.minecraft.world.item.Item> tag)) {
            return new PrototypedIngredientAlternativesItemStackTag(Lists.newArrayList(tag.location().toString()), ItemMatch.ITEM, 1);
        } else if (display instanceof SlotDisplay.Empty) {
            return new PrototypedIngredientAlternativesList<>(Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, ItemStack.EMPTY, ItemMatch.ITEM)));
        } else if (display instanceof SlotDisplay.ItemStackSlotDisplay(ItemStack stack)) {
            return new PrototypedIngredientAlternativesList<>(Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, stack, ItemMatch.ITEM | ItemMatch.DATA)));
        } else {
            PrototypedIngredientAlternativesList<ItemStack, Integer> prototypes = new PrototypedIngredientAlternativesList<>(display.resolveForStacks(ContextMap.EMPTY).stream()
                    .map(item -> new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, item, ItemMatch.ITEM))
                    .collect(Collectors.toList()));
            if (prototypes.getAlternatives().isEmpty()) {
                prototypes = new PrototypedIngredientAlternativesList<>(Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, ItemStack.EMPTY, ItemMatch.ITEM)));
            }
            return prototypes;
        }
    }

    /**
     * A heuristical method for converting an ingredient to a list of prototyped ingredients.
     * @param ingredient An ingredient.
     * @return A list of prototyped ingredients.
     */
    public static IPrototypedIngredientAlternatives<ItemStack, Integer> getPrototypesFromIngredient(Ingredient ingredient, @Nullable SlotDisplay display) {
        if (display != null) {
            return getPrototypesFromDisplay(display);
        } else if (ingredient.isCustom()) {
            return new PrototypedIngredientAlternativesList<>(Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK,
                    new ItemStack(ingredient.getCustomIngredient().items().findFirst().get().value()), ItemMatch.ITEM | ItemMatch.DATA)));
        } else {
            PrototypedIngredientAlternativesList<ItemStack, Integer> prototypes = new PrototypedIngredientAlternativesList<>(ingredient.getValues().stream()
                    .map(item -> new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, new ItemStack(item), ItemMatch.ITEM))
                    .collect(Collectors.toList()));
            if (prototypes.getAlternatives().isEmpty()) {
                prototypes = new PrototypedIngredientAlternativesList<>(Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, ItemStack.EMPTY, ItemMatch.ITEM)));
            }
            return prototypes;
        }
    }

    @Nullable
    public static <C extends RecipeInput, T extends Recipe<C>> IRecipeDefinition recipeToRecipeDefinition(T recipe, Level level) {
        ItemStack recipeOutput = IModHelpers.get().getMinecraftHelpers().getRecipeOutput(recipe, level);
        if (recipeOutput.isEmpty()) {
            return null;
        }

        List<Ingredient> ingredients = recipe.placementInfo().ingredients();
        int inputSize = ingredients.size();
        List<IPrototypedIngredientAlternatives<ItemStack, Integer>> inputIngredients;
        if (inputSize == 0) {
            return null;
        }
        RecipeDisplay recipeDisplay = recipe.display().get(0);

        if (recipe instanceof ShapedRecipe) {
            int width = 3;
            int height = 3;
            inputIngredients = Lists.newArrayListWithCapacity(9);
            for (int i = 0; i < width * height; i++) {
                inputIngredients.add(new PrototypedIngredientAlternativesList<>(Lists.newArrayList(
                        new PrototypedIngredient<>(IngredientComponents.ITEMSTACK, ItemStack.EMPTY, ItemMatch.ITEM | ItemMatch.DATA)
                )));
            }
            ShapedCraftingRecipeDisplay recipeDisplayShaped = (ShapedCraftingRecipeDisplay) recipeDisplay;
            PlaceRecipeHelper.placeRecipe(width, height, recipeDisplayShaped.width(), recipeDisplayShaped.height(), recipeDisplayShaped.ingredients(), (display, destinationSlot, x, y) -> {
                inputIngredients.set(destinationSlot, getPrototypesFromDisplay(display));
            });
        } else {
            // Shapeless
            inputIngredients = Lists.newArrayListWithCapacity(inputSize);
            List<SlotDisplay> displayIngredients = recipeDisplay instanceof ShapelessCraftingRecipeDisplay recipeDisplayShapeless ? recipeDisplayShapeless.ingredients() : null;
            for (int i = 0; i < ingredients.size(); i++) {
                inputIngredients.add(i, getPrototypesFromIngredient(ingredients.get(i), displayIngredients != null ? displayIngredients.get(i) : null));
            }
        }
        return RecipeDefinition.ofAlternatives(IngredientComponent.ITEMSTACK, inputIngredients,
                MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, recipeOutput));
    }

    @Override
    public Collection<IRecipeDefinition> getRecipes() {
        Pair<RecipeType<?>, ResourceLocation> cacheKey = Pair.of(recipeType, worldSupplier.get().dimension().location());
        Collection<IRecipeDefinition> cached = CACHED_RECIPES.get(cacheKey);
        if (cached == null) {
            if (worldSupplier.get().recipeAccess() instanceof RecipeManager recipeManager) {
                cached = recipeManager.getRecipes().stream()
                        .filter(holder -> holder.value().getType() == recipeType && !holder.value().isSpecial())
                        .map(recipe -> VanillaRecipeTypeRecipeHandler.recipeToRecipeDefinition(recipe.value(), this.worldSupplier.get()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            } else {
                cached = Collections.emptyList();
            }
            CACHED_RECIPES.put(cacheKey, cached);
        }
        return cached;
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        List<ItemStack> recipeIngredients = input.getInstances(IngredientComponent.ITEMSTACK);
        if (this.ignoreEmptySlots) {
            recipeIngredients = recipeIngredients.stream().filter(itemStack -> !itemStack.isEmpty()).toList();
        }
        if (input.getComponents().size() != 1 || recipeIngredients.isEmpty()) {
            return null;
        }

        // First try the recipe in a 3x3 grid
        CraftingContainer inventoryCrafting = new TransientCraftingContainer(DUMMY_CONTAINTER, 3, 3);
        for (int i = 0; i < recipeIngredients.size(); i++) {
            inventoryCrafting.setItem(i, recipeIngredients.get(i));
        }

        C recipeInput = this.createRecipeInput.apply(inventoryCrafting);
        T recipe = IModHelpers.get().getCraftingHelpers().findRecipeCached(recipeType, recipeInput, worldSupplier.get(), true)
                .map(RecipeHolder::value)
                .orElse(null);
        if (recipe == null) {
            // If that failed, try in a 2x2 grid
            if (recipeIngredients.size() <= 4) {
                CraftingContainer inventoryCraftingSmall = new TransientCraftingContainer(DUMMY_CONTAINTER, 2, 2);
                for (int i = 0; i < recipeIngredients.size(); i++) {
                    inventoryCraftingSmall.setItem(i, recipeIngredients.get(i));
                }

                recipe = IModHelpers.get().getCraftingHelpers().findRecipeCached(recipeType, recipeInput, worldSupplier.get(), true)
                        .map(RecipeHolder::value)
                        .orElse(null);
            }

            if (recipe == null) {
                return null;
            }
        }

        return MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, IModHelpers.get().getMinecraftHelpers().getRecipeOutput(recipe, this.worldSupplier.get()));
    }
}
