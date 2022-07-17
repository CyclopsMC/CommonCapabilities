package org.cyclops.commoncapabilities.vanilla_capability_inventorystate;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.commoncapabilities.api.capability.inventorystate.IInventoryState;
import org.cyclops.commoncapabilities.capability.inventorystate.InventoryStateConfig;

/**
 * A simple test mod which will print inventory states.
 * @author rubensworks
 */
@Mod("test_commoncapabilities_vanilla_capability_inventorystate")
public class TestCapabilityInventoryStateMod {

    public TestCapabilityInventoryStateMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTileInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getItemStack().isEmpty()) return;
        if (event.getItemStack().getItem() != Items.ARROW) return;


        BlockEntity te = event.getLevel().getBlockEntity(event.getPos());
        if (te != null && te.getCapability(InventoryStateConfig.CAPABILITY, event.getFace()).isPresent()) {
            event.setCanceled(true);
            IInventoryState inventoryState = te.getCapability(InventoryStateConfig.CAPABILITY, event.getFace()).orElse(null);
            System.out.println("Inventory state: " + inventoryState.getState());
        }
    }

    @SubscribeEvent
    public void onEntityInteract(AttackEntityEvent event) {
        if (event.getEntity() == null) return;
        if (event.getEntity().getMainHandItem().isEmpty()) return;
        if (event.getEntity().getMainHandItem().getItem() != Items.ARROW) return;

        Entity target = event.getTarget();
        if (target != null && target.getCapability(InventoryStateConfig.CAPABILITY, null).isPresent()) {
            event.setCanceled(true);
            IInventoryState inventoryState = target.getCapability(InventoryStateConfig.CAPABILITY, null).orElse(null);
            System.out.println("Inventory state: " + inventoryState.getState());
        }
    }

    @SubscribeEvent
    public void onItemInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().isEmpty()) return;
        if (event.getItemStack().getItem() != Items.ARROW) return;

        ItemStack itemStack = event.getItemStack();
        if (itemStack.getCapability(InventoryStateConfig.CAPABILITY, null).isPresent()) {
            event.setCanceled(true);
            IInventoryState inventoryState = itemStack.getCapability(InventoryStateConfig.CAPABILITY, null).orElse(null);
            System.out.println("Inventory state: " + inventoryState.getState());
        }
    }
}
