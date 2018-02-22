package org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.ItemHandlerRecipeTarget;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Recipe handler capability for the vanilla furnace.
 * @author rubensworks
 */
public class VanillaFurnaceRecipeHandler implements IRecipeHandler {

    private static final Set<IngredientComponent<?, ?, ?>> COMPONENTS_INPUT  = Sets.newHashSet(IngredientComponent.ITEMSTACK);
    private static final Set<IngredientComponent<?, ?, ?>> COMPONENTS_OUTPUT = Sets.newHashSet(IngredientComponent.ITEMSTACK);

    private final TileEntityFurnace tile;
    private List<IRecipeDefinition> recipes = null;

    public VanillaFurnaceRecipeHandler(TileEntityFurnace tile) {
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
        return component == IngredientComponent.ITEMSTACK && size == 1;
    }

    @Override
    public List<IRecipeDefinition> getRecipes() {
        if (recipes != null) {
            return recipes;
        }
        return recipes = Lists.newArrayList(Collections2.transform(FurnaceRecipes.instance().getSmeltingList().entrySet(),
                new Function<Map.Entry<ItemStack, ItemStack>, IRecipeDefinition>() {
                    @Nullable
                    @Override
                    public IRecipeDefinition apply(Map.Entry<ItemStack, ItemStack> input) {
                        return RecipeDefinition.ofIngredient(IngredientComponent.ITEMSTACK, Lists.newArrayList(
                                new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, input.getKey(), ItemMatch.DAMAGE | ItemMatch.NBT)),
                                MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, input.getValue()));
                    }
                }));
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        List<ItemStack> recipeIngredients = input.getInstances(IngredientComponent.ITEMSTACK);
        if (input.getComponents().size() != 1 || recipeIngredients.size() != 1) {
            return null;
        }
        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(recipeIngredients.get(0));
        return MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, result);
    }

    @Nullable
    @Override
    public <R> R[] getInputComponentTargets(IngredientComponent<?, R, ?> component) {
        if (component == IngredientComponent.ITEMSTACK) {
            return (R[]) new ItemHandlerRecipeTarget[]{
                    new ItemHandlerRecipeTarget(
                            tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP), 0)
            };
        }
        return null;
    }

    @Nullable
    @Override
    public <R> R[] getOutputComponentTargets(IngredientComponent<?, R, ?> component) {
        if (component == IngredientComponent.ITEMSTACK) {
            return (R[]) new ItemHandlerRecipeTarget[]{
                    new ItemHandlerRecipeTarget(
                            tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN), 0)
            };
        }
        return null;
    }
}
