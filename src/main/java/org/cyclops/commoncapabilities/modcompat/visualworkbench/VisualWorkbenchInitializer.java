package org.cyclops.commoncapabilities.modcompat.visualworkbench;

import fuzs.visualworkbench.init.ModRegistry;
import fuzs.visualworkbench.world.level.block.entity.CraftingTableBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.recipehandler.VanillaRecipeTypeRecipeHandler;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * @author rubensworks
 */
public class VisualWorkbenchInitializer implements ICompatInitializer {
    @Override
    public void initialize(IModBase mod) {
        CapabilityConstructorRegistry registry = CommonCapabilities._instance.getCapabilityConstructorRegistry();
        registry.registerBlockEntity(ModRegistry.CRAFTING_TABLE_BLOCK_ENTITY_TYPE::value,
                new ICapabilityConstructor<CraftingTableBlockEntity, Direction, IRecipeHandler, BlockEntityType<CraftingTableBlockEntity>>() {
                    @Override
                    public BaseCapability<IRecipeHandler, Direction> getCapability() {
                        return org.cyclops.commoncapabilities.api.capability.Capabilities.RecipeHandler.BLOCK;
                    }

                    @Override
                    public @Nullable ICapabilityProvider<CraftingTableBlockEntity, Direction, IRecipeHandler> createProvider(BlockEntityType<CraftingTableBlockEntity> capabilityKey) {
                        return (blockEntity, side) -> new VanillaRecipeTypeRecipeHandler<>(blockEntity::getLevel,
                                RecipeType.CRAFTING, (size) -> size > 0, CraftingContainer::asCraftInput, false, false);
                    }
                });
    }
}
