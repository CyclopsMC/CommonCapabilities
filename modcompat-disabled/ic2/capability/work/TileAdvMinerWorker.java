package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.api.item.ElectricItem;
import ic2.core.block.machine.tileentity.TileEntityAdvMiner;
import ic2.core.item.tool.ItemScanner;
import ic2.core.util.StackUtil;

/**
 * Worker capability for {@link TileEntityAdvMiner}.
 * @author rubensworks
 */
public class TileAdvMinerWorker extends TileElectricMachineWorkerBase<TileEntityAdvMiner> {
    public TileAdvMinerWorker(TileEntityAdvMiner tile) {
        super(tile);
    }

    @Override
    public boolean canWork() {
        return super.canWork()
                || (getEnergy().getEnergy() >= 512.0D
                && !getTile().getWorld().isBlockPowered(getTile().getPos())
                && !StackUtil.isEmpty(getTile().scannerSlot.get())
                && ElectricItem.manager.canUse(getTile().scannerSlot.get(), 64.0D)
        );
    }
}
