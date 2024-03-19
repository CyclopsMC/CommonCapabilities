package org.cyclops.commoncapabilities.modcompat.vanilla;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.RegistryEntries;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.energystorage.VanillaEntityItemEnergyStorage;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.energystorage.VanillaEntityItemFrameEnergyStorage;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.fluidhandler.VanillaEntityItemFluidHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.fluidhandler.VanillaEntityItemFrameFluidHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler.VanillaBlockComposterItemHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler.VanillaEntityItemFrameItemHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler.VanillaEntityItemItemHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler.VanillaItemBundleItemHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler.VanillaItemShulkerBoxItemHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler.VanillaBrewingStandRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler.VanillaRecipeTypeRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.temperature.VanillaAbstractFurnaceTemperature;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.temperature.VanillaCampfireTemperature;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.temperature.VanillaUniversalBucketTemperature;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.work.VanillaAbstractFurnaceWorker;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.work.VanillaBrewingStandWorker;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.work.VanillaCampfireWorker;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.modcompat.capabilities.IBlockCapabilityConstructor;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;

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
            registry.registerBlockEntity(() -> BlockEntityType.FURNACE,
                    new ICapabilityConstructor<FurnaceBlockEntity, Direction, IWorker, BlockEntityType<FurnaceBlockEntity>>() {
                        @Override
                        public BaseCapability<IWorker, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.Worker.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<FurnaceBlockEntity, Direction, IWorker> createProvider(BlockEntityType<FurnaceBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaAbstractFurnaceWorker(blockEntity);
                        }
                    });
            registry.registerBlockEntity(() -> BlockEntityType.BLAST_FURNACE,
                    new ICapabilityConstructor<BlastFurnaceBlockEntity, Direction, IWorker, BlockEntityType<BlastFurnaceBlockEntity>>() {
                        @Override
                        public BaseCapability<IWorker, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.Worker.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<BlastFurnaceBlockEntity, Direction, IWorker> createProvider(BlockEntityType<BlastFurnaceBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaAbstractFurnaceWorker(blockEntity);
                        }
                    });
            registry.registerBlockEntity(() -> BlockEntityType.SMOKER,
                    new ICapabilityConstructor<SmokerBlockEntity, Direction, IWorker, BlockEntityType<SmokerBlockEntity>>() {
                        @Override
                        public BaseCapability<IWorker, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.Worker.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<SmokerBlockEntity, Direction, IWorker> createProvider(BlockEntityType<SmokerBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaAbstractFurnaceWorker(blockEntity);
                        }
                    });
            registry.registerBlockEntity(() -> BlockEntityType.BREWING_STAND,
                    new ICapabilityConstructor<BrewingStandBlockEntity, Direction, IWorker, BlockEntityType<BrewingStandBlockEntity>>() {
                        @Override
                        public BaseCapability<IWorker, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.Worker.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<BrewingStandBlockEntity, Direction, IWorker> createProvider(BlockEntityType<BrewingStandBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaBrewingStandWorker(blockEntity);
                        }
                    });
            registry.registerBlockEntity(() -> BlockEntityType.CAMPFIRE,
                    new ICapabilityConstructor<CampfireBlockEntity, Direction, IWorker, BlockEntityType<CampfireBlockEntity>>() {
                        @Override
                        public BaseCapability<IWorker, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.Worker.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<CampfireBlockEntity, Direction, IWorker> createProvider(BlockEntityType<CampfireBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaCampfireWorker(blockEntity);
                        }
                    });

            // Temperature
            registry.registerBlockEntity(() -> BlockEntityType.FURNACE,
                    new ICapabilityConstructor<FurnaceBlockEntity, Direction, ITemperature, BlockEntityType<FurnaceBlockEntity>>() {
                        @Override
                        public BaseCapability<ITemperature, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.Temperature.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<FurnaceBlockEntity, Direction, ITemperature> createProvider(BlockEntityType<FurnaceBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaAbstractFurnaceTemperature(blockEntity);
                        }
                    });
            registry.registerBlockEntity(() -> BlockEntityType.BLAST_FURNACE,
                    new ICapabilityConstructor<BlastFurnaceBlockEntity, Direction, ITemperature, BlockEntityType<BlastFurnaceBlockEntity>>() {
                        @Override
                        public BaseCapability<ITemperature, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.Temperature.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<BlastFurnaceBlockEntity, Direction, ITemperature> createProvider(BlockEntityType<BlastFurnaceBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaAbstractFurnaceTemperature(blockEntity);
                        }
                    });
            registry.registerBlockEntity(() -> BlockEntityType.SMOKER,
                    new ICapabilityConstructor<SmokerBlockEntity, Direction, ITemperature, BlockEntityType<SmokerBlockEntity>>() {
                        @Override
                        public BaseCapability<ITemperature, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.Temperature.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<SmokerBlockEntity, Direction, ITemperature> createProvider(BlockEntityType<SmokerBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaAbstractFurnaceTemperature(blockEntity);
                        }
                    });
            registry.registerBlockEntity(() -> BlockEntityType.CAMPFIRE,
                    new ICapabilityConstructor<CampfireBlockEntity, Direction, ITemperature, BlockEntityType<CampfireBlockEntity>>() {
                        @Override
                        public BaseCapability<ITemperature, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.Temperature.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<CampfireBlockEntity, Direction, ITemperature> createProvider(BlockEntityType<CampfireBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaCampfireTemperature(blockEntity);
                        }
                    });
            registry.registerInheritableItem(BucketItem.class, new ICapabilityConstructor<ItemStack, Void, ITemperature, BucketItem>() {
                @Override
                public BaseCapability<ITemperature, Void> getCapability() {
                    return org.cyclops.commoncapabilities.api.capability.Capabilities.Temperature.ITEM;
                }

                @Override
                public ICapabilityProvider<ItemStack, Void, ITemperature> createProvider(BucketItem capabilityKey) {
                    return (itemStack, context) -> new VanillaUniversalBucketTemperature(itemStack);
                }
            });


            // ItemHandler
            ICapabilityConstructor<ItemStack, Void, IItemHandler, ItemLike> shulkerboxConstructor = new ICapabilityConstructor<>() {
                @Override
                public BaseCapability<IItemHandler, Void> getCapability() {
                    return Capabilities.ItemHandler.ITEM;
                }

                @Override
                public ICapabilityProvider<ItemStack, Void, IItemHandler> createProvider(ItemLike host) {
                    return (itemStack, context) -> new VanillaItemShulkerBoxItemHandler(itemStack);
                }
            };
            registry.registerItem(RegistryEntries.ITEM_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_WHITE_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_ORANGE_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_MAGENTA_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_LIGHT_BLUE_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_YELLOW_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_LIME_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_PINK_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_GRAY_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_LIGHT_GRAY_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_CYAN_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_PURPLE_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_BLUE_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_BROWN_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_GREEN_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_RED_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_BLACK_SHULKER_BOX::get, shulkerboxConstructor);
            registry.registerItem(RegistryEntries.ITEM_BUNDLE::get, new ICapabilityConstructor<ItemStack, Void, IItemHandler, ItemLike>() {
                @Override
                public BaseCapability<IItemHandler, Void> getCapability() {
                    return Capabilities.ItemHandler.ITEM;
                }

                @Override
                public ICapabilityProvider<ItemStack, Void, IItemHandler> createProvider(ItemLike host) {
                    return (itemStack, context) -> new VanillaItemBundleItemHandler(itemStack);
                }
            });
            registry.registerEntity(() -> EntityType.ITEM,
                    new ICapabilityConstructor<ItemEntity, Void, IItemHandler, EntityType<ItemEntity>>() {
                        @Override
                        public BaseCapability<IItemHandler, Void> getCapability() {
                            return Capabilities.ItemHandler.ENTITY;
                        }

                        @Override
                        public ICapabilityProvider<ItemEntity, Void, IItemHandler> createProvider(EntityType<ItemEntity> host) {
                            return (entity, context) -> {
                                if (entity.getItem().getCapability(Capabilities.ItemHandler.ITEM) != null) {
                                    return new VanillaEntityItemItemHandler(entity);
                                }
                                return null;
                            };
                        }
                    });
            registry.registerEntity(() -> EntityType.ITEM_FRAME,
                    new ICapabilityConstructor<ItemFrame, Void, IItemHandler, EntityType<ItemFrame>>() {
                        @Override
                        public BaseCapability<IItemHandler, Void> getCapability() {
                            return Capabilities.ItemHandler.ENTITY;
                        }

                        @Override
                        public ICapabilityProvider<ItemFrame, Void, IItemHandler> createProvider(EntityType<ItemFrame> capabilityKey) {
                            return (entity, context) -> {
                                if (entity.getItem().getCapability(Capabilities.ItemHandler.ITEM) != null) {
                                    return new VanillaEntityItemFrameItemHandler(entity);
                                }
                                return null;
                            };
                        }
                    });
            registry.registerEntity(() -> EntityType.GLOW_ITEM_FRAME,
                    new ICapabilityConstructor<GlowItemFrame, Void, IItemHandler, EntityType<GlowItemFrame>>() {
                        @Override
                        public BaseCapability<IItemHandler, Void> getCapability() {
                            return Capabilities.ItemHandler.ENTITY;
                        }

                        @Override
                        public ICapabilityProvider<GlowItemFrame, Void, IItemHandler> createProvider(EntityType<GlowItemFrame> capabilityKey) {
                            return (entity, context) -> {
                                if (entity.getItem().getCapability(Capabilities.ItemHandler.ITEM) != null) {
                                    return new VanillaEntityItemFrameItemHandler(entity);
                                }
                                return null;
                            };
                        }
                    });
            registry.registerBlock(() -> (ComposterBlock) Blocks.COMPOSTER,
                    new IBlockCapabilityConstructor<ComposterBlock, Direction, IItemHandler, ComposterBlock>() {
                        @Override
                        public BaseCapability<IItemHandler, Direction> getCapability() {
                            return Capabilities.ItemHandler.BLOCK;
                        }

                        @Override
                        public IBlockCapabilityProvider<IItemHandler, Direction> createProvider(ComposterBlock capabilityKey) {
                            return (level, pos, state, blockEntity, side) -> new VanillaBlockComposterItemHandler(state, level, pos, side);
                        }
                    });

            // FluidHandler
            registry.registerEntity(() -> EntityType.ITEM,
                    new ICapabilityConstructor<ItemEntity, Direction, IFluidHandler, EntityType<ItemEntity>>() {
                        @Override
                        public BaseCapability<IFluidHandler, Direction> getCapability() {
                            return Capabilities.FluidHandler.ENTITY;
                        }

                        @Override
                        public ICapabilityProvider<ItemEntity, Direction, IFluidHandler> createProvider(EntityType<ItemEntity> capabilityKey) {
                            return (entity, context) -> {
                                if (entity.getItem().getCapability(Capabilities.FluidHandler.ITEM) != null) {
                                    new VanillaEntityItemFluidHandler(entity);
                                }
                                return null;
                            };
                        }
                    });
            registry.registerEntity(() -> EntityType.ITEM_FRAME,
                    new ICapabilityConstructor<ItemFrame, Direction, IFluidHandler, EntityType<ItemFrame>>() {
                        @Override
                        public BaseCapability<IFluidHandler, Direction> getCapability() {
                            return Capabilities.FluidHandler.ENTITY;
                        }

                        @Override
                        public ICapabilityProvider<ItemFrame, Direction, IFluidHandler> createProvider(EntityType<ItemFrame> capabilityKey) {
                            return (entity, context) -> {
                                if (entity.getItem().getCapability(Capabilities.FluidHandler.ITEM) != null) {
                                    new VanillaEntityItemFrameFluidHandler(entity);
                                }
                                return null;
                            };
                        }
                    });
            registry.registerEntity(() -> EntityType.GLOW_ITEM_FRAME,
                    new ICapabilityConstructor<GlowItemFrame, Direction, IFluidHandler, EntityType<GlowItemFrame>>() {
                        @Override
                        public BaseCapability<IFluidHandler, Direction> getCapability() {
                            return Capabilities.FluidHandler.ENTITY;
                        }

                        @Override
                        public ICapabilityProvider<GlowItemFrame, Direction, IFluidHandler> createProvider(EntityType<GlowItemFrame> capabilityKey) {
                            return (entity, context) -> {
                                if (entity.getItem().getCapability(Capabilities.FluidHandler.ITEM) != null) {
                                    new VanillaEntityItemFrameFluidHandler(entity);
                                }
                                return null;
                            };
                        }
                    });

            // EnergyStorage
            registry.registerEntity(() -> EntityType.ITEM,
                    new ICapabilityConstructor<ItemEntity, Direction, IEnergyStorage, EntityType<ItemEntity>>() {
                        @Override
                        public BaseCapability<IEnergyStorage, Direction> getCapability() {
                            return Capabilities.EnergyStorage.ENTITY;
                        }

                        @Override
                        public ICapabilityProvider<ItemEntity, Direction, IEnergyStorage> createProvider(EntityType<ItemEntity> capabilityKey) {
                            return (entity, context) -> {
                                if (entity.getItem().getCapability(Capabilities.EnergyStorage.ITEM) != null) {
                                    new VanillaEntityItemEnergyStorage(entity);
                                }
                                return null;
                            };
                        }
                    });
            registry.registerEntity(() -> EntityType.ITEM_FRAME,
                    new ICapabilityConstructor<ItemFrame, Direction, IEnergyStorage, EntityType<ItemFrame>>() {
                        @Override
                        public BaseCapability<IEnergyStorage, Direction> getCapability() {
                            return Capabilities.EnergyStorage.ENTITY;
                        }

                        @Override
                        public ICapabilityProvider<ItemFrame, Direction, IEnergyStorage> createProvider(EntityType<ItemFrame> capabilityKey) {
                            return (entity, context) -> {
                                if (entity.getItem().getCapability(Capabilities.EnergyStorage.ITEM) != null) {
                                    new VanillaEntityItemFrameEnergyStorage(entity);
                                }
                                return null;
                            };
                        }
                    });
            registry.registerEntity(() -> EntityType.GLOW_ITEM_FRAME,
                    new ICapabilityConstructor<GlowItemFrame, Direction, IEnergyStorage, EntityType<GlowItemFrame>>() {
                        @Override
                        public BaseCapability<IEnergyStorage, Direction> getCapability() {
                            return Capabilities.EnergyStorage.ENTITY;
                        }

                        @Override
                        public ICapabilityProvider<GlowItemFrame, Direction, IEnergyStorage> createProvider(EntityType<GlowItemFrame> capabilityKey) {
                            return (entity, context) -> {
                                if (entity.getItem().getCapability(Capabilities.EnergyStorage.ITEM) != null) {
                                    new VanillaEntityItemFrameEnergyStorage(entity);
                                }
                                return null;
                            };
                        }
                    });

            // RecipeHandler
            registry.registerBlockEntity(() -> BlockEntityType.BREWING_STAND,
                    new ICapabilityConstructor<BrewingStandBlockEntity, Direction, IRecipeHandler, BlockEntityType<BrewingStandBlockEntity>>() {
                        @Override
                        public BaseCapability<IRecipeHandler, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.RecipeHandler.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<BrewingStandBlockEntity, Direction, IRecipeHandler> createProvider(BlockEntityType<BrewingStandBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> VanillaBrewingStandRecipeHandler.getInstance();
                        }
                    });
            registry.registerBlockEntity(() -> BlockEntityType.FURNACE,
                    new ICapabilityConstructor<FurnaceBlockEntity, Direction, IRecipeHandler, BlockEntityType<FurnaceBlockEntity>>() {
                        @Override
                        public BaseCapability<IRecipeHandler, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.RecipeHandler.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<FurnaceBlockEntity, Direction, IRecipeHandler> createProvider(BlockEntityType<FurnaceBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaRecipeTypeRecipeHandler<>(blockEntity::getLevel,
                                    RecipeType.SMELTING, (size) -> size == 1);
                        }
                    });
            registry.registerBlockEntity(() -> BlockEntityType.BLAST_FURNACE,
                    new ICapabilityConstructor<BlastFurnaceBlockEntity, Direction, IRecipeHandler, BlockEntityType<BlastFurnaceBlockEntity>>() {
                        @Override
                        public BaseCapability<IRecipeHandler, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.RecipeHandler.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<BlastFurnaceBlockEntity, Direction, IRecipeHandler> createProvider(BlockEntityType<BlastFurnaceBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaRecipeTypeRecipeHandler<>(blockEntity::getLevel,
                                    RecipeType.BLASTING, (size) -> size == 1);
                        }
                    });
            registry.registerBlockEntity(() -> BlockEntityType.SMOKER,
                    new ICapabilityConstructor<SmokerBlockEntity, Direction, IRecipeHandler, BlockEntityType<SmokerBlockEntity>>() {
                        @Override
                        public BaseCapability<IRecipeHandler, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.RecipeHandler.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<SmokerBlockEntity, Direction, IRecipeHandler> createProvider(BlockEntityType<SmokerBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaRecipeTypeRecipeHandler<>(blockEntity::getLevel,
                                    RecipeType.SMOKING, (size) -> size == 1);
                        }
                    });
            registry.registerBlockEntity(() -> BlockEntityType.CAMPFIRE,
                    new ICapabilityConstructor<CampfireBlockEntity, Direction, IRecipeHandler, BlockEntityType<CampfireBlockEntity>>() {
                        @Override
                        public BaseCapability<IRecipeHandler, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.RecipeHandler.BLOCK;
                        }

                        @Override
                        public ICapabilityProvider<CampfireBlockEntity, Direction, IRecipeHandler> createProvider(BlockEntityType<CampfireBlockEntity> capabilityKey) {
                            return (blockEntity, side) -> new VanillaRecipeTypeRecipeHandler<>(blockEntity::getLevel,
                                    RecipeType.CAMPFIRE_COOKING, (size) -> size == 1);
                        }
                    });
            registry.registerInheritableBlock(CraftingTableBlock.class,
                    new IBlockCapabilityConstructor<ComposterBlock, Direction, IRecipeHandler, Block>() {
                        @Override
                        public BaseCapability<IRecipeHandler, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.RecipeHandler.BLOCK;
                        }

                        @Override
                        public IBlockCapabilityProvider<IRecipeHandler, Direction> createProvider(Block capabilityKey) {
                            return (level, pos, state, blockEntity, side) -> new VanillaRecipeTypeRecipeHandler<>(() -> level, RecipeType.CRAFTING, (size) -> size > 0);
                        }
                    });
            registry.registerBlock(() -> (StonecutterBlock) Blocks.STONECUTTER,
                    new IBlockCapabilityConstructor<StonecutterBlock, Direction, IRecipeHandler, StonecutterBlock>() {
                        @Override
                        public BaseCapability<IRecipeHandler, Direction> getCapability() {
                            return org.cyclops.commoncapabilities.api.capability.Capabilities.RecipeHandler.BLOCK;
                        }

                        @Override
                        public IBlockCapabilityProvider<IRecipeHandler, Direction> createProvider(StonecutterBlock capabilityKey) {
                            return (level, pos, state, blockEntity, side) -> new VanillaRecipeTypeRecipeHandler<>(() -> level, RecipeType.STONECUTTING, (size) -> size == 1);
                        }
                    });
        };
    }

}
