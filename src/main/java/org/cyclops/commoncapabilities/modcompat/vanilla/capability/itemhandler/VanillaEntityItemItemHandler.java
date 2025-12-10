package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.commoncapabilities.modcompat.vanilla.capability.VanillaEntityItemCapabilityDelegator;

/**
 * An item handler for entity items that have an item handler.
 * @author rubensworks
 */
public class VanillaEntityItemItemHandler extends VanillaEntityItemCapabilityDelegator<ResourceHandler<ItemResource>> implements ResourceHandler<ItemResource> {

    public VanillaEntityItemItemHandler(ItemEntity entity) {
        super(entity);
    }

    @Override
    protected ItemCapability<ResourceHandler<ItemResource>, ItemAccess> getCapabilityType() {
        return Capabilities.Item.ITEM;
    }

    @Override
    public int size() {
        return getCapability()
                .map(ResourceHandler::size)
                .orElse(0);
    }

    @Override
    public ItemResource getResource(int slot) {
        return getCapability()
                .map(itemHandler -> itemHandler.getResource(slot))
                .orElse(ItemResource.EMPTY);
    }

    @Override
    public long getAmountAsLong(int slot) {
        return getCapability()
                .map(itemHandler -> itemHandler.getAmountAsLong(slot))
                .orElse(0L);
    }

    @Override
    public long getCapacityAsLong(int slot, ItemResource itemResource) {
        return getCapability()
                .map(itemHandler -> itemHandler.getCapacityAsLong(slot, itemResource))
                .orElse(0L);
    }

    @Override
    public boolean isValid(int slot, ItemResource itemResource) {
        return getCapability()
                .map(itemHandler -> itemHandler.isValid(slot, itemResource))
                .orElse(false);
    }

    @Override
    public int insert(int slot, ItemResource itemResource, int amount, TransactionContext transactionContext) {
        return getCapability()
                .map(itemHandler -> itemHandler.insert(slot, itemResource, amount, transactionContext))
                .orElse(0);
    }

    @Override
    public int insert(ItemResource itemResource, int amount, TransactionContext transactionContext) {
        return getCapability()
                .map(itemHandler -> itemHandler.insert(itemResource, amount, transactionContext))
                .orElse(0);
    }

    @Override
    public int extract(int slot, ItemResource itemResource, int amount, TransactionContext transactionContext) {
        return getCapability()
                .map(itemHandler -> itemHandler.extract(slot, itemResource, amount, transactionContext))
                .orElse(0);
    }

    @Override
    public int extract(ItemResource itemResource, int amount, TransactionContext transactionContext) {
        return getCapability()
                .map(itemHandler -> itemHandler.extract(itemResource, amount, transactionContext))
                .orElse(0);
    }
}
