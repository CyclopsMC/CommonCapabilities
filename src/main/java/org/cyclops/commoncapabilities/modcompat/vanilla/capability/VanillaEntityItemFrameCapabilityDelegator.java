package org.cyclops.commoncapabilities.modcompat.vanilla.capability;

import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

/**
 * An abstract capability capability delegator from entity item frame to inner itemstack.
 * @param <C> The capability type.
 * @author rubensworks
 */
public abstract class VanillaEntityItemFrameCapabilityDelegator<C> {

    private final ItemFrame entity;
    private final Direction side;

    public VanillaEntityItemFrameCapabilityDelegator(ItemFrame entity, Direction side) {
        this.entity = entity;
        this.side = side;
    }

    public ItemFrame getEntity() {
        return entity;
    }

    public Direction getSide() {
        return side;
    }

    protected ItemStack getItemStack() {
        return entity.getItem();
    }

    protected void updateItemStack(ItemStack itemStack) {
        entity.setItem(itemStack);
    }

    protected abstract Capability<C> getCapabilityType();

    protected LazyOptional<C> getCapability(ItemStack itemStack) {
        return itemStack.getCapability(getCapabilityType(), getSide());
    }

    protected LazyOptional<C> getCapability() {
        return getCapability(getItemStack());
    }
}
