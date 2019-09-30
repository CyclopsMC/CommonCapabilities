package org.cyclops.commoncapabilities.modcompat.vanilla.capability.work;

import net.minecraft.block.CampfireBlock;
import net.minecraft.tileentity.CampfireTileEntity;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * Worker capability for the vanilla campfire tile entities.
 * @author rubensworks
 */
public class VanillaCampfireWorker implements IWorker {
    private final CampfireTileEntity campfire;

    public VanillaCampfireWorker(CampfireTileEntity campfire) {
        this.campfire = campfire;
    }

    @Override
    public boolean hasWork() {
        return campfire.getInventory().stream().anyMatch(itemStack -> !itemStack.isEmpty());
    }

    @Override
    public boolean canWork() {
        return campfire.getBlockState().get(CampfireBlock.LIT);
    }
}
