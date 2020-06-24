package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

/**
 * An item handler wrapper for the composter block.
 * @author rubensworks
 */
public class VanillaBlockComposterItemHandler extends SidedInvWrapper {

    public VanillaBlockComposterItemHandler(BlockState blockState, IWorld world, BlockPos blockPos, Direction facing) {
        super(((ComposterBlock) Blocks.COMPOSTER).createInventory(blockState, world, blockPos), facing);
    }

}
