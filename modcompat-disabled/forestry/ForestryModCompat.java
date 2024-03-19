package org.cyclops.commoncapabilities.modcompat.forestry;

import forestry.core.config.Constants;
import forestry.core.items.ItemWrench;
import forestry.core.tiles.TileAnalyzer;
import forestry.core.tiles.TileEngine;
import forestry.core.tiles.TilePowered;
import forestry.energy.tiles.TileEngineBiogas;
import forestry.energy.tiles.TileEngineClockwork;
import forestry.energy.tiles.TileEnginePeat;
import forestry.factory.tiles.*;
import forestry.farming.tiles.TileFarmGearbox;
import net.minecraft.item.ItemStack;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.api.capability.wrench.DefaultWrench;
import org.cyclops.commoncapabilities.api.capability.wrench.IWrench;
import org.cyclops.commoncapabilities.capability.recipehandler.RecipeHandlerConfig;
import org.cyclops.commoncapabilities.capability.temperature.TemperatureConfig;
import org.cyclops.commoncapabilities.capability.worker.WorkerConfig;
import org.cyclops.commoncapabilities.capability.wrench.WrenchConfig;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler.TileCarpenterRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler.TileCentrifugeRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler.TileFabricatorRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler.TileFermenterRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler.TileSqueezerRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler.TileStillRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.temperature.TileEngineTemperature;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.work.TileEngineWorker;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.work.TileFarmGearboxWorker;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.work.TileMoistenerWorker;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.work.TilePoweredWorker;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.work.TileRainmakerWorker;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.work.TileRaintankWorker;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Capabilities for EnderIO.
 * @author rubensworks
 */
public class ForestryModCompat implements IModCompat {
    @Override
    public String getModID() {
        return Reference.MOD_FORESTRY;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getComment() {
        return "Worker, Wrench and Temperature capabilities for Forestry machines.";
    }

    @Override
    public void onInit(Step initStep) {
        if(initStep == Step.INIT) {
            CapabilityConstructorRegistry registry = CommonCapabilities._instance.getCapabilityConstructorRegistry();
            // Temperature
            registerEngineTemperature(registry, TileEngineBiogas.class, ITemperature.ZERO_CELCIUS + Constants.ENGINE_BRONZE_HEAT_MAX);
            registerEngineTemperature(registry, TileEngineClockwork.class, ITemperature.ZERO_CELCIUS + 300000);
            // TODO: re-enable when IC2 is back in 1.11
            //registerEngineTemperature(registry, TileEngineElectric.class, ITemperature.ZERO_CELCIUS + Constants.ENGINE_ELECTRIC_HEAT_MAX);
            registerEngineTemperature(registry, TileEnginePeat.class, ITemperature.ZERO_CELCIUS + Constants.ENGINE_COPPER_HEAT_MAX);

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
                            return new DefaultCapabilityProvider<>(WrenchConfig.CAPABILITY, new DefaultWrench());
                        }
                    });

            // Worker
            registerTilePoweredWorker(registry, TileStill.class);
            registerTilePoweredWorker(registry, TileCarpenter.class);
            registerTilePoweredWorker(registry, TileFermenter.class);
            registerTilePoweredWorker(registry, TileSqueezer.class);
            registerTilePoweredWorker(registry, TileBottler.class);
            registerTilePoweredWorker(registry, TileCentrifuge.class);
            registerTilePoweredWorker(registry, TileAnalyzer.class);
            registerTilePoweredWorker(registry, TileFabricator.class);

            registerTileEngineWorker(registry, TileEngineBiogas.class);
            registerTileEngineWorker(registry, TileEngineClockwork.class);
            // TODO: re-enable when IC2 is back in 1.11
            //registerTileEngineWorker(registry, TileEngineElectric.class);
            registerTileEngineWorker(registry, TileEnginePeat.class);

            registry.registerTile(TileMoistener.class,
                    new SimpleCapabilityConstructor<IWorker, TileMoistener>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileMoistener host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new TileMoistenerWorker(host));
                        }
                    });
            registry.registerTile(TileMillRainmaker.class,
                    new SimpleCapabilityConstructor<IWorker, TileMillRainmaker>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileMillRainmaker host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new TileRainmakerWorker(host));
                        }
                    });
            registry.registerTile(TileRaintank.class,
                    new SimpleCapabilityConstructor<IWorker, TileRaintank>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileRaintank host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new TileRaintankWorker(host));
                        }
                    });
            registry.registerTile(TileFarmGearbox.class,
                    new SimpleCapabilityConstructor<IWorker, TileFarmGearbox>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileFarmGearbox host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new TileFarmGearboxWorker(host));
                        }
                    });

            // Recipe Handler
            registerTileRecipeHandler(registry, TileStill.class, TileStillRecipeHandler::new);
            registerTileRecipeHandler(registry, TileCarpenter.class, TileCarpenterRecipeHandler::new);
            registerTileRecipeHandler(registry, TileFermenter.class, TileFermenterRecipeHandler::new);
            registerTileRecipeHandler(registry, TileSqueezer.class, TileSqueezerRecipeHandler::new);
            //registerTileRecipeHandler(registry, TileBottler.class, TileBottlerRecipeHandler::new);
            registerTileRecipeHandler(registry, TileCentrifuge.class, TileCentrifugeRecipeHandler::new);
            registerTileRecipeHandler(registry, TileFabricator.class, TileFabricatorRecipeHandler::new);
        }
    }

    protected static <T extends TileEngine> void registerEngineTemperature(
            CapabilityConstructorRegistry registry, Class<T> clazz, final double maximumTemperature) {
        registry.registerTile(clazz,
                new SimpleCapabilityConstructor<ITemperature, T>() {
                    @Override
                    public Capability<ITemperature> getCapability() {
                        return TemperatureConfig.CAPABILITY;
                    }

                    @Nullable
                    @Override
                    public ICapabilityProvider createProvider(T host) {
                        return new DefaultCapabilityProvider<>(this::getCapability,
                                new TileEngineTemperature(host, maximumTemperature,
                                        ITemperature.ZERO_CELCIUS, ITemperature.ZERO_CELCIUS));
                    }
                });
    }

    protected static <T extends TilePowered> void registerTilePoweredWorker(
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
                        return new DefaultCapabilityProvider<>(this::getCapability, new TilePoweredWorker(host));
                    }
                });
    }

    protected static <T extends TileEngine> void registerTileEngineWorker(
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
                        return new DefaultCapabilityProvider<>(this::getCapability, new TileEngineWorker(host));
                    }
                });
    }

    protected static <T extends TilePowered> void registerTileRecipeHandler(
            CapabilityConstructorRegistry registry, Class<T> clazz, Supplier<IRecipeHandler> recipeHandlerSupplier) {
        Wrapper<IRecipeHandler> recipeHandler = new Wrapper<>();
        registry.registerTile(clazz,
                new SimpleCapabilityConstructor<IRecipeHandler, T>() {
                    @Override
                    public Capability<IRecipeHandler> getCapability() {
                        return RecipeHandlerConfig.CAPABILITY;
                    }

                    @Nullable
                    @Override
                    public ICapabilityProvider createProvider(T host) {
                        if (recipeHandler.get() == null) {
                            recipeHandler.set(recipeHandlerSupplier.get());
                        }
                        return new DefaultCapabilityProvider<>(this::getCapability, recipeHandler.get());
                    }
                });
    }
}
