package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.api.item.ITerraformingBP;
import ic2.core.block.machine.tileentity.TileEntityTerra;
import net.minecraft.item.ItemStack;

/**
 * Worker capability for {@link TileEntityTerra}.
 * @author rubensworks
 */
public class TileTerraWorker extends TileElectricMachineWorkerBase<TileEntityTerra> {

    public TileTerraWorker(TileEntityTerra host) {
        super(host);
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        ItemStack stack = getTile().tfbpSlot.get();
        return super.canWork() || stack != null && getEnergy().getEnergy() >= ((ITerraformingBP) stack.getItem()).getConsume(stack);
    }
}
