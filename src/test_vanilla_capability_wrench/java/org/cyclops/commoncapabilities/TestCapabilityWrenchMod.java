package org.cyclops.commoncapabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.commoncapabilities.api.capability.wrench.DefaultWrench;
import org.cyclops.commoncapabilities.api.capability.wrench.IWrench;
import org.cyclops.commoncapabilities.api.capability.wrench.WrenchTarget;
import org.cyclops.commoncapabilities.capability.wrench.WrenchConfig;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;

import java.util.function.Consumer;

/**
 * A simple test mod which will add the wrench capability to a stick.
 * It will allow all wrench items to rotate blocks.
 * @author rubensworks
 */
@Mod("test_commoncapabilities_vanilla_capability_wrench")
public class TestCapabilityWrenchMod {

    public TestCapabilityWrenchMod() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(new Consumer<FMLCommonSetupEvent>() {
            @Override
            public void accept(FMLCommonSetupEvent event) {
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
        });
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().isEmpty()) return;
        if (!event.getItemStack().getCapability(WrenchConfig.CAPABILITY, null).isPresent()) return;

        System.out.println("wrenched!");
        IWrench wrench = event.getItemStack().getCapability(WrenchConfig.CAPABILITY, null).orElse(null);
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        Direction side = event.getFace();
        WrenchTarget wrenchTarget = WrenchTarget.forBlock(world, pos, side);
        PlayerEntity player = event.getEntityPlayer();
        if (wrench.canUse(player, wrenchTarget)) {
            wrench.beforeUse(player, wrenchTarget);
            world.getBlockState(pos).getBlock().rotate(world.getBlockState(pos), world, pos, Rotation.CLOCKWISE_90);
            wrench.afterUse(player, wrenchTarget);
        }
    }
}