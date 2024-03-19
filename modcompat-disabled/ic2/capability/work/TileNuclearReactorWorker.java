package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileEntityNuclearReactorElectric;

/**
 * Worker capability for {@link TileEntityNuclearReactorElectric}.
 * @author rubensworks
 */
public class TileNuclearReactorWorker implements IWorker {

    private final TileEntityNuclearReactorElectric host;

    public TileNuclearReactorWorker(TileEntityNuclearReactorElectric host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return host.produceEnergy() && host.isFluidCooled();
    }
}
