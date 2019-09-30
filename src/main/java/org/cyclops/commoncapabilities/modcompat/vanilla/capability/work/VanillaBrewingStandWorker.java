package org.cyclops.commoncapabilities.modcompat.vanilla.capability.work;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * Worker capability for the vanilla brewing stand tile entity.
 * @author rubensworks
 */
public class VanillaBrewingStandWorker implements IWorker {
    private static final int[] outputSlots = new int[] {0, 1, 2};
    
    private final BrewingStandTileEntity brewingStand;

    public VanillaBrewingStandWorker(BrewingStandTileEntity brewingStand) {
        this.brewingStand = brewingStand;
    }

    @Override
    public boolean hasWork() {
        NonNullList<ItemStack> inputs = NonNullList.withSize(outputSlots.length, ItemStack.EMPTY);
        for (int i = 0; i < inputs.size(); i++) {
            inputs.set(i, brewingStand.getStackInSlot(outputSlots[i]));
        }
        return BrewingRecipeRegistry.canBrew(inputs, brewingStand.getStackInSlot(outputSlots.length), outputSlots);
    }

    @Override
    public boolean canWork() {
        return brewingStand.fuel > 0;
    }
}
