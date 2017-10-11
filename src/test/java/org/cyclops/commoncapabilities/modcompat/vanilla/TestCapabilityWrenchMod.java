package org.cyclops.commoncapabilities.modcompat.vanilla;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.commoncapabilities.CommonCapabilities;
import org.cyclops.commoncapabilities.api.capability.wrench.DefaultWrench;
import org.cyclops.commoncapabilities.api.capability.wrench.IWrench;
import org.cyclops.commoncapabilities.api.capability.wrench.WrenchTarget;
import org.cyclops.commoncapabilities.capability.wrench.WrenchConfig;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;

/**
 * A simple test mod which will add the wrench capability to a stick.
 * It will allow all wrench items to rotate blocks.
 * @author rubensworks
 */
@Mod(modid="test.commoncapabilities.vanilla.capability.wrench",version="1.0")
public class TestCapabilityWrenchMod {
    
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent evt) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt) {
        CapabilityConstructorRegistry registry = CommonCapabilities._instance.getCapabilityConstructorRegistry();
        registry.registerItem(Item.class, new ICapabilityConstructor<IWrench, Item, ItemStack>() {
            @Override
            public Capability<IWrench> getCapability() {
                return WrenchConfig.CAPABILITY;
            }

            @Override
            public ICapabilityProvider createProvider(Item hostType, ItemStack host) {
                if(hostType == Items.STICK) {
                    return new DefaultCapabilityProvider<>(this::getCapability, new DefaultWrench());
                }
                return null;
            }
        });
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().isEmpty()) return;
        if (!event.getItemStack().hasCapability(WrenchConfig.CAPABILITY, null)) return;

        System.out.println("wrenched!");
        IWrench wrench = event.getItemStack().getCapability(WrenchConfig.CAPABILITY, null);
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        EnumFacing side = event.getFace();
        WrenchTarget wrenchTarget = WrenchTarget.forBlock(world, pos, side);
        EntityPlayer player = event.getEntityPlayer();
        if (wrench.canUse(player, wrenchTarget)) {
            wrench.beforeUse(player, wrenchTarget);
            world.getBlockState(pos).getBlock().rotateBlock(world, pos, side);
            wrench.afterUse(player, wrenchTarget);
        }
    }
}