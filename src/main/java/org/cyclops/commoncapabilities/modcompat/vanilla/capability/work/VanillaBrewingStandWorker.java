package org.cyclops.commoncapabilities.modcompat.vanilla.capability.work;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * Worker capability for the vanilla brewing stand tile entity.
 * @author rubensworks
 */
public class VanillaBrewingStandWorker implements IWorker {

    private final BrewingStandBlockEntity brewingStand;

    public VanillaBrewingStandWorker(BrewingStandBlockEntity brewingStand) {
        this.brewingStand = brewingStand;
    }

    @Override
    public boolean hasWork() {
        // Brewing is recipe-driven now, so defer to the brewing stand's own check
        return brewingStand.getLevel() instanceof ServerLevel serverLevel
                && BrewingStandBlockEntity.isBrewable(serverLevel, brewingStand);
    }

    @Override
    public boolean canWork() {
        return brewingStand.fuel > 0;
    }
}
