package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestIngredientMatcherItemStack {

    private static IngredientMatcherItemStack M;

    private static ItemStack W_0_1;
    private static ItemStack W_0_2;
    private static ItemStack W_0_1_T1;
    private static ItemStack W_0_2_T1;
    private static ItemStack W_0_1_T2;
    private static ItemStack W_0_2_T2;

    private static ItemStack W_1_1;
    private static ItemStack W_1_2;
    private static ItemStack W_1_1_T1;
    private static ItemStack W_1_2_T1;
    private static ItemStack W_1_1_T2;
    private static ItemStack W_1_2_T2;

    private static ItemStack L_0_1;
    private static ItemStack L_0_1_T1;

    private static ItemStack L_1_1;
    private static ItemStack L_1_1_T1;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        Bootstrap.register();

        M = new IngredientMatcherItemStack();

        NBTTagCompound tag1 = new NBTTagCompound();
        tag1.setInteger("key", 1);

        NBTTagCompound tag2 = new NBTTagCompound();
        tag2.setInteger("key", 2);

        W_0_1 = new ItemStack(Items.WOODEN_AXE, 1, 0);
        W_0_2 = new ItemStack(Items.WOODEN_AXE, 2, 0);
        W_0_1_T1 = new ItemStack(Items.WOODEN_AXE, 1, 0);
        W_0_1_T1.setTagCompound(tag1);
        W_0_2_T1 = new ItemStack(Items.WOODEN_AXE, 2, 0);
        W_0_2_T1.setTagCompound(tag1);
        W_0_1_T2 = new ItemStack(Items.WOODEN_AXE, 1, 0);
        W_0_1_T2.setTagCompound(tag2);
        W_0_2_T2 = new ItemStack(Items.WOODEN_AXE, 2, 0);
        W_0_2_T2.setTagCompound(tag2);

        W_1_1 = new ItemStack(Items.WOODEN_AXE, 1, 1);
        W_1_2 = new ItemStack(Items.WOODEN_AXE, 2, 1);
        W_1_1_T1 = new ItemStack(Items.WOODEN_AXE, 1, 1);
        W_1_1_T1.setTagCompound(tag1);
        W_1_2_T1 = new ItemStack(Items.WOODEN_AXE, 2, 1);
        W_1_2_T1.setTagCompound(tag1);
        W_1_1_T2 = new ItemStack(Items.WOODEN_AXE, 1, 1);
        W_1_1_T2.setTagCompound(tag2);
        W_1_2_T2 = new ItemStack(Items.WOODEN_AXE, 2, 1);
        W_1_2_T2.setTagCompound(tag2);

        L_0_1 = new ItemStack(Items.LEAD, 1, 0);
        L_0_1_T1 = new ItemStack(Items.LEAD, 1, 0);
        L_0_1_T1.setTagCompound(tag1);

        L_1_1 = new ItemStack(Items.LEAD, 1, 1);
        L_1_1_T1 = new ItemStack(Items.LEAD, 1, 1);
        L_1_1_T1.setTagCompound(tag1);
    }

    @Test
    public void testInstance() {
        assertThat(M.isInstance("a"), is(false));
        assertThat(M.isInstance(W_0_1), is(true));
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
    public void testWithCondition() {
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ANY), is(ItemMatch.ANY));
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), is(ItemMatch.ITEM));
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.DAMAGE), is(ItemMatch.DAMAGE));
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.NBT), is(ItemMatch.NBT));
        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.STACKSIZE), is(ItemMatch.STACKSIZE));

        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.DAMAGE), is(ItemMatch.ITEM | ItemMatch.DAMAGE));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.NBT), is(ItemMatch.ITEM | ItemMatch.NBT));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.NBT), ItemMatch.DAMAGE), is(ItemMatch.NBT | ItemMatch.DAMAGE));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.NBT), ItemMatch.STACKSIZE), is(ItemMatch.NBT | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.DAMAGE), ItemMatch.STACKSIZE), is(ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.STACKSIZE), ItemMatch.DAMAGE), is(ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.STACKSIZE), ItemMatch.NBT), is(ItemMatch.NBT | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.DAMAGE), ItemMatch.NBT), is(ItemMatch.NBT | ItemMatch.DAMAGE));

        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.NBT), ItemMatch.DAMAGE), is(ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.NBT), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.DAMAGE), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), ItemMatch.DAMAGE), is(ItemMatch.ITEM | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), ItemMatch.NBT), is(ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.DAMAGE), ItemMatch.NBT), is(ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE));

        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.DAMAGE), ItemMatch.NBT), ItemMatch.STACKSIZE), is(ItemMatch.NBT | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.NBT), ItemMatch.DAMAGE), ItemMatch.STACKSIZE), is(ItemMatch.NBT | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.DAMAGE), ItemMatch.STACKSIZE), ItemMatch.NBT), is(ItemMatch.NBT | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.NBT), ItemMatch.STACKSIZE), ItemMatch.DAMAGE), is(ItemMatch.NBT | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.STACKSIZE), ItemMatch.DAMAGE), ItemMatch.NBT), is(ItemMatch.NBT | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.STACKSIZE), ItemMatch.NBT), ItemMatch.DAMAGE), is(ItemMatch.NBT | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));

        assertThat(M.withCondition(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.DAMAGE), ItemMatch.NBT), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.NBT), ItemMatch.DAMAGE), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.DAMAGE), ItemMatch.STACKSIZE), ItemMatch.NBT), is(ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.NBT), ItemMatch.STACKSIZE), ItemMatch.DAMAGE), is(ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), ItemMatch.DAMAGE), ItemMatch.NBT), is(ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));
        assertThat(M.withCondition(M.withCondition(M.withCondition(M.withCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), ItemMatch.NBT), ItemMatch.DAMAGE), is(ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE | ItemMatch.STACKSIZE));

        assertThat(M.withCondition(M.getAnyMatchCondition(), ItemMatch.EXACT), is(ItemMatch.EXACT));
    }

    @Test
    public void testWithoutCondition() {
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ANY), is(ItemMatch.EXACT));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), is(ItemMatch.DAMAGE | ItemMatch.STACKSIZE | ItemMatch.NBT));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.DAMAGE), is(ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.NBT), is(ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE));
        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE));

        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.NBT), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.DAMAGE));
        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.STACKSIZE), ItemMatch.NBT), is(ItemMatch.ITEM | ItemMatch.DAMAGE));
        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.NBT), ItemMatch.DAMAGE), is(ItemMatch.ITEM | ItemMatch.STACKSIZE));
        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.DAMAGE), ItemMatch.NBT), is(ItemMatch.ITEM | ItemMatch.STACKSIZE));
        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.STACKSIZE), ItemMatch.DAMAGE), is(ItemMatch.ITEM | ItemMatch.NBT));
        assertThat(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.DAMAGE), ItemMatch.STACKSIZE), is(ItemMatch.ITEM | ItemMatch.NBT));

        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.NBT), ItemMatch.STACKSIZE), is(ItemMatch.DAMAGE));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), ItemMatch.NBT), is(ItemMatch.DAMAGE));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.NBT), ItemMatch.DAMAGE), is(ItemMatch.STACKSIZE));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.DAMAGE), ItemMatch.NBT), is(ItemMatch.STACKSIZE));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), ItemMatch.DAMAGE), is(ItemMatch.NBT));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.DAMAGE), ItemMatch.STACKSIZE), is(ItemMatch.NBT));

        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.DAMAGE), ItemMatch.NBT), ItemMatch.STACKSIZE), is(ItemMatch.ITEM));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.NBT), ItemMatch.DAMAGE), ItemMatch.STACKSIZE), is(ItemMatch.ITEM));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.NBT), ItemMatch.STACKSIZE), ItemMatch.DAMAGE), is(ItemMatch.ITEM));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.DAMAGE), ItemMatch.STACKSIZE), ItemMatch.NBT), is(ItemMatch.ITEM));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.STACKSIZE), ItemMatch.DAMAGE), ItemMatch.NBT), is(ItemMatch.ITEM));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.STACKSIZE), ItemMatch.NBT), ItemMatch.DAMAGE), is(ItemMatch.ITEM));

        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.DAMAGE), ItemMatch.NBT), ItemMatch.STACKSIZE), is(ItemMatch.ANY));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.NBT), ItemMatch.DAMAGE), ItemMatch.STACKSIZE), is(ItemMatch.ANY));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.NBT), ItemMatch.STACKSIZE), ItemMatch.DAMAGE), is(ItemMatch.ANY));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.DAMAGE), ItemMatch.STACKSIZE), ItemMatch.NBT), is(ItemMatch.ANY));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), ItemMatch.DAMAGE), ItemMatch.NBT), is(ItemMatch.ANY));
        assertThat(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.ITEM), ItemMatch.STACKSIZE), ItemMatch.NBT), ItemMatch.DAMAGE), is(ItemMatch.ANY));

        assertThat(M.withoutCondition(M.getExactMatchCondition(), ItemMatch.EXACT), is(ItemMatch.ANY));
    }

    @Test
    public void testHasCondition() {
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.ANY), is(false));
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.DAMAGE), is(true));
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.NBT), is(true));
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.STACKSIZE), is(true));
        assertThat(M.hasCondition(M.getExactMatchCondition(), ItemMatch.ITEM), is(true));

        assertThat(M.hasCondition(M.getAnyMatchCondition(), ItemMatch.ANY), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), ItemMatch.DAMAGE), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), ItemMatch.NBT), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), ItemMatch.STACKSIZE), is(false));
        assertThat(M.hasCondition(M.getAnyMatchCondition(), ItemMatch.ITEM), is(false));
    }

    @Test
    public void testMatches() {
        assertThat(M.matches(ItemStack.EMPTY, ItemStack.EMPTY, ItemMatch.EXACT), is(true));
        assertThat(M.matches(W_0_1, ItemStack.EMPTY, ItemMatch.EXACT), is(false));
        assertThat(M.matches(ItemStack.EMPTY, W_0_1, ItemMatch.EXACT), is(false));

        assertThat(M.matches(ItemStack.EMPTY, ItemStack.EMPTY, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, ItemStack.EMPTY, ItemMatch.ANY), is(true));
        assertThat(M.matches(ItemStack.EMPTY, W_0_1, ItemMatch.ANY), is(true));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.EXACT), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.EXACT), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.EXACT), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.ITEM), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.ITEM), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.ITEM), is(true));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.ITEM), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.ITEM), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.ANY), is(true));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.STACKSIZE), is(true));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.NBT), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.DAMAGE), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.ITEM | ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.ITEM | ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.ITEM | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.ITEM | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.ITEM | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.ITEM | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.ITEM | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.ITEM | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.ITEM | ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.ITEM | ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.ITEM | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.ITEM | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.ITEM | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.ITEM | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.ITEM | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.ITEM | ItemMatch.NBT), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.ITEM | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.ITEM | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.ITEM | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.ITEM | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.ITEM | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.ITEM | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.ITEM | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.ITEM | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.ITEM | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.ITEM | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.ITEM | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.ITEM | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.ITEM | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.ITEM | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.ITEM | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.ITEM | ItemMatch.DAMAGE), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.NBT | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.NBT | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.NBT | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.ITEM | ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.ITEM | ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
    }

    @Test
    public void testMatchesExactly() {
        assertThat(M.matchesExactly(ItemStack.EMPTY, ItemStack.EMPTY), is(true));
        assertThat(M.matchesExactly(W_0_1, ItemStack.EMPTY), is(false));
        assertThat(M.matchesExactly(ItemStack.EMPTY, W_0_1), is(false));

        assertThat(M.matchesExactly(W_0_1, W_0_1), is(true));
        assertThat(M.matchesExactly(W_0_1, W_0_2), is(false));
        assertThat(M.matchesExactly(W_0_1, W_0_1_T1), is(false));
        assertThat(M.matchesExactly(W_0_1, W_0_2_T1), is(false));
        assertThat(M.matchesExactly(W_0_1, W_0_1_T2), is(false));
        assertThat(M.matchesExactly(W_0_1, W_0_2_T2), is(false));
        assertThat(M.matchesExactly(W_0_1, L_0_1), is(false));
        assertThat(M.matchesExactly(W_0_1, L_0_1_T1), is(false));

        assertThat(M.matchesExactly(W_0_1, W_1_1), is(false));
        assertThat(M.matchesExactly(W_0_1, W_1_2), is(false));
        assertThat(M.matchesExactly(W_0_1, W_1_1_T1), is(false));
        assertThat(M.matchesExactly(W_0_1, W_1_2_T1), is(false));
        assertThat(M.matchesExactly(W_0_1, W_1_1_T2), is(false));
        assertThat(M.matchesExactly(W_0_1, W_1_2_T2), is(false));
        assertThat(M.matchesExactly(W_0_1, L_1_1), is(false));
        assertThat(M.matchesExactly(W_0_1, L_1_1_T1), is(false));
    }

    @Test
    public void testEmpty() {
        assertThat(M.getEmptyInstance(), is(ItemStack.EMPTY));
    }

    @Test
    public void testIsEmpty() {
        assertThat(M.isEmpty(W_0_1), is(false));
        assertThat(M.isEmpty(ItemStack.EMPTY), is(true));
    }

    @Test
    public void testHash() {
        assertThat(M.hash(W_0_1), is(M.hash(W_0_1.copy())));
        assertThat(M.hash(ItemStack.EMPTY), is(ItemStack.EMPTY.hashCode()));
    }

    @Test
    public void testCopy() {
        assertThat(M.matchesExactly(M.copy(W_0_1), W_0_1), is(true));
        assertThat(M.matchesExactly(M.copy(ItemStack.EMPTY), ItemStack.EMPTY), is(true));
    }

    @Test
    public void testCompare() {
        assertThat(M.compare(W_0_1, ItemStack.EMPTY), is(1));
        assertThat(M.compare(ItemStack.EMPTY, ItemStack.EMPTY), is(0));
        assertThat(M.compare(ItemStack.EMPTY, W_0_1), is(-1));

        assertThat(M.compare(W_0_1, W_0_1), is(0));
        assertThat(M.compare(W_0_1, W_0_2), is(-1));
        assertThat(M.compare(W_0_1, W_0_2_T1), is(-1));
        assertThat(M.compare(W_0_1, W_0_2_T2), is(-1));
        assertThat(M.compare(W_0_1, W_0_1_T1), is(-1));
        assertThat(M.compare(W_0_1, W_0_1_T2), is(-1));
        assertThat(M.compare(W_0_1, W_1_1), is(-1));
        assertThat(M.compare(W_0_1, W_1_1_T1), is(-1));
        assertThat(M.compare(W_0_1, W_1_1_T2), is(-1));
        assertThat(M.compare(W_0_1, W_1_2), is(-1));
        assertThat(M.compare(W_0_1, W_1_2_T1), is(-1));
        assertThat(M.compare(W_0_1, W_1_2_T2), is(-1));
        assertThat(M.compare(W_0_1, L_0_1), is(-149));
        assertThat(M.compare(W_0_1, L_0_1_T1), is(-149));
        assertThat(M.compare(W_0_1, L_1_1), is(-149));
        assertThat(M.compare(W_0_1, L_1_1_T1), is(-149));

        assertThat(M.compare(W_0_1_T1, W_0_1), is(1));
        assertThat(M.compare(W_0_1_T1, W_0_2), is(-1));
        assertThat(M.compare(W_0_1_T1, W_0_2_T1), is(-1));
        assertThat(M.compare(W_0_1_T1, W_0_2_T2), is(-1));
        assertThat(M.compare(W_0_1_T1, W_0_1_T1), is(0));
        assertThat(M.compare(W_0_1_T1, W_0_1_T2), is(-1));
        assertThat(M.compare(W_0_1_T1, W_1_1), is(-1));
        assertThat(M.compare(W_0_1_T1, W_1_1_T1), is(-1));
        assertThat(M.compare(W_0_1_T1, W_1_1_T2), is(-1));
        assertThat(M.compare(W_0_1_T1, W_1_2), is(-1));
        assertThat(M.compare(W_0_1_T1, W_1_2_T1), is(-1));
        assertThat(M.compare(W_0_1_T1, W_1_2_T2), is(-1));
        assertThat(M.compare(W_0_1_T1, L_0_1), is(-149));
        assertThat(M.compare(W_0_1_T1, L_0_1_T1), is(-149));
        assertThat(M.compare(W_0_1_T1, L_1_1), is(-149));
        assertThat(M.compare(W_0_1_T1, L_1_1_T1), is(-149));

        assertThat(M.compare(W_0_1_T2, W_0_1), is(1));
        assertThat(M.compare(W_0_1_T2, W_0_2), is(-1));
        assertThat(M.compare(W_0_1_T2, W_0_2_T1), is(-1));
        assertThat(M.compare(W_0_1_T2, W_0_2_T2), is(-1));
        assertThat(M.compare(W_0_1_T2, W_0_1_T1), is(1));
        assertThat(M.compare(W_0_1_T2, W_0_1_T2), is(0));
        assertThat(M.compare(W_0_1_T2, W_1_1), is(-1));
        assertThat(M.compare(W_0_1_T2, W_1_1_T1), is(-1));
        assertThat(M.compare(W_0_1_T2, W_1_1_T2), is(-1));
        assertThat(M.compare(W_0_1_T2, W_1_2), is(-1));
        assertThat(M.compare(W_0_1_T2, W_1_2_T1), is(-1));
        assertThat(M.compare(W_0_1_T2, W_1_2_T2), is(-1));
        assertThat(M.compare(W_0_1_T2, L_0_1), is(-149));
        assertThat(M.compare(W_0_1_T2, L_0_1_T1), is(-149));
        assertThat(M.compare(W_0_1_T2, L_1_1), is(-149));
        assertThat(M.compare(W_0_1_T2, L_1_1_T1), is(-149));

        assertThat(M.compare(W_1_1, W_0_1), is(1));
        assertThat(M.compare(W_1_1, W_0_2), is(1));
        assertThat(M.compare(W_1_1, W_0_2_T1), is(1));
        assertThat(M.compare(W_1_1, W_0_2_T2), is(1));
        assertThat(M.compare(W_1_1, W_0_1_T1), is(1));
        assertThat(M.compare(W_1_1, W_0_1_T2), is(1));
        assertThat(M.compare(W_1_1, W_1_1), is(0));
        assertThat(M.compare(W_1_1, W_1_1_T1), is(-1));
        assertThat(M.compare(W_1_1, W_1_1_T2), is(-1));
        assertThat(M.compare(W_1_1, W_1_2), is(-1));
        assertThat(M.compare(W_1_1, W_1_2_T1), is(-1));
        assertThat(M.compare(W_1_1, W_1_2_T2), is(-1));
        assertThat(M.compare(W_1_1, L_0_1), is(-149));
        assertThat(M.compare(W_1_1, L_0_1_T1), is(-149));
        assertThat(M.compare(W_1_1, L_1_1), is(-149));
        assertThat(M.compare(W_1_1, L_1_1_T1), is(-149));

        assertThat(M.compare(W_1_1_T1, W_0_1), is(1));
        assertThat(M.compare(W_1_1_T1, W_0_2), is(1));
        assertThat(M.compare(W_1_1_T1, W_0_2_T1), is(1));
        assertThat(M.compare(W_1_1_T1, W_0_2_T2), is(1));
        assertThat(M.compare(W_1_1_T1, W_0_1_T1), is(1));
        assertThat(M.compare(W_1_1_T1, W_0_1_T2), is(1));
        assertThat(M.compare(W_1_1_T1, W_1_1), is(1));
        assertThat(M.compare(W_1_1_T1, W_1_1_T1), is(0));
        assertThat(M.compare(W_1_1_T1, W_1_1_T2), is(-1));
        assertThat(M.compare(W_1_1_T1, W_1_2), is(-1));
        assertThat(M.compare(W_1_1_T1, W_1_2_T1), is(-1));
        assertThat(M.compare(W_1_1_T1, W_1_2_T2), is(-1));
        assertThat(M.compare(W_1_1_T1, L_0_1), is(-149));
        assertThat(M.compare(W_1_1_T1, L_0_1_T1), is(-149));
        assertThat(M.compare(W_1_1_T1, L_1_1), is(-149));
        assertThat(M.compare(W_1_1_T1, L_1_1_T1), is(-149));

        assertThat(M.compare(W_1_1_T2, W_0_1), is(1));
        assertThat(M.compare(W_1_1_T2, W_0_2), is(1));
        assertThat(M.compare(W_1_1_T2, W_0_2_T1), is(1));
        assertThat(M.compare(W_1_1_T2, W_0_2_T2), is(1));
        assertThat(M.compare(W_1_1_T2, W_0_1_T1), is(1));
        assertThat(M.compare(W_1_1_T2, W_0_1_T2), is(1));
        assertThat(M.compare(W_1_1_T2, W_1_1), is(1));
        assertThat(M.compare(W_1_1_T2, W_1_1_T1), is(1));
        assertThat(M.compare(W_1_1_T2, W_1_1_T2), is(0));
        assertThat(M.compare(W_1_1_T2, W_1_2), is(-1));
        assertThat(M.compare(W_1_1_T2, W_1_2_T1), is(-1));
        assertThat(M.compare(W_1_1_T2, W_1_2_T2), is(-1));
        assertThat(M.compare(W_1_1_T2, L_0_1), is(-149));
        assertThat(M.compare(W_1_1_T2, L_0_1_T1), is(-149));
        assertThat(M.compare(W_1_1_T2, L_1_1), is(-149));
        assertThat(M.compare(W_1_1_T2, L_1_1_T1), is(-149));

        assertThat(M.compare(W_0_2, W_0_1), is(1));
        assertThat(M.compare(W_0_2, W_0_1_T1), is(1));
        assertThat(M.compare(W_0_2, W_0_1_T2), is(1));
        assertThat(M.compare(W_0_2, W_0_2), is(0));
        assertThat(M.compare(W_0_2, W_0_2_T1), is(-1));
        assertThat(M.compare(W_0_2, W_0_2_T2), is(-1));
        assertThat(M.compare(W_0_2, W_1_1), is(-1));
        assertThat(M.compare(W_0_2, W_1_1_T1), is(-1));
        assertThat(M.compare(W_0_2, W_1_1_T2), is(-1));
        assertThat(M.compare(W_0_2, W_1_2), is(-1));
        assertThat(M.compare(W_0_2, W_1_2_T1), is(-1));
        assertThat(M.compare(W_0_2, W_1_2_T2), is(-1));
        assertThat(M.compare(W_0_2, L_0_1), is(-149));
        assertThat(M.compare(W_0_2, L_0_1_T1), is(-149));
        assertThat(M.compare(W_0_2, L_1_1), is(-149));
        assertThat(M.compare(W_0_2, L_1_1_T1), is(-149));

        assertThat(M.compare(W_0_2_T1, W_0_1), is(1));
        assertThat(M.compare(W_0_2_T1, W_0_1_T1), is(1));
        assertThat(M.compare(W_0_2_T1, W_0_1_T2), is(1));
        assertThat(M.compare(W_0_2_T1, W_0_2), is(1));
        assertThat(M.compare(W_0_2_T1, W_0_2_T1), is(0));
        assertThat(M.compare(W_0_2_T1, W_0_2_T2), is(-1));
        assertThat(M.compare(W_0_2_T1, W_1_1), is(-1));
        assertThat(M.compare(W_0_2_T1, W_1_1_T1), is(-1));
        assertThat(M.compare(W_0_2_T1, W_1_1_T2), is(-1));
        assertThat(M.compare(W_0_2_T1, W_1_2), is(-1));
        assertThat(M.compare(W_0_2_T1, W_1_2_T1), is(-1));
        assertThat(M.compare(W_0_2_T1, W_1_2_T2), is(-1));
        assertThat(M.compare(W_0_2_T1, L_1_1), is(-149));
        assertThat(M.compare(W_0_2_T1, L_1_1_T1), is(-149));
        assertThat(M.compare(W_0_2_T1, L_1_1), is(-149));
        assertThat(M.compare(W_0_2_T1, L_1_1_T1), is(-149));

        assertThat(M.compare(W_0_2_T2, W_0_1), is(1));
        assertThat(M.compare(W_0_2_T2, W_0_1_T1), is(1));
        assertThat(M.compare(W_0_2_T2, W_0_1_T2), is(1));
        assertThat(M.compare(W_0_2_T2, W_0_2), is(1));
        assertThat(M.compare(W_0_2_T2, W_0_2_T1), is(1));
        assertThat(M.compare(W_0_2_T2, W_0_2_T2), is(0));
        assertThat(M.compare(W_0_2_T2, W_1_1), is(-1));
        assertThat(M.compare(W_0_2_T2, W_1_1_T1), is(-1));
        assertThat(M.compare(W_0_2_T2, W_1_1_T2), is(-1));
        assertThat(M.compare(W_0_2_T2, W_1_2), is(-1));
        assertThat(M.compare(W_0_2_T2, W_1_2_T1), is(-1));
        assertThat(M.compare(W_0_2_T2, W_1_2_T2), is(-1));
        assertThat(M.compare(W_0_2_T2, L_0_1), is(-149));
        assertThat(M.compare(W_0_2_T2, L_0_1_T1), is(-149));
        assertThat(M.compare(W_0_2_T2, L_1_1), is(-149));
        assertThat(M.compare(W_0_2_T2, L_1_1_T1), is(-149));

        assertThat(M.compare(W_1_2, W_0_1), is(1));
        assertThat(M.compare(W_1_2, W_0_1_T1), is(1));
        assertThat(M.compare(W_1_2, W_0_1_T2), is(1));
        assertThat(M.compare(W_1_2, W_0_2), is(1));
        assertThat(M.compare(W_1_2, W_0_2_T1), is(1));
        assertThat(M.compare(W_1_2, W_0_2_T2), is(1));
        assertThat(M.compare(W_1_2, W_1_1), is(1));
        assertThat(M.compare(W_1_2, W_1_1_T1), is(1));
        assertThat(M.compare(W_1_2, W_1_1_T2), is(1));
        assertThat(M.compare(W_1_2, W_1_2), is(0));
        assertThat(M.compare(W_1_2, W_1_2_T1), is(-1));
        assertThat(M.compare(W_1_2, W_1_2_T2), is(-1));
        assertThat(M.compare(W_1_2, L_0_1), is(-149));
        assertThat(M.compare(W_1_2, L_0_1_T1), is(-149));
        assertThat(M.compare(W_1_2, L_1_1), is(-149));
        assertThat(M.compare(W_1_2, L_1_1_T1), is(-149));

        assertThat(M.compare(W_1_2_T1, W_0_1), is(1));
        assertThat(M.compare(W_1_2_T1, W_0_1_T1), is(1));
        assertThat(M.compare(W_1_2_T1, W_0_1_T2), is(1));
        assertThat(M.compare(W_1_2_T1, W_0_2), is(1));
        assertThat(M.compare(W_1_2_T1, W_0_2_T1), is(1));
        assertThat(M.compare(W_1_2_T1, W_0_2_T2), is(1));
        assertThat(M.compare(W_1_2_T1, W_1_1), is(1));
        assertThat(M.compare(W_1_2_T1, W_1_1_T1), is(1));
        assertThat(M.compare(W_1_2_T1, W_1_1_T2), is(1));
        assertThat(M.compare(W_1_2_T1, W_1_2), is(1));
        assertThat(M.compare(W_1_2_T1, W_1_2_T1), is(0));
        assertThat(M.compare(W_1_2_T1, W_1_2_T2), is(-1));
        assertThat(M.compare(W_1_2_T1, L_1_1), is(-149));
        assertThat(M.compare(W_1_2_T1, L_1_1_T1), is(-149));
        assertThat(M.compare(W_1_2_T1, L_1_1), is(-149));
        assertThat(M.compare(W_1_2_T1, L_1_1_T1), is(-149));

        assertThat(M.compare(W_1_2_T2, W_0_1), is(1));
        assertThat(M.compare(W_1_2_T2, W_0_1_T1), is(1));
        assertThat(M.compare(W_1_2_T2, W_0_1_T2), is(1));
        assertThat(M.compare(W_1_2_T2, W_0_2), is(1));
        assertThat(M.compare(W_1_2_T2, W_0_2_T1), is(1));
        assertThat(M.compare(W_1_2_T2, W_0_2_T2), is(1));
        assertThat(M.compare(W_1_2_T2, W_1_1), is(1));
        assertThat(M.compare(W_1_2_T2, W_1_1_T1), is(1));
        assertThat(M.compare(W_1_2_T2, W_1_1_T2), is(1));
        assertThat(M.compare(W_1_2_T2, W_1_2), is(1));
        assertThat(M.compare(W_1_2_T2, W_1_2_T1), is(1));
        assertThat(M.compare(W_1_2_T2, W_1_2_T2), is(0));
        assertThat(M.compare(W_1_2_T2, L_0_1), is(-149));
        assertThat(M.compare(W_1_2_T2, L_0_1_T1), is(-149));
        assertThat(M.compare(W_1_2_T2, L_1_1), is(-149));
        assertThat(M.compare(W_1_2_T2, L_1_1_T1), is(-149));

        assertThat(M.compare(L_0_1, W_0_1), is(149));
        assertThat(M.compare(L_0_1, W_0_1_T1), is(149));
        assertThat(M.compare(L_0_1, W_0_1_T2), is(149));
        assertThat(M.compare(L_0_1, W_0_2), is(149));
        assertThat(M.compare(L_0_1, W_0_2_T1), is(149));
        assertThat(M.compare(L_0_1, W_0_2_T2), is(149));
        assertThat(M.compare(L_0_1, W_1_1), is(149));
        assertThat(M.compare(L_0_1, W_1_1_T1), is(149));
        assertThat(M.compare(L_0_1, W_1_1_T2), is(149));
        assertThat(M.compare(L_0_1, W_1_2), is(149));
        assertThat(M.compare(L_0_1, W_1_2_T1), is(149));
        assertThat(M.compare(L_0_1, W_1_2_T2), is(149));
        assertThat(M.compare(L_0_1, L_0_1), is(0));
        assertThat(M.compare(L_0_1, L_0_1_T1), is(-1));
        assertThat(M.compare(L_0_1, L_1_1), is(-1));
        assertThat(M.compare(L_0_1, L_1_1_T1), is(-1));

        assertThat(M.compare(L_0_1_T1, W_0_1), is(149));
        assertThat(M.compare(L_0_1_T1, W_0_1_T1), is(149));
        assertThat(M.compare(L_0_1_T1, W_0_1_T2), is(149));
        assertThat(M.compare(L_0_1_T1, W_0_2), is(149));
        assertThat(M.compare(L_0_1_T1, W_0_2_T1), is(149));
        assertThat(M.compare(L_0_1_T1, W_0_2_T2), is(149));
        assertThat(M.compare(L_0_1_T1, W_1_1), is(149));
        assertThat(M.compare(L_0_1_T1, W_1_1_T1), is(149));
        assertThat(M.compare(L_0_1_T1, W_1_1_T2), is(149));
        assertThat(M.compare(L_0_1_T1, W_1_2), is(149));
        assertThat(M.compare(L_0_1_T1, W_1_2_T1), is(149));
        assertThat(M.compare(L_0_1_T1, W_1_2_T2), is(149));
        assertThat(M.compare(L_0_1_T1, L_0_1), is(1));
        assertThat(M.compare(L_0_1_T1, L_0_1_T1), is(0));
        assertThat(M.compare(L_0_1_T1, L_1_1), is(-1));
        assertThat(M.compare(L_0_1_T1, L_1_1_T1), is(-1));

        assertThat(M.compare(L_1_1, W_0_1), is(149));
        assertThat(M.compare(L_1_1, W_0_1_T1), is(149));
        assertThat(M.compare(L_1_1, W_0_1_T2), is(149));
        assertThat(M.compare(L_1_1, W_0_2), is(149));
        assertThat(M.compare(L_1_1, W_0_2_T1), is(149));
        assertThat(M.compare(L_1_1, W_0_2_T2), is(149));
        assertThat(M.compare(L_1_1, W_1_1), is(149));
        assertThat(M.compare(L_1_1, W_1_1_T1), is(149));
        assertThat(M.compare(L_1_1, W_1_1_T2), is(149));
        assertThat(M.compare(L_1_1, W_1_2), is(149));
        assertThat(M.compare(L_1_1, W_1_2_T1), is(149));
        assertThat(M.compare(L_1_1, W_1_2_T2), is(149));
        assertThat(M.compare(L_1_1, L_0_1), is(1));
        assertThat(M.compare(L_1_1, L_0_1_T1), is(1));
        assertThat(M.compare(L_1_1, L_1_1), is(0));
        assertThat(M.compare(L_1_1, L_1_1_T1), is(-1));

        assertThat(M.compare(L_1_1_T1, W_0_1), is(149));
        assertThat(M.compare(L_1_1_T1, W_0_1_T1), is(149));
        assertThat(M.compare(L_1_1_T1, W_0_1_T2), is(149));
        assertThat(M.compare(L_1_1_T1, W_0_2), is(149));
        assertThat(M.compare(L_1_1_T1, W_0_2_T1), is(149));
        assertThat(M.compare(L_1_1_T1, W_0_2_T2), is(149));
        assertThat(M.compare(L_1_1_T1, W_1_1), is(149));
        assertThat(M.compare(L_1_1_T1, W_1_1_T1), is(149));
        assertThat(M.compare(L_1_1_T1, W_1_1_T2), is(149));
        assertThat(M.compare(L_1_1_T1, W_1_2), is(149));
        assertThat(M.compare(L_1_1_T1, W_1_2_T1), is(149));
        assertThat(M.compare(L_1_1_T1, W_1_2_T2), is(149));
        assertThat(M.compare(L_1_1_T1, L_0_1), is(1));
        assertThat(M.compare(L_1_1_T1, L_0_1_T1), is(1));
        assertThat(M.compare(L_1_1_T1, L_1_1), is(1));
        assertThat(M.compare(L_1_1_T1, L_1_1_T1), is(0));
    }

}
