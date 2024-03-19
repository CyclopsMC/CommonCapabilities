package org.cyclops.commoncapabilities.modcompat.enderio.capability.work;

import crazypants.enderio.machine.crafter.DummyCraftingGrid;
import crazypants.enderio.machine.crafter.TileCrafter;
import org.cyclops.commoncapabilities.core.Helpers;
import org.cyclops.commoncapabilities.modcompat.enderio.EnderIOHelpers;

/**
 * Worker capability for the crafter.
 * @author rubensworks
 */
public class TileCrafterWorker extends AbstractPowerConsumerWorker<TileCrafter> {

    public TileCrafterWorker(TileCrafter tile) {
        super(tile);
    }

    @Override
    public boolean canWork() {
        if (!super.canWork()) {
            return false;
        }
        DummyCraftingGrid craftingGrid = Helpers.getFieldValue(getTile(), EnderIOHelpers.FIELD_TILECRAFTER_CRAFTINGGRID);
        if (craftingGrid == null || !craftingGrid.hasValidRecipe()) {
            return false;
        }
        Boolean canMergeOutput = Helpers.invokeMethod(getTile(), EnderIOHelpers.METHOD_TILECRAFTER_CANMERGEOUTPUT);
        return canMergeOutput != null && canMergeOutput;
    }

    @Override
    public boolean hasWork() {
        for (int i = 0; i < 9; i++) {
            if (getTile().getStackInSlot(i) != null) {
                return true;
            }
        }
        return false;
    }
}
