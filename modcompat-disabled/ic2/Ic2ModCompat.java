package org.cyclops.commoncapabilities.modcompat.ic2;

import ic2.api.reactor.IReactor;
import ic2.core.block.generator.tileentity.*;
import ic2.core.block.heatgenerator.tileentity.TileEntityElectricHeatGenerator;
import ic2.core.block.heatgenerator.tileentity.TileEntityFluidHeatGenerator;
import ic2.core.block.heatgenerator.tileentity.TileEntitySolidHeatGenerator;
import ic2.core.block.kineticgenerator.tileentity.TileEntitySteamKineticGenerator;
import ic2.core.block.kineticgenerator.tileentity.TileEntityWindKineticGenerator;
import ic2.core.block.machine.tileentity.*;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import ic2.core.item.tool.ItemToolWrench;
import ic2.core.item.tool.ItemToolWrenchElectric;
import net.minecraft.item.ItemStack;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.api.capability.wrench.DefaultWrench;
import org.cyclops.commoncapabilities.api.capability.wrench.IWrench;
import org.cyclops.commoncapabilities.capability.temperature.TemperatureConfig;
import org.cyclops.commoncapabilities.capability.worker.WorkerConfig;
import org.cyclops.commoncapabilities.capability.wrench.WrenchConfig;
import org.cyclops.commoncapabilities.modcompat.ic2.capability.temperature.TileReactorTemperature;
import org.cyclops.commoncapabilities.modcompat.ic2.capability.work.*;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;

import javax.annotation.Nullable;

/**
 * Capabilities for IC2.
 *
 * @author rubensworks
 */
