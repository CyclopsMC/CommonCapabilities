package org.cyclops.commoncapabilities.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.api.capability.Capabilities;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * @author rubensworks
 */
@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsVanillaCapabilitiesWorker {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 1, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapFurnaceOff(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.FURNACE);

        helper.succeedIf(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), false, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), false, "Worker can work does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapFurnaceOn(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.FURNACE);

        FurnaceBlockEntity blockEntity = helper.getBlockEntity(POS);
        blockEntity.setItem(0, new ItemStack(Blocks.COBBLESTONE));
        blockEntity.setItem(1, new ItemStack(Items.COAL));

        helper.succeedWhen(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), true, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), true, "Worker can work does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapFurnaceLit(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.FURNACE);

        FurnaceBlockEntity blockEntity = helper.getBlockEntity(POS);
        blockEntity.setItem(1, new ItemStack(Items.COAL));

        helper.succeedWhen(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), false, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), true, "Worker can work does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapBlastFurnaceOff(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.BLAST_FURNACE);

        helper.succeedIf(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), false, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), false, "Worker can work does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapBlastFurnaceOn(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.BLAST_FURNACE);

        BlastFurnaceBlockEntity blockEntity = helper.getBlockEntity(POS);
        blockEntity.setItem(0, new ItemStack(Blocks.IRON_ORE));
        blockEntity.setItem(1, new ItemStack(Items.COAL));

        helper.succeedWhen(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), true, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), true, "Worker can work does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapSmokerOff(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.SMOKER);

        helper.succeedIf(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), false, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), false, "Worker can work does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapSmokerOn(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.SMOKER);

        SmokerBlockEntity blockEntity = helper.getBlockEntity(POS);
        blockEntity.setItem(0, new ItemStack(Items.PORKCHOP));
        blockEntity.setItem(1, new ItemStack(Items.COAL));

        helper.succeedWhen(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), true, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), true, "Worker can work does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapCampfireOff(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, false));

        helper.succeedIf(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), false, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), false, "Worker can work does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapCampfireOn(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, true));

        helper.succeedWhen(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), false, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), true, "Worker can work does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapCampfireFilled(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, false));

        CampfireBlockEntity blockEntity = helper.getBlockEntity(POS);
        blockEntity.placeFood(helper.makeMockPlayer(GameType.SURVIVAL), new ItemStack(Items.PORKCHOP), 10);

        helper.succeedWhen(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), true, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), false, "Worker can work does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapBrewingstandOff(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.BREWING_STAND);

        helper.succeedIf(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), false, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), false, "Worker can work does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapBrewingstandFueled(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.BREWING_STAND);

        BrewingStandBlockEntity blockEntity = helper.getBlockEntity(POS);
        blockEntity.fuel = 10;

        helper.succeedIf(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), false, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), true, "Worker can work does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockWorkerCapBrewingstandValidInput(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.BREWING_STAND);

        BrewingStandBlockEntity blockEntity = helper.getBlockEntity(POS);
        ItemStack waterBottle = new ItemStack(Items.POTION);
        waterBottle.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        blockEntity.setItem(0, waterBottle);
        blockEntity.setItem(1, waterBottle);
        blockEntity.setItem(2, waterBottle);
        blockEntity.setItem(3, new ItemStack(Items.NETHER_WART));

        helper.succeedIf(() -> {
            IWorker worker = helper.getLevel().getCapability(Capabilities.Worker.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(worker != null, "Worker does not exist");
            helper.assertValueEqual(worker.hasWork(), true, "Worker has work does not match");
            helper.assertValueEqual(worker.canWork(), false, "Worker can work does not match");
        });
    }

}
