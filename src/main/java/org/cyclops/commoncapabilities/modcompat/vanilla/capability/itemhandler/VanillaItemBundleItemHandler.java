package org.cyclops.commoncapabilities.modcompat.vanilla.capability.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
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
        getItemStack().set(DataComponents.BUNDLE_CONTENTS, new BundleContents(itemStacks.stream().filter(stack -> !stack.isEmpty()).map(ItemStackTemplate::fromNonEmptyStack).toList()));
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

    @Override
    public int insert(int slot, ItemResource itemResource, int amount, TransactionContext transaction) {
        if (!(isValid(slot, itemResource))) {
            return 0;
        }
        return super.insert(slot, itemResource, Math.min(amount, getMaxAmountToAdd(itemResource.toStack(amount))), transaction);
    }

    // Copied from BundleContents

    private int getMaxAmountToAdd(ItemStack stackToAdd) {
        BundleContents container = getItemStack().get(DataComponents.BUNDLE_CONTENTS);
        Fraction containerWeight = container.weight().result().orElse(Fraction.ONE);
        Fraction fraction = Fraction.ONE.subtract(containerWeight);
        Fraction itemWeight = BundleContents.getWeight(stackToAdd).result().orElse(Fraction.ONE);
        return Math.max(fraction.divideBy(itemWeight).intValue(), 0);
    }
}
