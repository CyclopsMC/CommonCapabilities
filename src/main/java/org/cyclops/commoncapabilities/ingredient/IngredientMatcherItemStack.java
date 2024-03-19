package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.cyclopscore.helper.Helpers;
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
    public Integer getAnyMatchCondition() {
        return ItemMatch.ANY;
    }

    @Override
    public Integer getExactMatchCondition() {
        return ItemMatch.EXACT;
    }

    @Override
    public Integer getExactMatchNoQuantityCondition() {
        return ItemMatch.ITEM | ItemMatch.TAG;
    }

    @Override
    public Integer withCondition(Integer matchCondition, Integer with) {
        return matchCondition | with;
    }

    @Override
    public Integer withoutCondition(Integer matchCondition, Integer without) {
        return matchCondition & ~without;
    }

    @Override
    public boolean hasCondition(Integer matchCondition, Integer searchCondition) {
        return (matchCondition & searchCondition) > 0;
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
    public long getQuantity(ItemStack instance) {
        return instance.getCount();
    }

    @Override
    public ItemStack withQuantity(ItemStack instance, long quantity) {
        if (instance.getCount() == quantity) {
            return instance;
        }
        ItemStack copy = instance.copy();
        copy.setCount(Helpers.castSafe(quantity));
        return copy;
    }

    @Override
    public long getMaximumQuantity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int conditionCompare(Integer a, Integer b) {
        return Integer.compare(a, b);
    }

    @Override
    public String localize(ItemStack instance) {
        return instance.getHoverName().getString();
    }

    @Override
    public MutableComponent getDisplayName(ItemStack instance) {
        return (MutableComponent) instance.getHoverName();
    }

    @Override
    public String toString(ItemStack instance) {
        return String.format("%s %s %s", BuiltInRegistries.ITEM.getKey(instance.getItem()), instance.getCount(), instance.getTag());
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
            int c1 = o1.getCount();
            int c2 = o2.getCount();
            if (c1 == c2) {
                return IngredientHelpers.compareTags(o1.getTag(), o2.getTag());
            }
            return c1 - c2;
        }
        return Item.getId(o1.getItem()) - Item.getId(o2.getItem());
    }
}
