package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for energy.
 * @author rubensworks
 */
public class IngredientSerializerEnergy implements IIngredientSerializer<Integer, Void> {
    @Override
    public NBTBase serializeInstance(Integer instance) {
        return new NBTTagInt(instance);
    }

    @Override
    public Integer deserializeInstance(NBTBase tag) throws IllegalArgumentException {
        if (!(tag instanceof NBTTagInt)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagInt");
        }
        return ((NBTTagInt) tag).getInt();
    }

    @Override
    public NBTBase serializeCondition(Void matchCondition) {
        return new NBTTagByte((byte) 0);
    }

    @Override
    public Void deserializeCondition(NBTBase tag) throws IllegalArgumentException {
        return null;
    }
}
