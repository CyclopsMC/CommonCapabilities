package org.cyclops.commoncapabilities.modcompat.ic2;

import ic2.core.block.generator.tileentity.*;
import ic2.core.block.kineticgenerator.tileentity.TileEntitySteamKineticGenerator;
import ic2.core.block.machine.tileentity.*;
import org.cyclops.commoncapabilities.core.Helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Helpers for IC2.
 * @author rubensworks
 */
public class Ic2Helpers {

    public static Method METHOD_TILEIRONFURNACE_CANOPERATE = Helpers.getMethod(TileEntityIronFurnace.class, "canOperate");

    public static Method METHOD_TILECONDENSER_PROCESSOUTPUTSLOT = Helpers.getMethod(TileEntityCondenser.class, "processOutputSlot", boolean.class);

    public static Method METHOD_TILEMINER_PROCESSOUTPUTSLOT = Helpers.getMethod(TileEntityMiner.class, "getOperationPos");

    public static Method METHOD_TILEELETROLYZER_CANOPERATE = Helpers.getMethod(TileEntityElectrolyzer.class, "canOperate");

    public static Method METHOD_TILESTANDARDMACHINE_GETOUTPUT = Helpers.getMethod(TileEntityStandardMachine.class, "getOutput");

    public static Field FIELD_TILEBASEGENERATOR_ENERGY = Helpers.getField(TileEntityBaseGenerator.class, "energy");
    public static Field FIELD_TILEBASEGENERATOR_PRODUCTION = Helpers.getField(TileEntityBaseGenerator.class, "production");

    public static Field FIELD_TILESTEAMKINETICGENERATOR_ISTURBINEFILLED = Helpers.getField(TileEntitySteamKineticGenerator.class, "isTurbineFilledWithWater");
    public static Field FIELD_TILESTEAMKINETICGENERATOR_STEAMTANK = Helpers.getField(TileEntitySteamKineticGenerator.class, "steamTank");

    public static Field FIELD_TILESTIRLINGGENERATOR_ENERGY = Helpers.getField(TileEntityStirlingGenerator.class, "energy");
    public static Field FIELD_TILESTIRLINGGENERATOR_PRODUCTION = Helpers.getField(TileEntityStirlingGenerator.class, "production");
    public static Field FIELD_TILESEMIFLUIDGENERATOR_FLUIDTANK = Helpers.getField(TileEntitySemifluidGenerator.class, "fluidTank");
    public static Field FIELD_TILEKINETICGENERATOR_ENERGY = Helpers.getField(TileEntityKineticGenerator.class, "energy");
    public static Field FIELD_TILEKINETICGENERATOR_PRODUCTION = Helpers.getField(TileEntityKineticGenerator.class, "production");
    public static Field FIELD_TILEGEOGENERATOR_FLUIDTANK = Helpers.getField(TileEntityGeoGenerator.class, "fluidTank");
    public static Field FIELD_TILEELECTRICMACHINE_ENERGY = Helpers.getField(TileEntityElectricMachine.class, "energy");
    public static Field FIELD_TILESFLUIDDISTRIBUTOR_FLUIDTANK = Helpers.getField(TileEntityFluidDistributor.class, "fluidTank");

    public static Field FIELD_TILESOLARDISTILLER_SKYLIGHT = Helpers.getField(TileEntitySolarDestiller.class, "skyLight");

}
