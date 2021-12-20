package org.cyclops.commoncapabilities.modcompat.vanilla;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
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
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler.VanillaBlockComposterItemHandler;
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
            registry.registerTile(FurnaceBlockEntity.class,
                    new SimpleCapabilityConstructor<IWorker, FurnaceBlockEntity>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(FurnaceBlockEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaAbstractFurnaceWorker(host));
                        }
                    });
            registry.registerTile(BlastFurnaceBlockEntity.class,
                    new SimpleCapabilityConstructor<IWorker, BlastFurnaceBlockEntity>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(BlastFurnaceBlockEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaAbstractFurnaceWorker(host));
                        }
                    });
            registry.registerTile(SmokerBlockEntity.class,
                    new SimpleCapabilityConstructor<IWorker, SmokerBlockEntity>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(SmokerBlockEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaAbstractFurnaceWorker(host));
                        }
                    });
            registry.registerTile(BrewingStandBlockEntity.class,
                    new SimpleCapabilityConstructor<IWorker, BrewingStandBlockEntity>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Override
                        public ICapabilityProvider createProvider(BrewingStandBlockEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaBrewingStandWorker(host));
                        }
                    });
            registry.registerTile(CampfireBlockEntity.class,
                    new SimpleCapabilityConstructor<IWorker, CampfireBlockEntity>() {
                        @Override
                        public Capability<IWorker> getCapability() {
                            return WorkerConfig.CAPABILITY;
                        }

                        @Override
                        public ICapabilityProvider createProvider(CampfireBlockEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaCampfireWorker(host));
                        }
                    });

            // Temperature
            registry.registerTile(FurnaceBlockEntity.class,
                    new SimpleCapabilityConstructor<ITemperature, FurnaceBlockEntity>() {
                        @Override
                        public Capability<ITemperature> getCapability() {
                            return TemperatureConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(FurnaceBlockEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaAbstractFurnaceTemperature(host));
                        }
                    });
            registry.registerTile(BlastFurnaceBlockEntity.class,
                    new SimpleCapabilityConstructor<ITemperature, BlastFurnaceBlockEntity>() {
                        @Override
                        public Capability<ITemperature> getCapability() {
                            return TemperatureConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(BlastFurnaceBlockEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaAbstractFurnaceTemperature(host));
                        }
                    });
            registry.registerTile(SmokerBlockEntity.class,
                    new SimpleCapabilityConstructor<ITemperature, SmokerBlockEntity>() {
                        @Override
                        public Capability<ITemperature> getCapability() {
                            return TemperatureConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(SmokerBlockEntity host) {
                            return new DefaultCapabilityProvider<>(this::getCapability, new VanillaAbstractFurnaceTemperature(host));
                        }
                    });
            registry.registerTile(CampfireBlockEntity.class,
                    new SimpleCapabilityConstructor<ITemperature, CampfireBlockEntity>() {
                        @Override
                        public Capability<ITemperature> getCapability() {
                            return TemperatureConfig.CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(CampfireBlockEntity host) {
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
            registry.registerEntity(ItemFrame.class,
                    new ICapabilityConstructor<IItemHandler, ItemFrame, ItemFrame>() {
                        @Override
                        public Capability<IItemHandler> getCapability() {
                            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemFrame hostType, final ItemFrame host) {
                            return new ICapabilityProvider() {
                                @Override
                                public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
                                    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                                            && host.getItem().getCapability(capability, facing).isPresent()
                                            ? LazyOptional.of(() -> new VanillaEntityItemFrameItemHandler(host, facing)).cast()
                                            : LazyOptional.empty();
                                }
                            };
                        }
                    });
            BlockCapabilities.getInstance().register(new IBlockCapabilityConstructor() {
                @Nullable
                @Override
                public Block getBlock() {
                    return Blocks.COMPOSTER;
                }

                @Override
                public IBlockCapabilityProvider createProvider() {
                    return new IBlockCapabilityProvider() {
                        @Override
                        public <T> LazyOptional<T> getCapability(@Nonnull BlockState blockState, @Nonnull Capability<T> capability,
                                                                 @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nullable Direction facing) {
                            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                                return LazyOptional.of(() -> new VanillaBlockComposterItemHandler(blockState, (LevelAccessor) world, pos, facing)).cast();
                            }
                            return LazyOptional.empty();
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
            registry.registerEntity(ItemFrame.class,
                    new ICapabilityConstructor<IFluidHandler, ItemFrame, ItemFrame>() {
                        @Override
                        public Capability<IFluidHandler> getCapability() {
                            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemFrame hostType, final ItemFrame host) {
                            return new ICapabilityProvider() {
                                @Override
                                public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
                                    return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                                            && host.getItem().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, facing).isPresent()
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
            registry.registerEntity(ItemFrame.class,
                    new ICapabilityConstructor<IEnergyStorage, ItemFrame, ItemFrame>() {
                        @Override
                        public Capability<IEnergyStorage> getCapability() {
                            return CapabilityEnergy.ENERGY;
                        }

                        @Nullable
                        @Override
                        public ICapabilityProvider createProvider(ItemFrame hostType, final ItemFrame host) {
                            return new ICapabilityProvider() {
                                @Override
                                public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
                                    return capability == CapabilityEnergy.ENERGY
                                            && host.getItem().getCapability(capability, facing).isPresent()
                                            ? LazyOptional.of(() -> new VanillaEntityItemFrameEnergyStorage(host, facing)).cast()
                                            : LazyOptional.empty();
                                }
                            };
                        }
                    });

            // RecipeHandler
            registry.registerTile(BrewingStandBlockEntity.class, new ICapabilityConstructor<IRecipeHandler, BrewingStandBlockEntity, BrewingStandBlockEntity>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(BrewingStandBlockEntity hostType, BrewingStandBlockEntity host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, VanillaBrewingStandRecipeHandler.getInstance());
                }
            });
            registry.registerTile(FurnaceBlockEntity.class, new ICapabilityConstructor<IRecipeHandler, FurnaceBlockEntity, FurnaceBlockEntity>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(FurnaceBlockEntity hostType, FurnaceBlockEntity host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, new VanillaRecipeTypeRecipeHandler<>(host::getLevel,
                            RecipeType.SMELTING, (size) -> size == 1));
                }
            });
            registry.registerTile(BlastFurnaceBlockEntity.class, new ICapabilityConstructor<IRecipeHandler, BlastFurnaceBlockEntity, BlastFurnaceBlockEntity>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(BlastFurnaceBlockEntity hostType, BlastFurnaceBlockEntity host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, new VanillaRecipeTypeRecipeHandler<>(host::getLevel,
                            RecipeType.BLASTING, (size) -> size == 1));
                }
            });
            registry.registerTile(SmokerBlockEntity.class, new ICapabilityConstructor<IRecipeHandler, SmokerBlockEntity, SmokerBlockEntity>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(SmokerBlockEntity hostType, SmokerBlockEntity host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, new VanillaRecipeTypeRecipeHandler<>(host::getLevel,
                            RecipeType.SMOKING, (size) -> size == 1));
                }
            });
            registry.registerTile(CampfireBlockEntity.class, new ICapabilityConstructor<IRecipeHandler, CampfireBlockEntity, CampfireBlockEntity>() {
                @Override
                public Capability<IRecipeHandler> getCapability() {
                    return RecipeHandlerConfig.CAPABILITY;
                }

                @Nullable
                @Override
                public ICapabilityProvider createProvider(CampfireBlockEntity hostType, CampfireBlockEntity host) {
                    return new DefaultCapabilityProvider<>(this::getCapability, new VanillaRecipeTypeRecipeHandler<>(host::getLevel,
                            RecipeType.CAMPFIRE_COOKING, (size) -> size == 1));
                }
            });
            BlockCapabilities.getInstance().register(new IBlockCapabilityConstructor() {
                @Nullable
                @Override
                public Block getBlock() {
                    return null;
                }

                @Override
                public IBlockCapabilityProvider createProvider() {
                    return new IBlockCapabilityProvider() {
                        @Override
                        public <T> LazyOptional<T> getCapability(@Nonnull BlockState blockState, @Nonnull Capability<T> capability,
                                                                 @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nullable Direction facing) {
                            if (blockState.getBlock() instanceof CraftingTableBlock && capability == RecipeHandlerConfig.CAPABILITY) {
                                return LazyOptional.of(() -> new VanillaRecipeTypeRecipeHandler<>(() -> (Level) world,
                                        RecipeType.CRAFTING, (size) -> size > 0)).cast();
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
                                                                 @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nullable Direction facing) {
                            if (capability == RecipeHandlerConfig.CAPABILITY) {
                                return LazyOptional.of(() -> new VanillaRecipeTypeRecipeHandler<>(() -> (Level) world,
                                        RecipeType.STONECUTTING, (size) -> size == 1)).cast();
                            }
                            return LazyOptional.empty();
                        }
                    };
                }
            });
        };
    }

}
