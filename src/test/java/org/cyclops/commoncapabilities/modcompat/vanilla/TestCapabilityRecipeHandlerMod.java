package org.cyclops.commoncapabilities.modcompat.vanilla;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.commoncapabilities.api.capability.block.BlockCapabilities;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.capability.recipehandler.RecipeHandlerConfig;

import java.util.Arrays;

/**
 * A simple test mod which will test the recipe handler of a block.
 * @author rubensworks
 */
@Mod(modid="test.commoncapabilities.vanilla.capability.recipehandler",version="1.0")
public class TestCapabilityRecipeHandlerMod {
    
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent evt) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTileInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getItemStack().isEmpty()) return;
        if (event.getItemStack().getItem() != Item.getItemFromBlock(Blocks.CRAFTING_TABLE)) return;
        if (event.getEntityPlayer().getEntityWorld().isRemote) return;

        IRecipeHandler recipeHandler = null;
        TileEntity te = event.getWorld().getTileEntity(event.getPos());
        IBlockState blockState = event.getWorld().getBlockState(event.getPos());
        if (te != null && te.hasCapability(RecipeHandlerConfig.CAPABILITY, event.getFace())) {
            recipeHandler = te.getCapability(RecipeHandlerConfig.CAPABILITY, event.getFace());
        } else if (BlockCapabilities.getInstance().hasCapability(blockState, RecipeHandlerConfig.CAPABILITY,
                event.getWorld(), event.getPos(), event.getFace())) {
            recipeHandler = BlockCapabilities.getInstance().getCapability(blockState, RecipeHandlerConfig.CAPABILITY,
                    event.getWorld(), event.getPos(), event.getFace());
        }

        if (recipeHandler != null) {
            event.setCanceled(true);
            System.out.println("---Recipe Handler---");
            System.out.println("Recipe components input: ");
            for (IngredientComponent<?, ?, ?> recipeComponent : recipeHandler.getRecipeInputComponents()) {
                System.out.println("  " + recipeComponent.toString());
                System.out.println("    targets: " + Arrays.toString(recipeHandler.getInputComponentTargets(recipeComponent)));
            }
            System.out.println("Recipe components output: ");
            for (IngredientComponent<?, ?, ?> recipeComponent : recipeHandler.getRecipeOutputComponents()) {
                System.out.println("  " + recipeComponent.toString());
                System.out.println("    targets: " + Arrays.toString(recipeHandler.getOutputComponentTargets(recipeComponent)));
            }
            System.out.println("Recipes: " + recipeHandler.getRecipes().size());
            for (IRecipeDefinition recipe : recipeHandler.getRecipes()) {
                System.out.println(recipe.toString());
                System.out.println("  Simulated output: " + recipeHandler.simulate(MixedIngredients.fromRecipeInput(recipe)));
            }
            System.out.println("---     ---");
        }
    }
}