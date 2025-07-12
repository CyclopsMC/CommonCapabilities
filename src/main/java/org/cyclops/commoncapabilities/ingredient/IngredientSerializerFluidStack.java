package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for FluidStacks.
 * @author rubensworks
 */
public class IngredientSerializerFluidStack implements IIngredientSerializer<FluidStack, Integer> {
    @Override
    public void serializeInstance(ValueOutput valueOutput, FluidStack instance) {
        valueOutput.store("i", FluidStack.OPTIONAL_CODEC, instance);
    }

    @Override
    public FluidStack deserializeInstance(ValueInput valueInput) throws IllegalArgumentException {
        return valueInput.read("i", FluidStack.OPTIONAL_CODEC).orElseThrow();
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
        return tag.asInt().orElseThrow();
    }
}
