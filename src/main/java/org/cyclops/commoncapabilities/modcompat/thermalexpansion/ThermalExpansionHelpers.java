package org.cyclops.commoncapabilities.modcompat.thermalexpansion;

import cofh.thermalexpansion.block.machine.TileMachineBase;
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

}
