package org.cyclops.commoncapabilities.modcompat.vanilla.capability.work;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * Worker capability for the vanilla furnace tile entities.
 * @author rubensworks
 */
public class VanillaAbstractFurnaceWorker implements IWorker {

    private final AbstractFurnaceBlockEntity furnace;

    public VanillaAbstractFurnaceWorker(AbstractFurnaceBlockEntity furnace) {
        this.furnace = furnace;
    }

    @Override
    public boolean hasWork() {
        ItemStack toMelt = furnace.getItem(0);
        if (toMelt.isEmpty() || !(furnace.getLevel() instanceof ServerLevel serverLevel)) {
            return false;
        }
        SingleRecipeInput input = new SingleRecipeInput(toMelt);
        return furnace.quickCheck.getRecipeFor(input, serverLevel)
                .map(RecipeHolder::value)
                .map(recipe -> !((AbstractCookingRecipe) recipe).assemble(input).isEmpty())
                .orElse(false);
    }

    @Override
    public boolean canWork() {
        return furnace.litTimeRemaining > 0 || furnace.getItem(1).has(DataComponents.COOKING_FUEL);
    }
}
