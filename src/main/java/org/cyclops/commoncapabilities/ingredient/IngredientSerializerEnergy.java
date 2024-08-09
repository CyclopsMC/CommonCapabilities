package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
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
        if (tag instanceof IntTag) {
            // TODO: needed for backwards-compatibility, remove in next major version
            return Long.valueOf(((IntTag) tag).getAsInt());
        }
        if (tag instanceof LongTag) {
            return ((LongTag) tag).getAsLong();
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
        return ((ByteTag) tag).getAsByte() == 1;
    }
}
