package org.cyclops.commoncapabilities.vanilla_capability_worker;

import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.cyclops.commoncapabilities.api.capability.Capabilities;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * A simple test mod which will print the work status for worker tiles.
 * @author rubensworks
 */
@Mod("test_commoncapabilities_vanilla_capability_worker")
public class TestCapabilityWorkerMod {

    public TestCapabilityWorkerMod() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getItemStack().isEmpty()) return;
        if (event.getItemStack().getItem() != Items.BLAZE_ROD) return;

        IWorker worker = event.getLevel().getCapability(Capabilities.Worker.BLOCK, event.getPos(), event.getFace());
        if (worker != null) {
            event.setCanceled(true);
            System.out.println("Has work: " + worker.hasWork());
            System.out.println("Can work: " + worker.canWork());
        }
    }
}
