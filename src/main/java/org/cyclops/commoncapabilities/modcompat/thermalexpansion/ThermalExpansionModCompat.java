package org.cyclops.commoncapabilities.modcompat.thermalexpansion;

import cofh.thermalexpansion.block.device.*;
import cofh.thermalexpansion.block.machine.*;
import cofh.thermalexpansion.block.storage.ItemBlockCache;
import cofh.thermalexpansion.block.storage.ItemBlockStrongbox;
import cofh.thermalexpansion.block.storage.TileCache;
import cofh.thermalexpansion.item.ItemSatchel;
import cofh.thermalfoundation.item.ItemWrench;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ISlotlessItemHandler;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.api.capability.wrench.DefaultWrench;
import org.cyclops.commoncapabilities.api.capability.wrench.IWrench;
import org.cyclops.commoncapabilities.capability.itemhandler.SlotlessItemHandlerConfig;
import org.cyclops.commoncapabilities.capability.recipehandler.RecipeHandlerConfig;
import org.cyclops.commoncapabilities.capability.worker.WorkerConfig;
import org.cyclops.commoncapabilities.capability.wrench.WrenchConfig;
import org.cyclops.commoncapabilities.modcompat.thermalexpansion.itemhandler.ItemSatchelItemHandler;
import org.cyclops.commoncapabilities.modcompat.thermalexpansion.itemhandler.ItemBlockCacheItemHandler;
import org.cyclops.commoncapabilities.modcompat.thermalexpansion.itemhandler.ItemBlockStrongboxItemHandler;
import org.cyclops.commoncapabilities.modcompat.thermalexpansion.recipehandler.*;
import org.cyclops.commoncapabilities.modcompat.thermalexpansion.slotlessitemhandler.TileCacheSlotlessItemHandler;
import org.cyclops.commoncapabilities.modcompat.thermalexpansion.work.TileDeviceBaseWorker;
import org.cyclops.commoncapabilities.modcompat.thermalexpansion.work.TileMachineBaseWorker;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;

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

            // ItemHandler
            registry.registerItem(ItemSatchel.class,
                    new ICapabilityConstructor<IItemHandler, ItemSatchel, ItemStack>() {
                        @Override
                        public Capability<IItemHandler> getCapability() {
                            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemSatchel hostType, ItemStack host) {
                            return new DefaultCapabilityProvider<>(this, new ItemSatchelItemHandler(host));
                        }
                    });
            registry.registerItem(ItemBlockCache.class,
                    new ICapabilityConstructor<IItemHandler, ItemBlockCache, ItemStack>() {
                        @Override
                        public Capability<IItemHandler> getCapability() {
                            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemBlockCache hostType, ItemStack host) {
                            return new DefaultCapabilityProvider<>(this, new ItemBlockCacheItemHandler(host));
                        }
                    });
            registry.registerItem(ItemBlockStrongbox.class,
                    new ICapabilityConstructor<IItemHandler, ItemBlockStrongbox, ItemStack>() {
                        @Override
                        public Capability<IItemHandler> getCapability() {
                            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemBlockStrongbox hostType, ItemStack host) {
                            return new DefaultCapabilityProvider<>(this, new ItemBlockStrongboxItemHandler(host));
                        }
                    });

            // SlotlessItemHandler
            registry.registerTile(TileCache.class,
                    new ICapabilityConstructor<ISlotlessItemHandler, TileCache, TileCache>() {
                        @Override
                        public Capability<ISlotlessItemHandler> getCapability() {
                            return SlotlessItemHandlerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileCache hostType, TileCache host) {
                            return new DefaultCapabilityProvider<>(this, new TileCacheSlotlessItemHandler(host));
                        }
                    });

            // Worker
            registerTileMachineBaseWorker(registry, TileFurnace.class);
            registerTileMachineBaseWorker(registry, TilePulverizer.class);
            registerTileMachineBaseWorker(registry, TileSawmill.class);
            registerTileMachineBaseWorker(registry, TileSmelter.class);
            registerTileMachineBaseWorker(registry, TileInsolator.class);
            registerTileMachineBaseWorker(registry, TileCompactor.class);
            registerTileMachineBaseWorker(registry, TileCrucible.class);
            registerTileMachineBaseWorker(registry, TileRefinery.class);
            registerTileMachineBaseWorker(registry, TileTransposer.class);
            registerTileMachineBaseWorker(registry, TileCentrifuge.class);
            registerTileMachineBaseWorker(registry, TileBrewer.class);
            registerTileMachineBaseWorker(registry, TileEnchanter.class);
            registerTileMachineBaseWorker(registry, TilePrecipitator.class);
            registerTileMachineBaseWorker(registry, TileExtruder.class);
            registerTileDeviceBaseWorker(registry, TileChunkLoader.class);
            registerTileDeviceBaseWorker(registry, TileDiffuser.class);
            registerTileDeviceBaseWorker(registry, TileFactorizer.class);
            registerTileDeviceBaseWorker(registry, TileFisher.class);
            registerTileDeviceBaseWorker(registry, TileFluidBuffer.class);
            registerTileDeviceBaseWorker(registry, TileHeatSink.class);
            registerTileDeviceBaseWorker(registry, TileItemBuffer.class);
            registerTileDeviceBaseWorker(registry, TileItemCollector.class);
            registerTileDeviceBaseWorker(registry, TileLexicon.class);
            registerTileDeviceBaseWorker(registry, TileMobCatcher.class);
            registerTileDeviceBaseWorker(registry, TileNullifier.class);
            registerTileDeviceBaseWorker(registry, TileTapper.class);
            registerTileDeviceBaseWorker(registry, TileWaterGen.class);
            registerTileDeviceBaseWorker(registry, TileXpCollector.class);

            // Recipe Handler
            registry.registerTile(TileFurnace.class,
                    new SimpleCapabilityConstructor<IRecipeHandler, TileFurnace>() {
                        @Override
                        public Capability<IRecipeHandler> getCapability() {
                            return RecipeHandlerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileFurnace host) {
                            return new DefaultCapabilityProvider<>(this, new TileFurnaceRecipeHandler(host));
                        }
                    });
            registerTileRecipeHandler(registry, TilePulverizer.class, new TilePulverizerRecipeHandler());
            registerTileRecipeHandler(registry, TileSawmill.class, new TileSawmillRecipeHandler());
            registerTileRecipeHandler(registry, TileSmelter.class, new TileSmelterRecipeHandler());
            registerTileRecipeHandler(registry, TileInsolator.class, new TileInsolatorRecipeHandler());
            registry.registerTile(TileCompactor.class,
                    new SimpleCapabilityConstructor<IRecipeHandler, TileCompactor>() {
                        @Override
                        public Capability<IRecipeHandler> getCapability() {
                            return RecipeHandlerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileCompactor host) {
                            return new DefaultCapabilityProvider<>(this, new TileCompactorRecipeHandler(host));
                        }
                    });
            registerTileRecipeHandler(registry, TileCrucible.class, new TileCrucibleRecipeHandler());
            registry.registerTile(TileRefinery.class,
                    new SimpleCapabilityConstructor<IRecipeHandler, TileRefinery>() {
                        @Override
                        public Capability<IRecipeHandler> getCapability() {
                            return RecipeHandlerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileRefinery host) {
                            return new DefaultCapabilityProvider<>(this, new TileRefineryRecipeHandler(host));
                        }
                    });
            registry.registerTile(TileCentrifuge.class,
                    new SimpleCapabilityConstructor<IRecipeHandler, TileCentrifuge>() {
                        @Override
                        public Capability<IRecipeHandler> getCapability() {
                            return RecipeHandlerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileCentrifuge host) {
                            return new DefaultCapabilityProvider<>(this, new TileCentrifugeRecipeHandler(host));
                        }
                    });
            registerTileRecipeHandler(registry, TileBrewer.class, new TileBrewerRecipeHandler());
            registerTileRecipeHandler(registry, TileEnchanter.class, new TileEnchanterRecipeHandler());
            registerTileRecipeHandler(registry, TilePrecipitator.class, new TilePrecipitatorRecipeHandler());
            registry.registerTile(TileExtruder.class,
                    new SimpleCapabilityConstructor<IRecipeHandler, TileExtruder>() {
                        @Override
                        public Capability<IRecipeHandler> getCapability() {
                            return RecipeHandlerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileExtruder host) {
                            return new DefaultCapabilityProvider<>(this, new TileExtruderRecipeHandler(host));
                        }
                    });
        }
    }

    protected static <T extends TileMachineBase> void registerTileMachineBaseWorker(
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
                        return new DefaultCapabilityProvider<>(this, new TileMachineBaseWorker(host));
                    }
                });
    }

    protected static <T extends TileDeviceBase> void registerTileDeviceBaseWorker(
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
                        return new DefaultCapabilityProvider<>(this, new TileDeviceBaseWorker(host));
                    }
                });
    }

    protected static <T extends TileEntity> void registerTileRecipeHandler(
            CapabilityConstructorRegistry registry, Class<T> clazz, IRecipeHandler recipeHandler) {
        registry.registerTile(clazz,
                new SimpleCapabilityConstructor<IRecipeHandler, T>() {
                    @Override
                    public Capability<IRecipeHandler> getCapability() {
                        return RecipeHandlerConfig.CAPABILITY;
                    }

                    @Nullable
                    @Override
                    public ICapabilityProvider createProvider(T host) {
                        return new DefaultCapabilityProvider<>(this, recipeHandler);
                    }
                });
    }

}
