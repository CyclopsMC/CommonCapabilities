package org.cyclops.commoncapabilities.modcompat.vanilla.capability;

import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public interface IVanillaEntityItemCapabilityDelegator {

    public ItemStack getItemStack();

    public void updateItemStack(ItemStack itemStack);

    public static class ItemAccessEntity implements ItemAccess {

        private final IVanillaEntityItemCapabilityDelegator delegator;
        private final SnapshotJournal<ItemStack> snapshotJournal;
        @Nullable
        private ItemStack uncommittedStack = null;

        public ItemAccessEntity(IVanillaEntityItemCapabilityDelegator delegator) {
            this.delegator = delegator;
            this.snapshotJournal = new SnapshotJournal<>() {
                @Override
                protected ItemStack createSnapshot() {
                    return delegator.getItemStack().copy();
                }

                @Override
                protected void revertToSnapshot(ItemStack itemResource) {
                    delegator.updateItemStack(itemResource);
                }

                @Override
                protected void onRootCommit(ItemStack originalState) {
                    super.onRootCommit(originalState);
                    if (uncommittedStack != null) {
                        delegator.updateItemStack(uncommittedStack);
                        uncommittedStack = null;
                    }
                }
            };
        }

        @Override
        public ItemResource getResource() {
            return ItemResource.of(delegator.getItemStack());
        }

        @Override
        public int getAmount() {
            return delegator.getItemStack().getCount();
        }

        @Override
        public int insert(ItemResource itemResource, int amount, TransactionContext transactionContext) {
            if (delegator.getItemStack().isEmpty()) {
                this.snapshotJournal.updateSnapshots(transactionContext);
                delegator.updateItemStack(itemResource.toStack(amount));
                return amount;
            }
            return amount;
        }

        @Override
        public int extract(ItemResource itemResource, int amount, TransactionContext transactionContext) {
            ItemStack stack = delegator.getItemStack();
            if (!stack.isEmpty()) {
                int extracted = Math.min(amount, stack.getCount());
                stack = stack.copy();
                stack.shrink(extracted);
                delegator.updateItemStack(stack);
                this.snapshotJournal.updateSnapshots(transactionContext);
                return extracted;
            }
            return 0;
        }

        public static ItemAccessEntity of(ItemEntity entity) {
            return new ItemAccessEntity(new IVanillaEntityItemCapabilityDelegator() {
                @Override
                public ItemStack getItemStack() {
                    return entity.getItem();
                }

                @Override
                public void updateItemStack(ItemStack itemStack) {
                    entity.setItem(itemStack);
                }
            });
        }

        public static ItemAccessEntity of(ItemFrame entity) {
            return new ItemAccessEntity(new IVanillaEntityItemCapabilityDelegator() {
                @Override
                public ItemStack getItemStack() {
                    return entity.getItem();
                }

                @Override
                public void updateItemStack(ItemStack itemStack) {
                    entity.setItem(itemStack);
                }
            });
        }
    }

}
