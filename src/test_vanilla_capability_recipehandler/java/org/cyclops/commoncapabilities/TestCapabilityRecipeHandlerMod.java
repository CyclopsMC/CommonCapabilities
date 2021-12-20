package org.cyclops.commoncapabilities;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.commoncapabilities.api.capability.block.BlockCapabilities;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.capability.recipehandler.RecipeHandlerConfig;

/**
 * A simple test mod which will test the recipe handler of a block.
 * @author rubensworks
 */
@Mod("test_commoncapabilities_vanilla_capability_recipehandler")
public class TestCapabilityRecipeHandlerMod {

    public TestCapabilityRecipeHandlerMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTileInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getItemStack().isEmpty()) return;
        if (event.getItemStack().getItem() != Item.byBlock(Blocks.CRAFTING_TABLE)) return;
        if (event.getPlayer().level.isClientSide()) return;

        IRecipeHandler recipeHandler = null;
        BlockEntity te = event.getWorld().getBlockEntity(event.getPos());
        BlockState blockState = event.getWorld().getBlockState(event.getPos());
        if (te != null && te.getCapability(RecipeHandlerConfig.CAPABILITY, event.getFace()).isPresent()) {
            recipeHandler = te.getCapability(RecipeHandlerConfig.CAPABILITY, event.getFace()).orElse(null);
        } else if (BlockCapabilities.getInstance().getCapability(blockState, RecipeHandlerConfig.CAPABILITY,
                event.getWorld(), event.getPos(), event.getFace()).isPresent()) {
            recipeHandler = BlockCapabilities.getInstance().getCapability(blockState, RecipeHandlerConfig.CAPABILITY,
                    event.getWorld(), event.getPos(), event.getFace()).orElse(null);
        }

        if (recipeHandler != null) {
            event.setCanceled(true);
            System.out.println("---Recipe Handler---");
            System.out.println("Recipe components input: ");
            for (IngredientComponent<?, ?> recipeComponent : recipeHandler.getRecipeInputComponents()) {
                System.out.println("  " + recipeComponent.toString());
            }
            System.out.println("Recipe components output: ");
            for (IngredientComponent<?, ?> recipeComponent : recipeHandler.getRecipeOutputComponents()) {
                System.out.println("  " + recipeComponent.toString());
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