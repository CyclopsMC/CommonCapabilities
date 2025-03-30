package org.cyclops.commoncapabilities.gametest;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.Reference;
import org.cyclops.commoncapabilities.api.capability.Capabilities;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;

/**
 * @author rubensworks
 */
@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsVanillaCapabilitiesBlockRecipeHandler {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 1, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockRecipeHandlerCapBrewingStand(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.BREWING_STAND);

        helper.succeedIf(() -> {
            // Check if recipe handler capability exists and is valid
            IRecipeHandler recipeHandler = helper.getLevel().getCapability(Capabilities.RecipeHandler.BLOCK, helper.absolutePos(POS), Direction.NORTH);

            helper.assertTrue(recipeHandler != null, "Recipe handler does not exist");
            helper.assertValueEqual(recipeHandler.getRecipeInputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Input components are incorrect");
            helper.assertValueEqual(recipeHandler.getRecipeOutputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Output components are incorrect");
            helper.assertTrue(recipeHandler.getRecipes().size() >= 279, "Recipe count is less than 279");
            for (IRecipeDefinition recipe : recipeHandler.getRecipes()) {
                helper.assertTrue(recipeHandler.simulate(MixedIngredients.fromRecipeInput(recipe)) != null, "Recipe simulation failed for " + recipe);
            }
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockRecipeHandlerCapFurnace(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.FURNACE);

        helper.succeedIf(() -> {
            // Check if recipe handler capability exists and is valid
            IRecipeHandler recipeHandler = helper.getLevel().getCapability(Capabilities.RecipeHandler.BLOCK, helper.absolutePos(POS), Direction.NORTH);

            helper.assertTrue(recipeHandler != null, "Recipe handler does not exist");
            helper.assertValueEqual(recipeHandler.getRecipeInputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Input components are incorrect");
            helper.assertValueEqual(recipeHandler.getRecipeOutputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Output components are incorrect");
            helper.assertTrue(recipeHandler.getRecipes().size() >= 70, "Recipe count is less than 70");
            for (IRecipeDefinition recipe : recipeHandler.getRecipes()) {
                helper.assertTrue(recipeHandler.simulate(MixedIngredients.fromRecipeInput(recipe)) != null, "Recipe simulation failed for " + recipe);
            }
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockRecipeHandlerCapBlastFurnace(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.BLAST_FURNACE);

        helper.succeedIf(() -> {
            // Check if recipe handler capability exists and is valid
            IRecipeHandler recipeHandler = helper.getLevel().getCapability(Capabilities.RecipeHandler.BLOCK, helper.absolutePos(POS), Direction.NORTH);

            helper.assertTrue(recipeHandler != null, "Recipe handler does not exist");
            helper.assertValueEqual(recipeHandler.getRecipeInputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Input components are incorrect");
            helper.assertValueEqual(recipeHandler.getRecipeOutputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Output components are incorrect");
            helper.assertTrue(recipeHandler.getRecipes().size() >= 24, "Recipe count is less than 24");
            for (IRecipeDefinition recipe : recipeHandler.getRecipes()) {
                helper.assertTrue(recipeHandler.simulate(MixedIngredients.fromRecipeInput(recipe)) != null, "Recipe simulation failed for " + recipe);
            }
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockRecipeHandlerCapSmoker(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.SMOKER);

        helper.succeedIf(() -> {
            // Check if recipe handler capability exists and is valid
            IRecipeHandler recipeHandler = helper.getLevel().getCapability(Capabilities.RecipeHandler.BLOCK, helper.absolutePos(POS), Direction.NORTH);

            helper.assertTrue(recipeHandler != null, "Recipe handler does not exist");
            helper.assertValueEqual(recipeHandler.getRecipeInputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Input components are incorrect");
            helper.assertValueEqual(recipeHandler.getRecipeOutputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Output components are incorrect");
            helper.assertTrue(recipeHandler.getRecipes().size() >= 9, "Recipe count is less than 9");
            for (IRecipeDefinition recipe : recipeHandler.getRecipes()) {
                helper.assertTrue(recipeHandler.simulate(MixedIngredients.fromRecipeInput(recipe)) != null, "Recipe simulation failed for " + recipe);
            }
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockRecipeHandlerCapCampFire(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.CAMPFIRE);

        helper.succeedIf(() -> {
            // Check if recipe handler capability exists and is valid
            IRecipeHandler recipeHandler = helper.getLevel().getCapability(Capabilities.RecipeHandler.BLOCK, helper.absolutePos(POS), Direction.NORTH);

            helper.assertTrue(recipeHandler != null, "Recipe handler does not exist");
            helper.assertValueEqual(recipeHandler.getRecipeInputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Input components are incorrect");
            helper.assertValueEqual(recipeHandler.getRecipeOutputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Output components are incorrect");
            helper.assertTrue(recipeHandler.getRecipes().size() >= 9, "Recipe count is less than 9");
            for (IRecipeDefinition recipe : recipeHandler.getRecipes()) {
                helper.assertTrue(recipeHandler.simulate(MixedIngredients.fromRecipeInput(recipe)) != null, "Recipe simulation failed for " + recipe);
            }
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockRecipeHandlerCapCrafingTable(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.CRAFTING_TABLE);

        helper.succeedIf(() -> {
            // Check if recipe handler capability exists and is valid
            IRecipeHandler recipeHandler = helper.getLevel().getCapability(Capabilities.RecipeHandler.BLOCK, helper.absolutePos(POS), Direction.NORTH);

            helper.assertTrue(recipeHandler != null, "Recipe handler does not exist");
            helper.assertValueEqual(recipeHandler.getRecipeInputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Input components are incorrect");
            helper.assertValueEqual(recipeHandler.getRecipeOutputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Output components are incorrect");
            System.out.println(recipeHandler.getRecipes().size());
            helper.assertTrue(recipeHandler.getRecipes().size() >= 888, "Recipe count is less than 888");
            for (IRecipeDefinition recipe : recipeHandler.getRecipes()) {
                helper.assertTrue(recipeHandler.simulate(MixedIngredients.fromRecipeInput(recipe)) != null, "Recipe simulation failed for " + recipe);
            }
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockRecipeHandlerCapStoneCutter(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.STONECUTTER);

        helper.succeedIf(() -> {
            // Check if recipe handler capability exists and is valid
            IRecipeHandler recipeHandler = helper.getLevel().getCapability(Capabilities.RecipeHandler.BLOCK, helper.absolutePos(POS), Direction.NORTH);

            helper.assertTrue(recipeHandler != null, "Recipe handler does not exist");
            helper.assertValueEqual(recipeHandler.getRecipeInputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Input components are incorrect");
            helper.assertValueEqual(recipeHandler.getRecipeOutputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Output components are incorrect");
            helper.assertTrue(recipeHandler.getRecipes().size() >= 250, "Recipe count is less than 250");
            for (IRecipeDefinition recipe : recipeHandler.getRecipes()) {
                helper.assertTrue(recipeHandler.simulate(MixedIngredients.fromRecipeInput(recipe)) != null, "Recipe simulation failed for " + recipe);
            }
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockRecipeHandlerCapSmithingTable(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.SMITHING_TABLE);

        helper.succeedIf(() -> {
            // Check if recipe handler capability exists and is valid
            IRecipeHandler recipeHandler = helper.getLevel().getCapability(Capabilities.RecipeHandler.BLOCK, helper.absolutePos(POS), Direction.NORTH);

            helper.assertTrue(recipeHandler != null, "Recipe handler does not exist");
            helper.assertValueEqual(recipeHandler.getRecipeInputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Input components are incorrect");
            helper.assertValueEqual(recipeHandler.getRecipeOutputComponents(), Sets.newHashSet(IngredientComponents.ITEMSTACK), "Output components are incorrect");
            System.out.println(recipeHandler.getRecipes().size());
            helper.assertTrue(recipeHandler.getRecipes().size() >= 0, "Recipe count is less than 0");
            for (IRecipeDefinition recipe : recipeHandler.getRecipes()) {
                helper.assertTrue(recipeHandler.simulate(MixedIngredients.fromRecipeInput(recipe)) != null, "Recipe simulation failed for " + recipe);
            }
        });
    }

}
