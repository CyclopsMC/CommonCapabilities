package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.util.Constants;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for ItemStacks.
 * @author rubensworks
 */
public class IngredientSerializerItemStack implements IIngredientSerializer<ItemStack, Integer> {
    @Override
    public NBTBase serializeInstance(ItemStack instance) {
        NBTTagCompound tag = instance.serializeNBT();
        if (instance.getCount() > 127) {
            tag.setInteger("ExtendedCount", instance.getCount());
        }
        return tag;
    }

    @Override
    public ItemStack deserializeInstance(NBTBase tag) throws IllegalArgumentException {
        if (!(tag instanceof NBTTagCompound)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagCompound");
        }
        NBTTagCompound stackTag = (NBTTagCompound) tag;
        ItemStack itemStack = new ItemStack(stackTag);
        if (stackTag.hasKey("ExtendedCount", Constants.NBT.TAG_INT)) {
            itemStack.setCount(stackTag.getInteger("ExtendedCount"));
        }
        return itemStack;
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
