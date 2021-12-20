package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for FluidStacks.
 * @author rubensworks
 */
public class IngredientSerializerFluidStack implements IIngredientSerializer<FluidStack, Integer> {
    @Override
    public INBT serializeInstance(FluidStack instance) {
        return instance.isEmpty() ? new CompoundNBT() : instance.writeToNBT(new CompoundNBT());
    }

    @Override
    public FluidStack deserializeInstance(INBT tag) throws IllegalArgumentException {
        if (!(tag instanceof CompoundNBT)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagCompound");
        }
        return FluidStack.loadFluidStackFromNBT((CompoundNBT) tag);
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
        return ((IntNBT) tag).getAsInt();
    }
}
