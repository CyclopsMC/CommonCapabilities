package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for energy.
 * @author rubensworks
 */
public class IngredientSerializerEnergy implements IIngredientSerializer<Long, Boolean> {
    @Override
    public Tag serializeInstance(HolderLookup.Provider lookupProvider, Long instance) {
        return LongTag.valueOf(instance);
    }

    @Override
    public Long deserializeInstance(HolderLookup.Provider lookupProvider, Tag tag) throws IllegalArgumentException {
        if (tag instanceof LongTag) {
            return tag.asLong().orElseThrow();
        }
        throw new IllegalArgumentException("This deserializer only accepts LongNBT");
    }

    @Override
    public Tag serializeCondition(Boolean matchCondition) {
        return ByteTag.valueOf((byte) (matchCondition ? 1 : 0));
    }

    @Override
    public Boolean deserializeCondition(Tag tag) throws IllegalArgumentException {
        if (!(tag instanceof ByteTag)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagByte");
        }
        return tag.asByte().orElseThrow() == 1;
    }
}
