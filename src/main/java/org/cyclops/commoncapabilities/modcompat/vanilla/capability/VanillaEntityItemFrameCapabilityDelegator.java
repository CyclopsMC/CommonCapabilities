package org.cyclops.commoncapabilities.modcompat.vanilla.capability;

import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.transfer.access.ItemAccess;

import java.util.Optional;

/**
 * An abstract capability capability delegator from entity item frame to inner itemstack.
 * @param <C> The capability type.
 * @author rubensworks
 */
public abstract class VanillaEntityItemFrameCapabilityDelegator<C> implements IVanillaEntityItemCapabilityDelegator {

    private final ItemFrame entity;

    public VanillaEntityItemFrameCapabilityDelegator(ItemFrame entity) {
        this.entity = entity;
    }

    public ItemFrame getEntity() {
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
