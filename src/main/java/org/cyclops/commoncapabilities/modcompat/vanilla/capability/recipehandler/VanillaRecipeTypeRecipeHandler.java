package org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.*;
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
    private final boolean checkOutput;

    public static Map<Pair<RecipeType<?>, ResourceLocation>, Collection<IRecipeDefinition>> CACHED_RECIPES = Maps.newHashMap();

    public VanillaRecipeTypeRecipeHandler(Supplier<Level> worldSupplier, RecipeType<T> recipeType, Predicate<Integer> inputSizePredicate, Function<CraftingContainer, C> createRecipeInput, boolean ignoreEmptySlots, boolean checkOutput) {
        this.worldSupplier = worldSupplier;
        this.recipeType = recipeType;
        this.inputSizePredicate = inputSizePredicate;
        this.createRecipeInput = createRecipeInput;
        this.ignoreEmptySlots = ignoreEmptySlots;
        this.checkOutput = checkOutput;
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
     * A heuristical method for converting an ingredient to a list of prototyped ingredients.
     * @param ingredient An ingredient.
     * @return A list of prototyped ingredients.
     */
    public static List<IPrototypedIngredient<ItemStack, Integer>> getPrototypesFromIngredient(Ingredient ingredient) {
        if (ingredient.isCustom() && ingredient.getCustomIngredient() instanceof CompoundIngredient compoundIngredient) {
            return Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK,
                    compoundIngredient.getItems().findFirst().get(), ItemMatch.ITEM | ItemMatch.DATA));
//        } else if (ingredient instanceof OreIngredient) { // TODO: somehow detect tags in the future, see ShapelessRecipeBuilder
//            return Arrays.stream(ingredient.getMatchingStacks())
//                    .map(itemStack -> new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, itemStack, ItemMatch.ITEM))
//                    .collect(Collectors.toList());
        } else {
            return Arrays.stream(ingredient.getItems())
                    .map(itemStack -> new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, itemStack, ItemMatch.ITEM))
                    .collect(Collectors.toList());
        }
    }

    @Nullable
    public static <C extends RecipeInput, T extends Recipe<C>> IRecipeDefinition recipeToRecipeDefinition(T recipe, Level level) {
        if (recipe.getResultItem(level.registryAccess()).isEmpty()) {
            return null;
        }
        int inputSize = recipe.getIngredients().size();
        List<List<IPrototypedIngredient<ItemStack, Integer>>> inputIngredients;
        if (inputSize == 0) {
            return null;
        }

        if (recipe instanceof ShapedRecipe shapedRecipe) {
            inputIngredients = Lists.newArrayListWithCapacity(9);
            // We keep the grid shape for shaped recipes
            for (int h = 0; h < 3; h++) {
                for (int w = 0; w < 3; w++) {
                    if (h < shapedRecipe.getHeight() && w < shapedRecipe.getWidth()) {
                        inputIngredients.add(getRecipeInputPrototypes(recipe, w + h * shapedRecipe.getWidth()));
                    } else {
                        inputIngredients.add(Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, ItemStack.EMPTY, ItemMatch.ITEM)));
                    }
                }
            }
        } else {
            // Shapeless
            inputIngredients = Lists.newArrayListWithCapacity(inputSize);
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                inputIngredients.add(i, getRecipeInputPrototypes(recipe, i));
            }
        }
        return RecipeDefinition.ofIngredients(IngredientComponent.ITEMSTACK, inputIngredients,
                MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, recipe.getResultItem(level.registryAccess())));
    }

    protected static <C extends RecipeInput, T extends Recipe<C>> List<IPrototypedIngredient<ItemStack, Integer>> getRecipeInputPrototypes(T recipe, int index) {
        Ingredient ingredient = recipe.getIngredients().get(index);
        List<IPrototypedIngredient<ItemStack, Integer>> prototypes = getPrototypesFromIngredient(ingredient);
        if (prototypes.isEmpty()) {
            prototypes.add(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, ItemStack.EMPTY, ItemMatch.ITEM));
        }
        return prototypes;
    }

    @Override
    public Collection<IRecipeDefinition> getRecipes() {
        Pair<RecipeType<?>, ResourceLocation> cacheKey = Pair.of(recipeType, worldSupplier.get().dimension().location());
        Collection<IRecipeDefinition> cached = CACHED_RECIPES.get(cacheKey);
        if (cached == null) {
            cached = worldSupplier.get().getRecipeManager().getRecipes().stream()
                    .filter(holder -> holder.value().getType() == recipeType && !holder.value().isSpecial())
                    .map(recipe -> VanillaRecipeTypeRecipeHandler.recipeToRecipeDefinition(recipe.value(), this.worldSupplier.get()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            CACHED_RECIPES.put(cacheKey, cached);
        }
        return cached;
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        // Get inputs
        C recipeInput = this.createNewRecipeInput(input);
        if (recipeInput == null) {
            return null;
        }

        // Find recipe by checking input
        T recipe = IModHelpers.get().getCraftingHelpers().findRecipeCached(recipeType, recipeInput, worldSupplier.get(), true)
                .map(RecipeHolder::value)
                .orElse(null);
        if (recipe == null) {
            return null;
        }

        return MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, recipe.getResultItem(this.worldSupplier.get().registryAccess()));
    }

    @Nullable
    @Override
    public @org.jetbrains.annotations.Nullable IMixedIngredients simulate(IRecipeDefinition recipe) {
        MixedIngredients input = MixedIngredients.fromRecipeInput(recipe);

        // Not only check the input, but also the output if needed.
        if (this.checkOutput) {
            // Get inputs
            C recipeInput = this.createNewRecipeInput(input);
            if (recipeInput == null) {
                return null;
            }

            // Get expected output
            IMixedIngredients output = recipe.getOutput();
            List<ItemStack> recipeOutputs = output.getInstances(IngredientComponent.ITEMSTACK);
            if (output.getComponents().size() != 1 || recipeOutputs.size() != 1) {
                return null;
            }
            ItemStack recipeOutput = recipeOutputs.getFirst();
            Level level = worldSupplier.get();

            // Find recipe with input AND output
            return IModHelpers.get().getCraftingHelpers().findRecipes(level, recipeType)
                    .stream()
                    .filter(recipeHolder ->
                            recipeHolder.value().matches(recipeInput, level) &&
                                    ItemStack.isSameItemSameComponents(recipeHolder.value().getResultItem(level.registryAccess()), recipeOutput))
                    .findFirst()
                    .map(recipeHolder -> MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, recipeHolder.value().getResultItem(this.worldSupplier.get().registryAccess())))
                    .orElse(null);
        }

        return this.simulate(input);
    }

    @Nullable
    protected C createNewRecipeInput(IMixedIngredients input) {
        // Get inputs
        List<ItemStack> recipeIngredients = input.getInstances(IngredientComponent.ITEMSTACK);
        if (this.ignoreEmptySlots) {
            recipeIngredients = recipeIngredients.stream().filter(itemStack -> !itemStack.isEmpty()).toList();
        }
        if (input.getComponents().size() != 1 || recipeIngredients.isEmpty()) {
            return null;
        }

        // Prepare recipe input object
        CraftingContainer inventoryCrafting = new TransientCraftingContainer(DUMMY_CONTAINTER, 3, 3);
        for (int i = 0; i < recipeIngredients.size(); i++) {
            inventoryCrafting.setItem(i, recipeIngredients.get(i));
        }
        return this.createRecipeInput.apply(inventoryCrafting);
    }
}
