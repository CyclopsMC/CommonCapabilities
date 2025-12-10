package org.cyclops.commoncapabilities.modcompat.vanilla.capability;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.transfer.access.ItemAccess;

import java.util.Optional;

/**
 * An abstract capability capability delegator from entity item to inner itemstack.
 * @param <C> The capability type.
 * @author rubensworks
 */
public abstract class VanillaEntityItemCapabilityDelegator<C> implements IVanillaEntityItemCapabilityDelegator {

    private final ItemEntity entity;

    public VanillaEntityItemCapabilityDelegator(ItemEntity entity) {
        this.entity = entity;
    }

    public ItemEntity getEntity() {
        return entity;
    }

    @Override
    public ItemStack getItemStack() {
        return entity.getItem();
    }

    @Override
    public void updateItemStack(ItemStack itemStack) {
        entity.setItem(itemStack);
    }

    protected abstract ItemCapability<C, ItemAccess> getCapabilityType();

    protected Optional<C> getCapability(ItemStack itemStack) {
        return Optional.ofNullable(itemStack.getCapability(getCapabilityType(), new ItemAccessEntity(this)));
    }

    protected Optional<C> getCapability() {
        return getCapability(getItemStack());
    }

}
