package org.cyclops.commoncapabilities.modcompat.enderio;

import crazypants.enderio.machine.AbstractPoweredTaskEntity;
import crazypants.enderio.machine.IMachineRecipe;
import crazypants.enderio.machine.MachineRecipeInput;
import crazypants.enderio.machine.MachineRecipeRegistry;
import crazypants.enderio.machine.crafter.TileCrafter;
import org.cyclops.commoncapabilities.core.Helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Helpers for EnderIO capabilities.
 * @author rubensworks
 */
public class EnderIOHelpers {

    public static Method METHOD_TILE_GETRECIPEINPUTS = Helpers.getMethod(AbstractPoweredTaskEntity.class, "getRecipeInputs");
    public static Method METHOD_TILECRAFTER_CANMERGEOUTPUT = Helpers.getMethod(TileCrafter.class, "canMergeOutput");
    public static Field FIELD_TILECRAFTER_CRAFTINGGRID = Helpers.getField(TileCrafter.class, "craftingGrid");

    public static IMachineRecipe getCachedMachineRecipe(AbstractPoweredTaskEntity tile) {
        MachineRecipeInput[] recipeInputs = Helpers.invokeMethod(tile, METHOD_TILE_GETRECIPEINPUTS);
        if (recipeInputs != null) {
            return MachineRecipeRegistry.instance.getRecipeForInputs(tile.getMachineName(), recipeInputs);
        }
        return null;
    }

}
