package org.cyclops.commoncapabilities.modcompat.visualworkbench;

import org.cyclops.commoncapabilities.Reference;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;

/**
 * Capabilities for Vanilla.
 * @author rubensworks
 */
public class VisualWorkbenchModCompat implements IModCompat {

    @Override
    public String getId() {
        return Reference.MOD_VISUALWORKBENCH;
    }

    @Override
    public boolean isEnabledDefault() {
        return true;
    }

    @Override
    public String getComment() {
        return "Visual Workbench capabilities.";
    }

    @Override
    public ICompatInitializer createInitializer() {
        return new VisualWorkbenchInitializer();
    }

}
