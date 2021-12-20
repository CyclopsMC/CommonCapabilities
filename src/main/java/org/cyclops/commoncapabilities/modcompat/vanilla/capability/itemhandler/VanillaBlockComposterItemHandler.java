package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

/**
 * An item handler wrapper for the composter block.
 * @author rubensworks
 */
public class VanillaBlockComposterItemHandler extends SidedInvWrapper {

    public VanillaBlockComposterItemHandler(BlockState blockState, LevelAccessor world, BlockPos blockPos, Direction facing) {
        super(((ComposterBlock) Blocks.COMPOSTER).getContainer(blockState, world, blockPos), facing);
    }

}
