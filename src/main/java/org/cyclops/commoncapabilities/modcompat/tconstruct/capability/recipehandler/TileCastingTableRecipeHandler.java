package org.cyclops.commoncapabilities.modcompat.tconstruct.capability.recipehandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.Cast;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Recipe handler capability for the casting basin.
 * @author rubensworks
 */
public class TileCastingTableRecipeHandler extends TileCastingRecipeHandler {

    private static final TileCastingTableRecipeHandler INSTANCE = new TileCastingTableRecipeHandler();
    private static final Map<Triple<Item, Item, Fluid>, List<ItemStack>> CAST_DICT = Maps.newHashMap();

    private static Collection<IRecipeDefinition> RECIPES = null;

    public static TileCastingTableRecipeHandler getInstance() {
        return INSTANCE;
    }

    private TileCastingTableRecipeHandler() {

    }

    @Override
    protected void putRecipeItemStackInputs(Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs, CastingRecipe recipeCasting) {
        if(recipeCasting.cast != null && recipeCasting.getResult() != null && recipeCasting.getResult().getItem() instanceof Cast) {
            Triple<Item, Item, Fluid> output = Triple.of(recipeCasting.getResult().getItem(), Cast.getPartFromTag(recipeCasting.getResult()), recipeCasting.getFluid().getFluid());

            if(!CAST_DICT.containsKey(output)) {
                List<ItemStack> list = Lists.newLinkedList();
                CAST_DICT.put(output, list);
                super.putRecipeItemStackInputs(inputs, recipeCasting);
            }

            CAST_DICT.get(output).addAll(recipeCasting.cast.getInputs());
        } else {
            super.putRecipeItemStackInputs(inputs, recipeCasting);
        }
    }

    @Override
    protected List<ICastingRecipe> getCastingRecipes() {
        return TinkerRegistry.getAllTableCastingRecipes();
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
        return TinkerRegistry.getTableCasting(itemStack, fluid);
    }
}
