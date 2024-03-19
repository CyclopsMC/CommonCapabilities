package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

/**
 * An item handler wrapper for the composter block.
 * @author rubensworks
 */
public class VanillaBlockComposterItemHandler extends SidedInvWrapper {

    public VanillaBlockComposterItemHandler(BlockState blockState, Level world, BlockPos blockPos, Direction facing) {
        super(((ComposterBlock) Blocks.COMPOSTER).getContainer(blockState, world, blockPos), facing);
    }

}
