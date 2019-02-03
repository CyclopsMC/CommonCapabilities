package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.nbt.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestNBTBaseComparator {

    private static NBTBaseComparator COMP;

    private static NBTTagEnd E;

    private static NBTTagByte B_0;
    private static NBTTagByte B_1;
    private static NBTTagByte B_2;

    private static NBTTagShort S_0;
    private static NBTTagShort S_1;
    private static NBTTagShort S_2;

    private static NBTTagInt I_0;
    private static NBTTagInt I_1;
    private static NBTTagInt I_2;

    private static NBTTagLong L_0;
    private static NBTTagLong L_1;
    private static NBTTagLong L_2;

    private static NBTTagFloat F_0;
    private static NBTTagFloat F_1;
    private static NBTTagFloat F_2;

    private static NBTTagDouble D_0;
    private static NBTTagDouble D_1;
    private static NBTTagDouble D_2;

    private static NBTTagByteArray BA_0;
    private static NBTTagByteArray BA_1;
    private static NBTTagByteArray BA_2;

    private static NBTTagString STR_0;
    private static NBTTagString STR_1;
    private static NBTTagString STR_2;
    private static NBTTagString STR_X;

    private static NBTTagList LST_B_012;
    private static NBTTagList LST_I_;
    private static NBTTagList LST_I_0;
    private static NBTTagList LST_I_1;
    private static NBTTagList LST_I_01;
    private static NBTTagList LST_I_012;
    private static NBTTagList LST_STR_012;

    private static NBTTagCompound CMP_;
    private static NBTTagCompound CMP_AB;
    private static NBTTagCompound CMP_AI;
    private static NBTTagCompound CMP_AI_BI;
    private static NBTTagCompound CMP_AB_BB;
    private static NBTTagCompound CMP_AI_BB;
    private static NBTTagCompound CMP_AB_BI;
    private static NBTTagCompound CMP_AB_CI;

    private static NBTTagIntArray IA_0;
    private static NBTTagIntArray IA_1;
    private static NBTTagIntArray IA_2;

    private static NBTTagLongArray LA_0;
    private static NBTTagLongArray LA_1;
    private static NBTTagLongArray LA_2;

    private static NBTUnknown UNKNOWN0;
    private static NBTUnknown UNKNOWN1;

    private static NBTTagCompound CMP_NESTED0;
    private static NBTTagCompound CMP_NESTED1;
    private static NBTTagCompound CMP_NESTED2;

    @BeforeClass
    public static void init() {
        COMP = new NBTBaseComparator();

        E = (NBTTagEnd) new NBTTagList().get(-1);

        B_0 = new NBTTagByte((byte) 0);
        B_1 = new NBTTagByte((byte) 1);
        B_2 = new NBTTagByte((byte) 2);

        S_0 = new NBTTagShort((short) 0);
        S_1 = new NBTTagShort((short) 1);
        S_2 = new NBTTagShort((short) 2);

        I_0 = new NBTTagInt(0);
        I_1 = new NBTTagInt(1);
        I_2 = new NBTTagInt(2);

        L_0 = new NBTTagLong(0);
        L_1 = new NBTTagLong(1);
        L_2 = new NBTTagLong(2);

        F_0 = new NBTTagFloat(0);
        F_1 = new NBTTagFloat(1);
        F_2 = new NBTTagFloat(2);

        D_0 = new NBTTagDouble(0);
        D_1 = new NBTTagDouble(1);
        D_2 = new NBTTagDouble(2);

        BA_0 = new NBTTagByteArray(new byte[]{0});
        BA_1 = new NBTTagByteArray(new byte[]{1});
        BA_2 = new NBTTagByteArray(new byte[]{2});

        STR_0 = new NBTTagString("0");
        STR_1 = new NBTTagString("1");
        STR_2 = new NBTTagString("2");
        STR_X = new NBTTagString("X");

        LST_B_012 = new NBTTagList();
        LST_B_012.appendTag(B_0);
        LST_B_012.appendTag(B_1);
        LST_B_012.appendTag(B_2);
        LST_I_ = new NBTTagList();
        LST_I_0 = new NBTTagList();
        LST_I_0.appendTag(I_0);
        LST_I_1 = new NBTTagList();
        LST_I_1.appendTag(I_1);
        LST_I_01 = new NBTTagList();
        LST_I_01.appendTag(I_0);
        LST_I_01.appendTag(I_1);
        LST_I_012 = new NBTTagList();
        LST_I_012.appendTag(I_0);
        LST_I_012.appendTag(I_1);
        LST_I_012.appendTag(I_2);
        LST_STR_012 = new NBTTagList();
        LST_STR_012.appendTag(STR_0);
        LST_STR_012.appendTag(STR_1);
        LST_STR_012.appendTag(STR_2);

        CMP_ = new NBTTagCompound();
        CMP_AB = new NBTTagCompound();
        CMP_AB.setTag("A", B_0);
        CMP_AI = new NBTTagCompound();
        CMP_AI.setTag("A", I_0);
        CMP_AI_BI = new NBTTagCompound();
        CMP_AI_BI.setTag("A", I_0);
        CMP_AI_BI.setTag("B", I_0);
        CMP_AB_BB = new NBTTagCompound();
        CMP_AB_BB.setTag("A", B_0);
        CMP_AB_BB.setTag("B", B_0);
        CMP_AI_BB = new NBTTagCompound();
        CMP_AI_BB.setTag("A", I_0);
        CMP_AI_BB.setTag("B", B_0);
        CMP_AB_BI = new NBTTagCompound();
        CMP_AB_BI.setTag("A", B_0);
        CMP_AB_BI.setTag("B", I_0);
        CMP_AB_CI = new NBTTagCompound();
        CMP_AB_CI.setTag("A", B_0);
        CMP_AB_CI.setTag("C", I_0);

        IA_0 = new NBTTagIntArray(new int[]{0});
        IA_1 = new NBTTagIntArray(new int[]{1});
        IA_2 = new NBTTagIntArray(new int[]{2});

        LA_0 = new NBTTagLongArray(new long[]{0});
        LA_1 = new NBTTagLongArray(new long[]{1});
        LA_2 = new NBTTagLongArray(new long[]{2});

        UNKNOWN0 = new NBTUnknown();
        UNKNOWN1 = new NBTUnknown();

        CMP_NESTED0 = new NBTTagCompound();
        CMP_NESTED0.setString("a", "b");
        CMP_NESTED0.setBoolean("some boolean", true);
        NBTTagCompound subTag = new NBTTagCompound();
        subTag.setDouble("double", 4);
        subTag.setInteger("int", 268940);
        NBTTagCompound subSubTag = new NBTTagCompound();
        subSubTag.setInteger("value", 100);
        subSubTag.setBoolean("flag", true);
        subTag.setTag("and another thing", subSubTag);
        CMP_NESTED0.setTag("another thing", subTag);

        CMP_NESTED1 = new NBTTagCompound();
        CMP_NESTED1.setString("a", "b");
        CMP_NESTED1.setBoolean("some boolean", true);
        NBTTagCompound subTag1 = new NBTTagCompound();
        subTag1.setDouble("double", 4);
        subTag1.setInteger("int", 268940);
        NBTTagCompound subSubTag1 = new NBTTagCompound();
        subSubTag1.setInteger("value", 101);
        subSubTag1.setBoolean("flag", true);
        subTag1.setTag("and another thing", subSubTag1);
        CMP_NESTED1.setTag("another thing", subTag1);

        CMP_NESTED2 = new NBTTagCompound();
        CMP_NESTED2.setString("a", "b");
        CMP_NESTED2.setBoolean("some boolean", true);
        NBTTagCompound subTag2 = new NBTTagCompound();
        subTag2.setDouble("double", 4);
        subTag2.setInteger("int", 268940);
        NBTTagCompound subSubTag2 = new NBTTagCompound();
        subSubTag2.setInteger("value", 102);
        subSubTag2.setBoolean("flag", true);
        subTag2.setTag("and another thing", subSubTag2);
        CMP_NESTED2.setTag("another thing", subTag2);
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

    public static class NBTUnknown extends NBTTagInt {
        public NBTUnknown() {
            super(0);
        }

        @Override
        public byte getId() {
            return 20;
        }
    }

}
