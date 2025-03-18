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
    public void testEntityItemCapItemShulkerboxRemove(GameTestHelper helper) {
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
        // Create shulker box itemstack
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
    public void testEntityItemCapItemBundleRemove(GameTestHelper helper) {
        // Create shulker box itemstack
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

}
