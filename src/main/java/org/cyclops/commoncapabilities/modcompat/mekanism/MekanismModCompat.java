package org.cyclops.commoncapabilities.modcompat.mekanism;

import org.cyclops.commoncapabilities.Reference;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;

/**
 * Capabilities for Vanilla.
 * @author rubensworks
 */
public class MekanismModCompat implements IModCompat {

    @Override
    public String getId() {
        return Reference.MOD_MEKANISM;
    }

    @Override
    public boolean isEnabledDefault() {
        return true;
    }

    @Override
    public String getComment() {
        return "Ingredient types for chemicals.";
    }

    @Override
    public ICompatInitializer createInitializer() {
        return new MekanismCompatInitializer();
    }

}
