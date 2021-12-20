package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for ItemStacks.
 * @author rubensworks
 */
public class IngredientSerializerItemStack implements IIngredientSerializer<ItemStack, Integer> {
    @Override
    public Tag serializeInstance(ItemStack instance) {
        CompoundTag tag = instance.serializeNBT();
        if (instance.getCount() > 127) {
            tag.putInt("ExtendedCount", instance.getCount());
            tag.putByte("Count", (byte)1);
        }
        return tag;
    }

    @Override
    public ItemStack deserializeInstance(Tag tag) throws IllegalArgumentException {
        if (!(tag instanceof CompoundTag)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagCompound");
        }
        CompoundTag stackTag = (CompoundTag) tag;
        ItemStack itemStack = ItemStack.of(stackTag);
        if (stackTag.contains("ExtendedCount", Tag.TAG_INT)) {
            itemStack.setCount(stackTag.getInt("ExtendedCount"));
        }
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
        return ((IntTag) tag).getAsInt();
    }
}
