package org.cyclops.commoncapabilities.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.items.IItemHandler;
import org.cyclops.commoncapabilities.Reference;

/**
 * @author rubensworks
 */
@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsVanillaCapabilitiesItemItemHandler {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 1, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemShulkerboxAdd(GameTestHelper helper) {
        // Create shulker box itemstack
        ItemStack itemStack = new ItemStack(Items.SHULKER_BOX);

        // Add item to shulker box
        IItemHandler itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM);
        ItemStack remaining = itemHandler.insertItem(0, new ItemStack(Items.APPLE), false);

        helper.succeedIf(() -> {
            helper.assertTrue(remaining.isEmpty(), "Remaining of insertion is not empty");
            helper.assertTrue(itemHandler.getStackInSlot(0).getItem() == Items.APPLE, "Item was not added");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemShulkerboxRemove(GameTestHelper helper) {
        // Create shulker box itemstack
        ItemStack itemStack = new ItemStack(Items.SHULKER_BOX);

        // Remove item from shulker box
        IItemHandler itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM);
        itemHandler.insertItem(0, new ItemStack(Items.APPLE), false);
        ItemStack removed = itemHandler.extractItem(0, 1, false);

        helper.succeedIf(() -> {
            helper.assertTrue(!removed.isEmpty(), "Removed item is empty");
            helper.assertTrue(itemHandler.getStackInSlot(0).isEmpty(), "Item was not removed");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemBundleAdd(GameTestHelper helper) {
        // Create bundle itemstack
        ItemStack itemStack = new ItemStack(Items.BUNDLE);

        // Add item to shulker box
        IItemHandler itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM);
        ItemStack remaining = itemHandler.insertItem(0, new ItemStack(Items.APPLE), false);

        helper.succeedIf(() -> {
            helper.assertTrue(remaining.isEmpty(), "Remaining of insertion is not empty");
            helper.assertTrue(itemHandler.getStackInSlot(0).getItem() == Items.APPLE, "Item was not added");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemBundleRemove(GameTestHelper helper) {
        // Create bundle itemstack
        ItemStack itemStack = new ItemStack(Items.BUNDLE);

        // Remove item from shulker box
        IItemHandler itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM);
        itemHandler.insertItem(0, new ItemStack(Items.APPLE), false);
        ItemStack removed = itemHandler.extractItem(0, 1, false);

        helper.succeedIf(() -> {
            helper.assertTrue(!removed.isEmpty(), "Removed item is empty");
            helper.assertTrue(itemHandler.getStackInSlot(0).isEmpty(), "Item was not removed");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemBundleAddMultiple(GameTestHelper helper) {
        // Create bundle itemstack
        ItemStack itemStack = new ItemStack(Items.BUNDLE);

        // Add item to shulker box
        IItemHandler itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM);
        ItemStack remaining1 = itemHandler.insertItem(0, new ItemStack(Items.APPLE), false);
        ItemStack remaining2 = itemHandler.insertItem(0, new ItemStack(Items.APPLE), false);
        ItemStack remaining3 = itemHandler.insertItem(1, new ItemStack(Items.APPLE), false);
        ItemStack remaining4 = itemHandler.insertItem(1, new ItemStack(Items.APPLE), false);

        helper.succeedIf(() -> {
            helper.assertTrue(remaining1.isEmpty(), "Remaining of insertion 1 is not empty");
            helper.assertTrue(remaining2.isEmpty(), "Remaining of insertion 2 is not empty");
            helper.assertTrue(remaining3.isEmpty(), "Remaining of insertion 3 is not empty");
            helper.assertTrue(remaining4.isEmpty(), "Remaining of insertion 4 is not empty");
            helper.assertTrue(itemHandler.getSlots() == 3, "Slot count was not 3");
            helper.assertTrue(itemHandler.getStackInSlot(0).getItem() == Items.APPLE, "Item was not added in slot 0");
            helper.assertTrue(itemHandler.getStackInSlot(0).getCount() == 2, "Item count was not 2 in slot 0");
            helper.assertTrue(itemHandler.getStackInSlot(1).getItem() == Items.APPLE, "Item was not added in slot 1");
            helper.assertTrue(itemHandler.getStackInSlot(1).getCount() == 2, "Item count was not 2 in slot 1");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemBundleRemoveMultiple(GameTestHelper helper) {
        // Create bundle itemstack
        ItemStack itemStack = new ItemStack(Items.BUNDLE);

        // Remove item from shulker box
        IItemHandler itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM);
        itemHandler.insertItem(0, new ItemStack(Items.APPLE), false);
        itemHandler.insertItem(0, new ItemStack(Items.APPLE), false);
        itemHandler.insertItem(1, new ItemStack(Items.APPLE), false);
        itemHandler.insertItem(1, new ItemStack(Items.APPLE), false);
        ItemStack removed1 = itemHandler.extractItem(1, 2, false);
        ItemStack removed2 = itemHandler.extractItem(0, 1, false);
        ItemStack removed3 = itemHandler.extractItem(0, 1, false);

        helper.succeedIf(() -> {
            helper.assertTrue(removed1.getCount() == 2, "Removed item 3 is not 2");
            helper.assertTrue(!removed2.isEmpty(), "Removed item 1 is empty");
            helper.assertTrue(!removed3.isEmpty(), "Removed item 2 is empty");
            helper.assertTrue(itemHandler.getSlots() == 1, "Slot count was not 1");
            helper.assertTrue(itemHandler.getStackInSlot(0).isEmpty(), "Item was not removed in slot 0");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemBundleAddRejectOverfullSameSlot(GameTestHelper helper) {
        // Create bundle itemstack
        ItemStack itemStack = new ItemStack(Items.BUNDLE);

        // Add item to shulker box
        IItemHandler itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM);
        ItemStack remaining1 = itemHandler.insertItem(0, new ItemStack(Items.APPLE, 32), false);
        ItemStack remaining2 = itemHandler.insertItem(0, new ItemStack(Items.APPLE, 64), false);

        helper.succeedIf(() -> {
            helper.assertTrue(remaining1.isEmpty(), "Remaining of insertion 1 is not empty");
            helper.assertValueEqual(remaining2.getCount(), 32, "Remaining of insertion 2");
            helper.assertTrue(itemHandler.getStackInSlot(0).getItem() == Items.APPLE, "Item was not added");
            helper.assertValueEqual(itemHandler.getStackInSlot(0).getCount(), 64, "Item stored count");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemBundleAddRejectOverfullOtherSlot(GameTestHelper helper) {
        // Create bundle itemstack
        ItemStack itemStack = new ItemStack(Items.BUNDLE);

        // Add item to shulker box
        IItemHandler itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM);
        ItemStack remaining1 = itemHandler.insertItem(0, new ItemStack(Items.APPLE, 32), false);
        ItemStack remaining2 = itemHandler.insertItem(1, new ItemStack(Items.APPLE, 64), false);

        helper.succeedIf(() -> {
            helper.assertTrue(remaining1.isEmpty(), "Remaining of insertion 1 is not empty");
            helper.assertValueEqual(remaining2.getCount(), 32, "Remaining of insertion 2");
            helper.assertTrue(itemHandler.getStackInSlot(0).getItem() == Items.APPLE, "Item was not added in slot 0");
            helper.assertTrue(itemHandler.getStackInSlot(1).getItem() == Items.APPLE, "Item was not added in slot 1");
            helper.assertValueEqual(itemHandler.getStackInSlot(0).getCount(), 32, "Item stored count slot 0");
            helper.assertValueEqual(itemHandler.getStackInSlot(1).getCount(), 32, "Item stored count slot 1");
        });
    }

}
