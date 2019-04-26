package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.NBTBase;

import javax.annotation.Nullable;
import java.util.Comparator;

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
    public static int compareTags(@Nullable NBTBase tag1, @Nullable NBTBase tag2) {
        if (tag1 == null) {
            if (tag2 == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (tag2 == null) {
            return 1;
        } else {
            return NBTBaseComparator.INSTANCE.compare(tag1, tag2);
        }
    }

}
