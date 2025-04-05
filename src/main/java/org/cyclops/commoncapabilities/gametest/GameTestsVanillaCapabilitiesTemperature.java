package org.cyclops.commoncapabilities.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.api.capability.Capabilities;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;

/**
 * @author rubensworks
 */
@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsVanillaCapabilitiesTemperature {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 1, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockTemperatureCapFurnaceOff(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.FURNACE);

        helper.succeedIf(() -> {
            ITemperature temperature = helper.getLevel().getCapability(Capabilities.Temperature.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(temperature != null, "Temperature handler does not exist");
            helper.assertValueEqual(temperature.getTemperature(), ITemperature.ZERO_CELCIUS, "Temperature does not match");
            helper.assertValueEqual(temperature.getMaximumTemperature(), Double.MAX_VALUE, "Temperature max does not match");
            helper.assertValueEqual(temperature.getMinimumTemperature(), ITemperature.ZERO_CELCIUS, "Temperature min does not match");
            helper.assertValueEqual(temperature.getDefaultTemperature(), ITemperature.ZERO_CELCIUS, "Temperature default does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockTemperatureCapFurnaceOn(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.FURNACE);

        FurnaceBlockEntity blockEntity = helper.getBlockEntity(POS);
        blockEntity.setItem(0, new ItemStack(Blocks.COBBLESTONE));
        blockEntity.setItem(1, new ItemStack(Items.COAL));

        helper.succeedWhen(() -> {
            ITemperature temperature = helper.getLevel().getCapability(Capabilities.Temperature.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(temperature != null, "Temperature handler does not exist");
            helper.assertValueEqual(temperature.getTemperature(), ITemperature.ZERO_CELCIUS + 1600, "Temperature does not match");
            helper.assertValueEqual(temperature.getMaximumTemperature(), Double.MAX_VALUE, "Temperature max does not match");
            helper.assertValueEqual(temperature.getMinimumTemperature(), ITemperature.ZERO_CELCIUS, "Temperature min does not match");
            helper.assertValueEqual(temperature.getDefaultTemperature(), ITemperature.ZERO_CELCIUS, "Temperature default does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockTemperatureCapBlastFurnaceOff(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.BLAST_FURNACE);

        helper.succeedIf(() -> {
            ITemperature temperature = helper.getLevel().getCapability(Capabilities.Temperature.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(temperature != null, "Temperature handler does not exist");
            helper.assertValueEqual(temperature.getTemperature(), ITemperature.ZERO_CELCIUS, "Temperature does not match");
            helper.assertValueEqual(temperature.getMaximumTemperature(), Double.MAX_VALUE, "Temperature max does not match");
            helper.assertValueEqual(temperature.getMinimumTemperature(), ITemperature.ZERO_CELCIUS, "Temperature min does not match");
            helper.assertValueEqual(temperature.getDefaultTemperature(), ITemperature.ZERO_CELCIUS, "Temperature default does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockTemperatureCapBlastFurnaceOn(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.BLAST_FURNACE);

        BlastFurnaceBlockEntity blockEntity = helper.getBlockEntity(POS);
        blockEntity.setItem(0, new ItemStack(Blocks.IRON_ORE));
        blockEntity.setItem(1, new ItemStack(Items.COAL));

        helper.succeedWhen(() -> {
            ITemperature temperature = helper.getLevel().getCapability(Capabilities.Temperature.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(temperature != null, "Temperature handler does not exist");
            helper.assertValueEqual(temperature.getTemperature(), ITemperature.ZERO_CELCIUS + 800, "Temperature does not match");
            helper.assertValueEqual(temperature.getMaximumTemperature(), Double.MAX_VALUE, "Temperature max does not match");
            helper.assertValueEqual(temperature.getMinimumTemperature(), ITemperature.ZERO_CELCIUS, "Temperature min does not match");
            helper.assertValueEqual(temperature.getDefaultTemperature(), ITemperature.ZERO_CELCIUS, "Temperature default does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockTemperatureCapSmokerOff(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.SMOKER);

        helper.succeedIf(() -> {
            ITemperature temperature = helper.getLevel().getCapability(Capabilities.Temperature.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(temperature != null, "Temperature handler does not exist");
            helper.assertValueEqual(temperature.getTemperature(), ITemperature.ZERO_CELCIUS, "Temperature does not match");
            helper.assertValueEqual(temperature.getMaximumTemperature(), Double.MAX_VALUE, "Temperature max does not match");
            helper.assertValueEqual(temperature.getMinimumTemperature(), ITemperature.ZERO_CELCIUS, "Temperature min does not match");
            helper.assertValueEqual(temperature.getDefaultTemperature(), ITemperature.ZERO_CELCIUS, "Temperature default does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockTemperatureCapSmokerOn(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.SMOKER);

        SmokerBlockEntity blockEntity = helper.getBlockEntity(POS);
        blockEntity.setItem(0, new ItemStack(Items.PORKCHOP));
        blockEntity.setItem(1, new ItemStack(Items.COAL));

        helper.succeedWhen(() -> {
            ITemperature temperature = helper.getLevel().getCapability(Capabilities.Temperature.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(temperature != null, "Temperature handler does not exist");
            helper.assertValueEqual(temperature.getTemperature(), ITemperature.ZERO_CELCIUS + 800, "Temperature does not match");
            helper.assertValueEqual(temperature.getMaximumTemperature(), Double.MAX_VALUE, "Temperature max does not match");
            helper.assertValueEqual(temperature.getMinimumTemperature(), ITemperature.ZERO_CELCIUS, "Temperature min does not match");
            helper.assertValueEqual(temperature.getDefaultTemperature(), ITemperature.ZERO_CELCIUS, "Temperature default does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockTemperatureCapCampfireOff(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, false));

        helper.succeedIf(() -> {
            ITemperature temperature = helper.getLevel().getCapability(Capabilities.Temperature.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(temperature != null, "Temperature handler does not exist");
            helper.assertValueEqual(temperature.getTemperature(), ITemperature.ZERO_CELCIUS, "Temperature does not match");
            helper.assertValueEqual(temperature.getMaximumTemperature(), ITemperature.ZERO_CELCIUS + 1100, "Temperature max does not match");
            helper.assertValueEqual(temperature.getMinimumTemperature(), ITemperature.ZERO_CELCIUS, "Temperature min does not match");
            helper.assertValueEqual(temperature.getDefaultTemperature(), ITemperature.ZERO_CELCIUS, "Temperature default does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockTemperatureCapCampfireOn(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, true));

        helper.succeedWhen(() -> {
            ITemperature temperature = helper.getLevel().getCapability(Capabilities.Temperature.BLOCK, helper.absolutePos(POS), Direction.NORTH);
            helper.assertTrue(temperature != null, "Temperature handler does not exist");
            helper.assertValueEqual(temperature.getTemperature(), ITemperature.ZERO_CELCIUS + 1100, "Temperature does not match");
            helper.assertValueEqual(temperature.getMaximumTemperature(), ITemperature.ZERO_CELCIUS + 1100, "Temperature max does not match");
            helper.assertValueEqual(temperature.getMinimumTemperature(), ITemperature.ZERO_CELCIUS, "Temperature min does not match");
            helper.assertValueEqual(temperature.getDefaultTemperature(), ITemperature.ZERO_CELCIUS, "Temperature default does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemTemperatureCapBucketEmpty(GameTestHelper helper) {
        ItemStack itemStack = new ItemStack(Items.BUCKET);

        helper.succeedIf(() -> {
            ITemperature temperature = itemStack.getCapability(Capabilities.Temperature.ITEM);
            helper.assertTrue(temperature != null, "Temperature handler does not exist");
            helper.assertValueEqual(temperature.getTemperature(), 0D, "Temperature does not match");
            helper.assertValueEqual(temperature.getMaximumTemperature(), Double.MAX_VALUE, "Temperature max does not match");
            helper.assertValueEqual(temperature.getMinimumTemperature(), ITemperature.ZERO_CELCIUS, "Temperature min does not match");
            helper.assertValueEqual(temperature.getDefaultTemperature(), 0D, "Temperature default does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemTemperatureCapBucketWater(GameTestHelper helper) {
        ItemStack itemStack = new ItemStack(Items.WATER_BUCKET);

        helper.succeedIf(() -> {
            ITemperature temperature = itemStack.getCapability(Capabilities.Temperature.ITEM);
            helper.assertTrue(temperature != null, "Temperature handler does not exist");
            helper.assertValueEqual(temperature.getTemperature(), 300D, "Temperature does not match");
            helper.assertValueEqual(temperature.getMaximumTemperature(), Double.MAX_VALUE, "Temperature max does not match");
            helper.assertValueEqual(temperature.getMinimumTemperature(), ITemperature.ZERO_CELCIUS, "Temperature min does not match");
            helper.assertValueEqual(temperature.getDefaultTemperature(), 300D, "Temperature default does not match");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testItemTemperatureCapBucketLava(GameTestHelper helper) {
        ItemStack itemStack = new ItemStack(Items.LAVA_BUCKET);

        helper.succeedIf(() -> {
            ITemperature temperature = itemStack.getCapability(Capabilities.Temperature.ITEM);
            helper.assertTrue(temperature != null, "Temperature handler does not exist");
            helper.assertValueEqual(temperature.getTemperature(), 1300D, "Temperature does not match");
            helper.assertValueEqual(temperature.getMaximumTemperature(), Double.MAX_VALUE, "Temperature max does not match");
            helper.assertValueEqual(temperature.getMinimumTemperature(), ITemperature.ZERO_CELCIUS, "Temperature min does not match");
            helper.assertValueEqual(temperature.getDefaultTemperature(), 1300D, "Temperature default does not match");
        });
    }

}
