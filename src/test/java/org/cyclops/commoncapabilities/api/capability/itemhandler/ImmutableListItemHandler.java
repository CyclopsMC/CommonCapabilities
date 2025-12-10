package org.cyclops.commoncapabilities.api.capability.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

/**
 * A simple immutable list-based item handler.
 * @author rubensworks
 */
public class ImmutableListItemHandler extends ItemStacksResourceHandler {
    public ImmutableListItemHandler(NonNullList<ItemStack> itemStacks) {
        super(itemStacks);
    }
}
