package org.cyclops.commoncapabilities.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.gametest.GameTest;

/**
 * @author rubensworks
 */
public class GameTestsVanillaCapabilitiesEntityItemFrame {

    public static final String TEMPLATE_EMPTY = "commoncapabilities:empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 1, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEntityItemFrameAdd(GameTestHelper helper) {
        // Spawn item frame with shulker box
        ItemFrame entity = helper.spawn(EntityType.ITEM_FRAME, POS);
        entity.setItem(new ItemStack(Items.SHULKER_BOX));

        // Add item to shulker box
        ResourceHandler<ItemResource> itemHandler = entity.getCapability(Capabilities.Item.ENTITY);
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
    public void testEntityItemFrameRemove(GameTestHelper helper) {
        // Spawn item frame with shulker box
        ItemFrame entity = helper.spawn(EntityType.ITEM_FRAME, POS);
        entity.setItem(new ItemStack(Items.SHULKER_BOX));

        // Remove item from shulker box
        ResourceHandler<ItemResource> itemHandler = entity.getCapability(Capabilities.Item.ENTITY);
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

}
