package org.cyclops.commoncapabilities.modcompat.vanilla.capability;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

/**
 * An abstract capability capability delegator from entity item to inner itemstack.
 * @param <C> The capability type.
 * @author rubensworks
 */
public abstract class VanillaEntityItemCapabilityDelegator<C> {

    private final ItemEntity entity;
    private final Direction side;

    public VanillaEntityItemCapabilityDelegator(ItemEntity entity, Direction side) {
        this.entity = entity;
        this.side = side;
    }

    public ItemEntity getEntity() {
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
