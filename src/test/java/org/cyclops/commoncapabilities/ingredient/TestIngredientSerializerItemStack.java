package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientSerializerItemStack {

    private static IngredientSerializerItemStack S;
    private static NBTTagCompound TAG;
    private static NBTTagCompound I_TAG1;
    private static NBTTagCompound I_TAG2;
    private static NBTTagCompound I_TAG_EMPTY;
    private static ItemStack I1;
    private static ItemStack I2;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        Bootstrap.register();

        S = new IngredientSerializerItemStack();

        TAG = new NBTTagCompound();
        TAG.setBoolean("flag", true);

        I_TAG1 = new NBTTagCompound();
        I_TAG1.setString("id", "minecraft:apple");
        I_TAG1.setByte("Count", (byte) 1);
        I_TAG1.setShort("Damage", (short) 0);

        I_TAG2 = new NBTTagCompound();
        I_TAG2.setString("id", "minecraft:lead");
        I_TAG2.setByte("Count", (byte) 2);
        I_TAG2.setShort("Damage", (short) 3);
        I_TAG2.setTag("tag", TAG);

        I_TAG_EMPTY = new NBTTagCompound();
        I_TAG_EMPTY.setString("id", "minecraft:air");
        I_TAG_EMPTY.setByte("Count", (byte) 1);
        I_TAG_EMPTY.setShort("Damage", (short) 0);

        I1 = new ItemStack(Items.APPLE);
        I2 = new ItemStack(Items.LEAD, 2, 3);
        I2.setTagCompound(TAG);
    }

    @Test
    public void serializeInstance() {
        assertThat(S.serializeInstance(I1), is(I_TAG1));
        assertThat(S.serializeInstance(I2), is(I_TAG2));
        assertThat(S.serializeInstance(ItemStack.EMPTY), is(I_TAG_EMPTY));
    }

    @Test
    public void deserializeInstance() {
        assertThat(ItemStack.areItemStacksEqual(I1, S.deserializeInstance(I_TAG1)), is(true));
        assertThat(ItemStack.areItemStacksEqual(I2, S.deserializeInstance(I_TAG2)), is(true));
        assertThat(ItemStack.areItemStacksEqual(ItemStack.EMPTY, S.deserializeInstance(I_TAG_EMPTY)), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeInstanceInvalid() {
        S.deserializeInstance(new NBTTagString("0"));
    }

    @Test
    public void serializeCondition() {
        assertThat(S.serializeCondition(1), is(new NBTTagInt(1)));
    }

    @Test
    public void deserializeCondition() {
        assertThat(S.deserializeCondition(new NBTTagInt(1)), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeConditionInvalid() {
        S.deserializeCondition(new NBTTagString("0"));
    }

}
