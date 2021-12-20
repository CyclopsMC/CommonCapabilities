package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.core.block.kineticgenerator.tileentity.TileEntityWindKineticGenerator;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import TileEntityWindKineticGenerator;

/**
 * Worker capability for {@link TileEntityWindKineticGenerator}.
 * @author rubensworks
 */
public class TileWindKineticGeneratorWorker implements IWorker {

    private final TileEntityWindKineticGenerator host;

    public TileWindKineticGeneratorWorker(TileEntityWindKineticGenerator host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        return true;
    }

    @Override
    public boolean canWork() {
        return host.getActive() || (host.getWindStrength() >= host.getMinWindStrength()
                && host.getWindStrength() <= host.getMaxWindStrength() && host.hasRotor() && host.rotorHasSpace());
    }
}
