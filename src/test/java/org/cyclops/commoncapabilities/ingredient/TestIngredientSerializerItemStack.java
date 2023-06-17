package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientSerializerItemStack {

    private static IngredientSerializerItemStack S;
    private static CompoundTag TAG;
    private static CompoundTag I_TAG1;
    private static CompoundTag I_TAG2;
    private static CompoundTag I_TAG1L;
    private static CompoundTag I_TAG2L;
    private static CompoundTag I_TAG_EMPTY;
    private static ItemStack I1;
    private static ItemStack I2;
    private static ItemStack I1L;
    private static ItemStack I2L;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();

        S = new IngredientSerializerItemStack();

        TAG = new CompoundTag();
        TAG.putBoolean("flag", true);

        I_TAG1 = new CompoundTag();
        I_TAG1.putString("id", "minecraft:apple");
        I_TAG1.putByte("Count", (byte) 1);

        I_TAG2 = new CompoundTag();
        I_TAG2.putString("id", "minecraft:lead");
        I_TAG2.putByte("Count", (byte) 2);
        I_TAG2.put("tag", TAG);

        I_TAG1L = new CompoundTag();
        I_TAG1L.putString("id", "minecraft:apple");
        I_TAG1L.putByte("Count", (byte) 1);
        I_TAG1L.putInt("ExtendedCount", 128);

        I_TAG2L = new CompoundTag();
        I_TAG2L.putString("id", "minecraft:lead");
        I_TAG2L.putByte("Count", (byte) 1);
        I_TAG2L.put("tag", TAG);
        I_TAG2L.putInt("ExtendedCount", 2000);

        I_TAG_EMPTY = new CompoundTag();
        I_TAG_EMPTY.putString("id", "minecraft:air");
        I_TAG_EMPTY.putByte("Count", (byte) 0);

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
        assertThat(ItemStack.isSameItemSameTags(I1, S.deserializeInstance(I_TAG1)), is(true));
        assertThat(ItemStack.isSameItemSameTags(I2, S.deserializeInstance(I_TAG2)), is(true));
    }

    @Test
    public void deserializeInstanceLarge() {
        assertThat(ItemStack.isSameItemSameTags(I1L, S.deserializeInstance(I_TAG1L)), is(true));
        assertThat(ItemStack.isSameItemSameTags(I2L, S.deserializeInstance(I_TAG2L)), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeInstanceInvalid() {
        S.deserializeInstance(StringTag.valueOf("0"));
    }

    @Test
    public void serializeCondition() {
        assertThat(S.serializeCondition(1), is(IntTag.valueOf(1)));
    }

    @Test
    public void deserializeCondition() {
        assertThat(S.deserializeCondition(IntTag.valueOf(1)), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeConditionInvalid() {
        S.deserializeCondition(StringTag.valueOf("0"));
    }

}
