package org.cyclops.commoncapabilities.modcompat.thermalexpansion.itemhandler;

import cofh.api.item.IInventoryContainerItem;
import cofh.core.gui.container.InventoryContainerItemWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

/**
 * An item handler wrapper for {@link IInventoryContainerItem}.
 * @author rubensworks
 */
public class InventoryContainerItemItemHandler extends InvWrapper {

    public InventoryContainerItemItemHandler(ItemStack itemStack) {
        super(new InventoryContainerItemWrapper(itemStack));
    }

}
