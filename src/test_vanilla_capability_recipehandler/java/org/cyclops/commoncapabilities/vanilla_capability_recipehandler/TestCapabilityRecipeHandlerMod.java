package org.cyclops.commoncapabilities.vanilla_capability_recipehandler;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.cyclops.commoncapabilities.api.capability.Capabilities;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;

/**
 * A simple test mod which will test the recipe handler of a block.
 * @author rubensworks
 */
@Mod("test_commoncapabilities_vanilla_capability_recipehandler")
public class TestCapabilityRecipeHandlerMod {

    public TestCapabilityRecipeHandlerMod() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTileInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getItemStack().isEmpty()) return;
        if (event.getItemStack().getItem() != Item.byBlock(Blocks.CRAFTING_TABLE)) return;
        if (event.getEntity().level().isClientSide()) return;

        IRecipeHandler recipeHandler = event.getLevel().getCapability(Capabilities.RecipeHandler.BLOCK, event.getPos(), event.getFace());

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
