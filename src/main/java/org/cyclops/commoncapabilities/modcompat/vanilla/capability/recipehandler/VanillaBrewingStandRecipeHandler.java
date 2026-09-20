package org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler;

import com.google.common.collect.Sets;
import net.minecraft.core.NonNullList;
import net.minecraft.potion.PotionHelperCommonCapabilities;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BrewingInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;

import javax.annotation.Nullable;
import java.util.List;
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
        return PotionHelperCommonCapabilities.getVanillaRecipes();
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        List<ItemStack> recipeIngredients = input.getInstances(IngredientComponent.ITEMSTACK);
        if (input.getComponents().size() != 1
                || !isValidSizeInput(IngredientComponent.ITEMSTACK, recipeIngredients.size())) {
            return null;
        }

        // Slot 0 holds the reagent, slots 1-3 the potions
        Level level = ServerLifecycleHooks.getCurrentServer().overworld();
        if (!(level.recipeAccess() instanceof RecipeManager recipeManager)) {
            return null;
        }

        ItemStack reagent = recipeIngredients.get(0);
        NonNullList<ItemStack> brewingItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
        boolean brewed = false;
        for (int i = 1; i < recipeIngredients.size(); i++) {
            ItemStack potion = recipeIngredients.get(i).copy();
            BrewingInput brewingInput = new BrewingInput(potion, reagent);
            ItemStack output = recipeManager.getRecipeFor(RecipeType.BREWING, brewingInput, level)
                    .map(recipe -> recipe.value().assemble(brewingInput))
                    .orElse(ItemStack.EMPTY);
            if (!output.isEmpty()) {
                brewingItemStacks.set(i, output);
                brewed = true;
            }
        }

        return brewed ? MixedIngredients.ofInstances(IngredientComponent.ITEMSTACK, brewingItemStacks) : null;
    }

}
