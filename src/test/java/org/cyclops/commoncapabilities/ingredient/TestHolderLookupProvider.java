package org.cyclops.commoncapabilities.ingredient;

import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author rubensworks
 */
public class TestHolderLookupProvider {

    public static HolderLookup.Provider get() {
        return new HolderLookup.Provider() {

            @Override
            public Stream<ResourceKey<? extends Registry<?>>> listRegistries() {
                return Stream.empty();
            }

            @Override
            public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> pRegistryKey) {
                return Optional.empty();
            }

            @Override
            public <V> RegistryOps<V> createSerializationContext(DynamicOps<V> pOps) {
                return RegistryOps.create(pOps, this);
            }
        };
    }

}
