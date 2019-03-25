package org.cyclops.commoncapabilities.modcompat.tconstruct;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.capability.recipehandler.RecipeHandlerConfig;
import org.cyclops.commoncapabilities.capability.worker.WorkerConfig;
import org.cyclops.commoncapabilities.modcompat.tconstruct.capability.recipehandler.TileCastingBasinRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.tconstruct.capability.recipehandler.TileCastingTableRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.tconstruct.capability.recipehandler.TileDryingRackRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.tconstruct.capability.recipehandler.TileSmelteryRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.tconstruct.capability.work.TileCastingWorker;
import org.cyclops.commoncapabilities.modcompat.tconstruct.capability.work.TileDryingRackWorker;
import org.cyclops.commoncapabilities.modcompat.tconstruct.capability.work.TileSmelteryWorker;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;
import slimeknights.tconstruct.gadgets.tileentity.TileDryingRack;
import slimeknights.tconstruct.smeltery.tileentity.TileCastingBasin;
import slimeknights.tconstruct.smeltery.tileentity.TileCastingTable;
import slimeknights.tconstruct.smeltery.tileentity.TileSmeltery;

import javax.annotation.Nullable;

/**
 * Capabilities for TConstruct.
 * @author rubensworks
 */
public class TConstructModCompat implements IModCompat {
    @Override
    public String getModID() {
        return Reference.MOD_TCONSTRUCT;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getComment() {
        return "Worker capabilities for TCon machines.";
    }

    @Override
    public void onInit(Step initStep) {
        if(initStep == Step.INIT) {
            CapabilityConstructorRegistry registry = CommonCapabilities._instance.getCapabilityConstructorRegistry();
            // Worker
            registry.registerTile(TileSmeltery.class,
                    new SimpleCapabilityConstructor<IWorker, TileSmeltery>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileSmeltery host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new TileSmelteryWorker(host));
                        }
                    });
            registry.registerTile(TileCastingTable.class,
                    new SimpleCapabilityConstructor<IWorker, TileCastingTable>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileCastingTable host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new TileCastingWorker(host));
                        }
                    });
            registry.registerTile(TileCastingBasin.class,
                    new SimpleCapabilityConstructor<IWorker, TileCastingBasin>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileCastingBasin host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new TileCastingWorker(host));
                        }
                    });
            registry.registerTile(TileDryingRack.class,
                    new SimpleCapabilityConstructor<IWorker, TileDryingRack>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(TileDryingRack host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new TileDryingRackWorker(host));
                        }
                    });

            // RecipeHandler
            registry.registerTile(TileSmeltery.class, new ICapabilityConstructor<IRecipeHandler, TileSmeltery, TileSmeltery>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(TileSmeltery hostType, TileSmeltery host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, TileSmelteryRecipeHandler.getInstance());
                }
            });
            registry.registerTile(TileCastingTable.class, new ICapabilityConstructor<IRecipeHandler, TileCastingTable, TileCastingTable>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(TileCastingTable hostType, TileCastingTable host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, TileCastingTableRecipeHandler.getInstance());
                }
            });
            registry.registerTile(TileCastingBasin.class, new ICapabilityConstructor<IRecipeHandler, TileCastingBasin, TileCastingBasin>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(TileCastingBasin hostType, TileCastingBasin host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, TileCastingBasinRecipeHandler.getInstance());
                }
            });
            registry.registerTile(TileDryingRack.class, new ICapabilityConstructor<IRecipeHandler, TileDryingRack, TileDryingRack>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(TileDryingRack hostType, TileDryingRack host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, TileDryingRackRecipeHandler.getInstance());
                }
            });
        }
    }

}