public class Ic2ModCompat implements IModCompat {
    @Override
    public String getModID() {
        return Reference.MOD_IC2;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getComment() {
        return "Temperature and wrench capabilities for IC2 tiles and items.";
    }

    @Override
    public void onInit(Step initStep) {
        if (initStep == Step.PREINIT) {
            // Don't crash when IC2Classic is loaded.
            // Because it shares the same main mod id with IC2Exp
            // https://github.com/CyclopsMC/CyclopsCore/issues/28
            try {
                Class.forName("ic2.core.item.tool.ItemToolWrenchElectric");
            } catch (ClassNotFoundException e) {
                return;
            }

            CapabilityConstructorRegistry registry = CommonCapabilities._instance.getCapabilityConstructorRegistry();

            // Wrench
            registry.registerItem(ItemToolWrenchElectric.class,
                    new ICapabilityConstructor<IWrench, ItemToolWrenchElectric, ItemStack>() {
                        @Override
                        public Capability<IWrench> getCapability() {
                            return WrenchConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemToolWrenchElectric hostType, final ItemStack host) {
                            return new DefaultCapabilityProvider<>(WrenchConfig.CAPABILITY, new DefaultWrench());
                        }
                    });
            registry.registerItem(ItemToolWrench.class,
                    new ICapabilityConstructor<IWrench, ItemToolWrench, ItemStack>() {
                        @Override
                        public Capability<IWrench> getCapability() {
                            return WrenchConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemToolWrench hostType, final ItemStack host) {
                            return new DefaultCapabilityProvider<>(WrenchConfig.CAPABILITY, new DefaultWrench());
                        }
                    });

            // Temperature
            registry.registerInheritableTile(IReactor.class,
                    new SimpleCapabilityConstructor<ITemperature, IReactor>() {

                        @Override
                        public Capability<ITemperature> getCapability() {
                            return TemperatureConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(IReactor host) {
                            return new DefaultCapabilityProvider<>(TemperatureConfig.CAPABILITY, new TileReactorTemperature(host));
                        }
                    });

            // Worker: generators
            registerTileBaseGenerator(registry, TileEntityGenerator.class);
            registry.registerTile(TileEntityGeoGenerator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityGeoGenerator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityGeoGenerator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileGeoGeneratorWorker(host));
                        }
                    });
            registry.registerTile(TileEntityKineticGenerator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityKineticGenerator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityKineticGenerator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileKineticGeneratorWorker(host));
                        }
                    });
            registerTileBaseGenerator(registry, TileEntityRTGenerator.class);
            registry.registerTile(TileEntitySemifluidGenerator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntitySemifluidGenerator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntitySemifluidGenerator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileSemifluidGeneratorWorker(host));
                        }
                    });
            registry.registerTile(TileEntitySolarGenerator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntitySolarGenerator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntitySolarGenerator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileSolarGeneratorWorker(host));
                        }
                    });
            registry.registerTile(TileEntityStirlingGenerator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityStirlingGenerator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityStirlingGenerator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileStirlingGeneratorWorker(host));
                        }
                    });
            registerTileBaseGenerator(registry, TileEntityWaterGenerator.class);
            registerTileBaseGenerator(registry, TileEntityWindGenerator.class);
            registry.registerTile(TileEntityElectricHeatGenerator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityElectricHeatGenerator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityElectricHeatGenerator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileElectricHeatGeneratorWorker(host));
                        }
                    });
            registry.registerTile(TileEntityFluidHeatGenerator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityFluidHeatGenerator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityFluidHeatGenerator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileFluidHeatGeneratorWorker(host));
                        }
                    });
            registry.registerTile(TileEntitySolidHeatGenerator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntitySolidHeatGenerator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntitySolidHeatGenerator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileSolidHeatGeneratorWorker(host));
                        }
                    });
            registry.registerTile(TileEntitySteamKineticGenerator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntitySteamKineticGenerator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntitySteamKineticGenerator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileSteamKineticGeneratorWorker(host));
                        }
                    });
            registry.registerTile(TileEntityWindKineticGenerator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityWindKineticGenerator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityWindKineticGenerator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileWindKineticGeneratorWorker(host));
                        }
                    });
            registry.registerTile(TileEntityNuclearReactorElectric.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityNuclearReactorElectric>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityNuclearReactorElectric host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileNuclearReactorWorker(host));
                        }
                    });

            // Worker: machines
            registerTileStandardMachine(registry, TileEntityMacerator.class);
            registerTileStandardMachine(registry, TileEntityElectricFurnace.class);
            registerTileStandardMachine(registry, TileEntityFluidBottler.class);
            registerTileStandardMachine(registry, TileEntityCanner.class);
            registerTileStandardMachine(registry, TileEntitySolidCanner.class);
            registerTileStandardMachine(registry, TileEntityCompressor.class);
            registerTileStandardMachine(registry, TileEntityExtractor.class);
            registerTileStandardMachine(registry, TileEntityRecycler.class);
            registerTileStandardMachine(registry, TileEntityBlockCutter.class);
            registerTileStandardMachine(registry, TileEntityCentrifuge.class);
            registerTileStandardMachine(registry, TileEntityMetalFormer.class);
            registerTileStandardMachine(registry, TileEntityOreWashing.class);
            registry.registerTile(TileEntityIronFurnace.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityIronFurnace>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityIronFurnace host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileIronFurnaceWorker(host));
                        }
                    });
            registry.registerTile(TileEntityCondenser.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityCondenser>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityCondenser host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileCondensorWorker(host));
                        }
                    });
            registry.registerTile(TileEntityFluidDistributor.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityFluidDistributor>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityFluidDistributor host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileFluidDistributorWorker(host));
                        }
                    });
            registry.registerTile(TileEntityFluidRegulator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityFluidRegulator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityFluidRegulator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileFluidRegulatorWorker(host));
                        }
                    });
            registry.registerTile(TileEntityPump.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityPump>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityPump host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TilePumpWorker(host));
                        }
                    });
            registry.registerTile(TileEntitySolarDestiller.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntitySolarDestiller>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntitySolarDestiller host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileSolarDistillerWorker(host));
                        }
                    });
            registry.registerTile(TileEntitySteamGenerator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntitySteamGenerator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntitySteamGenerator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileSteamGeneratorWorker(host));
                        }
                    });
            registry.registerTile(TileEntityItemBuffer.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityItemBuffer>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityItemBuffer host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileItemBufferWorker(host));
                        }
                    });
            registry.registerTile(TileEntityMagnetizer.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityMagnetizer>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityMagnetizer host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileMagnetizerWorker(host));
                        }
                    });
            registry.registerTile(TileEntityTerra.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityTerra>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityTerra host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileTerraWorker(host));
                        }
                    });
            registry.registerTile(TileEntityTeleporter.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityTeleporter>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityTeleporter host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileTeleporterWorker(host));
                        }
                    });
            registry.registerTile(TileEntityTesla.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityTesla>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityTesla host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileTeslaWorker(host));
                        }
                    });
            registry.registerTile(TileEntityBlastFurnace.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityBlastFurnace>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityBlastFurnace host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileBlastFurnaceWorker(host));
                        }
                    });
            registry.registerTile(TileEntityFermenter.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityFermenter>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityFermenter host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileFermenterWorker(host));
                        }
                    });
            registry.registerTile(TileEntityInduction.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityInduction>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityInduction host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileInductionWorker(host));
                        }
                    });
            registry.registerTile(TileEntityAdvMiner.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityAdvMiner>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityAdvMiner host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileAdvMinerWorker(host));
                        }
                    });
            registry.registerTile(TileEntityCropHarvester.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityCropHarvester>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityCropHarvester host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileCropHarvesterWorker(host));
                        }
                    });
            registry.registerTile(TileEntityCropmatron.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityCropmatron>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityCropmatron host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileCropmatronWorker(host));
                        }
                    });
            registry.registerTile(TileEntityMiner.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityMiner>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityMiner host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileMinerWorker(host));
                        }
                    });
            registry.registerTile(TileEntityElectrolyzer.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityElectrolyzer>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityElectrolyzer host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileElectrolyzerWorker(host));
                        }
                    });
            registry.registerTile(TileEntityMatter.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityMatter>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityMatter host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileMatterWorker(host));
                        }
                    });
            registry.registerTile(TileEntityReplicator.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityReplicator>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityReplicator host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileReplicatorWorker(host));
                        }
                    });
            registry.registerTile(TileEntityScanner.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityScanner>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityScanner host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileScannerWorker(host));
                        }
                    });
            registry.registerTile(TileEntityChunkloader.class,
                    new SimpleCapabilityConstructor<IWorker, TileEntityChunkloader>() {

                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileEntityChunkloader host) {
                            return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileChunkloaderWorker(host));
                        }
                    });
        }
    }

    protected static <T extends TileEntityBaseGenerator> void registerTileBaseGenerator(
            CapabilityConstructorRegistry registry, Class<T> clazz) {
        registry.registerTile(clazz,
                new SimpleCapabilityConstructor<IWorker, T>() {
                    @Override
                    public Capability<IWorker> getCapability() {
                        return WorkerConfig.CAPABILITY;
                    }

                    @Nullable
                    @Override
                    public ICapabilityProvider createProvider(T host) {
                        return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileBaseGeneratorWorker<>(host));
                    }
                });
    }

    protected static <T extends TileEntityStandardMachine> void registerTileStandardMachine(
            CapabilityConstructorRegistry registry, Class<T> clazz) {
        registry.registerTile(clazz,
                new SimpleCapabilityConstructor<IWorker, T>() {
                    @Override
                    public Capability<IWorker> getCapability() {
                        return WorkerConfig.CAPABILITY;
                    }

                    @Nullable
                    @Override
                    public ICapabilityProvider createProvider(T host) {
                        return new DefaultCapabilityProvider<>(WorkerConfig.CAPABILITY, new TileStandardMachineWorker(host));
                    }
                });
    }

}
