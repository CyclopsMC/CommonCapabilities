package org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreIngredient;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Recipe handler capability for the vanilla crafting table.
 * @author rubensworks
 */
public class VanillaCraftingTableRecipeHandler implements IRecipeHandler {

    private static final Set<IngredientComponent<?, ?>> COMPONENTS_INPUT  = Sets.newHashSet(IngredientComponent.ITEMSTACK);
    private static final Set<IngredientComponent<?, ?>> COMPONENTS_OUTPUT = Sets.newHashSet(IngredientComponent.ITEMSTACK);

    private final Container DUMMY_CONTAINTER = new Container() {
        @Override
        public boolean canInteractWith(EntityPlayer playerIn) {
            return true;
        }
    };

    private final World world;

    public VanillaCraftingTableRecipeHandler(World world) {
        this.world = world;
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
        return component == IngredientComponent.ITEMSTACK && size > 0;
    }

    /**
     * A heuristical method for converting an ingredient to a list of prototyped ingredients.
     * @param ingredient An ingredient.
     * @return A list of prototyped ingredients.
     */
    public static List<IPrototypedIngredient<ItemStack, Integer>> getPrototypesFromIngredient(Ingredient ingredient) {
        if (ingredient instanceof IngredientNBT) {
            return Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK,
                    ingredient.getMatchingStacks()[0], ItemMatch.DAMAGE | ItemMatch.NBT));
        } else if (ingredient instanceof OreIngredient) {
            return Arrays.stream(ingredient.getMatchingStacks())
                    .map(itemStack -> new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, itemStack, ItemMatch.DAMAGE))
                    .collect(Collectors.toList());
        } else {
            return Arrays.stream(ingredient.getMatchingStacks())
                    .map(itemStack -> new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, itemStack, ItemMatch.DAMAGE))
                    .collect(Collectors.toList());
        }
    }

    public static IRecipeDefinition recipeToRecipeDefinition(IRecipe recipe) {
        List<List<IPrototypedIngredient<ItemStack, Integer>>> inputIngredients = Lists.newArrayListWithCapacity(recipe.getIngredients().size());
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            List<IPrototypedIngredient<ItemStack, Integer>> prototypes = getPrototypesFromIngredient(ingredient);
            inputIngredients.add(i, prototypes);
        }
        return RecipeDefinition.ofIngredients(IngredientComponent.ITEMSTACK, inputIngredients,
                MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, recipe.getRecipeOutput()));
    }

    @Override
    public Collection<IRecipeDefinition> getRecipes() {
        return Collections2.transform(ForgeRegistries.RECIPES.getValuesCollection(), new Function<IRecipe, IRecipeDefinition>() {
            @Nullable
            @Override
            public IRecipeDefinition apply(@Nullable IRecipe input) {
                return VanillaCraftingTableRecipeHandler.recipeToRecipeDefinition(input);
            }
        });
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        List<ItemStack> recipeIngredients = input.getInstances(IngredientComponent.ITEMSTACK);
        if (input.getComponents().size() != 1 || recipeIngredients.size() < 1) {
            return null;
        }

        int rows = 2;
        int columns = 2;
        if (recipeIngredients.size() > 4) {
            rows = 3;
            columns = 3;
        }
        InventoryCrafting inventoryCrafting = new InventoryCrafting(DUMMY_CONTAINTER, rows, columns);
        for (int i = 0; i < recipeIngredients.size(); i++) {
            inventoryCrafting.setInventorySlotContents(i, recipeIngredients.get(i));
        }

        IRecipe recipe = CraftingManager.findMatchingRecipe(inventoryCrafting, world);
        if (recipe == null) {
            return null;
        }

        return MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, recipe.getRecipeOutput());
    }
}
