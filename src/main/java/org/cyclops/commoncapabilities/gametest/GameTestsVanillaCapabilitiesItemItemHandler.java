package org.cyclops.commoncapabilities.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.gametest.GameTest;

/**
 * @author rubensworks
 */
public class GameTestsVanillaCapabilitiesItemItemHandler {

    public static final String TEMPLATE_EMPTY = "commoncapabilities:empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 1, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemShulkerboxAdd(GameTestHelper helper) {
        // Create shulker box itemstack
        ItemStack itemStack = new ItemStack(Items.SHULKER_BOX);

        // Add item to shulker box
        ResourceHandler<ItemResource> itemHandler = itemStack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(itemStack));
        int inserted;
        try (var tx = Transaction.openRoot()) {
            inserted = itemHandler.insert(ItemResource.of(Items.APPLE), 1, tx);
            tx.commit();
        }

        helper.succeedIf(() -> {
            helper.assertTrue(inserted == 1, Component.literal("Remaining of insertion is not empty"));
            helper.assertTrue(itemHandler.getResource(0).getItem() == Items.APPLE, Component.literal("Item was not added"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemShulkerboxRemove(GameTestHelper helper) {
        // Create shulker box itemstack
        ItemStack itemStack = new ItemStack(Items.SHULKER_BOX);

        // Remove item from shulker box
        ResourceHandler<ItemResource> itemHandler = itemStack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(itemStack));
        int removed;
        try (var tx = Transaction.openRoot()) {
            itemHandler.insert(ItemResource.of(Items.APPLE), 1, tx);
            removed = itemHandler.extract(ItemResource.of(Items.APPLE), 1, tx);
            tx.commit();
        }

        helper.succeedIf(() -> {
            helper.assertTrue(removed == 1, Component.literal("Removed item is empty"));
            helper.assertTrue(itemHandler.getResource(0).isEmpty(), Component.literal("Item was not removed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemBundleAdd(GameTestHelper helper) {
        // Create bundle itemstack
        ItemStack itemStack = new ItemStack(Items.BUNDLE);

        // Add item to shulker box
        ResourceHandler<ItemResource> itemHandler = itemStack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(itemStack));
        int inserted;
        try (var tx = Transaction.openRoot()) {
            inserted = itemHandler.insert(ItemResource.of(Items.APPLE), 1, tx);
            tx.commit();
        }

        helper.succeedIf(() -> {
            helper.assertTrue(inserted == 1, Component.literal("Remaining of insertion is not empty"));
            helper.assertTrue(itemHandler.getResource(0).getItem() == Items.APPLE, Component.literal("Item was not added"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemBundleRemove(GameTestHelper helper) {
        // Create bundle itemstack
        ItemStack itemStack = new ItemStack(Items.BUNDLE);

        // Remove item from shulker box
        ResourceHandler<ItemResource> itemHandler = itemStack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(itemStack));
        int removed;
        try (var tx = Transaction.openRoot()) {
            itemHandler.insert(ItemResource.of(Items.APPLE), 1, tx);
            removed = itemHandler.extract(ItemResource.of(Items.APPLE), 1, tx);
            tx.commit();
        }

        helper.succeedIf(() -> {
            helper.assertTrue(removed == 1, Component.literal("Removed item is empty"));
            helper.assertTrue(itemHandler.getResource(0).isEmpty(), Component.literal("Item was not removed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemBundleAddMultiple(GameTestHelper helper) {
        // Create bundle itemstack
        ItemStack itemStack = new ItemStack(Items.BUNDLE);

        // Add item to shulker box
        ResourceHandler<ItemResource> itemHandler = itemStack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(itemStack));
        int inserted1;
        int inserted2;
        int inserted3;
        int inserted4;
        try (var tx = Transaction.openRoot()) {
            inserted1 = itemHandler.insert(0, ItemResource.of(Items.APPLE), 1, tx);
            inserted2 = itemHandler.insert(0, ItemResource.of(Items.APPLE), 1, tx);
            inserted3 = itemHandler.insert(1, ItemResource.of(Items.APPLE), 1, tx);
            inserted4 = itemHandler.insert(1, ItemResource.of(Items.APPLE), 1, tx);
            tx.commit();
        }

        helper.succeedIf(() -> {
            helper.assertTrue(inserted1 == 1, Component.literal("Remaining of insertion 1 is not empty"));
            helper.assertTrue(inserted2 == 1, Component.literal("Remaining of insertion 2 is not empty"));
            helper.assertTrue(inserted3 == 1, Component.literal("Remaining of insertion 3 is not empty"));
            helper.assertTrue(inserted4 == 1, Component.literal("Remaining of insertion 4 is not empty"));
            helper.assertTrue(itemHandler.size() == 3, Component.literal("Slot count was not 3"));
            helper.assertTrue(itemHandler.getResource(0).getItem() == Items.APPLE, Component.literal("Item was not added"));
            helper.assertTrue(itemHandler.getAmountAsInt(0) == 2, Component.literal("Item count was not 2 in slot 0"));
            helper.assertTrue(itemHandler.getResource(1).getItem() == Items.APPLE, Component.literal("Item was not added"));
            helper.assertTrue(itemHandler.getAmountAsInt(1) == 2, Component.literal("Item count was not 2 in slot 1"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemBundleRemoveMultiple(GameTestHelper helper) {
        // Create bundle itemstack
        ItemStack itemStack = new ItemStack(Items.BUNDLE);

        // Remove item from shulker box
        ResourceHandler<ItemResource> itemHandler = itemStack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(itemStack));
        int removed1;
        int removed2;
        int removed3;
        try (var tx = Transaction.openRoot()) {
            itemHandler.insert(0, ItemResource.of(Items.APPLE), 1, tx);
            itemHandler.insert(0, ItemResource.of(Items.APPLE), 1, tx);
            itemHandler.insert(1, ItemResource.of(Items.APPLE), 1, tx);
            itemHandler.insert(1, ItemResource.of(Items.APPLE), 1, tx);
            removed1 = itemHandler.extract(1, ItemResource.of(Items.APPLE), 2, tx);
            removed2 = itemHandler.extract(0, ItemResource.of(Items.APPLE), 1, tx);
            removed3 = itemHandler.extract(0, ItemResource.of(Items.APPLE), 1, tx);
            tx.commit();
        }

        helper.succeedIf(() -> {
            helper.assertTrue(removed1 == 2, Component.literal("Removed item 3 is not 2"));
            helper.assertTrue(removed2 == 1, Component.literal("Removed item 1 is empty"));
            helper.assertTrue(removed3 == 1, Component.literal("Removed item 2 is empty"));
            helper.assertTrue(itemHandler.size() == 1, Component.literal("Slot count was not 1"));
            helper.assertTrue(itemHandler.getResource(0).isEmpty(), Component.literal("Item was not removed in slot 0"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemBundleAddRejectOverfullSameSlot(GameTestHelper helper) {
        // Create bundle itemstack
        ItemStack itemStack = new ItemStack(Items.BUNDLE);

        // Add item to shulker box
        ResourceHandler<ItemResource> itemHandler = itemStack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(itemStack));
        int inserted1;
        int inserted2;
        try (var tx = Transaction.openRoot()) {
            inserted1 = itemHandler.insert(0, ItemResource.of(Items.APPLE), 32, tx);
            inserted2 = itemHandler.insert(0, ItemResource.of(Items.APPLE), 64, tx);
            tx.commit();
        }

        helper.succeedIf(() -> {
            helper.assertValueEqual(32, inserted1, "Inserted 1");
            helper.assertValueEqual(32, inserted2, "Inserted 2");
            helper.assertTrue(itemHandler.getResource(0).getItem() == Items.APPLE, "Item was not added");
            helper.assertValueEqual(64, itemHandler.getAmountAsInt(0), "Item stored count");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemItemHandlerCapItemBundleAddRejectOverfullOtherSlot(GameTestHelper helper) {
        // Create bundle itemstack
        ItemStack itemStack = new ItemStack(Items.BUNDLE);

        // Add item to shulker box
        ResourceHandler<ItemResource> itemHandler = itemStack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(itemStack));
        int inserted1;
        int inserted2;
        try (var tx = Transaction.openRoot()) {
            inserted1 = itemHandler.insert(0, ItemResource.of(Items.APPLE), 32, tx);
            inserted2 = itemHandler.insert(1, ItemResource.of(Items.APPLE), 64, tx);
            tx.commit();
        }

        helper.succeedIf(() -> {
            helper.assertValueEqual(32, inserted1, "Inserted 1");
            helper.assertValueEqual(32, inserted2, "Inserted 2");
            helper.assertTrue(itemHandler.getResource(0).getItem() == Items.APPLE, "Item was not added in slot 0");
            helper.assertTrue(itemHandler.getResource(1).getItem() == Items.APPLE, "Item was not added in slot 1");
            helper.assertValueEqual(itemHandler.getAmountAsInt(0), 32, "Item stored count slot 0");
            helper.assertValueEqual(itemHandler.getAmountAsInt(1), 32, "Item stored count slot 1");
        });
    }

}
