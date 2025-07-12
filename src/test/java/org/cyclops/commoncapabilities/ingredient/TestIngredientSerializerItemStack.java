package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.cyclops.commoncapabilities.TestInitHelpers.deserialize;
import static org.cyclops.commoncapabilities.TestInitHelpers.serialize;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientSerializerItemStack {

    private static IngredientSerializerItemStack S;
    private static DataComponentPatch DATA;
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

        DATA = DataComponentPatch.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .build();

        I_TAG1 = new CompoundTag();
        CompoundTag subI_TAG1 = new CompoundTag();
        I_TAG1.put("i", subI_TAG1);
        subI_TAG1.putString("id", "minecraft:apple");
        subI_TAG1.putInt("count", 1);

        I_TAG2 = new CompoundTag();
        CompoundTag subI_TAG2 = new CompoundTag();
        I_TAG2.put("i", subI_TAG2);
        subI_TAG2.putString("id", "minecraft:lead");
        subI_TAG2.putInt("count", 2);
        subI_TAG2.put("components", DataComponentPatch.CODEC.encodeStart(NbtOps.INSTANCE, DATA).getOrThrow());

        I_TAG1L = new CompoundTag();
        CompoundTag subI_TAG1L = new CompoundTag();
        I_TAG1L.put("i", subI_TAG1L);
        subI_TAG1L.putString("id", "minecraft:apple");
        subI_TAG1L.putInt("count", 99);
        I_TAG1L.putInt("ExtendedCount", 128);

        I_TAG2L = new CompoundTag();
        CompoundTag subI_TAG2L = new CompoundTag();
        I_TAG2L.put("i", subI_TAG2L);
        subI_TAG2L.putString("id", "minecraft:lead");
        subI_TAG2L.putInt("count", 99);
        subI_TAG2L.put("components", DataComponentPatch.CODEC.encodeStart(NbtOps.INSTANCE, DATA).getOrThrow());
        I_TAG2L.putInt("ExtendedCount", 2000);

        I_TAG_EMPTY = new CompoundTag();
        CompoundTag subI_TAG_EMPTY = new CompoundTag();
        I_TAG_EMPTY.put("i", subI_TAG_EMPTY);

        I1 = new ItemStack(Items.APPLE);
        I2 = new ItemStack(Items.LEAD, 2);
        I2.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        I1L = new ItemStack(Items.APPLE, 128);
        I2L = new ItemStack(Items.LEAD, 2000);
        I2L.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
    }

    @Test
    public void serializeInstance() {
        assertThat(serialize(o -> S.serializeInstance(o, I1)), is(I_TAG1));
        assertThat(serialize(o -> S.serializeInstance(o, I2)), is(I_TAG2));
        assertThat(serialize(o -> S.serializeInstance(o, ItemStack.EMPTY)), is(I_TAG_EMPTY));
    }

    @Test
    public void serializeInstanceLarge() {
        assertThat(serialize(o -> S.serializeInstance(o, I1L)), is(I_TAG1L));
        assertThat(serialize(o -> S.serializeInstance(o, I2L)), is(I_TAG2L));
    }

    @Test
    public void deserializeInstance() {
        assertThat(ItemStack.isSameItemSameComponents(deserialize(I_TAG1, S::deserializeInstance), I1), is(true));
        assertThat(ItemStack.isSameItemSameComponents(deserialize(I_TAG2, S::deserializeInstance), I2), is(true));
    }

    @Test
    public void deserializeInstanceLarge() {
        assertThat(ItemStack.isSameItemSameComponents(deserialize(I_TAG1L, S::deserializeInstance), I1L), is(true));
        assertThat(ItemStack.isSameItemSameComponents(deserialize(I_TAG2L, S::deserializeInstance), I2L), is(true));
    }

    @Test(expected = NoSuchElementException.class)
    public void deserializeInstanceInvalid() {
        deserialize(new CompoundTag(), S::deserializeInstance);
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
