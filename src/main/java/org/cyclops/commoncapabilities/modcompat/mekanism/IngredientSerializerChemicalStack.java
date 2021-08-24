package org.cyclops.commoncapabilities.modcompat.mekanism;

import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for ChemicalStacks.
 * @author rubensworks
 */
public abstract class IngredientSerializerChemicalStack<S extends ChemicalStack<C>, C extends Chemical<C>> implements IIngredientSerializer<S, Integer> {
    @Override
    public INBT serializeInstance(S instance) {
        return instance.isEmpty() ? new CompoundNBT() : instance.write(new CompoundNBT());
    }

    protected abstract S readFromNbt(CompoundNBT tag);

    @Override
    public S deserializeInstance(INBT tag) throws IllegalArgumentException {
        if (!(tag instanceof CompoundNBT)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagCompound");
        }
        return this.readFromNbt((CompoundNBT) tag);
    }

    @Override
    public INBT serializeCondition(Integer matchCondition) {
        return IntNBT.valueOf(matchCondition);
    }

    @Override
    public Integer deserializeCondition(INBT tag) throws IllegalArgumentException {
        if (!(tag instanceof IntNBT)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagInt");
        }
        return ((IntNBT) tag).getInt();
    }
}
