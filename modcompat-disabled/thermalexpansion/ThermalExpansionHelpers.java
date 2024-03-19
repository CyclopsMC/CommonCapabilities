package org.cyclops.commoncapabilities.modcompat.thermalexpansion;

import cofh.thermalexpansion.block.machine.TileCompactor;
import cofh.thermalexpansion.block.machine.TileExtruder;
import cofh.thermalexpansion.block.machine.TileMachineBase;
import cofh.thermalexpansion.block.machine.TileRefinery;
import forestry.core.tiles.TileEngine;
import forestry.core.tiles.TileMill;
import org.cyclops.commoncapabilities.core.Helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Helpers for TE capabilities.
 * @author rubensworks
 */
public class ThermalExpansionHelpers {

    public static Method METHOD_TILEMACHINEBASE_HASVALIDINPUT = Helpers.getMethod(TileMachineBase.class, "hasValidInput");

    public static Field FIELD_TILEREFINERY_AUGMENTPOTION = Helpers.getField(TileRefinery.class, "augmentPotion");
    public static Field FIELD_TILEEXTRUDER_AUGMENTSEDIMANTARY = Helpers.getField(TileExtruder.class, "augmentSedimentary");
    public static Field FIELD_TILECOMPACTOR_MODE = Helpers.getField(TileCompactor.class, "mode");

}
