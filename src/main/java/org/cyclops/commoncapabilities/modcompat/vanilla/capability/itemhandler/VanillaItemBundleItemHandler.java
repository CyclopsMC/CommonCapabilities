package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.apache.commons.lang3.math.Fraction;
import org.cyclops.commoncapabilities.capability.itemhandler.ItemItemHandler;

/**
 * An item handler wrapper for the bundle.
 * @author rubensworks
 */
public class VanillaItemBundleItemHandler extends ItemItemHandler {

    public VanillaItemBundleItemHandler(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected NonNullList<ItemStack> getItemList() {
        BundleContents container = getItemStack().get(DataComponents.BUNDLE_CONTENTS);
        if (container != null) {
            NonNullList<ItemStack> list = NonNullList.create();
            container.itemCopyStream().forEach(list::add);
            list.add(ItemStack.EMPTY);
            return list;
        }
        return NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    protected void setItemList(NonNullList<ItemStack> itemStacks) {
        getItemStack().set(DataComponents.BUNDLE_CONTENTS, new BundleContents(itemStacks.stream().filter(stack -> !stack.isEmpty()).toList()));
    }

    @Override
    public int size() {
        return getItemList().size();
    }


    @Override
    public long getCapacityAsLong(int slot, ItemResource itemResource) {
        return 64;
    }

    @Override
    public boolean isValid(int slot, ItemResource itemResource) {
        return itemResource.isEmpty() || (itemResource.getItem().canFitInsideContainerItems());
    }

    protected boolean isAmountValid(ItemStack stack) {
        return getMaxAmountToAdd(stack) > 0;
    }

    @Override
    public int insert(int slot, ItemResource itemResource, int amount, TransactionContext transaction) {
        if (!(isValid(slot, itemResource) && isAmountValid(itemResource.toStack(amount)))) {
            return 0;
        }
        return super.insert(slot, itemResource, amount, transaction);
    }

    // Copied from BundleContents

    private int getMaxAmountToAdd(ItemStack stackToAdd) {
        BundleContents container = getItemStack().get(DataComponents.BUNDLE_CONTENTS);
        Fraction fraction = Fraction.ONE.subtract(container.weight());
        return Math.max(fraction.divideBy(BundleContents.getWeight(stackToAdd)).intValue(), 0);
    }
}
