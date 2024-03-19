package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.IntTag;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for FluidStacks.
 * @author rubensworks
 */
public class IngredientSerializerFluidStack implements IIngredientSerializer<FluidStack, Integer> {
    @Override
    public Tag serializeInstance(FluidStack instance) {
        return instance.isEmpty() ? new CompoundTag() : instance.writeToNBT(new CompoundTag());
    }

    @Override
    public FluidStack deserializeInstance(Tag tag) throws IllegalArgumentException {
        if (!(tag instanceof CompoundTag)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagCompound");
        }
        return FluidStack.loadFluidStackFromNBT((CompoundTag) tag);
    }

    @Override
    public Tag serializeCondition(Integer matchCondition) {
        return IntTag.valueOf(matchCondition);
    }

    @Override
    public Integer deserializeCondition(Tag tag) throws IllegalArgumentException {
        if (!(tag instanceof IntTag)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagInt");
        }
        return ((IntTag) tag).getAsInt();
    }
}
