package org.cyclops.commoncapabilities.modcompat.tconstruct.capability.recipehandler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;

import java.util.Collection;
import java.util.List;

/**
 * Recipe handler capability for the casting basin.
 * @author rubensworks
 */
public class TileCastingBasinRecipeHandler extends TileCastingRecipeHandler {

    private static final TileCastingBasinRecipeHandler INSTANCE = new TileCastingBasinRecipeHandler();

    private static Collection<IRecipeDefinition> RECIPES = null;

    public static TileCastingBasinRecipeHandler getInstance() {
        return INSTANCE;
    }

    private TileCastingBasinRecipeHandler() {

    }

    @Override
    protected List<ICastingRecipe> getCastingRecipes() {
        return TinkerRegistry.getAllBasinCastingRecipes();
    }

    @Override
    public Collection<IRecipeDefinition> getRecipes() {
        if (RECIPES == null) {
            RECIPES = super.getRecipes();
        }
        return RECIPES;
    }

    @Override
    protected ICastingRecipe getCastingRecipe(ItemStack itemStack, Fluid fluid) {
        return TinkerRegistry.getBasinCasting(itemStack, fluid);
    }
}
