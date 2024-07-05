package org.cyclops.commoncapabilities.vanilla_capability_itemitemhandler;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * A simple test mod which will print inventory states.
 * @author rubensworks
 */
@Mod("test_commoncapabilities_vanilla_capability_itemitemhandler")
public class TestCapabilityItemItemHandlerMod {

    public TestCapabilityItemItemHandlerMod() {
        NeoForge.EVENT_BUS.register(this);
    }

    protected void printSlots(IItemHandler itemHandler) {
        System.out.println("Item handler slots: " + itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            System.out.println("Slot " + i + " :" + itemHandler.getStackInSlot(i));
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().isEmpty()) return;

        Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
        if (block == Blocks.HAY_BLOCK) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.getCapability(Capabilities.ItemHandler.ITEM) != null) {
                event.setCanceled(true);
                IItemHandler itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM, null);
                printSlots(itemHandler);

                System.out.println("Adding apple to random slot");
                ItemStack remaining = itemHandler.insertItem((int) (Math.random() * itemHandler.getSlots()), new ItemStack(Items.APPLE), false);
                System.out.println("Remaining: " + remaining);
            }
        } else if (block == Blocks.STONE) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.getCapability(Capabilities.ItemHandler.ITEM, null) != null) {
                event.setCanceled(true);
                IItemHandler itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM, null);
                printSlots(itemHandler);

                int slot = (int) (Math.random() * itemHandler.getSlots());
                System.out.println("Removing apple from random slot " + slot);
                ItemStack extracted = itemHandler.extractItem(slot, 1, false);
                System.out.println("Extracted: " + extracted);
            }
        }
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().isEmpty()) return;

        Block block = event.getLevel().getBlockState(event.getEntity().getOnPos()).getBlock();
        if (block == Blocks.HAY_BLOCK || block == Blocks.STONE) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        }
    }
}
