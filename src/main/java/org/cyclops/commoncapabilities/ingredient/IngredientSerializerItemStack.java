package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for ItemStacks.
 * @author rubensworks
 */
public class IngredientSerializerItemStack implements IIngredientSerializer<ItemStack, Integer> {
    @Override
    public void serializeInstance(ValueOutput valueOutput, ItemStack instance) {
        int count = instance.getCount();
        if (instance.getCount() > 99) {
            instance = instance.copy();
            instance.setCount(99);
            valueOutput.putInt("ExtendedCount", count);
        }
        valueOutput.store("i", ItemStack.OPTIONAL_CODEC, instance);
    }

    @Override
    public ItemStack deserializeInstance(ValueInput valueInput) throws IllegalArgumentException {
        ItemStack itemStack = valueInput.read("i", ItemStack.OPTIONAL_CODEC).orElseThrow();
        valueInput.getInt("ExtendedCount").ifPresent(itemStack::setCount);
        return itemStack;
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
