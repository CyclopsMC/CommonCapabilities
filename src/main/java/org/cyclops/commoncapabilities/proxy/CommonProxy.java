package org.cyclops.commoncapabilities.proxy;

import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxy extends CommonProxyComponent {

    @Override
    public ModBaseNeoForge<?> getMod() {
        return CommonCapabilities._instance;
    }

}
