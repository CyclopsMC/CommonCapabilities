package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for FluidStacks.
 * @author rubensworks
 */
public class IngredientSerializerFluidStack implements IIngredientSerializer<FluidStack, Integer> {
    @Override
    public NBTBase serializeInstance(FluidStack instance) {
        return instance == null ? new NBTTagCompound() : instance.writeToNBT(new NBTTagCompound());
    }

    @Override
    public FluidStack deserializeInstance(NBTBase tag) throws IllegalArgumentException {
        if (!(tag instanceof NBTTagCompound)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagCompound");
        }
        return FluidStack.loadFluidStackFromNBT((NBTTagCompound) tag);
    }

    @Override
    public NBTBase serializeCondition(Integer matchCondition) {
        return new NBTTagInt(matchCondition);
    }

    @Override
    public Integer deserializeCondition(NBTBase tag) throws IllegalArgumentException {
        if (!(tag instanceof NBTTagInt)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagInt");
        }
        return ((NBTTagInt) tag).getInt();
    }
}
