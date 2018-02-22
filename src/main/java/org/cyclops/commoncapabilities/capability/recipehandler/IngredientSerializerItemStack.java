package org.cyclops.commoncapabilities.capability.recipehandler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

/**
 * Serializer for ItemStacks.
 * @author rubensworks
 */
public class IngredientSerializerItemStack implements IIngredientSerializer<ItemStack, Integer> {
    @Override
    public NBTBase serializeInstance(ItemStack instance) {
        return instance.serializeNBT();
    }

    @Override
    public ItemStack deserializeInstance(NBTBase tag) throws IllegalArgumentException {
        if (!(tag instanceof NBTTagCompound)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagCompound");
        }
        return new ItemStack((NBTTagCompound) tag);
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
