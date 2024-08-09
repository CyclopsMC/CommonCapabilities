package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for FluidStacks.
 * @author rubensworks
 */
public class IngredientSerializerFluidStack implements IIngredientSerializer<FluidStack, Integer> {
    @Override
    public Tag serializeInstance(HolderLookup.Provider lookupProvider, FluidStack instance) {
        return instance.isEmpty() ? new CompoundTag() : FluidStack.OPTIONAL_CODEC.encodeStart(lookupProvider.createSerializationContext(NbtOps.INSTANCE), instance).getOrThrow();
    }

    @Override
    public FluidStack deserializeInstance(HolderLookup.Provider lookupProvider, Tag tag) throws IllegalArgumentException {
        try {
            return FluidStack.OPTIONAL_CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), tag).getOrThrow();
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(e);
        }
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
