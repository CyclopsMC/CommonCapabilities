package org.cyclops.commoncapabilities.modcompat.vanilla;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.BlastFurnaceTileEntity;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.tileentity.CampfireTileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.SmokerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.api.capability.block.BlockCapabilities;
import org.cyclops.commoncapabilities.api.capability.block.IBlockCapabilityConstructor;
import org.cyclops.commoncapabilities.api.capability.block.IBlockCapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.capability.recipehandler.RecipeHandlerConfig;
import org.cyclops.commoncapabilities.capability.temperature.TemperatureConfig;
import org.cyclops.commoncapabilities.capability.worker.WorkerConfig;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.energystorage.VanillaEntityItemEnergyStorage;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.energystorage.VanillaEntityItemFrameEnergyStorage;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.fluidhandler.VanillaEntityItemFluidHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.fluidhandler.VanillaEntityItemFrameFluidHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler.VanillaEntityItemFrameItemHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler.VanillaEntityItemItemHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler.VanillaItemShulkerBoxItemHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler.VanillaBrewingStandRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler.VanillaRecipeTypeRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.temperature.VanillaAbstractFurnaceTemperature;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.temperature.VanillaCampfireTemperature;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.temperature.VanillaUniversalBucketTemperature;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.work.VanillaAbstractFurnaceWorker;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.work.VanillaBrewingStandWorker;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.work.VanillaCampfireWorker;
import org.cyclops.commoncapabilities.RegistryEntries;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Capabilities for Vanilla.
 * @author rubensworks
 */
public class VanillaModCompat implements IModCompat {

    @Override
    public String getId() {
        return Reference.MOD_VANILLA;
    }

    @Override
    public boolean isEnabledDefault() {
        return true;
    }

    @Override
    public String getComment() {
        return "Furnace and Brewing stand capabilities.";
    }

