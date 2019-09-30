package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraftforge.common.util.Constants;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for ItemStacks.
 * @author rubensworks
 */
public class IngredientSerializerItemStack implements IIngredientSerializer<ItemStack, Integer> {
    @Override
    public INBT serializeInstance(ItemStack instance) {
        CompoundNBT tag = instance.serializeNBT();
        if (instance.getCount() > 127) {
            tag.putInt("ExtendedCount", instance.getCount());
            tag.putByte("Count", (byte)1);
        }
        return tag;
    }

    @Override
    public ItemStack deserializeInstance(INBT tag) throws IllegalArgumentException {
        if (!(tag instanceof CompoundNBT)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagCompound");
        }
        CompoundNBT stackTag = (CompoundNBT) tag;
        ItemStack itemStack = ItemStack.read(stackTag);
        if (stackTag.contains("ExtendedCount", Constants.NBT.TAG_INT)) {
            itemStack.setCount(stackTag.getInt("ExtendedCount"));
        }
        return itemStack;
    }

    @Override
    public INBT serializeCondition(Integer matchCondition) {
        return new IntNBT(matchCondition);
    }

    @Override
    public Integer deserializeCondition(INBT tag) throws IllegalArgumentException {
        if (!(tag instanceof IntNBT)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagInt");
        }
        return ((IntNBT) tag).getInt();
    }
}
