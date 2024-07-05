package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A comparator implementation for Data Components.
 * @author rubensworks
 */
public class DataComparator implements Comparator<DataComponentMap> {

    /**
     * A comparator for Data Components. (This is set in GeneralConfig)
     */
    public static Comparator<DataComponentMap> INSTANCE = new DataComparator(null);

    private final Set<ResourceLocation> ignoreDataComponentTypes;

    public DataComparator(@Nullable Set<ResourceLocation> ignoreDataComponentTypes) {
        this.ignoreDataComponentTypes = ignoreDataComponentTypes;
    }

    @Override
    public int compare(DataComponentMap o1, DataComponentMap o2) {
        return this.compare(o1, o2, this.ignoreDataComponentTypes);
    }

    protected int compare(DataComponentMap o1, DataComponentMap o2, @Nullable Set<ResourceLocation> ignoreDataComponentTypes) {
        // Return immediately if identical
        if (o1 == o2) {
            return 0;
        }

        // Determine keys to compare
        Set<DataComponentType<?>> k1 = o1.keySet();
        Set<DataComponentType<?>> k2 = o2.keySet();
        if (ignoreDataComponentTypes != null) {
            k1 = k1.stream().filter(k -> !ignoreDataComponentTypes.contains(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(k))).collect(Collectors.toSet());
            k2 = k2.stream().filter(k -> !ignoreDataComponentTypes.contains(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(k))).collect(Collectors.toSet());
        }

        // Check if keys are equal
        if (!k1.equals(k2)) {
            String[] k1a = k1.stream().map(k -> BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(k).toString()).toArray(String[]::new);
            String[] k2a = k2.stream().map(k -> BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(k).toString()).toArray(String[]::new);
            Arrays.sort(k1a);
            Arrays.sort(k2a);
            int minLength = Math.min(k1a.length, k2a.length);
            for (int i = 0; i < minLength; i++) {
                int result = k1a[i].compareTo(k2a[i]);
                if (result != 0) {
                    return result;
                }
            }
            return k1a.length - k2a.length;
        }

        // Compare values
        for (DataComponentType<?> key : k1) {
            int comp = this.compareRaw(o1.get(key), o2.get(key));
            if (comp != 0) {
                return comp;
            }
        }

        // Otherwise, assume equality
        return 0;
    }

    private int compareRaw(Object o1, Object o2) {
        if (o1 == o2 || o1.equals(o2)) {
            return 0;
        }
        if (o1 instanceof Comparable comparable) {
            return comparable.compareTo(o2);
        }
        return Integer.compare(System.identityHashCode(o1), System.identityHashCode(o2));
    }

}
