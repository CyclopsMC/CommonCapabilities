package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import org.cyclops.commoncapabilities.capability.itemhandler.ItemItemHandler;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * An item handler wrapper for the shulker box in item form.
 * @author rubensworks
 */
public class VanillaItemShulkerBoxItemHandler extends ItemItemHandler {

    public VanillaItemShulkerBoxItemHandler(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected NonNullList<ItemStack> getItemList() {
        NonNullList<ItemStack> itemStacks = NonNullList.withSize(getSlots(), ItemStack.EMPTY);
        NBTTagCompound rootTag = getItemStack().getTagCompound();
        if (rootTag != null && rootTag.hasKey("BlockEntityTag", MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal())) {
            NBTTagCompound entityTag = rootTag.getCompoundTag("BlockEntityTag");
            if (entityTag.hasKey("Items", MinecraftHelpers.NBTTag_Types.NBTTagList.ordinal())) {
                ItemStackHelper.loadAllItems(entityTag, itemStacks);
            }
        }
        return itemStacks;
    }

    @Override
    protected void setItemList(NonNullList<ItemStack> itemStacks) {
        NBTTagCompound rootTag = ItemStackHelpers.getSafeTagCompound(getItemStack());
        if (!rootTag.hasKey("BlockEntityTag", MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal())) {
            rootTag.setTag("BlockEntityTag", new NBTTagCompound());
        }
        ItemStackHelper.saveAllItems(rootTag.getCompoundTag("BlockEntityTag"), itemStacks);
    }

    @Override
    public int getSlots() {
        return 27;
    }


    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }
}
