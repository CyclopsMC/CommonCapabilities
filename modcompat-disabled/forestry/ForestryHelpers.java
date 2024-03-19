package org.cyclops.commoncapabilities.modcompat.forestry;

import forestry.core.tiles.TileEngine;
import forestry.core.tiles.TileMill;
import org.cyclops.commoncapabilities.core.Helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Helpers for Forestry capabilities.
 * @author rubensworks
 */
public class ForestryHelpers {

    public static Method METHOD_TILEENGINE_ISBURNING = Helpers.getMethod(TileEngine.class, "isBurning");
    public static Method METHOD_TILEENGINE_MAYBURN = Helpers.getMethod(TileEngine.class, "mayBurn");

    public static Field FIELD_TILEMILL_CHARGE = Helpers.getField(TileMill.class, "charge");

}
