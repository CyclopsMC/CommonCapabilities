package org.cyclops.commoncapabilities.vanilla_capability_temperature;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.cyclops.commoncapabilities.api.capability.Capabilities;
import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;

/**
 * A simple test mod which will print the temperature capability for tiles, entities and items.
 * @author rubensworks
 */
@Mod("test_commoncapabilities_vanilla_capability_temperature")
public class TestCapabilityWorldTemperatureMod {

    public TestCapabilityWorldTemperatureMod() {
        NeoForge.EVENT_BUS.register(this);
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

        ITemperature temperature = event.getLevel().getCapability(Capabilities.Temperature.BLOCK, event.getPos(), event.getFace());
        if (temperature != null) {
            event.setCanceled(true);
            printTemperature(temperature);
        }
    }

    @SubscribeEvent
    public void onEntityInteract(AttackEntityEvent event) {
        if (event.getEntity() == null) return;
        if (event.getEntity().getMainHandItem().isEmpty()) return;
        if (event.getEntity().getMainHandItem().getItem() != Items.BEETROOT) return;

        Entity target = event.getTarget();
        if (target != null && target.getCapability(Capabilities.Temperature.ENTITY, event.getEntity().getDirection()) != null) {
            event.setCanceled(true);
            ITemperature temperature = target.getCapability(Capabilities.Temperature.ENTITY, event.getEntity().getDirection());
            printTemperature(temperature);
        }
    }

    @SubscribeEvent
    public void onItemInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().isEmpty()) return;
        if (!event.getEntity().isCrouching()) return;

        ItemStack itemStack = event.getItemStack();
        if (itemStack.getCapability(Capabilities.Temperature.ITEM, null) != null) {
            event.setCanceled(true);
            ITemperature temperature = itemStack.getCapability(Capabilities.Temperature.ITEM, null);
            printTemperature(temperature);
        }
    }
}
