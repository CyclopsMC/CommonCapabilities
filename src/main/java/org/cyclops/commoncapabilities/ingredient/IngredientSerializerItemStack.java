package org.cyclops.commoncapabilities.ingredient;

import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtOps;
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
        Tag tag = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, instance)
                .getOrThrow(false, JsonParseException::new);
        if (instance.getCount() > 127) {
            ((CompoundTag) tag).putInt("ExtendedCount", instance.getCount());
            ((CompoundTag) tag).putByte("Count", (byte)1);
        }
        return tag;
    }

    @Override
    public ItemStack deserializeInstance(Tag tag) throws IllegalArgumentException {
        ItemStack itemStack = ItemStack.CODEC.parse(NbtOps.INSTANCE, tag)
                .getOrThrow(false, JsonParseException::new);


        if (!(tag instanceof CompoundTag stackTag)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagCompound");
        }
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