    @Override
    public ICompatInitializer createInitializer() {
        return () -> {
            CapabilityConstructorRegistry registry = CommonCapabilities._instance.getCapabilityConstructorRegistry();
            // Worker
            registry.registerTile(FurnaceTileEntity.class,
                    new SimpleCapabilityConstructor<IWorker, FurnaceTileEntity>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(FurnaceTileEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaAbstractFurnaceWorker(host));
                        }
                    });
            registry.registerTile(BlastFurnaceTileEntity.class,
                    new SimpleCapabilityConstructor<IWorker, BlastFurnaceTileEntity>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(BlastFurnaceTileEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaAbstractFurnaceWorker(host));
                        }
                    });
            registry.registerTile(SmokerTileEntity.class,
                    new SimpleCapabilityConstructor<IWorker, SmokerTileEntity>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(SmokerTileEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaAbstractFurnaceWorker(host));
                        }
                    });
            registry.registerTile(BrewingStandTileEntity.class,
                    new SimpleCapabilityConstructor<IWorker, BrewingStandTileEntity>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Override
                        public ICapabilityProvider createProvider(BrewingStandTileEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaBrewingStandWorker(host));
                        }
                    });
            registry.registerTile(CampfireTileEntity.class,
                    new SimpleCapabilityConstructor<IWorker, CampfireTileEntity>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Override
                        public ICapabilityProvider createProvider(CampfireTileEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaCampfireWorker(host));
                        }
                    });

            // Temperature
            registry.registerTile(FurnaceTileEntity.class,
                    new SimpleCapabilityConstructor<ITemperature, FurnaceTileEntity>() {
                        @Override
                        public Capability<ITemperature> getCapability() {
                            return TemperatureConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(FurnaceTileEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaAbstractFurnaceTemperature(host));
                        }
                    });
            registry.registerTile(BlastFurnaceTileEntity.class,
                    new SimpleCapabilityConstructor<ITemperature, BlastFurnaceTileEntity>() {
                        @Override
                        public Capability<ITemperature> getCapability() {
                            return TemperatureConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(BlastFurnaceTileEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaAbstractFurnaceTemperature(host));
                        }
                    });
            registry.registerTile(SmokerTileEntity.class,
                    new SimpleCapabilityConstructor<ITemperature, SmokerTileEntity>() {
                        @Override
                        public Capability<ITemperature> getCapability() {
                            return TemperatureConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(SmokerTileEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaAbstractFurnaceTemperature(host));
                        }
                    });
            registry.registerTile(CampfireTileEntity.class,
                    new SimpleCapabilityConstructor<ITemperature, CampfireTileEntity>() {
                        @Override
                        public Capability<ITemperature> getCapability() {
                            return TemperatureConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(CampfireTileEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaCampfireTemperature(host));
                        }
                    });
            registry.registerItem(BucketItem.class,
                    new ICapabilityConstructor<ITemperature, BucketItem, ItemStack>() {
                        @Override
                        public Capability<ITemperature> getCapability() {
                            return TemperatureConfig.CAPABILITY;
                        }

                        @Override
                        public ICapabilityProvider createProvider(BucketItem hostType, ItemStack host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaUniversalBucketTemperature(host));
                        }
                    });

            // ItemHandler
            ICapabilityConstructor<IItemHandler, Item, ItemStack> shulkerboxConstructor = new ICapabilityConstructor<IItemHandler, Item, ItemStack>() {
                @Override
                public Capability<IItemHandler> getCapability() {
                    return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(Item hostType, ItemStack host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, new VanillaItemShulkerBoxItemHandler(host));
                }
            };
            registry.registerItem(() -> RegistryEntries.ITEM_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_WHITE_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_ORANGE_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_MAGENTA_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_LIGHT_BLUE_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_YELLOW_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_LIME_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_PINK_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_GRAY_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_LIGHT_GRAY_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_CYAN_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_PURPLE_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_BLUE_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_BROWN_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_GREEN_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_RED_SHULKER_BOX, shulkerboxConstructor);
            registry.registerItem(() -> RegistryEntries.ITEM_BLACK_SHULKER_BOX, shulkerboxConstructor);
            registry.registerEntity(ItemEntity.class,
                    new ICapabilityConstructor<IItemHandler, ItemEntity, ItemEntity>() {
                        @Override
                        public Capability<IItemHandler> getCapability() {
                            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemEntity hostType, final ItemEntity host) {
                            return new ICapabilityProvider() {
                                @Override
                                public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
                                    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                                            && host.getItem().getCapability(capability, facing).isPresent()
                                            ? LazyOptional.of(() -> new VanillaEntityItemItemHandler(host, facing)).cast()
                                            : LazyOptional.empty();
                                }
                            };
                        }
                    });
            registry.registerEntity(ItemFrameEntity.class,
                    new ICapabilityConstructor<IItemHandler, ItemFrameEntity, ItemFrameEntity>() {
                        @Override
                        public Capability<IItemHandler> getCapability() {
                            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemFrameEntity hostType, final ItemFrameEntity host) {
                            return new ICapabilityProvider() {
                                @Override
                                public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
                                    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                                            && host.getDisplayedItem().getCapability(capability, facing).isPresent()
                                            ? LazyOptional.of(() -> new VanillaEntityItemFrameItemHandler(host, facing)).cast()
                                            : LazyOptional.empty();
                                }
                            };
                        }
                    });

            // FluidHandler
            registry.registerEntity(ItemEntity.class,
                    new ICapabilityConstructor<IFluidHandler, ItemEntity, ItemEntity>() {
                        @Override
                        public Capability<IFluidHandler> getCapability() {
                            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemEntity hostType, final ItemEntity host) {
                            return new ICapabilityProvider() {
                                @Override
                                public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
                                    return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                                            && host.getItem().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, facing).isPresent()
                                            ? LazyOptional.of(() -> new VanillaEntityItemFluidHandler(host, facing)).cast()
                                            : LazyOptional.empty();
                                }
                            };
                        }
                    });
            registry.registerEntity(ItemFrameEntity.class,
                    new ICapabilityConstructor<IFluidHandler, ItemFrameEntity, ItemFrameEntity>() {
                        @Override
                        public Capability<IFluidHandler> getCapability() {
                            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemFrameEntity hostType, final ItemFrameEntity host) {
                            return new ICapabilityProvider() {
                                @Override
                                public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
                                    return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                                            && host.getDisplayedItem().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, facing).isPresent()
                                            ? LazyOptional.of(() -> new VanillaEntityItemFrameFluidHandler(host, facing)).cast()
                                            : LazyOptional.empty();
                                }
                            };
                        }
                    });

            // EnergyStorage
            registry.registerEntity(ItemEntity.class,
                    new ICapabilityConstructor<IEnergyStorage, ItemEntity, ItemEntity>() {
                        @Override
                        public Capability<IEnergyStorage> getCapability() {
                            return CapabilityEnergy.ENERGY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemEntity hostType, final ItemEntity host) {
                            return new ICapabilityProvider() {
                                @Override
                                public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
                                    return capability == CapabilityEnergy.ENERGY
                                            && host.getItem().getCapability(capability, facing).isPresent()
                                            ? LazyOptional.of(() -> new VanillaEntityItemEnergyStorage(host, facing)).cast()
                                            : LazyOptional.empty();
                                }
                            };
                        }
                    });
            registry.registerEntity(ItemFrameEntity.class,
                    new ICapabilityConstructor<IEnergyStorage, ItemFrameEntity, ItemFrameEntity>() {
                        @Override
                        public Capability<IEnergyStorage> getCapability() {
                            return CapabilityEnergy.ENERGY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemFrameEntity hostType, final ItemFrameEntity host) {
                            return new ICapabilityProvider() {
                                @Override
                                public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
                                    return capability == CapabilityEnergy.ENERGY
                                            && host.getDisplayedItem().getCapability(capability, facing).isPresent()
                                            ? LazyOptional.of(() -> new VanillaEntityItemFrameEnergyStorage(host, facing)).cast()
                                            : LazyOptional.empty();
                                }
                            };
                        }
                    });

            // RecipeHandler
            registry.registerTile(BrewingStandTileEntity.class, new ICapabilityConstructor<IRecipeHandler, BrewingStandTileEntity, BrewingStandTileEntity>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(BrewingStandTileEntity hostType, BrewingStandTileEntity host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, VanillaBrewingStandRecipeHandler.getInstance());
                }
            });
            registry.registerTile(FurnaceTileEntity.class, new ICapabilityConstructor<IRecipeHandler, FurnaceTileEntity, FurnaceTileEntity>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(FurnaceTileEntity hostType, FurnaceTileEntity host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, new VanillaRecipeTypeRecipeHandler<>(host::getWorld,
                            IRecipeType.SMELTING, (size) -> size == 1));
                }
            });
            registry.registerTile(BlastFurnaceTileEntity.class, new ICapabilityConstructor<IRecipeHandler, BlastFurnaceTileEntity, BlastFurnaceTileEntity>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(BlastFurnaceTileEntity hostType, BlastFurnaceTileEntity host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, new VanillaRecipeTypeRecipeHandler<>(host::getWorld,
                            IRecipeType.BLASTING, (size) -> size == 1));
                }
            });
            registry.registerTile(SmokerTileEntity.class, new ICapabilityConstructor<IRecipeHandler, SmokerTileEntity, SmokerTileEntity>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(SmokerTileEntity hostType, SmokerTileEntity host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, new VanillaRecipeTypeRecipeHandler<>(host::getWorld,
                            IRecipeType.SMOKING, (size) -> size == 1));
                }
            });
            registry.registerTile(CampfireTileEntity.class, new ICapabilityConstructor<IRecipeHandler, CampfireTileEntity, CampfireTileEntity>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(CampfireTileEntity hostType, CampfireTileEntity host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, new VanillaRecipeTypeRecipeHandler<>(host::getWorld,
                            IRecipeType.CAMPFIRE_COOKING, (size) -> size == 1));
                }
            });
            BlockCapabilities.getInstance().register(new IBlockCapabilityConstructor() {
                @Nullable
                @Override
                public Block getBlock() {
                    return Blocks.CRAFTING_TABLE;
                }

                @Override
                public IBlockCapabilityProvider createProvider() {
                    return new IBlockCapabilityProvider() {
                        @Override
                        public <T> LazyOptional<T> getCapability(@Nonnull BlockState blockState, @Nonnull Capability<T> capability,
                                                                 @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nullable Direction facing) {
                            if (capability == RecipeHandlerConfig.CAPABILITY) {
                                return LazyOptional.of(() -> new VanillaRecipeTypeRecipeHandler<>(() -> (World) world,
                                        IRecipeType.CRAFTING, (size) -> size > 0)).cast();
                            }
                            return LazyOptional.empty();
                        }
                    };
                }
            });
            BlockCapabilities.getInstance().register(new IBlockCapabilityConstructor() {
                @Nullable
                @Override
                public Block getBlock() {
                    return Blocks.STONECUTTER;
                }

                @Override
                public IBlockCapabilityProvider createProvider() {
                    return new IBlockCapabilityProvider() {
                        @Override
                        public <T> LazyOptional<T> getCapability(@Nonnull BlockState blockState, @Nonnull Capability<T> capability,
                                                                 @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nullable Direction facing) {
                            if (capability == RecipeHandlerConfig.CAPABILITY) {
                                return LazyOptional.of(() -> new VanillaRecipeTypeRecipeHandler<>(() -> (World) world,
                                        IRecipeType.STONECUTTING, (size) -> size == 1)).cast();
                            }
                            return LazyOptional.empty();
                        }
                    };
                }
            });
        };
    }

}
