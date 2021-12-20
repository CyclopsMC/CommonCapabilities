package org.cyclops.commoncapabilities.modcompat.enderio.capability.work;

import crazypants.enderio.machine.AbstractPowerConsumerEntity;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import AbstractPowerConsumerEntity;

/**
 * Worker capability for AbstractPoweredTaskEntity machines.
 * @author rubensworks
 */
public class AbstractPowerConsumerWorker<T extends AbstractPowerConsumerEntity> implements IWorker {

    private final T tile;

    public AbstractPowerConsumerWorker(T tile) {
        this.tile = tile;
    }

    protected T getTile() {
        return tile;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return tile.hasPower();
    }

}
