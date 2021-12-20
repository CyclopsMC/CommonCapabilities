package org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.NBTIngredient;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IPrototypedIngredient;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import org.cyclops.cyclopscore.helper.CraftingHelpers;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Recipe handler capability for recipe types.
 * @author rubensworks
 */
public class VanillaRecipeTypeRecipeHandler<C extends IInventory, T extends IRecipe<C>> implements IRecipeHandler {

    private static final Set<IngredientComponent<?, ?>> COMPONENTS_INPUT  = Sets.newHashSet(IngredientComponent.ITEMSTACK);
    private static final Set<IngredientComponent<?, ?>> COMPONENTS_OUTPUT = Sets.newHashSet(IngredientComponent.ITEMSTACK);

    public static final Container DUMMY_CONTAINTER = new Container(ContainerType.CRAFTING, 0) {
        @Override
        public boolean stillValid(PlayerEntity playerIn) {
            return true;
        }
    };

    private final Supplier<World> worldSupplier;
    private final IRecipeType<T> recipeType;
    private final Predicate<Integer> inputSizePredicate;

    public VanillaRecipeTypeRecipeHandler(Supplier<World> worldSupplier, IRecipeType<T> recipeType, Predicate<Integer> inputSizePredicate) {
        this.worldSupplier = worldSupplier;
        this.recipeType = recipeType;
        this.inputSizePredicate = inputSizePredicate;
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
        if (ingredient instanceof NBTIngredient) {
            return Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK,
                    ingredient.getItems()[0], ItemMatch.ITEM | ItemMatch.NBT));
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
    public static <C extends IInventory, T extends IRecipe<C>> IRecipeDefinition recipeToRecipeDefinition(T recipe) {
        if (recipe.getResultItem().isEmpty()) {
            return null;
        }
        int inputSize = recipe.getIngredients().size();
        List<List<IPrototypedIngredient<ItemStack, Integer>>> inputIngredients = Lists.newArrayListWithCapacity(inputSize);
        if (inputSize == 0) {
            return null;
        }

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            List<IPrototypedIngredient<ItemStack, Integer>> prototypes = getPrototypesFromIngredient(ingredient);
            if (prototypes.isEmpty()) {
                prototypes.add(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, ItemStack.EMPTY, ItemMatch.ITEM));
            }
            inputIngredients.add(i, prototypes);
        }
        return RecipeDefinition.ofIngredients(IngredientComponent.ITEMSTACK, inputIngredients,
                MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, recipe.getResultItem()));
    }

    @Override
    public Collection<IRecipeDefinition> getRecipes() {
        return worldSupplier.get().getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == recipeType)
                .map(VanillaRecipeTypeRecipeHandler::recipeToRecipeDefinition)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        List<ItemStack> recipeIngredients = input.getInstances(IngredientComponent.ITEMSTACK);
        if (input.getComponents().size() != 1 || recipeIngredients.size() < 1) {
            return null;
        }

        CraftingInventory inventoryCrafting = new CraftingInventory(DUMMY_CONTAINTER, 3, 3);
        for (int i = 0; i < recipeIngredients.size(); i++) {
            inventoryCrafting.setItem(i, recipeIngredients.get(i));
        }

        T recipe = CraftingHelpers.findRecipeCached(recipeType, (C) inventoryCrafting, worldSupplier.get(), true).orElse(null);
        if (recipe == null) {
            return null;
        }

        return MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, recipe.getResultItem());
    }
}
