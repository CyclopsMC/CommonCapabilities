package org.cyclops.commoncapabilities.modcompat.vanilla.capability.work;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * Worker capability for the vanilla furnace tile entities.
 * @author rubensworks
 */
public class VanillaAbstractFurnaceWorker implements IWorker {
    private final AbstractFurnaceTileEntity furnace;

    public VanillaAbstractFurnaceWorker(AbstractFurnaceTileEntity furnace) {
        this.furnace = furnace;
    }

    @Override
    public boolean hasWork() {
        ItemStack toMelt = furnace.getItem(0);
        IRecipe<?> recipe = furnace.getLevel().getRecipeManager().getRecipeFor(furnace.recipeType, furnace, furnace.getLevel()).orElse(null);
        return !toMelt.isEmpty() && furnace.canBurn(recipe);
    }

    @Override
    public boolean canWork() {
        return furnace.isLit() || furnace.getBurnDuration(furnace.getItem(1)) > 0;
    }
}
