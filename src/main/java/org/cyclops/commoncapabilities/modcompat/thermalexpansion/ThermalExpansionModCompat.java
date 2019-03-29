package org.cyclops.commoncapabilities.modcompat.thermalexpansion;

import cofh.thermalfoundation.item.ItemWrench;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.api.capability.wrench.DefaultWrench;
import org.cyclops.commoncapabilities.api.capability.wrench.IWrench;
import org.cyclops.commoncapabilities.capability.wrench.WrenchConfig;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;

import javax.annotation.Nullable;

/**
 * Capabilities for Thermal Expansion.
 * @author rubensworks
 */
public class ThermalExpansionModCompat implements IModCompat {
    @Override
    public String getModID() {
        return Reference.MOD_THERMALEXPANSION;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getComment() {
        return "Capabilities for Thermal Expansion.";
    }

    @Override
    public void onInit(Step initStep) {
        if(initStep == Step.INIT) {
            CapabilityConstructorRegistry registry = CommonCapabilities._instance.getCapabilityConstructorRegistry();

            // Wrench
            registry.registerItem(ItemWrench.class,
                    new ICapabilityConstructor<IWrench, ItemWrench, ItemStack>() {
                        @Override
                        public Capability<IWrench> getCapability() {
                            return WrenchConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemWrench hostType, final ItemStack host) {
                            return new DefaultCapabilityProvider<>(this, new DefaultWrench());
                        }
                    });
        }
    }

}
