package org.cyclops.commoncapabilities.modcompat.vanilla.capability.work;

import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * Worker capability for the vanilla campfire tile entities.
 * @author rubensworks
 */
public class VanillaCampfireWorker implements IWorker {
    private final CampfireBlockEntity campfire;

    public VanillaCampfireWorker(CampfireBlockEntity campfire) {
        this.campfire = campfire;
    }

    @Override
    public boolean hasWork() {
        return campfire.getItems().stream().anyMatch(itemStack -> !itemStack.isEmpty());
    }

    @Override
    public boolean canWork() {
        return campfire.getBlockState().getValue(CampfireBlock.LIT);
    }
}
