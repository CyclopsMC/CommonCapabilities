package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.core.component.DataComponentMap;

import javax.annotation.Nullable;

/**
 * Helper methods for ingredients.
 * @author rubensworks
 */
public final class IngredientHelpers {

    /**
     * Compare the given NBT tags with each other for order.
     * @param tag1 An NBT tag.
     * @param tag2 An NBT tag.
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     */
    public static int compareData(@Nullable DataComponentMap tag1, @Nullable DataComponentMap tag2) {
        if (tag1 == null) {
            if (tag2 == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (tag2 == null) {
            return 1;
        } else {
            return DataComparator.INSTANCE.compare(tag1, tag2);
        }
    }

}
