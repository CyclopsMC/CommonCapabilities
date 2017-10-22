package org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.cyclops.commoncapabilities.api.capability.recipehandler.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Recipe handler capability for the vanilla crafting table.
 * @author rubensworks
 */
public class VanillaCraftingTableRecipeHandler implements IRecipeHandler {

    private static final Set<RecipeComponent<?, ?>> COMPONENTS_INPUT  = Sets.newHashSet(RecipeComponent.ITEMSTACK);
    private static final Set<RecipeComponent<?, ?>> COMPONENTS_OUTPUT = Sets.newHashSet(RecipeComponent.ITEMSTACK);

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
    public Set<RecipeComponent<?, ?>> getRecipeInputComponents() {
        return COMPONENTS_INPUT;
    }

    @Override
    public Set<RecipeComponent<?, ?>> getRecipeOutputComponents() {
        return COMPONENTS_OUTPUT;
    }

    @Override
    public boolean isValidSizeInput(RecipeComponent component, int size) {
        return component == RecipeComponent.ITEMSTACK && size > 0;
    }

    @Override
    public List<RecipeDefinition> getRecipes() {
        return Lists.transform(ForgeRegistries.RECIPES.getValues(), new Function<IRecipe, RecipeDefinition>() {
            @Nullable
            @Override
            public RecipeDefinition apply(@Nullable IRecipe input) {
                IRecipeIngredient[] inputIngredients = new IRecipeIngredient[input.getIngredients().size()];
                for (int i = 0; i < inputIngredients.length; i++) {
                    inputIngredients[i] = new RecipeIngredientItemStack(input.getIngredients().get(i));
                }
                return new RecipeDefinition(inputIngredients,
                        new IRecipeIngredient[]{new RecipeIngredientItemStack(input.getRecipeOutput(), true)});
            }
        });
    }

    @Nullable
    @Override
    public RecipeIngredients simulate(RecipeIngredients input) {
        List<IRecipeIngredient<ItemStack, ItemHandlerRecipeTarget>> recipeIngredients = input.getIngredients(RecipeComponent.ITEMSTACK);
        if (input.getComponents().size() != 1 || recipeIngredients.size() < 1) {
            return null;
        }

        int rows = 2;
        int columns = 2;
        if (input.getIngredients(RecipeComponent.ITEMSTACK).size() > 4) {
            rows = 3;
            columns = 3;
        }
        InventoryCrafting inventoryCrafting = new InventoryCrafting(DUMMY_CONTAINTER, rows, columns);
        for (int i = 0; i < recipeIngredients.size(); i++) {
            inventoryCrafting.setInventorySlotContents(i,
                    Iterables.getFirst(recipeIngredients.get(i).getMatchingInstances(),
                            ItemStack.EMPTY));
        }

        IRecipe recipe = CraftingManager.findMatchingRecipe(inventoryCrafting, world);
        if (recipe == null) {
            return null;
        }

        return new RecipeIngredients(new RecipeIngredientItemStack(recipe.getCraftingResult(inventoryCrafting)));
    }

    @Nullable
    @Override
    public <R> R[] getInputComponentTargets(RecipeComponent<?, R> component) {
        return null;
    }

    @Nullable
    @Override
    public <R> R[] getOutputComponentTargets(RecipeComponent<?, R> component) {
        return null;
    }
}
