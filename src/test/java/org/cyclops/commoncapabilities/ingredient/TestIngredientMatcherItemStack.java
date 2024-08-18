package org.cyclops.commoncapabilities.ingredient;

import com.google.common.collect.Sets;
import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.cyclops.commoncapabilities.ModBaseMocked;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;
import org.cyclops.cyclopscore.nbt.path.NbtParseException;
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
        CyclopsCoreInstance.MOD = new ModBaseMocked();

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
        W_1_T1.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        W_2_T1 = new ItemStack(Items.WHITE_WOOL, 2);
        W_2_T1.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        W_1_T2 = new ItemStack(Items.WHITE_WOOL, 1);
        W_1_T2.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
        W_2_T2 = new ItemStack(Items.WHITE_WOOL, 2);
        W_2_T2.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);

        L_1 = new ItemStack(Items.LEAD, 1);
        L_1_T1 = new ItemStack(Items.LEAD, 1);
        L_1_T1.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);

        B_1 = new ItemStack(Items.BLACK_WOOL, 1);
        B_1_T3 = new ItemStack(Items.BLACK_WOOL, 1);
        B_1_T3.set(DataComponents.DAMAGE, 3);
        B_1_T4 = new ItemStack(Items.BLACK_WOOL, 1);
        B_1_T4.set(DataComponents.DAMAGE, 4);

        ItemMatch.DATA_COMPARATOR = DataComparator.INSTANCE = new DataComparator(Sets.newHashSet(
                BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(DataComponents.DAMAGE)
        ));
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
        assertThat(M.getExactMatchNoQuantityCondition(), is(ItemMatch.ITEM | ItemMatch.DATA));
    }

    @Test
    public void testWithCondition() {
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ANY), is(ItemMatch.ANY));
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), is(ItemMatch.ITEM));
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.DATA), is(ItemMatch.DATA));
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.STACKSIZE), is(ItemMatch.STACKSIZE));

        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.DATA), is(ItemMatch.ITEM | ItemMatch.DATA));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.DATA), ItemMatch.STACKSIZE), is(ItemMatch.DATA | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.STACKSIZE), ItemMatch.DATA), is(ItemMatch.DATA | ItemMatch.STACKSIZE));

        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.DATA), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.DATA | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), ItemMatch.DATA), is(ItemMatch.ITEM | ItemMatch.DATA | ItemMatch.STACKSIZE));

        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.EXACT), is(ItemMatch.EXACT));
    }

    @Test
    public void testWithoutCondition() {
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ANY), is(ItemMatch.EXACT));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), is(ItemMatch.STACKSIZE | ItemMatch.DATA));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.DATA), is(ItemMatch.ITEM | ItemMatch.STACKSIZE));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.DATA));

        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.DATA), ItemMatch.STACKSIZE), is(ItemMatch.ITEM));
        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.STACKSIZE), ItemMatch.DATA), is(ItemMatch.ITEM));

        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.DATA), ItemMatch.STACKSIZE), is(ItemMatch.ANY));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), ItemMatch.DATA), is(ItemMatch.ANY));

        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.EXACT), is(ItemMatch.ANY));
    }

    @Test
    public void testHasCondition() {
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.ANY), is(false));
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.DATA), is(true));
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.STACKSIZE), is(true));
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.ITEM), is(true));

        assertThat(M.hasCondition(M.getAnyMatchCondition(), ItemMatch.ANY), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), ItemMatch.DATA), is(false));
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

        assertThat(M.matches(W_1, W_1, ItemMatch.DATA), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.DATA), is(true));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, L_1, ItemMatch.DATA), is(true));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.DATA), is(false));

        assertThat(M.matches(W_1, W_1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_1, L_1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));

        assertThat(M.matches(W_1, W_1, ItemMatch.ITEM | ItemMatch.DATA), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.ITEM | ItemMatch.DATA), is(true));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.ITEM | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.ITEM | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.ITEM | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.ITEM | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, L_1, ItemMatch.ITEM | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.ITEM | ItemMatch.DATA), is(false));

        assertThat(M.matches(W_1, W_1, ItemMatch.STACKSIZE | ItemMatch.DATA), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, L_1, ItemMatch.STACKSIZE | ItemMatch.DATA), is(true));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));

        assertThat(M.matches(W_1, W_1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DATA), is(true));
        assertThat(M.matches(W_1, W_2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_1_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, W_2_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, L_1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));
        assertThat(M.matches(W_1, L_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DATA), is(false));
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
        assertThat(M.compare(W_1, W_1_T1), is(20));
        assertThat(M.compare(W_1, W_1_T2), is(20));
        assertThat(M.compare(W_1, L_1), is(-926));
        assertThat(M.compare(W_1, L_1_T1), is(-926));

        assertThat(M.compare(W_1_T1, W_1), is(-20));
        assertThat(M.compare(W_1_T1, W_2), is(-1));
        assertThat(M.compare(W_1_T1, W_2_T1), is(-1));
        assertThat(M.compare(W_1_T1, W_2_T2), is(-1));
        assertThat(M.compare(W_1_T1, W_1_T1), is(0));
        assertThat(M.compare(W_1_T1, W_1_T2), is(1));
        assertThat(M.compare(W_1_T1, L_1), is(-926));
        assertThat(M.compare(W_1_T1, L_1_T1), is(-926));

        assertThat(M.compare(W_1_T2, W_1), is(-20));
        assertThat(M.compare(W_1_T2, W_2), is(-1));
        assertThat(M.compare(W_1_T2, W_2_T1), is(-1));
        assertThat(M.compare(W_1_T2, W_2_T2), is(-1));
        assertThat(M.compare(W_1_T2, W_1_T1), is(-1));
        assertThat(M.compare(W_1_T2, W_1_T2), is(0));
        assertThat(M.compare(W_1_T2, L_1), is(-926));
        assertThat(M.compare(W_1_T2, L_1_T1), is(-926));

        assertThat(M.compare(W_2, W_1), is(1));
        assertThat(M.compare(W_2, W_1_T1), is(1));
        assertThat(M.compare(W_2, W_1_T2), is(1));
        assertThat(M.compare(W_2, W_2), is(0));
        assertThat(M.compare(W_2, W_2_T1), is(20));
        assertThat(M.compare(W_2, W_2_T2), is(20));
        assertThat(M.compare(W_2, L_1), is(-926));
        assertThat(M.compare(W_2, L_1_T1), is(-926));

        assertThat(M.compare(W_2_T1, W_1), is(1));
        assertThat(M.compare(W_2_T1, W_1_T1), is(1));
        assertThat(M.compare(W_2_T1, W_1_T2), is(1));
        assertThat(M.compare(W_2_T1, W_2), is(-20));
        assertThat(M.compare(W_2_T1, W_2_T1), is(0));
        assertThat(M.compare(W_2_T1, W_2_T2), is(1));

        assertThat(M.compare(W_2_T2, W_1), is(1));
        assertThat(M.compare(W_2_T2, W_1_T1), is(1));
        assertThat(M.compare(W_2_T2, W_1_T2), is(1));
        assertThat(M.compare(W_2_T2, W_2), is(-20));
        assertThat(M.compare(W_2_T2, W_2_T1), is(-1));
        assertThat(M.compare(W_2_T2, W_2_T2), is(0));
        assertThat(M.compare(W_2_T2, L_1), is(-926));
        assertThat(M.compare(W_2_T2, L_1_T1), is(-926));

        assertThat(M.compare(L_1, W_1), is(926));
        assertThat(M.compare(L_1, W_1_T1), is(926));
        assertThat(M.compare(L_1, W_1_T2), is(926));
        assertThat(M.compare(L_1, W_2), is(926));
        assertThat(M.compare(L_1, W_2_T1), is(926));
        assertThat(M.compare(L_1, W_2_T2), is(926));
        assertThat(M.compare(L_1, L_1), is(0));
        assertThat(M.compare(L_1, L_1_T1), is(20));

        assertThat(M.compare(L_1_T1, W_1), is(926));
        assertThat(M.compare(L_1_T1, W_1_T1), is(926));
        assertThat(M.compare(L_1_T1, W_1_T2), is(926));
        assertThat(M.compare(L_1_T1, W_2), is(926));
        assertThat(M.compare(L_1_T1, W_2_T1), is(926));
        assertThat(M.compare(L_1_T1, W_2_T2), is(926));
        assertThat(M.compare(L_1_T1, L_1), is(-20));
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
        assertThat(M.toString(W_1), is("minecraft:white_wool 1 {minecraft:max_stack_size=>64, minecraft:lore=>ItemLore[lines=[], styledLines=[]], minecraft:enchantments=>ItemEnchantments{enchantments={}, showInTooltip=true}, minecraft:repair_cost=>0, minecraft:attribute_modifiers=>ItemAttributeModifiers[modifiers=[], showInTooltip=true], minecraft:rarity=>COMMON}"));
        assertThat(M.toString(W_1_T1), is("minecraft:white_wool 1 {minecraft:enchantment_glint_override=>true, minecraft:max_stack_size=>64, minecraft:lore=>ItemLore[lines=[], styledLines=[]], minecraft:enchantments=>ItemEnchantments{enchantments={}, showInTooltip=true}, minecraft:repair_cost=>0, minecraft:attribute_modifiers=>ItemAttributeModifiers[modifiers=[], showInTooltip=true], minecraft:rarity=>COMMON}"));
    }

}
