package org.cyclops.commoncapabilities.vanilla_capability_inventorystate;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.cyclops.commoncapabilities.api.capability.Capabilities;
import org.cyclops.commoncapabilities.api.capability.inventorystate.IInventoryState;

/**
 * A simple test mod which will print inventory states.
 * @author rubensworks
 */
@Mod("test_commoncapabilities_vanilla_capability_inventorystate")
public class TestCapabilityInventoryStateMod {

    public TestCapabilityInventoryStateMod() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTileInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getItemStack().isEmpty()) return;
        if (event.getItemStack().getItem() != Items.ARROW) return;

        IInventoryState inventoryState = event.getLevel().getCapability(Capabilities.InventoryState.BLOCK, event.getPos(), event.getFace());
        if (inventoryState != null) {
            event.setCanceled(true);
            System.out.println("Inventory state: " + inventoryState.getState());
        }
    }

    @SubscribeEvent
    public void onEntityInteract(AttackEntityEvent event) {
        if (event.getEntity() == null) return;
        if (event.getEntity().getMainHandItem().isEmpty()) return;
        if (event.getEntity().getMainHandItem().getItem() != Items.ARROW) return;

        Entity target = event.getTarget();
        if (target != null && target.getCapability(Capabilities.InventoryState.ENTITY, null) != null) {
            event.setCanceled(true);
            IInventoryState inventoryState = target.getCapability(Capabilities.InventoryState.ENTITY, null);
            System.out.println("Inventory state: " + inventoryState.getState());
        }
    }

    @SubscribeEvent
    public void onItemInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().isEmpty()) return;
        if (event.getItemStack().getItem() != Items.ARROW) return;

        ItemStack itemStack = event.getItemStack();
        if (itemStack.getCapability(Capabilities.InventoryState.ITEM, null) != null) {
            event.setCanceled(true);
            IInventoryState inventoryState = itemStack.getCapability(Capabilities.InventoryState.ITEM, null);
            System.out.println("Inventory state: " + inventoryState.getState());
        }
    }
}
