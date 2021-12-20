package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.registry.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.StringNBT;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientSerializerItemStack {

    private static IngredientSerializerItemStack S;
    private static CompoundNBT TAG;
    private static CompoundNBT I_TAG1;
    private static CompoundNBT I_TAG2;
    private static CompoundNBT I_TAG1L;
    private static CompoundNBT I_TAG2L;
    private static CompoundNBT I_TAG_EMPTY;
    private static ItemStack I1;
    private static ItemStack I2;
    private static ItemStack I1L;
    private static ItemStack I2L;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        Bootstrap.bootStrap();

        S = new IngredientSerializerItemStack();

        TAG = new CompoundNBT();
        TAG.putBoolean("flag", true);

        I_TAG1 = new CompoundNBT();
        I_TAG1.putString("id", "minecraft:apple");
        I_TAG1.putByte("Count", (byte) 1);

        I_TAG2 = new CompoundNBT();
        I_TAG2.putString("id", "minecraft:lead");
        I_TAG2.putByte("Count", (byte) 2);
        I_TAG2.put("tag", TAG);

        I_TAG1L = new CompoundNBT();
        I_TAG1L.putString("id", "minecraft:apple");
        I_TAG1L.putByte("Count", (byte) 1);
        I_TAG1L.putInt("ExtendedCount", 128);

        I_TAG2L = new CompoundNBT();
        I_TAG2L.putString("id", "minecraft:lead");
        I_TAG2L.putByte("Count", (byte) 1);
        I_TAG2L.put("tag", TAG);
        I_TAG2L.putInt("ExtendedCount", 2000);

        I_TAG_EMPTY = new CompoundNBT();
        I_TAG_EMPTY.putString("id", "minecraft:air");
        I_TAG_EMPTY.putByte("Count", (byte) 1);

        I1 = new ItemStack(Items.APPLE);
        I2 = new ItemStack(Items.LEAD, 2);
        I2.setTag(TAG);
        I1L = new ItemStack(Items.APPLE, 128);
        I2L = new ItemStack(Items.LEAD, 2000);
        I2L.setTag(TAG);
    }

    @Test
    public void serializeInstance() {
        assertThat(S.serializeInstance(I1), is(I_TAG1));
        assertThat(S.serializeInstance(I2), is(I_TAG2));
        assertThat(S.serializeInstance(ItemStack.EMPTY), is(I_TAG_EMPTY));
    }

    @Test
    public void serializeInstanceLarge() {
        assertThat(S.serializeInstance(I1L), is(I_TAG1L));
        assertThat(S.serializeInstance(I2L), is(I_TAG2L));
    }

    @Test
    public void deserializeInstance() {
        assertThat(ItemStack.isSame(I1, S.deserializeInstance(I_TAG1)), is(true));
        assertThat(ItemStack.isSame(I2, S.deserializeInstance(I_TAG2)), is(true));
    }

    @Test
    public void deserializeInstanceLarge() {
        assertThat(ItemStack.isSame(I1L, S.deserializeInstance(I_TAG1L)), is(true));
        assertThat(ItemStack.isSame(I2L, S.deserializeInstance(I_TAG2L)), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeInstanceInvalid() {
        S.deserializeInstance(StringNBT.valueOf("0"));
    }

    @Test
    public void serializeCondition() {
        assertThat(S.serializeCondition(1), is(IntNBT.valueOf(1)));
    }

    @Test
    public void deserializeCondition() {
        assertThat(S.deserializeCondition(IntNBT.valueOf(1)), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeConditionInvalid() {
        S.deserializeCondition(StringNBT.valueOf("0"));
    }

}
