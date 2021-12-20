package org.cyclops.commoncapabilities.vanilla_capability_entityitem;

import com.google.common.collect.Iterables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.cyclopscore.helper.FluidHelpers;

/**
 * A simple test mod which tests entity item (frame) capabilities.
 * @author rubensworks
 */
@Mod("test_commoncapabilities_vanilla_capability_entityitem")
public class TestCapabilityEntityItemMod {

    public TestCapabilityEntityItemMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    protected void printSlots(IItemHandler itemHandler) {
        System.out.println("Item handler slots: " + itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            System.out.println("Slot " + i + " :" + itemHandler.getStackInSlot(i));
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getPlayer() == null) return;
        if (event.getPlayer().getMainHandItem().isEmpty()) return;

        Entity target = Iterables.get(event.getPlayer().level.getEntities(null,
                new AABB(event.getPos().relative(event.getFace()))), 0, null);
        if (target != null) {
            Item heldItem = event.getPlayer().getMainHandItem().getItem();
            if (!(target instanceof ItemEntity) && !(target instanceof ItemFrame)) return;
            if (target.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, event.getFace()).isPresent()) {
                event.setCanceled(true);
                IItemHandler itemHandler = target.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, event.getFace()).orElse(null);
                printSlots(itemHandler);
                if (heldItem == Items.APPLE) {
                    System.out.println("Adding apple to random slot");
                    ItemStack remaining = itemHandler.insertItem((int) (Math.random() * itemHandler.getSlots()), new ItemStack(Items.APPLE), false);
                    System.out.println("Remaining: " + remaining);
                } else if (heldItem == Items.BOWL) {
                    int slot = (int) (Math.random() * itemHandler.getSlots());
                    System.out.println("Removing apple from random slot " + slot);
                    ItemStack extracted = itemHandler.extractItem(slot, 1, false);
                    System.out.println("Extracted: " + extracted);
                }
            } else if (target.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, event.getFace()).isPresent()) {
                event.setCanceled(true);
                IFluidHandler fluidHandler = target.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, event.getFace()).orElse(null);
                System.out.println(FluidHelpers.getAmount(FluidHelpers.getFluid(fluidHandler)));
                if (heldItem == Items.APPLE) {
                    System.out.println("Adding water");
                    int filled = fluidHandler.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
                    System.out.println("Filled: " + filled);
                } else if (heldItem == Items.BOWL) {
                    System.out.println("Removing water");
                    FluidStack drained = fluidHandler.drain(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
                    System.out.println("Drained: " + FluidHelpers.getAmount(drained));
                }
            } else if (target.getCapability(CapabilityEnergy.ENERGY, event.getFace()).isPresent()) {
                event.setCanceled(true);
                IEnergyStorage energyStorage = target.getCapability(CapabilityEnergy.ENERGY, event.getFace()).orElse(null);
                System.out.println(energyStorage.getEnergyStored() + " / " + energyStorage.getMaxEnergyStored());
                if (heldItem == Items.APPLE) {
                    System.out.println("Adding energy");
                    int added = energyStorage.receiveEnergy(1000, false);
                    System.out.println("Added: " + added);
                } else if (heldItem == Items.BOWL) {
                    System.out.println("Removing energy");
                    int removed = energyStorage.extractEnergy(1000, false);
                    System.out.println("Removed: " + removed);
                }
            }
        }
    }
}