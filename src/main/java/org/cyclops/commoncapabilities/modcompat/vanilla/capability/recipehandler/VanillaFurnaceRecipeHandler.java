package org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import org.cyclops.commoncapabilities.api.capability.recipehandler.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Recipe handler capability for the vanilla furnace.
 * @author rubensworks
 */
public class VanillaFurnaceRecipeHandler implements IRecipeHandler {

    private static final Set<RecipeComponent<?, ?>> COMPONENTS_INPUT  = Sets.newHashSet(RecipeComponent.ITEMSTACK);
    private static final Set<RecipeComponent<?, ?>> COMPONENTS_OUTPUT = Sets.newHashSet(RecipeComponent.ITEMSTACK);

    private final TileEntityFurnace tile;
    private List<RecipeDefinition> recipes = null;

    public VanillaFurnaceRecipeHandler(TileEntityFurnace tile) {
        this.tile = tile;
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
        return component == RecipeComponent.ITEMSTACK && size == 1;
    }

    @Override
    public List<RecipeDefinition> getRecipes() {
        if (recipes != null) {
            return recipes;
        }
        return recipes = Lists.newArrayList(Collections2.transform(FurnaceRecipes.instance().getSmeltingList().entrySet(),
                new Function<Map.Entry<ItemStack, ItemStack>, RecipeDefinition>() {
                    @Nullable
                    @Override
                    public RecipeDefinition apply(Map.Entry<ItemStack, ItemStack> input) {
                        return new RecipeDefinition(
                                new IRecipeIngredient[]{new RecipeIngredientItemStack(input.getKey())},
                                new IRecipeIngredient[]{new RecipeIngredientItemStack(input.getValue())}
                        );
                    }
                }));
    }

    @Nullable
    @Override
    public RecipeIngredients simulate(RecipeIngredients input) {
        List<IRecipeIngredient<ItemStack, ItemHandlerRecipeTarget>> recipeIngredients = input.getIngredients(RecipeComponent.ITEMSTACK);
        if (input.getIngredientsSize() != 1 || recipeIngredients.size() != 1) {
            return null;
        }
        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(
                Iterables.getFirst(recipeIngredients.get(0).getMatchingInstances(), ItemStack.EMPTY));
        return new RecipeIngredients(new RecipeIngredientItemStack(result));
    }

    @Nullable
    @Override
    public <R> R[] getInputComponentTargets(RecipeComponent<?, R> component) {
        if (component == RecipeComponent.ITEMSTACK) {
            return (R[]) new ItemHandlerRecipeTarget[]{
                    new ItemHandlerRecipeTarget(
                            tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP), 0)
            };
        }
        return null;
    }

    @Nullable
    @Override
    public <R> R[] getOutputComponentTargets(RecipeComponent<?, R> component) {
        if (component == RecipeComponent.ITEMSTACK) {
            return (R[]) new ItemHandlerRecipeTarget[]{
                    new ItemHandlerRecipeTarget(
                            tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN), 0)
            };
        }
        return null;
    }
}
