package org.cyclops.commoncapabilities.modcompat.vanilla.capability.work;

import lombok.SneakyThrows;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import java.lang.reflect.Field;

/**
 * Worker capability for the vanilla furnace tile entities.
 * @author rubensworks
 */
public class VanillaAbstractFurnaceWorker implements IWorker {

    private static Field FIELD_RECIPE_TYPE;
    static {
        try {
            Field fieldRecipeType = AbstractFurnaceBlockEntity.class.getDeclaredField("recipeType");
            fieldRecipeType.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private final AbstractFurnaceBlockEntity furnace;

    public VanillaAbstractFurnaceWorker(AbstractFurnaceBlockEntity furnace) {
        this.furnace = furnace;
    }

    @SneakyThrows
    @Override
    public boolean hasWork() {
        ItemStack toMelt = furnace.getItem(0);
        Recipe<?> recipe = furnace.getLevel().getRecipeManager().getRecipeFor(((RecipeType<? extends AbstractCookingRecipe >) FIELD_RECIPE_TYPE.get(furnace)), furnace, furnace.getLevel()).orElse(null);
        return !toMelt.isEmpty() && recipe != null && !((Recipe<WorldlyContainer>) recipe).assemble(furnace).isEmpty();
    }

    @Override
    public boolean canWork() {
        return furnace.isLit() || furnace.getBurnDuration(furnace.getItem(1)) > 0;
    }
}
