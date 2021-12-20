package org.cyclops.commoncapabilities.vanilla_capability_itemitemhandler;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * A simple test mod which will print inventory states.
 * @author rubensworks
 */
@Mod("test_commoncapabilities_vanilla_capability_itemitemhandler")
public class TestCapabilityItemItemHandlerMod {

    public TestCapabilityItemItemHandlerMod() {
        MinecraftForge.EVENT_BUS.register(this);
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

        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        if (block == Blocks.HAY_BLOCK) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).isPresent()) {
                event.setCanceled(true);
                IItemHandler itemHandler = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
                printSlots(itemHandler);

                System.out.println("Adding apple to random slot");
                ItemStack remaining = itemHandler.insertItem((int) (Math.random() * itemHandler.getSlots()), new ItemStack(Items.APPLE), false);
                System.out.println("Remaining: " + remaining);
            }
        } else if (block == Blocks.STONE) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).isPresent()) {
                event.setCanceled(true);
                IItemHandler itemHandler = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
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

        Block block = event.getWorld().getBlockState(event.getPlayer().getOnPos()).getBlock();
        if (block == Blocks.HAY_BLOCK || block == Blocks.STONE) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        }
    }
}
