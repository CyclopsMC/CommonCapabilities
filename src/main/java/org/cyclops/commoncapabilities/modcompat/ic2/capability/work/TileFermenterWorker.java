package org.cyclops.commoncapabilities.modcompat.ic2.capability.work;

import ic2.api.energy.tile.IHeatSource;
import ic2.api.recipe.IFermenterRecipeManager;
import ic2.api.recipe.Recipes;
import ic2.core.block.machine.tileentity.TileEntityFermenter;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.helper.TileHelpers;

import TileEntityFermenter;

/**
 * Worker capability for {@link TileEntityFermenter}.
 * @author rubensworks
 */
public class TileFermenterWorker implements IWorker {

    private final TileEntityFermenter host;

    public TileFermenterWorker(TileEntityFermenter host) {
        this.host = host;
    }

    @Override
    public boolean hasWork() {
        if (host.getActive()) {
            return true;
        }
        if (host.getInputTank().getFluid() == null) {
            return false;
        }
        IFermenterRecipeManager.FermentationProperty fp = Recipes.fermenter.getFermentationInformation(host.getInputTank().getFluid().getFluid());
        return fp != null && host.getInputTank().getFluidAmount() >= fp.inputAmount && fp.outputAmount <= host.getOutputTank().getCapacity() - host.getOutputTank().getFluidAmount();
    }

    @Override
    public boolean canWork() {
        return host.getActive()
                || (TileHelpers.getSafeTile(host.getWorld(), host.getPos().offset(host.getFacing()), IHeatSource.class) != null);
    }

}
