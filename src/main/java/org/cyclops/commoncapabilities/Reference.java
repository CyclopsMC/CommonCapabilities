package org.cyclops.commoncapabilities;

import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * Class that can hold basic static things that are better not hard-coded
 * like mod details, texture paths, ID's...
 * @author rubensworks (aka kroeserr)
 *
 */
@SuppressWarnings("javadoc")
public class Reference {

    // Mod info
    public static final String MOD_ID = "commoncapabilities";
    public static final String GA_TRACKING_ID = "UA-65307010-7";
    public static final String VERSION_URL = "https://raw.githubusercontent.com/CyclopsMC/Versions/master/" + MinecraftHelpers.getMinecraftVersionMajorMinor() + "/CommonCapabilities.txt";

    // Paths
    public static final String TEXTURE_PATH_GUI = "textures/gui/";
    public static final String TEXTURE_PATH_SKINS = "textures/skins/";
    public static final String TEXTURE_PATH_MODELS = "textures/models/";
    public static final String TEXTURE_PATH_ENTITIES = "textures/entities/";
    public static final String TEXTURE_PATH_GUIBACKGROUNDS = "textures/gui/title/background/";
    public static final String TEXTURE_PATH_ITEMS = "textures/items/";
    public static final String TEXTURE_PATH_PARTICLES = "textures/particles/";
    public static final String MODEL_PATH = "models/";

    // MOD ID's
    public static final String MOD_VANILLA = "minecraft";
    public static final String MOD_RF_API = "CoFHAPI";
    public static final String MOD_ENDERIO = "enderio";
    public static final String MOD_TCONSTRUCT = "tconstruct";
    public static final String MOD_FORESTRY = "forestry";
    public static final String MOD_IC2 = "ic2";
    public static final String MOD_THERMALEXPANSION = "thermalexpansion";

}
