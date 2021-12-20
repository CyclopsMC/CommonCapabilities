package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.LongNBT;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for energy.
 * @author rubensworks
 */
public class IngredientSerializerEnergy implements IIngredientSerializer<Long, Boolean> {
    @Override
    public INBT serializeInstance(Long instance) {
        return LongNBT.valueOf(instance);
    }

    @Override
    public Long deserializeInstance(INBT tag) throws IllegalArgumentException {
        if (tag instanceof IntNBT) {
            // TODO: needed for backwards-compatibility, remove in next major version
            return Long.valueOf(((IntNBT) tag).getAsInt());
        }
        if (tag instanceof LongNBT) {
            return ((LongNBT) tag).getAsLong();
        }
        throw new IllegalArgumentException("This deserializer only accepts LongNBT");
    }

    @Override
    public INBT serializeCondition(Boolean matchCondition) {
        return ByteNBT.valueOf((byte) (matchCondition ? 1 : 0));
    }

    @Override
    public Boolean deserializeCondition(INBT tag) throws IllegalArgumentException {
        if (!(tag instanceof ByteNBT)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagByte");
        }
        return ((ByteNBT) tag).getAsByte() == 1;
    }
}
