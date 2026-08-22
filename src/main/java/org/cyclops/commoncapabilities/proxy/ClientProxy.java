package org.cyclops.commoncapabilities.proxy;

import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;

/**
 * Proxy for the client side.
 *
 * @author rubensworks
 *
 */
public class ClientProxy extends ClientProxyComponent {

    public ClientProxy() {
        super(new CommonProxy());
    }

    @Override
    public ModBaseNeoForge<CommonCapabilities> getMod() {
        return CommonCapabilities._instance;
    }

}
