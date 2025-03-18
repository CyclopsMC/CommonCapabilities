package org.cyclops.commoncapabilities.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.items.IItemHandler;
import org.cyclops.commoncapabilities.Reference;

/**
 * @author rubensworks
 */
@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsVanillaCapabilitiesEntityItem {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 1, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEntityItemCapItemShulkerboxAdd(GameTestHelper helper) {
        // Spawn shulker box item entity
        ItemEntity entity = helper.spawnItem(Items.SHULKER_BOX, POS);

        // Add item to shulker box
        IItemHandler itemHandler = entity.getCapability(Capabilities.ItemHandler.ENTITY);
        ItemStack remaining = itemHandler.insertItem(0, new ItemStack(Items.APPLE), false);

        helper.succeedIf(() -> {
            helper.assertTrue(remaining.isEmpty(), "Remaining of insertion is not empty");
            helper.assertTrue(itemHandler.getStackInSlot(0).getItem() == Items.APPLE, "Item was not added");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEntityItemCapItemShulkerboxRemove(GameTestHelper helper) {
        // Spawn shulker box item entity
        ItemEntity entity = helper.spawnItem(Items.SHULKER_BOX, POS);

        // Remove item from shulker box
        IItemHandler itemHandler = entity.getCapability(Capabilities.ItemHandler.ENTITY);
        itemHandler.insertItem(0, new ItemStack(Items.APPLE), false);
        ItemStack removed = itemHandler.extractItem(0, 1, false);

        helper.succeedIf(() -> {
            helper.assertTrue(!removed.isEmpty(), "Removed item is empty");
            helper.assertTrue(itemHandler.getStackInSlot(0).isEmpty(), "Item was not removed");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEntityItemCapFluidBucketAdd(GameTestHelper helper) {
        // Spawn bucket item entity
        ItemEntity entity = helper.spawnItem(Items.BUCKET, POS);

        // Add fluid to bucket
        IFluidHandler fluidHandler = entity.getCapability(Capabilities.FluidHandler.ENTITY, Direction.UP);
        int filled = fluidHandler.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);

        helper.succeedIf(() -> {
            helper.assertTrue(filled == 1000, "Insertion was not 1000");
            helper.assertTrue(fluidHandler.getFluidInTank(0).getAmount() == 1000, "Fluid was not added");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEntityItemCapFluidBucketRemove(GameTestHelper helper) {
        // Spawn water bucket item entity
        ItemEntity entity = helper.spawnItem(Items.WATER_BUCKET, POS);

        // Remove water from bucket
        IFluidHandler fluidHandler = entity.getCapability(Capabilities.FluidHandler.ENTITY, Direction.UP);
        fluidHandler.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
        FluidStack drained = fluidHandler.drain(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);

        helper.succeedIf(() -> {
            helper.assertTrue(drained.getAmount() == 1000, "Removal was not 1000");
            helper.assertTrue(fluidHandler.getFluidInTank(0).isEmpty(), "Fluid was not removed");
        });
    }

}
