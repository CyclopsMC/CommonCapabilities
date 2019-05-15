package org.cyclops.commoncapabilities.api.ingredient;

import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.hamcrest.Matcher;

/**
 * @author rubensworks
 */
public class IngredientTestMatcher {

    public static <T, M> Matcher<T> isIngredient(IngredientComponent<T, M> component, T value) {
        return new IsEqualIngredient<>(component, value);
    }

    public static Matcher<ItemStack> isItemStack(ItemStack value) {
        return isIngredient(IngredientComponents.ITEMSTACK, value);
    }

}
