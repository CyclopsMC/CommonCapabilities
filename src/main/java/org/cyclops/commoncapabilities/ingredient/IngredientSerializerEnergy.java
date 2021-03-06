package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for energy.
 * @author rubensworks
 */
public class IngredientSerializerEnergy implements IIngredientSerializer<Integer, Boolean> {
    @Override
    public INBT serializeInstance(Integer instance) {
        return IntNBT.valueOf(instance);
    }

    @Override
    public Integer deserializeInstance(INBT tag) throws IllegalArgumentException {
        if (!(tag instanceof IntNBT)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagInt");
        }
        return ((IntNBT) tag).getInt();
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
        return ((ByteNBT) tag).getByte() == 1;
    }
}
