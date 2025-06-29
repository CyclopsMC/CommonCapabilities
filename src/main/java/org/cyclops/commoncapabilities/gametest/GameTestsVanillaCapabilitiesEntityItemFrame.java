package org.cyclops.commoncapabilities.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
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
        IItemHandler itemHandler = entity.getCapability(Capabilities.ItemHandler.ENTITY);
        ItemStack remaining = itemHandler.insertItem(0, new ItemStack(Items.APPLE), false);

        helper.succeedIf(() -> {
            helper.assertTrue(remaining.isEmpty(), Component.literal("Remaining of insertion is not empty"));
            helper.assertTrue(itemHandler.getStackInSlot(0).getItem() == Items.APPLE, Component.literal("Item was not added"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEntityItemFrameRemove(GameTestHelper helper) {
        // Spawn item frame with shulker box
        ItemFrame entity = helper.spawn(EntityType.ITEM_FRAME, POS);
        entity.setItem(new ItemStack(Items.SHULKER_BOX));

        // Remove item from shulker box
        IItemHandler itemHandler = entity.getCapability(Capabilities.ItemHandler.ENTITY);
        itemHandler.insertItem(0, new ItemStack(Items.APPLE), false);
        ItemStack removed = itemHandler.extractItem(0, 1, false);

        helper.succeedIf(() -> {
            helper.assertTrue(!removed.isEmpty(), Component.literal("Removed item is empty"));
            helper.assertTrue(itemHandler.getStackInSlot(0).isEmpty(), Component.literal("Item was not removed"));
        });
    }

}
