package org.cyclops.commoncapabilities;

import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.commoncapabilities.capability.worker.WorkerConfig;

/**
 * A simple test mod which will print the work status for worker tiles.
 * @author rubensworks
 */
@Mod("test_commoncapabilities_vanilla_capability_worker")
public class TestCapabilityWorkerMod {

    public TestCapabilityWorkerMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getItemStack().isEmpty()) return;
        if (event.getItemStack().getItem() != Items.BLAZE_ROD) return;

        TileEntity te = event.getWorld().getTileEntity(event.getPos());
        if (te != null && te.getCapability(WorkerConfig.CAPABILITY, event.getFace()).isPresent()) {
            event.setCanceled(true);
            IWorker worker = te.getCapability(WorkerConfig.CAPABILITY, event.getFace()).orElse(null);
            System.out.println("Has work: " + worker.hasWork());
            System.out.println("Can work: " + worker.canWork());
        }
    }
}