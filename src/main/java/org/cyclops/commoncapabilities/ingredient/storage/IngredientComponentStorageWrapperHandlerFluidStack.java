package org.cyclops.commoncapabilities.ingredient.storage;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidHandlerFluidStackIterator;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.capability.ICapabilityGetter;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageSlotted;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageWrapperHandler;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.ingredient.collection.FilteredIngredientCollectionIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

/**
 * Fluid storage wrapper handler for {@link IFluidHandler}.
 * @author rubensworks
 */
public class IngredientComponentStorageWrapperHandlerFluidStack<C>
        implements IIngredientComponentStorageWrapperHandler<FluidStack, Integer, IFluidHandler, C> {

    private final IngredientComponent<FluidStack, Integer> ingredientComponent;
    private final BaseCapability<? extends IFluidHandler, C> capability;

    public IngredientComponentStorageWrapperHandlerFluidStack(
            IngredientComponent<FluidStack, Integer> ingredientComponent,
            BaseCapability<? extends IFluidHandler, C> capability
    ) {
        this.ingredientComponent = Objects.requireNonNull(ingredientComponent);
        this.capability = capability;
    }

    public static IFluidHandler.FluidAction simulateToFluidAction(boolean simulate) {
        return simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
    }

    public static boolean fluidActionToSimulate(IFluidHandler.FluidAction fluidAction) {
        return fluidAction.simulate();
    }

    @Override
    public IIngredientComponentStorage<FluidStack, Integer> wrapComponentStorage(IFluidHandler storage) {
        return new ComponentStorageWrapper(getComponent(), storage);
    }

    @Override
    public IFluidHandler wrapStorage(IIngredientComponentStorage<FluidStack, Integer> componentStorage) {
        if (componentStorage instanceof IIngredientComponentStorageSlotted) {
            return new FluidStorageWrapperSlotted((IIngredientComponentStorageSlotted<FluidStack, Integer>) componentStorage);
        }
        return new FluidStorageWrapper(componentStorage);
    }

    @Override
    public Optional<IFluidHandler> getStorage(ICapabilityGetter<C> capabilityProvider, @Nullable C context) {
        return Optional.ofNullable(capabilityProvider.getCapability(this.capability, context));
    }

    @Override
    public IngredientComponent<FluidStack, Integer> getComponent() {
        return this.ingredientComponent;
    }

    public static class ComponentStorageWrapper implements IIngredientComponentStorageSlotted<FluidStack, Integer> {

        private final IngredientComponent<FluidStack, Integer> ingredientComponent;
        private final IFluidHandler storage;

        public ComponentStorageWrapper(IngredientComponent<FluidStack, Integer> ingredientComponent, IFluidHandler storage) {
            this.ingredientComponent = ingredientComponent;
            this.storage = storage;
        }

        @Override
        public IngredientComponent<FluidStack, Integer> getComponent() {
            return this.ingredientComponent;
        }

        @Override
        public Iterator<FluidStack> iterator() {
            return new FluidHandlerFluidStackIterator(storage);
        }

        @Override
        public Iterator<FluidStack> iterator(@Nonnull FluidStack prototype, Integer matchFlags) {
            if (getComponent().getMatcher().getAnyMatchCondition().equals(matchFlags)) {
                return iterator();
            }
            return new FilteredIngredientCollectionIterator<>(iterator(), getComponent().getMatcher(), prototype, matchFlags);
        }

        @Override
        public long getMaxQuantity() {
            long sum = 0;
            for (int i = 0; i < storage.getTanks(); i++) {
                sum = Math.addExact(sum, storage.getTankCapacity(i));
            }
            return sum;
        }

        @Override
        public FluidStack insert(@Nonnull FluidStack ingredient, boolean simulate) {
            // Don't continue if stack is empty
            if (ingredient.isEmpty()) {
                return FluidStack.EMPTY;
            }

            int totalAmount = ingredient.getAmount();
            int filledAmount = storage.fill(ingredient, simulateToFluidAction(simulate));
            if (filledAmount >= totalAmount) {
                return FluidStack.EMPTY;
            } else {
                int remaining = totalAmount - filledAmount;
                return new FluidStack(ingredient, remaining);
            }
        }

        @Override
        public FluidStack extract(@Nonnull FluidStack prototype, Integer matchFlags, boolean simulate) {
            // Don't continue if stack is empty
            if (prototype.isEmpty()) {
                return FluidStack.EMPTY;
            }

            // Optimize if ANY condition
            if (matchFlags == FluidMatch.ANY) {
                // Drain as much as possible
                return storage.drain(FluidHelpers.getAmount(prototype), simulateToFluidAction(simulate));
            }

            // Optimize if AMOUNT condition
            if (matchFlags == FluidMatch.AMOUNT) {
                // Drain the exact given amount
                FluidStack drainedSimulated = storage.drain(prototype.getAmount(), IFluidHandler.FluidAction.SIMULATE);
                if (drainedSimulated.isEmpty() || drainedSimulated.getAmount() != prototype.getAmount()) {
                    return FluidStack.EMPTY;
                }
                return simulate ? drainedSimulated : storage.drain(prototype.getAmount(), IFluidHandler.FluidAction.EXECUTE);
            }

            // In all other cases, we have to iterate over the tank contents,
            // and drain based on their contents.
            for (int i = 0; i < storage.getTanks(); i++) {
                FluidStack contents = storage.getFluidInTank(i);
                if (!contents.isEmpty()
                        && FluidMatch.areFluidStacksEqual(contents, prototype, matchFlags & ~FluidMatch.AMOUNT)) {
                    FluidStack toDrain = contents.copy();
                    toDrain.setAmount(prototype.getAmount());
                    FluidStack drained = storage.drain(toDrain, simulateToFluidAction(simulate));
                    if (FluidMatch.areFluidStacksEqual(drained, prototype, matchFlags)) {
                        return drained;
                    }
                }
            }

            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack extract(long maxQuantity, boolean simulate) {
            return storage.drain(Helpers.castSafe(maxQuantity), simulateToFluidAction(simulate));
        }

        @Override
        public int getSlots() {
            return storage.getTanks();
        }

        @Override
        public FluidStack getSlotContents(int slot) {
            return storage.getFluidInTank(slot);
        }

        @Override
        public long getMaxQuantity(int slot) {
            return storage.getTankCapacity(slot);
        }

        @Override
        public FluidStack insert(int slot, @Nonnull FluidStack ingredient, boolean simulate) {
            // There's no way to extract from a specific slot in IFluidHandler
            return insert(ingredient, simulate);
        }

        @Override
        public FluidStack extract(int slot, long maxQuantity, boolean simulate) {
            // There's no way to extract from a specific slot in IFluidHandler
            return extract(maxQuantity, simulate);
        }
    }

    public static class FluidStorageWrapper implements IFluidHandler {

        private final IIngredientComponentStorage<FluidStack, Integer> storage;

        public FluidStorageWrapper(IIngredientComponentStorage<FluidStack, Integer> storage) {
            this.storage = storage;
        }

        @Override
        public int getTanks() {
            // +1 so that at least one slot appears empty, for when others want to insert
            return Iterators.size(storage.iterator()) + 1;
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank) {
            return Iterables.get(this.storage, tank, FluidStack.EMPTY);
        }

        @Override
        public int getTankCapacity(int tank) {
            // Yes, this is an overestimate, as this is the total across ALL tanks
            return Helpers.castSafe(this.storage.getMaxQuantity());
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
            return true;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.isEmpty()) {
                return 0;
            }

            FluidStack inserted = storage.insert(resource, fluidActionToSimulate(action));
            return inserted.isEmpty() ? resource.getAmount() : resource.getAmount() - inserted.getAmount();
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            // Don't continue if stack is empty
            if (resource.isEmpty()) {
                return FluidStack.EMPTY;
            }

            FluidStack extractSimulated = storage.extract(resource, FluidMatch.FLUID | FluidMatch.TAG, true);
            if (extractSimulated != null) {
                FluidStack prototype = resource;
                if (prototype.getAmount() > extractSimulated.getAmount()) {
                    prototype = prototype.copy();
                    prototype.setAmount(extractSimulated.getAmount());
                }
                return storage.extract(prototype, FluidMatch.EXACT, fluidActionToSimulate(action));
            }
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return storage.extract(maxDrain, fluidActionToSimulate(action));
        }
    }

    public static class FluidStorageWrapperSlotted extends FluidStorageWrapper {

        private final IIngredientComponentStorageSlotted<FluidStack, Integer> storage;

        public FluidStorageWrapperSlotted(IIngredientComponentStorageSlotted<FluidStack, Integer> storage) {
            super(storage);
            this.storage = storage;
        }

        @Override
        public int getTanks() {
            return this.storage.getSlots();
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank) {
            int tanks = getTanks();
            if (tank < 0 || tank >= tanks) {
                throw new IndexOutOfBoundsException("Tank " + tank + " not in valid range - [0," + tanks + ")");
            }
            return this.storage.getSlotContents(tank);
        }

        @Override
        public int getTankCapacity(int tank) {
            return Helpers.castSafe(this.storage.getMaxQuantity(tank));
        }
    }

}
