package org.cyclops.commoncapabilities.modcompat.enderio;

import crazypants.enderio.machine.AbstractPoweredTaskEntity;
import crazypants.enderio.machine.alloy.TileAlloySmelter;
import crazypants.enderio.machine.buffer.TileBuffer;
import crazypants.enderio.machine.crafter.TileCrafter;
import crazypants.enderio.machine.farm.TileFarmStation;
import crazypants.enderio.machine.killera.TileKillerJoe;
import crazypants.enderio.machine.painter.TileEntityPainter;
import crazypants.enderio.machine.sagmill.TileSagMill;
import crazypants.enderio.machine.slicensplice.TileSliceAndSplice;
import crazypants.enderio.machine.soul.TileSoulBinder;
import crazypants.enderio.machine.spawner.TilePoweredSpawner;
import crazypants.enderio.machine.transceiver.TileTransceiver;
import crazypants.enderio.machine.vat.TileVat;
import crazypants.enderio.machine.wireless.TileWirelessCharger;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.capability.worker.WorkerConfig;
import org.cyclops.commoncapabilities.modcompat.enderio.capability.work.*;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;

import javax.annotation.Nullable;

/**
 * Capabilities for Vanilla.
 * @author rubensworks
 */
public class EnderIOModCompat implements IModCompat {
    @Override
    public String getModID() {
        return Reference.MOD_ENDERIO;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getComment() {
        return "Worker capabilities for EnderIO machines.";
    }

    @Override
    public void onInit(Step initStep) {
        if(initStep == Step.INIT) {
            CapabilityConstructorRegistry registry = CommonCapabilities._instance.getCapabilityConstructorRegistry();
            // Worker
            registerTaskWorker(registry, TileAlloySmelter.class);
            registerTaskWorker(registry, TileSagMill.class);
            registerTaskWorker(registry, TileFarmStation.class);
            registerTaskWorker(registry, TileEntityPainter.class);
            registerTaskWorker(registry, TileVat.class);
            registerTaskWorker(registry, TilePoweredSpawner.class);
            registerTaskWorker(registry, TileSoulBinder.class);
            registerTaskWorker(registry, TileSliceAndSplice.class);
            registerTaskWorker(registry, TileTransceiver.class);
            registry.registerTile(TileCrafter.class,
                    new ICapabilityConstructor<IWorker, TileCrafter>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileCrafter host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileCrafterWorker(host));
                        }
                    });
            registry.registerTile(TileBuffer.class,
                    new ICapabilityConstructor<IWorker, TileBuffer>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileBuffer host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileBufferWorker(host));
                        }
                    });
            registry.registerTile(TileWirelessCharger.class,
                    new ICapabilityConstructor<IWorker, TileWirelessCharger>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileWirelessCharger host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileWirelessChargerWorker(host));
                        }
                    });
            registry.registerTile(TileKillerJoe.class,
                    new ICapabilityConstructor<IWorker, TileKillerJoe>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileKillerJoe host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileKillerJoeWorker(host));
                        }
                    });
        }
    }

    protected static <T extends AbstractPoweredTaskEntity> void registerTaskWorker(
            CapabilityConstructorRegistry registry, Class<T> clazz) {
        registry.registerTile(clazz,
                new ICapabilityConstructor<IWorker, T>() {
                    @Override
                    public Capability<IWorker> getCapability() {
                        return WorkerConfig.CAPABILITY;
                    }

                    @Nullable
                    @Override
                    public ICapabilityProvider createProvider(T host) {
                        return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new AbstractPoweredTaskEntityWorker(host));
                    }
                });
    }
}
