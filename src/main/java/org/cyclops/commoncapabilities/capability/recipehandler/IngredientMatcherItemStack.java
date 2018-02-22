package org.cyclops.commoncapabilities.capability.recipehandler;

import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;

/**
 * Matcher for ItemStacks.
 * @author rubensworks
 */
public class IngredientMatcherItemStack implements IIngredientMatcher<ItemStack, Integer> {
    @Override
    public boolean matches(ItemStack a, ItemStack b, Integer matchCondition) {
        return ItemMatch.areItemStacksEqual(a, b, matchCondition);
    }

    @Override
    public boolean matchesExactly(ItemStack a, ItemStack b) {
        return matches(a, b, ItemMatch.EXACT);
    }

    @Override
    public int hash(ItemStack instance) {
        return ItemStackHelpers.getItemStackHashCode(instance);
    }
}
