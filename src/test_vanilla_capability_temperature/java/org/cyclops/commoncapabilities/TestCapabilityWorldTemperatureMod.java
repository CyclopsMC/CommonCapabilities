package org.cyclops.commoncapabilities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;
import org.cyclops.commoncapabilities.capability.temperature.TemperatureConfig;

/**
 * A simple test mod which will print the temperature capability for tiles, entities and items.
 * @author rubensworks
 */
@Mod("test_commoncapabilities_vanilla_capability_temperature")
public class TestCapabilityWorldTemperatureMod {

    public TestCapabilityWorldTemperatureMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    protected void printTemperature(ITemperature temperature) {
        System.out.println("Current temperature: " + temperature.getTemperature());
        System.out.println("Maximum temperature: " + temperature.getMaximumTemperature());
        System.out.println("Minimum temperature: " + temperature.getMinimumTemperature());
        System.out.println("Default temperature: " + temperature.getDefaultTemperature());
    }

    @SubscribeEvent
    public void onTileInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getItemStack().isEmpty()) return;
        if (event.getItemStack().getItem() != Items.BEETROOT) return;

        BlockEntity te = event.getWorld().getBlockEntity(event.getPos());
        if (te != null && te.getCapability(TemperatureConfig.CAPABILITY, event.getFace()).isPresent()) {
            event.setCanceled(true);
            ITemperature temperature = te.getCapability(TemperatureConfig.CAPABILITY, event.getFace()).orElse(null);
            printTemperature(temperature);
        }
    }

    @SubscribeEvent
    public void onEntityInteract(AttackEntityEvent event) {
        if (event.getPlayer() == null) return;
        if (event.getPlayer().getMainHandItem().isEmpty()) return;
        if (event.getPlayer().getMainHandItem().getItem() != Items.BEETROOT) return;

        Entity target = event.getTarget();
        if (target != null && target.getCapability(TemperatureConfig.CAPABILITY, null).isPresent()) {
            event.setCanceled(true);
            ITemperature temperature = target.getCapability(TemperatureConfig.CAPABILITY, null).orElse(null);
            printTemperature(temperature);
        }
    }

    @SubscribeEvent
    public void onItemInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().isEmpty()) return;
        if (!event.getPlayer().isCrouching()) return;

        ItemStack itemStack = event.getItemStack();
        if (itemStack.getCapability(TemperatureConfig.CAPABILITY, null).isPresent()) {
            event.setCanceled(true);
            ITemperature temperature = itemStack.getCapability(TemperatureConfig.CAPABILITY, null).orElse(null);
            printTemperature(temperature);
        }
    }
}