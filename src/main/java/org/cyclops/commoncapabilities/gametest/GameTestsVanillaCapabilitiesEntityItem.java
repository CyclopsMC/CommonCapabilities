package org.cyclops.commoncapabilities.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.gametest.GameTest;

/**
 * @author rubensworks
 */
public class GameTestsVanillaCapabilitiesEntityItem {

    public static final String TEMPLATE_EMPTY = "commoncapabilities:empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 1, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEntityItemCapItemShulkerboxAdd(GameTestHelper helper) {
        // Spawn shulker box item entity
        ItemEntity entity = helper.spawnItem(Items.SHULKER_BOX, POS);

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
    public void testEntityItemCapItemShulkerboxRemove(GameTestHelper helper) {
        // Spawn shulker box item entity
        ItemEntity entity = helper.spawnItem(Items.SHULKER_BOX, POS);

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

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEntityItemCapFluidBucketAdd(GameTestHelper helper) {
        // Spawn bucket item entity
        ItemEntity entity = helper.spawnItem(Items.BUCKET, POS);

        // Add fluid to bucket
        ResourceHandler<FluidResource> fluidHandler = entity.getCapability(Capabilities.Fluid.ENTITY, Direction.UP);
        int filled;
        try (var tx = Transaction.openRoot()) {
            filled = fluidHandler.insert(FluidResource.of(Fluids.WATER), 1000, tx);
            tx.commit();
        }

        helper.succeedIf(() -> {
            helper.assertTrue(filled == 1000, Component.literal("Insertion was not 1000"));
            helper.assertTrue(fluidHandler.getAmountAsInt(0) == 1000, Component.literal("Fluid was not added"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEntityItemCapFluidBucketRemove(GameTestHelper helper) {
        // Spawn water bucket item entity
        ItemEntity entity = helper.spawnItem(Items.WATER_BUCKET, POS);

        // Remove water from bucket
        ResourceHandler<FluidResource> fluidHandler = entity.getCapability(Capabilities.Fluid.ENTITY, Direction.UP);
        int drained;
        try (var tx = Transaction.openRoot()) {
            fluidHandler.insert(FluidResource.of(Fluids.WATER), 1000, tx);
            drained = fluidHandler.extract(FluidResource.of(Fluids.WATER), 1000, tx);
            tx.commit();
        }

        helper.succeedIf(() -> {
            helper.assertTrue(drained == 1000, Component.literal("Removal was not 1000"));
            helper.assertTrue(fluidHandler.getResource(0).isEmpty(), Component.literal("Fluid was not removed"));
        });
    }

}
