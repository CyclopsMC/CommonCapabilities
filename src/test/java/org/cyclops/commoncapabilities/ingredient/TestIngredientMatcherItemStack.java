package org.cyclops.commoncapabilities.ingredient;

import com.google.common.collect.Lists;
import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.cyclopscore.nbt.path.NbtParseException;
import org.cyclops.cyclopscore.nbt.path.NbtPath;
import org.cyclops.cyclopscore.nbt.path.navigate.NbtPathNavigationList;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientMatcherItemStack {

    private static IngredientMatcherItemStack M;

    private static ItemStack W_1;
    private static ItemStack W_2;
    private static ItemStack W_100;
    private static ItemStack W_123;
    private static ItemStack W_1_T1;
    private static ItemStack W_2_T1;
    private static ItemStack W_1_T2;
    private static ItemStack W_2_T2;

    private static ItemStack L_1;
    private static ItemStack L_1_T1;

    private static ItemStack B_1;
    private static ItemStack B_1_T3;
    private static ItemStack B_1_T4;

    @BeforeClass
    public static void init() throws NbtParseException {
        // We need the Minecraft registries to be filled
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();

        M = new IngredientMatcherItemStack();

        CompoundTag tag1 = new CompoundTag();
        tag1.putInt("key", 1);

        CompoundTag tag2 = new CompoundTag();
        tag2.putInt("key", 2);

        CompoundTag tag3 = new CompoundTag();
        tag3.putInt("ignored", 0);

        CompoundTag tag4 = new CompoundTag();
        tag4.putInt("ignored", 100);

        W_1 = new ItemStack(Items.WHITE_WOOL, 1);
        W_2 = new ItemStack(Items.WHITE_WOOL, 2);
        W_100 = new ItemStack(Items.WHITE_WOOL, 100);
        W_123 = new ItemStack(Items.WHITE_WOOL, 123);
        W_1_T1 = new ItemStack(Items.WHITE_WOOL, 1);
        W_1_T1.setTag(tag1);
        W_2_T1 = new ItemStack(Items.WHITE_WOOL, 2);
        W_2_T1.setTag(tag1);
        W_1_T2 = new ItemStack(Items.WHITE_WOOL, 1);
        W_1_T2.setTag(tag2);
        W_2_T2 = new ItemStack(Items.WHITE_WOOL, 2);
        W_2_T2.setTag(tag2);

        L_1 = new ItemStack(Items.LEAD, 1);
        L_1_T1 = new ItemStack(Items.LEAD, 1);
        L_1_T1.setTag(tag1);

        B_1 = new ItemStack(Items.BLACK_WOOL, 1);
        B_1_T3 = new ItemStack(Items.BLACK_WOOL, 1);
        B_1_T3.setTag(tag3);
        B_1_T4 = new ItemStack(Items.BLACK_WOOL, 1);
        B_1_T4.setTag(tag4);

        ItemMatch.TAG_COMPARATOR = TagComparator.INSTANCE = new TagComparator(new NbtPathNavigationList(Lists.newArrayList(NbtPath.parse("$.ignored").asNavigation())));
    }

    @Test
    public void testInstance() {
        assertThat(M.isInstance("a"), is(false));
        assertThat(M.isInstance(W_1), is(true));
        assertThat(M.isInstance(ItemStack.EMPTY), is(true));
    }

    @Test
    public void testGetAnyMatchCondition() {
        assertThat(M.getAnyMatchCondition(), is(ItemMatch.ANY));
    }

    @Test
    public void testGetExactMatchCondition() {
        assertThat(M.getExactMatchCondition(), is(ItemMatch.EXACT));
    }

    @Test
    public void testGetExactNoQuantityMatchCondition() {
        assertThat(M.getExactMatchNoQuantityCondition(), is(ItemMatch.ITEM | ItemMatch.TAG));
    }

    @Test
    public void testWithCondition() {
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ANY), is(ItemMatch.ANY));
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), is(ItemMatch.ITEM));
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.TAG), is(ItemMatch.TAG));
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.STACKSIZE), is(ItemMatch.STACKSIZE));

        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.TAG), is(ItemMatch.ITEM | ItemMatch.TAG));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.TAG), ItemMatch.STACKSIZE), is(ItemMatch.TAG | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.STACKSIZE), ItemMatch.TAG), is(ItemMatch.TAG | ItemMatch.STACKSIZE));

        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.TAG), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.TAG | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), ItemMatch.TAG), is(ItemMatch.ITEM | ItemMatch.TAG | ItemMatch.STACKSIZE));

        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.EXACT), is(ItemMatch.EXACT));
    }

    @Test
    public void testWithoutCondition() {
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ANY), is(ItemMatch.EXACT));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), is(ItemMatch.STACKSIZE | ItemMatch.TAG));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.TAG), is(ItemMatch.ITEM | ItemMatch.STACKSIZE));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.TAG));

        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.TAG), ItemMatch.STACKSIZE), is(ItemMatch.ITEM));
        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.STACKSIZE), ItemMatch.TAG), is(ItemMatch.ITEM));

        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.TAG), ItemMatch.STACKSIZE), is(ItemMatch.ANY));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), ItemMatch.TAG), is(ItemMatch.ANY));

        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.EXACT), is(ItemMatch.ANY));
    }

    @Test
    public void testHasCondition() {
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.ANY), is(false));
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.TAG), is(true));
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.STACKSIZE), is(true));
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.ITEM), is(true));

        assertThat(M.hasCondition(M.getAnyMatchCondition(), ItemMatch.ANY), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), ItemMatch.TAG), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), ItemMatch.STACKSIZE), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), is(false));
    }

    @Test
    public void testMatches() {
        assertThat(M.matches(ItemStack.EMPTY, ItemStack.EMPTY, ItemMatch.EXACT), is(true));
        assertThat(M.matches(W_1, ItemStack.EMPTY, ItemMatch.EXACT), is(false));
        assertThat(M.matches(ItemStack.EMPTY, W_1, ItemMatch.EXACT), is(false));

        assertThat(M.matches(ItemStack.EMPTY, ItemStack.EMPTY, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_1, ItemStack.EMPTY, ItemMatch.ANY), is(true));
        assertThat(M.matches(ItemStack.EMPTY, W_1, ItemMatch.ANY), is(true));

        assertThat(M.matches(W_1, W_1, ItemMatch.EXACT), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_1, L_1, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.EXACT), is(false));

        assertThat(M.matches(W_1, W_1, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_1, L_1, ItemMatch.ITEM), is(false));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.ITEM), is(false));

        assertThat(M.matches(W_1, W_1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_1, L_1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.ANY), is(true));

        assertThat(M.matches(W_1, W_1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_1, L_1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.STACKSIZE), is(true));

        assertThat(M.matches(W_1, W_1, ItemMatch.TAG), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.TAG), is(true));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, L_1, ItemMatch.TAG), is(true));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.TAG), is(false));

        assertThat(M.matches(W_1, W_1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_1, L_1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));

        assertThat(M.matches(W_1, W_1, ItemMatch.ITEM | ItemMatch.TAG), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.ITEM | ItemMatch.TAG), is(true));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.ITEM | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.ITEM | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.ITEM | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.ITEM | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, L_1, ItemMatch.ITEM | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.ITEM | ItemMatch.TAG), is(false));

        assertThat(M.matches(W_1, W_1, ItemMatch.STACKSIZE | ItemMatch.TAG), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, L_1, ItemMatch.STACKSIZE | ItemMatch.TAG), is(true));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));

        assertThat(M.matches(W_1, W_1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.TAG), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, L_1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.TAG), is(false));
    }

    @Test
    public void testMatchesExactly() {
        assertThat(M.matchesExactly(ItemStack.EMPTY, ItemStack.EMPTY), is(true));
        assertThat(M.matchesExactly(W_1, ItemStack.EMPTY), is(false));
        assertThat(M.matchesExactly(ItemStack.EMPTY, W_1), is(false));

        assertThat(M.matchesExactly(W_1, W_1), is(true));
        assertThat(M.matchesExactly(W_1, W_2), is(false));
        assertThat(M.matchesExactly(W_1, W_1_T1), is(false));
        assertThat(M.matchesExactly(W_1, W_2_T1), is(false));
        assertThat(M.matchesExactly(W_1, W_1_T2), is(false));
        assertThat(M.matchesExactly(W_1, W_2_T2), is(false));
        assertThat(M.matchesExactly(W_1, L_1), is(false));
        assertThat(M.matchesExactly(W_1, L_1_T1), is(false));
    }

    @Test
    public void testMatchesExactlyIgnoredTag() {
        assertThat(M.matchesExactly(B_1, B_1_T3), is(true));
        assertThat(M.matchesExactly(B_1_T3, B_1_T4), is(true));
    }

    @Test
    public void testEmpty() {
        assertThat(M.getEmptyInstance(), is(ItemStack.EMPTY));
    }

    @Test
    public void testIsEmpty() {
        assertThat(M.isEmpty(W_1), is(false));
        assertThat(M.isEmpty(ItemStack.EMPTY), is(true));
    }

    @Test
    public void testHash() {
        assertThat(M.hash(W_1), is(M.hash(W_1.copy())));
        assertThat(M.hash(ItemStack.EMPTY), is(ItemStack.EMPTY.hashCode()));
    }

    @Test
    public void testCopy() {
        assertThat(M.matchesExactly(M.copy(W_1), W_1), is(true));
        assertThat(M.matchesExactly(M.copy(ItemStack.EMPTY), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testCompare() {
        assertThat(M.compare(W_1, ItemStack.EMPTY), is(1));
        assertThat(M.compare(ItemStack.EMPTY, ItemStack.EMPTY), is(0));
        assertThat(M.compare(ItemStack.EMPTY, W_1), is(-1));

        assertThat(M.compare(W_1, W_1), is(0));
        assertThat(M.compare(W_1, W_2), is(-1));
        assertThat(M.compare(W_1, W_2_T1), is(-1));
        assertThat(M.compare(W_1, W_2_T2), is(-1));
        assertThat(M.compare(W_1, W_1_T1), is(-1));
        assertThat(M.compare(W_1, W_1_T2), is(-1));
        assertThat(M.compare(W_1, L_1), is(-902));
        assertThat(M.compare(W_1, L_1_T1), is(-902));

        assertThat(M.compare(W_1_T1, W_1), is(1));
        assertThat(M.compare(W_1_T1, W_2), is(-1));
        assertThat(M.compare(W_1_T1, W_2_T1), is(-1));
        assertThat(M.compare(W_1_T1, W_2_T2), is(-1));
        assertThat(M.compare(W_1_T1, W_1_T1), is(0));
        assertThat(M.compare(W_1_T1, W_1_T2), is(-1));
        assertThat(M.compare(W_1_T1, L_1), is(-902));
        assertThat(M.compare(W_1_T1, L_1_T1), is(-902));

        assertThat(M.compare(W_1_T2, W_1), is(1));
        assertThat(M.compare(W_1_T2, W_2), is(-1));
        assertThat(M.compare(W_1_T2, W_2_T1), is(-1));
        assertThat(M.compare(W_1_T2, W_2_T2), is(-1));
        assertThat(M.compare(W_1_T2, W_1_T1), is(1));
        assertThat(M.compare(W_1_T2, W_1_T2), is(0));
        assertThat(M.compare(W_1_T2, L_1), is(-902));
        assertThat(M.compare(W_1_T2, L_1_T1), is(-902));

        assertThat(M.compare(W_2, W_1), is(1));
        assertThat(M.compare(W_2, W_1_T1), is(1));
        assertThat(M.compare(W_2, W_1_T2), is(1));
        assertThat(M.compare(W_2, W_2), is(0));
        assertThat(M.compare(W_2, W_2_T1), is(-1));
        assertThat(M.compare(W_2, W_2_T2), is(-1));
        assertThat(M.compare(W_2, L_1), is(-902));
        assertThat(M.compare(W_2, L_1_T1), is(-902));

        assertThat(M.compare(W_2_T1, W_1), is(1));
        assertThat(M.compare(W_2_T1, W_1_T1), is(1));
        assertThat(M.compare(W_2_T1, W_1_T2), is(1));
        assertThat(M.compare(W_2_T1, W_2), is(1));
        assertThat(M.compare(W_2_T1, W_2_T1), is(0));
        assertThat(M.compare(W_2_T1, W_2_T2), is(-1));

        assertThat(M.compare(W_2_T2, W_1), is(1));
        assertThat(M.compare(W_2_T2, W_1_T1), is(1));
        assertThat(M.compare(W_2_T2, W_1_T2), is(1));
        assertThat(M.compare(W_2_T2, W_2), is(1));
        assertThat(M.compare(W_2_T2, W_2_T1), is(1));
        assertThat(M.compare(W_2_T2, W_2_T2), is(0));
        assertThat(M.compare(W_2_T2, L_1), is(-902));
        assertThat(M.compare(W_2_T2, L_1_T1), is(-902));

        assertThat(M.compare(L_1, W_1), is(902));
        assertThat(M.compare(L_1, W_1_T1), is(902));
        assertThat(M.compare(L_1, W_1_T2), is(902));
        assertThat(M.compare(L_1, W_2), is(902));
        assertThat(M.compare(L_1, W_2_T1), is(902));
        assertThat(M.compare(L_1, W_2_T2), is(902));
        assertThat(M.compare(L_1, L_1), is(0));
        assertThat(M.compare(L_1, L_1_T1), is(-1));

        assertThat(M.compare(L_1_T1, W_1), is(902));
        assertThat(M.compare(L_1_T1, W_1_T1), is(902));
        assertThat(M.compare(L_1_T1, W_1_T2), is(902));
        assertThat(M.compare(L_1_T1, W_2), is(902));
        assertThat(M.compare(L_1_T1, W_2_T1), is(902));
        assertThat(M.compare(L_1_T1, W_2_T2), is(902));
        assertThat(M.compare(L_1_T1, L_1), is(1));
        assertThat(M.compare(L_1_T1, L_1_T1), is(0));
    }

    @Test
    public void testGetQuantity() {
        assertThat(M.getQuantity(W_1), is(1L));
        assertThat(M.getQuantity(W_2), is(2L));
    }

    @Test
    public void testSetQuantity() {
        assertThat(M.matchesExactly(M.withQuantity(W_1, 100L), W_100), is(true));
        assertThat(M.matchesExactly(M.withQuantity(W_1, 123L), W_123), is(true));
    }

    @Test
    public void testGetMaximumQuantity() {
        assertThat(M.getMaximumQuantity(), is((long) Integer.MAX_VALUE));
    }

    @Test
    public void testConditionCompare() {
        assertThat(M.conditionCompare(0, 0), is(0));
        assertThat(M.conditionCompare(10, 2), is(1));
        assertThat(M.conditionCompare(2, 10), is(-1));
    }

    @Test
    public void testToString() {
        assertThat(M.toString(W_1), is("minecraft:white_wool 1 null"));
        assertThat(M.toString(W_1_T1), is("minecraft:white_wool 1 {key:1}"));
    }

}
