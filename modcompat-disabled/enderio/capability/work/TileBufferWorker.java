package org.cyclops.commoncapabilities.modcompat.enderio.capability.work;

import crazypants.enderio.machine.buffer.TileBuffer;

/**
 * Worker capability for the buffer.
 * @author rubensworks
 */
public class TileBufferWorker extends AbstractPowerConsumerWorker<TileBuffer> {

    public TileBufferWorker(TileBuffer tile) {
        super(tile);
    }

    @Override
    public boolean hasWork() {
        return getTile().hasInventory();
    }
}
