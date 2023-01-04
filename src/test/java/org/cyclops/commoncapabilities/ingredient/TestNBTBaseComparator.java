package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.*;
import org.cyclops.cyclopscore.nbt.path.NbtParseException;
import org.cyclops.cyclopscore.nbt.path.NbtPath;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.DataOutput;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestNBTBaseComparator {

    private static TagComparator COMP;

    private static EndTag E;

    private static ByteTag B_0;
    private static ByteTag B_1;
    private static ByteTag B_2;

    private static ShortTag S_0;
    private static ShortTag S_1;
    private static ShortTag S_2;

    private static IntTag I_0;
    private static IntTag I_1;
    private static IntTag I_2;

    private static LongTag L_0;
    private static LongTag L_1;
    private static LongTag L_2;

    private static FloatTag F_0;
    private static FloatTag F_1;
    private static FloatTag F_2;

    private static DoubleTag D_0;
    private static DoubleTag D_1;
    private static DoubleTag D_2;

    private static ByteArrayTag BA_0;
    private static ByteArrayTag BA_1;
    private static ByteArrayTag BA_2;

    private static StringTag STR_0;
    private static StringTag STR_1;
    private static StringTag STR_2;
    private static StringTag STR_X;

    private static ListTag LST_B_012;
    private static ListTag LST_I_;
    private static ListTag LST_I_0;
    private static ListTag LST_I_1;
    private static ListTag LST_I_01;
    private static ListTag LST_I_012;
    private static ListTag LST_STR_012;

    private static CompoundTag CMP_;
    private static CompoundTag CMP_AB;
    private static CompoundTag CMP_AI;
    private static CompoundTag CMP_AI_BI;
    private static CompoundTag CMP_AB_BB;
    private static CompoundTag CMP_AI_BB;
    private static CompoundTag CMP_AB_BI;
    private static CompoundTag CMP_AB_CI;

    private static IntArrayTag IA_0;
    private static IntArrayTag IA_1;
    private static IntArrayTag IA_2;

    private static LongArrayTag LA_0;
    private static LongArrayTag LA_1;
    private static LongArrayTag LA_2;

    private static TagUnknown UNKNOWN0;
    private static TagUnknown UNKNOWN1;

    private static CompoundTag CMP_NESTED0;
    private static CompoundTag CMP_NESTED1;
    private static CompoundTag CMP_NESTED2;

    @BeforeClass
    public static void init() {
        COMP = new TagComparator(null);

        E = EndTag.INSTANCE;

        B_0 = ByteTag.valueOf((byte) 0);
        B_1 = ByteTag.valueOf((byte) 1);
        B_2 = ByteTag.valueOf((byte) 2);

        S_0 = ShortTag.valueOf((short) 0);
        S_1 = ShortTag.valueOf((short) 1);
        S_2 = ShortTag.valueOf((short) 2);

        I_0 = IntTag.valueOf(0);
        I_1 = IntTag.valueOf(1);
        I_2 = IntTag.valueOf(2);

        L_0 = LongTag.valueOf(0);
        L_1 = LongTag.valueOf(1);
        L_2 = LongTag.valueOf(2);

        F_0 = FloatTag.valueOf(0);
        F_1 = FloatTag.valueOf(1);
        F_2 = FloatTag.valueOf(2);

        D_0 = DoubleTag.valueOf(0);
        D_1 = DoubleTag.valueOf(1);
        D_2 = DoubleTag.valueOf(2);

        BA_0 = new ByteArrayTag(new byte[]{0});
        BA_1 = new ByteArrayTag(new byte[]{1});
        BA_2 = new ByteArrayTag(new byte[]{2});

        STR_0 = StringTag.valueOf("0");
        STR_1 = StringTag.valueOf("1");
        STR_2 = StringTag.valueOf("2");
        STR_X = StringTag.valueOf("X");

        LST_B_012 = new ListTag();
        LST_B_012.add(B_0);
        LST_B_012.add(B_1);
        LST_B_012.add(B_2);
        LST_I_ = new ListTag();
        LST_I_0 = new ListTag();
        LST_I_0.add(I_0);
        LST_I_1 = new ListTag();
        LST_I_1.add(I_1);
        LST_I_01 = new ListTag();
        LST_I_01.add(I_0);
        LST_I_01.add(I_1);
        LST_I_012 = new ListTag();
        LST_I_012.add(I_0);
        LST_I_012.add(I_1);
        LST_I_012.add(I_2);
        LST_STR_012 = new ListTag();
        LST_STR_012.add(STR_0);
        LST_STR_012.add(STR_1);
        LST_STR_012.add(STR_2);

        CMP_ = new CompoundTag();
        CMP_AB = new CompoundTag();
        CMP_AB.put("A", B_0);
        CMP_AI = new CompoundTag();
        CMP_AI.put("A", I_0);
        CMP_AI_BI = new CompoundTag();
        CMP_AI_BI.put("A", I_0);
        CMP_AI_BI.put("B", I_0);
        CMP_AB_BB = new CompoundTag();
        CMP_AB_BB.put("A", B_0);
        CMP_AB_BB.put("B", B_0);
        CMP_AI_BB = new CompoundTag();
        CMP_AI_BB.put("A", I_0);
        CMP_AI_BB.put("B", B_0);
        CMP_AB_BI = new CompoundTag();
        CMP_AB_BI.put("A", B_0);
        CMP_AB_BI.put("B", I_0);
        CMP_AB_CI = new CompoundTag();
        CMP_AB_CI.put("A", B_0);
        CMP_AB_CI.put("C", I_0);

        IA_0 = new IntArrayTag(new int[]{0});
        IA_1 = new IntArrayTag(new int[]{1});
        IA_2 = new IntArrayTag(new int[]{2});

        LA_0 = new LongArrayTag(new long[]{0});
        LA_1 = new LongArrayTag(new long[]{1});
        LA_2 = new LongArrayTag(new long[]{2});

        UNKNOWN0 = new TagUnknown();
        UNKNOWN1 = new TagUnknown();

        CMP_NESTED0 = new CompoundTag();
        CMP_NESTED0.putString("a", "b");
        CMP_NESTED0.putBoolean("some boolean", true);
        CompoundTag subTag = new CompoundTag();
        subTag.putDouble("double", 4);
        subTag.putInt("int", 268940);
        CompoundTag subSubTag = new CompoundTag();
        subSubTag.putInt("value", 100);
        subSubTag.putBoolean("flag", true);
        subTag.put("and another thing", subSubTag);
        CMP_NESTED0.put("another thing", subTag);

        CMP_NESTED1 = new CompoundTag();
        CMP_NESTED1.putString("a", "b");
        CMP_NESTED1.putBoolean("some boolean", true);
        CompoundTag subTag1 = new CompoundTag();
        subTag1.putDouble("double", 4);
        subTag1.putInt("int", 268940);
        CompoundTag subSubTag1 = new CompoundTag();
        subSubTag1.putInt("value", 101);
        subSubTag1.putBoolean("flag", true);
        subTag1.put("and another thing", subSubTag1);
        CMP_NESTED1.put("another thing", subTag1);

        CMP_NESTED2 = new CompoundTag();
        CMP_NESTED2.putString("a", "b");
        CMP_NESTED2.putBoolean("some boolean", true);
        CompoundTag subTag2 = new CompoundTag();
        subTag2.putDouble("double", 4);
        subTag2.putInt("int", 268940);
        CompoundTag subSubTag2 = new CompoundTag();
        subSubTag2.putInt("value", 102);
        subSubTag2.putBoolean("flag", true);
        subTag2.put("and another thing", subSubTag2);
        CMP_NESTED2.put("another thing", subTag2);
    }

    @Test
    public void testEnd() {
        assertThat(COMP.compare(E, E), is(0));
    }

    @Test
    public void testByte() {
        assertThat(COMP.compare(B_0, B_0), is(0));
        assertThat(COMP.compare(B_1, B_0), is(1));
        assertThat(COMP.compare(B_2, B_0), is(2));

        assertThat(COMP.compare(B_0, B_1), is(-1));
        assertThat(COMP.compare(B_1, B_1), is(0));
        assertThat(COMP.compare(B_2, B_1), is(1));

        assertThat(COMP.compare(B_0, B_2), is(-2));
        assertThat(COMP.compare(B_1, B_2), is(-1));
        assertThat(COMP.compare(B_2, B_2), is(0));
    }

    @Test
    public void testShort() {
        assertThat(COMP.compare(S_0, S_0), is(0));
        assertThat(COMP.compare(S_1, S_0), is(1));
        assertThat(COMP.compare(S_2, S_0), is(2));

        assertThat(COMP.compare(S_0, S_1), is(-1));
        assertThat(COMP.compare(S_1, S_1), is(0));
        assertThat(COMP.compare(S_2, S_1), is(1));

        assertThat(COMP.compare(S_0, S_2), is(-2));
        assertThat(COMP.compare(S_1, S_2), is(-1));
        assertThat(COMP.compare(S_2, S_2), is(0));
    }

    @Test
    public void testInteger() {
        assertThat(COMP.compare(I_0, I_0), is(0));
        assertThat(COMP.compare(I_1, I_0), is(1));
        assertThat(COMP.compare(I_2, I_0), is(1));

        assertThat(COMP.compare(I_0, I_1), is(-1));
        assertThat(COMP.compare(I_1, I_1), is(0));
        assertThat(COMP.compare(I_2, I_1), is(1));

        assertThat(COMP.compare(I_0, I_2), is(-1));
        assertThat(COMP.compare(I_1, I_2), is(-1));
        assertThat(COMP.compare(I_2, I_2), is(0));
    }

    @Test
    public void testLong() {
        assertThat(COMP.compare(L_0, L_0), is(0));
        assertThat(COMP.compare(L_1, L_0), is(1));
        assertThat(COMP.compare(L_2, L_0), is(1));

        assertThat(COMP.compare(L_0, L_1), is(-1));
        assertThat(COMP.compare(L_1, L_1), is(0));
        assertThat(COMP.compare(L_2, L_1), is(1));

        assertThat(COMP.compare(L_0, L_2), is(-1));
        assertThat(COMP.compare(L_1, L_2), is(-1));
        assertThat(COMP.compare(L_2, L_2), is(0));
    }

    @Test
    public void testFloat() {
        assertThat(COMP.compare(F_0, F_0), is(0));
        assertThat(COMP.compare(F_1, F_0), is(1));
        assertThat(COMP.compare(F_2, F_0), is(1));

        assertThat(COMP.compare(F_0, F_1), is(-1));
        assertThat(COMP.compare(F_1, F_1), is(0));
        assertThat(COMP.compare(F_2, F_1), is(1));

        assertThat(COMP.compare(F_0, F_2), is(-1));
        assertThat(COMP.compare(F_1, F_2), is(-1));
        assertThat(COMP.compare(F_2, F_2), is(0));
    }

    @Test
    public void testDouble() {
        assertThat(COMP.compare(D_0, D_0), is(0));
        assertThat(COMP.compare(D_1, D_0), is(1));
        assertThat(COMP.compare(D_2, D_0), is(1));

        assertThat(COMP.compare(D_0, D_1), is(-1));
        assertThat(COMP.compare(D_1, D_1), is(0));
        assertThat(COMP.compare(D_2, D_1), is(1));

        assertThat(COMP.compare(D_0, D_2), is(-1));
        assertThat(COMP.compare(D_1, D_2), is(-1));
        assertThat(COMP.compare(D_2, D_2), is(0));
    }

    @Test
    public void testByteArray() {
        assertThat(COMP.compare(BA_0, BA_0), is(0));
        assertThat(COMP.compare(BA_1, BA_0), is(1));
        assertThat(COMP.compare(BA_2, BA_0), is(2));

        assertThat(COMP.compare(BA_0, BA_1), is(-1));
        assertThat(COMP.compare(BA_1, BA_1), is(0));
        assertThat(COMP.compare(BA_2, BA_1), is(1));

        assertThat(COMP.compare(BA_0, BA_2), is(-2));
        assertThat(COMP.compare(BA_1, BA_2), is(-1));
        assertThat(COMP.compare(BA_2, BA_2), is(0));
    }

    @Test
    public void testString() {
        assertThat(COMP.compare(STR_0, STR_0), is(0));
        assertThat(COMP.compare(STR_1, STR_0), is(1));
        assertThat(COMP.compare(STR_2, STR_0), is(2));

        assertThat(COMP.compare(STR_0, STR_1), is(-1));
        assertThat(COMP.compare(STR_1, STR_1), is(0));
        assertThat(COMP.compare(STR_2, STR_1), is(1));

        assertThat(COMP.compare(STR_0, STR_2), is(-2));
        assertThat(COMP.compare(STR_1, STR_2), is(-1));
        assertThat(COMP.compare(STR_2, STR_2), is(0));

        assertThat(COMP.compare(STR_2, STR_X), is(-38));
        assertThat(COMP.compare(STR_X, STR_2), is(38));
    }

    @Test
    public void testList() {
        assertThat(COMP.compare(LST_B_012, LST_I_), is(1));
        assertThat(COMP.compare(LST_I_, LST_I_), is(0));
        assertThat(COMP.compare(LST_I_0, LST_I_), is(3));
        assertThat(COMP.compare(LST_I_1, LST_I_), is(3));
        assertThat(COMP.compare(LST_I_01, LST_I_), is(3));
        assertThat(COMP.compare(LST_I_012, LST_I_), is(3));
        assertThat(COMP.compare(LST_STR_012, LST_I_), is(8));

        assertThat(COMP.compare(LST_B_012, LST_I_0), is(-2));
        assertThat(COMP.compare(LST_I_, LST_I_0), is(-3));
        assertThat(COMP.compare(LST_I_0, LST_I_0), is(0));
        assertThat(COMP.compare(LST_I_1, LST_I_0), is(1));
        assertThat(COMP.compare(LST_I_01, LST_I_0), is(1));
        assertThat(COMP.compare(LST_I_012, LST_I_0), is(2));
        assertThat(COMP.compare(LST_STR_012, LST_I_0), is(5));

        assertThat(COMP.compare(LST_B_012, LST_I_1), is(-2));
        assertThat(COMP.compare(LST_I_, LST_I_1), is(-3));
        assertThat(COMP.compare(LST_I_0, LST_I_1), is(-1));
        assertThat(COMP.compare(LST_I_1, LST_I_1), is(0));
        assertThat(COMP.compare(LST_I_01, LST_I_1), is(1));
        assertThat(COMP.compare(LST_I_012, LST_I_1), is(2));
        assertThat(COMP.compare(LST_STR_012, LST_I_1), is(5));

        assertThat(COMP.compare(LST_B_012, LST_I_01), is(-2));
        assertThat(COMP.compare(LST_I_, LST_I_01), is(-3));
        assertThat(COMP.compare(LST_I_0, LST_I_01), is(-1));
        assertThat(COMP.compare(LST_I_1, LST_I_01), is(-1));
        assertThat(COMP.compare(LST_I_01, LST_I_01), is(0));
        assertThat(COMP.compare(LST_I_012, LST_I_01), is(1));
        assertThat(COMP.compare(LST_STR_012, LST_I_01), is(5));

        assertThat(COMP.compare(LST_B_012, LST_I_012), is(-2));
        assertThat(COMP.compare(LST_I_, LST_I_012), is(-3));
        assertThat(COMP.compare(LST_I_0, LST_I_012), is(-2));
        assertThat(COMP.compare(LST_I_1, LST_I_012), is(-2));
        assertThat(COMP.compare(LST_I_01, LST_I_012), is(-1));
        assertThat(COMP.compare(LST_I_012, LST_I_012), is(0));
        assertThat(COMP.compare(LST_STR_012, LST_I_012), is(5));

        assertThat(COMP.compare(LST_B_012, LST_STR_012), is(-7));
        assertThat(COMP.compare(LST_I_, LST_STR_012), is(-8));
        assertThat(COMP.compare(LST_I_0, LST_STR_012), is(-5));
        assertThat(COMP.compare(LST_I_1, LST_STR_012), is(-5));
        assertThat(COMP.compare(LST_I_01, LST_STR_012), is(-5));
        assertThat(COMP.compare(LST_I_012, LST_STR_012), is(-5));
        assertThat(COMP.compare(LST_STR_012, LST_STR_012), is(0));

        assertThat(COMP.compare(LST_B_012, LST_B_012), is(0));
        assertThat(COMP.compare(LST_I_, LST_B_012), is(-1));
        assertThat(COMP.compare(LST_I_0, LST_B_012), is(2));
        assertThat(COMP.compare(LST_I_1, LST_B_012), is(2));
        assertThat(COMP.compare(LST_I_01, LST_B_012), is(2));
        assertThat(COMP.compare(LST_I_012, LST_B_012), is(2));
        assertThat(COMP.compare(LST_STR_012, LST_B_012), is(7));
    }

    @Test
    public void testCompound() {
        assertThat(COMP.compare(CMP_, CMP_), is(0));
        assertThat(COMP.compare(CMP_, CMP_AB), is(-1));
        assertThat(COMP.compare(CMP_, CMP_AI), is(-1));
        assertThat(COMP.compare(CMP_, CMP_AB_BB), is(-2));
        assertThat(COMP.compare(CMP_, CMP_AB_BI), is(-2));
        assertThat(COMP.compare(CMP_, CMP_AI_BB), is(-2));
        assertThat(COMP.compare(CMP_, CMP_AI_BI), is(-2));
        assertThat(COMP.compare(CMP_, CMP_AB_CI), is(-2));

        assertThat(COMP.compare(CMP_AB, CMP_), is(1));
        assertThat(COMP.compare(CMP_AB, CMP_AB), is(0));
        assertThat(COMP.compare(CMP_AB, CMP_AI), is(-2));
        assertThat(COMP.compare(CMP_AB, CMP_AB_BB), is(-1));
        assertThat(COMP.compare(CMP_AB, CMP_AB_BI), is(-1));
        assertThat(COMP.compare(CMP_AB, CMP_AI_BB), is(-1));
        assertThat(COMP.compare(CMP_AB, CMP_AI_BI), is(-1));
        assertThat(COMP.compare(CMP_AB, CMP_AB_CI), is(-1));

        assertThat(COMP.compare(CMP_AI, CMP_), is(1));
        assertThat(COMP.compare(CMP_AI, CMP_AB), is(2));
        assertThat(COMP.compare(CMP_AI, CMP_AI), is(0));
        assertThat(COMP.compare(CMP_AI, CMP_AB_BB), is(-1));
        assertThat(COMP.compare(CMP_AI, CMP_AB_BI), is(-1));
        assertThat(COMP.compare(CMP_AI, CMP_AI_BB), is(-1));
        assertThat(COMP.compare(CMP_AI, CMP_AI_BI), is(-1));
        assertThat(COMP.compare(CMP_AI, CMP_AB_CI), is(-1));

        assertThat(COMP.compare(CMP_AB_BB, CMP_), is(2));
        assertThat(COMP.compare(CMP_AB_BB, CMP_AB), is(1));
        assertThat(COMP.compare(CMP_AB_BB, CMP_AI), is(1));
        assertThat(COMP.compare(CMP_AB_BB, CMP_AB_BB), is(0));
        assertThat(COMP.compare(CMP_AB_BB, CMP_AB_BI), is(-2));
        assertThat(COMP.compare(CMP_AB_BB, CMP_AI_BB), is(-2));
        assertThat(COMP.compare(CMP_AB_BB, CMP_AI_BI), is(-2));
        assertThat(COMP.compare(CMP_AB_BB, CMP_AB_CI), is(-1));

        assertThat(COMP.compare(CMP_AB_BI, CMP_), is(2));
        assertThat(COMP.compare(CMP_AB_BI, CMP_AB), is(1));
        assertThat(COMP.compare(CMP_AB_BI, CMP_AI), is(1));
        assertThat(COMP.compare(CMP_AB_BI, CMP_AB_BB), is(2));
        assertThat(COMP.compare(CMP_AB_BI, CMP_AB_BI), is(0));
        assertThat(COMP.compare(CMP_AB_BI, CMP_AI_BB), is(-2));
        assertThat(COMP.compare(CMP_AB_BI, CMP_AI_BI), is(-2));
        assertThat(COMP.compare(CMP_AB_BI, CMP_AB_CI), is(-1));

        assertThat(COMP.compare(CMP_AI_BB, CMP_), is(2));
        assertThat(COMP.compare(CMP_AI_BB, CMP_AB), is(1));
        assertThat(COMP.compare(CMP_AI_BB, CMP_AI), is(1));
        assertThat(COMP.compare(CMP_AI_BB, CMP_AB_BB), is(2));
        assertThat(COMP.compare(CMP_AI_BB, CMP_AB_BI), is(2));
        assertThat(COMP.compare(CMP_AI_BB, CMP_AI_BB), is(0));
        assertThat(COMP.compare(CMP_AI_BB, CMP_AI_BI), is(-2));
        assertThat(COMP.compare(CMP_AI_BB, CMP_AB_CI), is(-1));

        assertThat(COMP.compare(CMP_AI_BI, CMP_), is(2));
        assertThat(COMP.compare(CMP_AI_BI, CMP_AB), is(1));
        assertThat(COMP.compare(CMP_AI_BI, CMP_AI), is(1));
        assertThat(COMP.compare(CMP_AI_BI, CMP_AB_BB), is(2));
        assertThat(COMP.compare(CMP_AI_BI, CMP_AB_BI), is(2));
        assertThat(COMP.compare(CMP_AI_BI, CMP_AI_BB), is(2));
        assertThat(COMP.compare(CMP_AI_BI, CMP_AI_BI), is(0));
        assertThat(COMP.compare(CMP_AI_BI, CMP_AB_CI), is(-1));

        assertThat(COMP.compare(CMP_AB_CI, CMP_), is(2));
        assertThat(COMP.compare(CMP_AB_CI, CMP_AB), is(1));
        assertThat(COMP.compare(CMP_AB_CI, CMP_AI), is(1));
        assertThat(COMP.compare(CMP_AB_CI, CMP_AB_BB), is(1));
        assertThat(COMP.compare(CMP_AB_CI, CMP_AB_BI), is(1));
        assertThat(COMP.compare(CMP_AB_CI, CMP_AI_BB), is(1));
        assertThat(COMP.compare(CMP_AB_CI, CMP_AI_BI), is(1));
        assertThat(COMP.compare(CMP_AB_CI, CMP_AB_CI), is(0));
    }

    @Test
    public void testIntArray() {
        assertThat(COMP.compare(IA_0, IA_0), is(0));
        assertThat(COMP.compare(IA_1, IA_0), is(1));
        assertThat(COMP.compare(IA_2, IA_0), is(1));

        assertThat(COMP.compare(IA_0, IA_1), is(-1));
        assertThat(COMP.compare(IA_1, IA_1), is(0));
        assertThat(COMP.compare(IA_2, IA_1), is(1));

        assertThat(COMP.compare(IA_0, IA_2), is(-1));
        assertThat(COMP.compare(IA_1, IA_2), is(-1));
        assertThat(COMP.compare(IA_2, IA_2), is(0));
    }

    @Test
    public void testLongArray() {
        assertThat(COMP.compare(LA_0, LA_0), is(0));
        assertThat(COMP.compare(LA_1, LA_0), is(1));
        assertThat(COMP.compare(LA_2, LA_0), is(1));

        assertThat(COMP.compare(LA_0, LA_1), is(-1));
        assertThat(COMP.compare(LA_1, LA_1), is(0));
        assertThat(COMP.compare(LA_2, LA_1), is(1));

        assertThat(COMP.compare(LA_0, LA_2), is(-1));
        assertThat(COMP.compare(LA_1, LA_2), is(-1));
        assertThat(COMP.compare(LA_2, LA_2), is(0));
    }

    @Test
    public void testUnknown() {
        assertThat(COMP.compare(UNKNOWN0, UNKNOWN0), is(0));
        assertThat(COMP.compare(UNKNOWN0, UNKNOWN1), is(0));
        assertThat(COMP.compare(UNKNOWN1, UNKNOWN0), is(0));
        assertThat(COMP.compare(UNKNOWN1, UNKNOWN1), is(0));
    }

    @Test
    public void testCompoundNested() {
        assertThat(COMP.compare(CMP_NESTED0, CMP_NESTED0), is(0));
        assertThat(COMP.compare(CMP_NESTED1, CMP_NESTED0), is(1));
        assertThat(COMP.compare(CMP_NESTED2, CMP_NESTED0), is(1));

        assertThat(COMP.compare(CMP_NESTED0, CMP_NESTED1), is(-1));
        assertThat(COMP.compare(CMP_NESTED1, CMP_NESTED1), is(0));
        assertThat(COMP.compare(CMP_NESTED2, CMP_NESTED1), is(1));

        assertThat(COMP.compare(CMP_NESTED0, CMP_NESTED2), is(-1));
        assertThat(COMP.compare(CMP_NESTED1, CMP_NESTED2), is(-1));
        assertThat(COMP.compare(CMP_NESTED2, CMP_NESTED2), is(0));

        assertThat(COMP.compare(CMP_NESTED0.copy(), CMP_NESTED0.copy()), is(0));
        assertThat(COMP.compare(CMP_NESTED1.copy(), CMP_NESTED1.copy()), is(0));
        assertThat(COMP.compare(CMP_NESTED2.copy(), CMP_NESTED2.copy()), is(0));
    }

    @Test
    public void testCompoundNestedFilteredMatchExact() throws NbtParseException {
        TagComparator comp = new TagComparator(NbtPath
                .parse("[\"another thing\"][\"and another thing\"].value").asNavigation());

        assertThat(comp.compare(CMP_NESTED0, CMP_NESTED0), is(0));
        assertThat(comp.compare(CMP_NESTED1, CMP_NESTED0), is(0));
        assertThat(comp.compare(CMP_NESTED2, CMP_NESTED0), is(0));

        assertThat(comp.compare(CMP_NESTED0, CMP_NESTED1), is(0));
        assertThat(comp.compare(CMP_NESTED1, CMP_NESTED1), is(0));
        assertThat(comp.compare(CMP_NESTED2, CMP_NESTED1), is(0));

        assertThat(comp.compare(CMP_NESTED0, CMP_NESTED2), is(0));
        assertThat(comp.compare(CMP_NESTED1, CMP_NESTED2), is(0));
        assertThat(comp.compare(CMP_NESTED2, CMP_NESTED2), is(0));

        assertThat(comp.compare(CMP_NESTED0.copy(), CMP_NESTED0.copy()), is(0));
        assertThat(comp.compare(CMP_NESTED1.copy(), CMP_NESTED1.copy()), is(0));
        assertThat(comp.compare(CMP_NESTED2.copy(), CMP_NESTED2.copy()), is(0));
    }

    @Test
    public void testCompoundNestedFilteredMatchWildcard() throws NbtParseException {
        TagComparator comp = new TagComparator(NbtPath
                .parse("[\"another thing\"][\"and another thing\"]*").asNavigation());

        assertThat(comp.compare(CMP_NESTED0, CMP_NESTED0), is(0));
        assertThat(comp.compare(CMP_NESTED1, CMP_NESTED0), is(0));
        assertThat(comp.compare(CMP_NESTED2, CMP_NESTED0), is(0));

        assertThat(comp.compare(CMP_NESTED0, CMP_NESTED1), is(0));
        assertThat(comp.compare(CMP_NESTED1, CMP_NESTED1), is(0));
        assertThat(comp.compare(CMP_NESTED2, CMP_NESTED1), is(0));

        assertThat(comp.compare(CMP_NESTED0, CMP_NESTED2), is(0));
        assertThat(comp.compare(CMP_NESTED1, CMP_NESTED2), is(0));
        assertThat(comp.compare(CMP_NESTED2, CMP_NESTED2), is(0));

        assertThat(comp.compare(CMP_NESTED0.copy(), CMP_NESTED0.copy()), is(0));
        assertThat(comp.compare(CMP_NESTED1.copy(), CMP_NESTED1.copy()), is(0));
        assertThat(comp.compare(CMP_NESTED2.copy(), CMP_NESTED2.copy()), is(0));
    }

    @Test
    public void testCompoundNestedFilteredNoMatch() throws NbtParseException {
        TagComparator comp = new TagComparator(NbtPath
                .parse("[\"another thing\"][\"and another thing\"].noMatch").asNavigation());

        assertThat(comp.compare(CMP_NESTED0, CMP_NESTED0), is(0));
        assertThat(comp.compare(CMP_NESTED1, CMP_NESTED0), is(1));
        assertThat(comp.compare(CMP_NESTED2, CMP_NESTED0), is(1));

        assertThat(comp.compare(CMP_NESTED0, CMP_NESTED1), is(-1));
        assertThat(comp.compare(CMP_NESTED1, CMP_NESTED1), is(0));
        assertThat(comp.compare(CMP_NESTED2, CMP_NESTED1), is(1));

        assertThat(comp.compare(CMP_NESTED0, CMP_NESTED2), is(-1));
        assertThat(comp.compare(CMP_NESTED1, CMP_NESTED2), is(-1));
        assertThat(comp.compare(CMP_NESTED2, CMP_NESTED2), is(0));

        assertThat(comp.compare(CMP_NESTED0.copy(), CMP_NESTED0.copy()), is(0));
        assertThat(comp.compare(CMP_NESTED1.copy(), CMP_NESTED1.copy()), is(0));
        assertThat(comp.compare(CMP_NESTED2.copy(), CMP_NESTED2.copy()), is(0));
    }

    @Test
    public void testMixed() {
        assertThat(COMP.compare(E, E), is(0));
        assertThat(COMP.compare(E, B_0), is(-1));
        assertThat(COMP.compare(E, S_0), is(-2));
        assertThat(COMP.compare(E, I_0), is(-3));
        assertThat(COMP.compare(E, L_0), is(-4));
        assertThat(COMP.compare(E, F_0), is(-5));
        assertThat(COMP.compare(E, D_0), is(-6));
        assertThat(COMP.compare(E, BA_0), is(-7));
        assertThat(COMP.compare(E, STR_0), is(-8));
        assertThat(COMP.compare(E, LST_I_), is(-9));
        assertThat(COMP.compare(E, LST_I_0), is(-9));
        assertThat(COMP.compare(E, CMP_), is(-10));
        assertThat(COMP.compare(E, CMP_AB), is(-10));
        assertThat(COMP.compare(E, IA_0), is(-11));
        assertThat(COMP.compare(E, LA_0), is(-12));
        assertThat(COMP.compare(E, UNKNOWN0), is(-20));

        assertThat(COMP.compare(B_0, E), is(1));
        assertThat(COMP.compare(B_0, B_0), is(0));
        assertThat(COMP.compare(B_0, S_0), is(-1));
        assertThat(COMP.compare(B_0, I_0), is(-2));
        assertThat(COMP.compare(B_0, L_0), is(-3));
        assertThat(COMP.compare(B_0, F_0), is(-4));
        assertThat(COMP.compare(B_0, D_0), is(-5));
        assertThat(COMP.compare(B_0, BA_0), is(-6));
        assertThat(COMP.compare(B_0, STR_0), is(-7));
        assertThat(COMP.compare(B_0, LST_I_), is(-8));
        assertThat(COMP.compare(B_0, LST_I_0), is(-8));
        assertThat(COMP.compare(B_0, CMP_), is(-9));
        assertThat(COMP.compare(B_0, CMP_AB), is(-9));
        assertThat(COMP.compare(B_0, IA_0), is(-10));
        assertThat(COMP.compare(B_0, LA_0), is(-11));
        assertThat(COMP.compare(B_0, UNKNOWN0), is(-19));

        assertThat(COMP.compare(S_0, E), is(2));
        assertThat(COMP.compare(S_0, B_0), is(1));
        assertThat(COMP.compare(S_0, S_0), is(0));
        assertThat(COMP.compare(S_0, I_0), is(-1));
        assertThat(COMP.compare(S_0, L_0), is(-2));
        assertThat(COMP.compare(S_0, F_0), is(-3));
        assertThat(COMP.compare(S_0, D_0), is(-4));
        assertThat(COMP.compare(S_0, BA_0), is(-5));
        assertThat(COMP.compare(S_0, STR_0), is(-6));
        assertThat(COMP.compare(S_0, LST_I_), is(-7));
        assertThat(COMP.compare(S_0, LST_I_0), is(-7));
        assertThat(COMP.compare(S_0, CMP_), is(-8));
        assertThat(COMP.compare(S_0, CMP_AB), is(-8));
        assertThat(COMP.compare(S_0, IA_0), is(-9));
        assertThat(COMP.compare(S_0, LA_0), is(-10));
        assertThat(COMP.compare(S_0, UNKNOWN0), is(-18));

        assertThat(COMP.compare(I_0, E), is(3));
        assertThat(COMP.compare(I_0, B_0), is(2));
        assertThat(COMP.compare(I_0, S_0), is(1));
        assertThat(COMP.compare(I_0, I_0), is(0));
        assertThat(COMP.compare(I_0, L_0), is(-1));
        assertThat(COMP.compare(I_0, F_0), is(-2));
        assertThat(COMP.compare(I_0, D_0), is(-3));
        assertThat(COMP.compare(I_0, BA_0), is(-4));
        assertThat(COMP.compare(I_0, STR_0), is(-5));
        assertThat(COMP.compare(I_0, LST_I_), is(-6));
        assertThat(COMP.compare(I_0, LST_I_0), is(-6));
        assertThat(COMP.compare(I_0, CMP_), is(-7));
        assertThat(COMP.compare(I_0, CMP_AB), is(-7));
        assertThat(COMP.compare(I_0, IA_0), is(-8));
        assertThat(COMP.compare(I_0, LA_0), is(-9));
        assertThat(COMP.compare(I_0, UNKNOWN0), is(-17));

        assertThat(COMP.compare(L_0, E), is(4));
        assertThat(COMP.compare(L_0, B_0), is(3));
        assertThat(COMP.compare(L_0, S_0), is(2));
        assertThat(COMP.compare(L_0, I_0), is(1));
        assertThat(COMP.compare(L_0, L_0), is(0));
        assertThat(COMP.compare(L_0, F_0), is(-1));
        assertThat(COMP.compare(L_0, D_0), is(-2));
        assertThat(COMP.compare(L_0, BA_0), is(-3));
        assertThat(COMP.compare(L_0, STR_0), is(-4));
        assertThat(COMP.compare(L_0, LST_I_), is(-5));
        assertThat(COMP.compare(L_0, LST_I_0), is(-5));
        assertThat(COMP.compare(L_0, CMP_), is(-6));
        assertThat(COMP.compare(L_0, CMP_AB), is(-6));
        assertThat(COMP.compare(L_0, IA_0), is(-7));
        assertThat(COMP.compare(L_0, LA_0), is(-8));
        assertThat(COMP.compare(L_0, UNKNOWN0), is(-16));

        assertThat(COMP.compare(F_0, E), is(5));
        assertThat(COMP.compare(F_0, B_0), is(4));
        assertThat(COMP.compare(F_0, S_0), is(3));
        assertThat(COMP.compare(F_0, I_0), is(2));
        assertThat(COMP.compare(F_0, L_0), is(1));
        assertThat(COMP.compare(F_0, F_0), is(0));
        assertThat(COMP.compare(F_0, D_0), is(-1));
        assertThat(COMP.compare(F_0, BA_0), is(-2));
        assertThat(COMP.compare(F_0, STR_0), is(-3));
        assertThat(COMP.compare(F_0, LST_I_), is(-4));
        assertThat(COMP.compare(F_0, LST_I_0), is(-4));
        assertThat(COMP.compare(F_0, CMP_), is(-5));
        assertThat(COMP.compare(F_0, CMP_AB), is(-5));
        assertThat(COMP.compare(F_0, IA_0), is(-6));
        assertThat(COMP.compare(F_0, LA_0), is(-7));
        assertThat(COMP.compare(F_0, UNKNOWN0), is(-15));

        assertThat(COMP.compare(D_0, E), is(6));
        assertThat(COMP.compare(D_0, B_0), is(5));
        assertThat(COMP.compare(D_0, S_0), is(4));
        assertThat(COMP.compare(D_0, I_0), is(3));
        assertThat(COMP.compare(D_0, L_0), is(2));
        assertThat(COMP.compare(D_0, F_0), is(1));
        assertThat(COMP.compare(D_0, D_0), is(0));
        assertThat(COMP.compare(D_0, BA_0), is(-1));
        assertThat(COMP.compare(D_0, STR_0), is(-2));
        assertThat(COMP.compare(D_0, LST_I_), is(-3));
        assertThat(COMP.compare(D_0, LST_I_0), is(-3));
        assertThat(COMP.compare(D_0, CMP_), is(-4));
        assertThat(COMP.compare(D_0, CMP_AB), is(-4));
        assertThat(COMP.compare(D_0, IA_0), is(-5));
        assertThat(COMP.compare(D_0, LA_0), is(-6));
        assertThat(COMP.compare(D_0, UNKNOWN0), is(-14));

        assertThat(COMP.compare(BA_0, E), is(7));
        assertThat(COMP.compare(BA_0, B_0), is(6));
        assertThat(COMP.compare(BA_0, S_0), is(5));
        assertThat(COMP.compare(BA_0, I_0), is(4));
        assertThat(COMP.compare(BA_0, L_0), is(3));
        assertThat(COMP.compare(BA_0, F_0), is(2));
        assertThat(COMP.compare(BA_0, D_0), is(1));
        assertThat(COMP.compare(BA_0, BA_0), is(0));
        assertThat(COMP.compare(BA_0, STR_0), is(-1));
        assertThat(COMP.compare(BA_0, LST_I_), is(-2));
        assertThat(COMP.compare(BA_0, LST_I_0), is(-2));
        assertThat(COMP.compare(BA_0, CMP_), is(-3));
        assertThat(COMP.compare(BA_0, CMP_AB), is(-3));
        assertThat(COMP.compare(BA_0, IA_0), is(-4));
        assertThat(COMP.compare(BA_0, LA_0), is(-5));
        assertThat(COMP.compare(BA_0, UNKNOWN0), is(-13));

        assertThat(COMP.compare(STR_0, E), is(8));
        assertThat(COMP.compare(STR_0, B_0), is(7));
        assertThat(COMP.compare(STR_0, S_0), is(6));
        assertThat(COMP.compare(STR_0, I_0), is(5));
        assertThat(COMP.compare(STR_0, L_0), is(4));
        assertThat(COMP.compare(STR_0, F_0), is(3));
        assertThat(COMP.compare(STR_0, D_0), is(2));
        assertThat(COMP.compare(STR_0, BA_0), is(1));
        assertThat(COMP.compare(STR_0, STR_0), is(0));
        assertThat(COMP.compare(STR_0, LST_I_), is(-1));
        assertThat(COMP.compare(STR_0, LST_I_0), is(-1));
        assertThat(COMP.compare(STR_0, CMP_), is(-2));
        assertThat(COMP.compare(STR_0, CMP_AB), is(-2));
        assertThat(COMP.compare(STR_0, IA_0), is(-3));
        assertThat(COMP.compare(STR_0, LA_0), is(-4));
        assertThat(COMP.compare(STR_0, UNKNOWN0), is(-12));

        assertThat(COMP.compare(LST_I_, E), is(9));
        assertThat(COMP.compare(LST_I_, B_0), is(8));
        assertThat(COMP.compare(LST_I_, S_0), is(7));
        assertThat(COMP.compare(LST_I_, I_0), is(6));
        assertThat(COMP.compare(LST_I_, L_0), is(5));
        assertThat(COMP.compare(LST_I_, F_0), is(4));
        assertThat(COMP.compare(LST_I_, D_0), is(3));
        assertThat(COMP.compare(LST_I_, BA_0), is(2));
        assertThat(COMP.compare(LST_I_, STR_0), is(1));
        assertThat(COMP.compare(LST_I_, LST_I_), is(0));
        assertThat(COMP.compare(LST_I_, LST_I_0), is(-3));
        assertThat(COMP.compare(LST_I_, CMP_), is(-1));
        assertThat(COMP.compare(LST_I_, CMP_AB), is(-1));
        assertThat(COMP.compare(LST_I_, IA_0), is(-2));
        assertThat(COMP.compare(LST_I_, LA_0), is(-3));
        assertThat(COMP.compare(LST_I_, UNKNOWN0), is(-11));

        assertThat(COMP.compare(LST_I_0, E), is(9));
        assertThat(COMP.compare(LST_I_0, B_0), is(8));
        assertThat(COMP.compare(LST_I_0, S_0), is(7));
        assertThat(COMP.compare(LST_I_0, I_0), is(6));
        assertThat(COMP.compare(LST_I_0, L_0), is(5));
        assertThat(COMP.compare(LST_I_0, F_0), is(4));
        assertThat(COMP.compare(LST_I_0, D_0), is(3));
        assertThat(COMP.compare(LST_I_0, BA_0), is(2));
        assertThat(COMP.compare(LST_I_0, STR_0), is(1));
        assertThat(COMP.compare(LST_I_0, LST_I_), is(3));
        assertThat(COMP.compare(LST_I_0, LST_I_0), is(0));
        assertThat(COMP.compare(LST_I_0, CMP_), is(-1));
        assertThat(COMP.compare(LST_I_0, CMP_AB), is(-1));
        assertThat(COMP.compare(LST_I_0, IA_0), is(-2));
        assertThat(COMP.compare(LST_I_0, LA_0), is(-3));
        assertThat(COMP.compare(LST_I_0, UNKNOWN0), is(-11));

        assertThat(COMP.compare(CMP_, E), is(10));
        assertThat(COMP.compare(CMP_, B_0), is(9));
        assertThat(COMP.compare(CMP_, S_0), is(8));
        assertThat(COMP.compare(CMP_, I_0), is(7));
        assertThat(COMP.compare(CMP_, L_0), is(6));
        assertThat(COMP.compare(CMP_, F_0), is(5));
        assertThat(COMP.compare(CMP_, D_0), is(4));
        assertThat(COMP.compare(CMP_, BA_0), is(3));
        assertThat(COMP.compare(CMP_, STR_0), is(2));
        assertThat(COMP.compare(CMP_, LST_I_), is(1));
        assertThat(COMP.compare(CMP_, LST_I_0), is(1));
        assertThat(COMP.compare(CMP_, CMP_), is(0));
        assertThat(COMP.compare(CMP_, CMP_AB), is(-1));
        assertThat(COMP.compare(CMP_, IA_0), is(-1));
        assertThat(COMP.compare(CMP_, LA_0), is(-2));
        assertThat(COMP.compare(CMP_, UNKNOWN0), is(-10));

        assertThat(COMP.compare(CMP_AB, E), is(10));
        assertThat(COMP.compare(CMP_AB, B_0), is(9));
        assertThat(COMP.compare(CMP_AB, S_0), is(8));
        assertThat(COMP.compare(CMP_AB, I_0), is(7));
        assertThat(COMP.compare(CMP_AB, L_0), is(6));
        assertThat(COMP.compare(CMP_AB, F_0), is(5));
        assertThat(COMP.compare(CMP_AB, D_0), is(4));
        assertThat(COMP.compare(CMP_AB, BA_0), is(3));
        assertThat(COMP.compare(CMP_AB, STR_0), is(2));
        assertThat(COMP.compare(CMP_AB, LST_I_), is(1));
        assertThat(COMP.compare(CMP_AB, LST_I_0), is(1));
        assertThat(COMP.compare(CMP_AB, CMP_), is(1));
        assertThat(COMP.compare(CMP_AB, CMP_AB), is(0));
        assertThat(COMP.compare(CMP_AB, IA_0), is(-1));
        assertThat(COMP.compare(CMP_AB, LA_0), is(-2));
        assertThat(COMP.compare(CMP_AB, UNKNOWN0), is(-10));

        assertThat(COMP.compare(IA_0, E), is(11));
        assertThat(COMP.compare(IA_0, B_0), is(10));
        assertThat(COMP.compare(IA_0, S_0), is(9));
        assertThat(COMP.compare(IA_0, I_0), is(8));
        assertThat(COMP.compare(IA_0, L_0), is(7));
        assertThat(COMP.compare(IA_0, F_0), is(6));
        assertThat(COMP.compare(IA_0, D_0), is(5));
        assertThat(COMP.compare(IA_0, BA_0), is(4));
        assertThat(COMP.compare(IA_0, STR_0), is(3));
        assertThat(COMP.compare(IA_0, LST_I_), is(2));
        assertThat(COMP.compare(IA_0, LST_I_0), is(2));
        assertThat(COMP.compare(IA_0, CMP_), is(1));
        assertThat(COMP.compare(IA_0, CMP_AB), is(1));
        assertThat(COMP.compare(IA_0, IA_0), is(0));
        assertThat(COMP.compare(IA_0, LA_0), is(-1));
        assertThat(COMP.compare(IA_0, UNKNOWN0), is(-9));

        assertThat(COMP.compare(LA_0, E), is(12));
        assertThat(COMP.compare(LA_0, B_0), is(11));
        assertThat(COMP.compare(LA_0, S_0), is(10));
        assertThat(COMP.compare(LA_0, I_0), is(9));
        assertThat(COMP.compare(LA_0, L_0), is(8));
        assertThat(COMP.compare(LA_0, F_0), is(7));
        assertThat(COMP.compare(LA_0, D_0), is(6));
        assertThat(COMP.compare(LA_0, BA_0), is(5));
        assertThat(COMP.compare(LA_0, STR_0), is(4));
        assertThat(COMP.compare(LA_0, LST_I_), is(3));
        assertThat(COMP.compare(LA_0, LST_I_0), is(3));
        assertThat(COMP.compare(LA_0, CMP_), is(2));
        assertThat(COMP.compare(LA_0, CMP_AB), is(2));
        assertThat(COMP.compare(LA_0, IA_0), is(1));
        assertThat(COMP.compare(LA_0, LA_0), is(0));
        assertThat(COMP.compare(LA_0, UNKNOWN0), is(-8));
    }

    public static class TagUnknown implements Tag {
        @Override
        public void write(DataOutput output) throws IOException {

        }

        @Override
        public byte getId() {
            return 20;
        }

        @Override
        public TagType<?> getType() {
            return null;
        }

        @Override
        public Tag copy() {
            return null;
        }

        @Override
        public int sizeInBytes() {
            return 0;
        }

        @Override
        public void accept(TagVisitor p_178208_) {

        }

        @Override
        public StreamTagVisitor.ValueResult accept(StreamTagVisitor p_197572_) {
            return null;
        }
    }

}
