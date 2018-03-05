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
    public void testMatches() {
        assertThat(M.matches(ItemStack.EMPTY, ItemStack.EMPTY, ItemMatch.EXACT), is(true));
        assertThat(M.matches(W_0_1, ItemStack.EMPTY, ItemMatch.EXACT), is(false));
        assertThat(M.matches(ItemStack.EMPTY, W_0_1, ItemMatch.EXACT), is(false));

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

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.ANY), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.ANY), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.ANY), is(true));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.ANY), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.ANY), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.STACKSIZE), is(true));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.STACKSIZE), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.STACKSIZE), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.NBT), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.DAMAGE), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(true));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.STACKSIZE | ItemMatch.NBT), is(false));

        assertThat(M.matches(W_0_1, W_0_1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_0_1_T2, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(true));
        assertThat(M.matches(W_0_1, W_0_2_T2, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.STACKSIZE | ItemMatch.DAMAGE), is(false));
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
        assertThat(M.matches(W_0_1, L_0_1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_0_1_T1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_1_T2, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, W_1_2_T2, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
        assertThat(M.matches(W_0_1, L_1_1_T1, ItemMatch.NBT | ItemMatch.DAMAGE), is(false));
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
