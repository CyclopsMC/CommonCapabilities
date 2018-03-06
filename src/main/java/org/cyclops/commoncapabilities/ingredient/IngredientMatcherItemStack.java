package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.item.Item;
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
    public boolean isInstance(Object object) {
        return object instanceof ItemStack;
    }

    @Override
    public Integer getExactMatchCondition() {
        return ItemMatch.EXACT;
    }

    @Override
    public boolean matches(ItemStack a, ItemStack b, Integer matchCondition) {
        return ItemMatch.areItemStacksEqual(a, b, matchCondition);
    }

    @Override
    public ItemStack getEmptyInstance() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isEmpty(ItemStack instance) {
        return instance.isEmpty();
    }

    @Override
    public int hash(ItemStack instance) {
        return ItemStackHelpers.getItemStackHashCode(instance);
    }

    @Override
    public ItemStack copy(ItemStack instance) {
        return instance.copy();
    }

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        if (o1.isEmpty()) {
            if (o2.isEmpty()) {
                return 0;
            } else {
                return -1;
            }
        } else if (o2.isEmpty()) {
            return 1;
        } else if (o1.getItem() == o2.getItem()) {
            int m1 = o1.getMetadata();
            int m2 = o2.getMetadata();
            if (m1 == m2) {
                int c1 = o1.getCount();
                int c2 = o2.getCount();
                if (c1 == c2) {
                    return IngredientHelpers.compareTags(o1.getTagCompound(), o2.getTagCompound());
                }
                return c1 - c2;
            }
            return m1 - m2;
        }
        return Item.getIdFromItem(o1.getItem()) - Item.getIdFromItem(o2.getItem());
    }
}
