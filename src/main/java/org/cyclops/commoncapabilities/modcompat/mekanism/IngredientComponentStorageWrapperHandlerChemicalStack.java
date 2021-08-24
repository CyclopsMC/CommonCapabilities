package org.cyclops.commoncapabilities.modcompat.mekanism;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import mekanism.api.Action;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageSlotted;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageWrapperHandler;
import org.cyclops.cyclopscore.ingredient.collection.FilteredIngredientCollectionIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;

/**
 * Chemical storage wrapper handler for {@link IChemicalHandler}.
 * @author rubensworks
 */
public abstract class IngredientComponentStorageWrapperHandlerChemicalStack<C extends Chemical<C>, S extends ChemicalStack<C>, H extends IChemicalHandler<C, S>>
        implements IIngredientComponentStorageWrapperHandler<S, Integer, H> {

    private final IngredientComponent<S, Integer> ingredientComponent;
    private final Capability<H> handlerCapability;

    public IngredientComponentStorageWrapperHandlerChemicalStack(IngredientComponent<S, Integer> ingredientComponent,
                                                                 Capability<H> handlerCapability) {
        this.ingredientComponent = Objects.requireNonNull(ingredientComponent);
        this.handlerCapability = handlerCapability;
    }

    public static Action simulateToChemicalAction(boolean simulate) {
        return simulate ? Action.SIMULATE : Action.EXECUTE;
    }

    public static boolean chemicalActionToSimulate(Action chemicalAction) {
        return chemicalAction.simulate();
    }

    @Override
    public IIngredientComponentStorage<S, Integer> wrapComponentStorage(H storage) {
        return new ComponentStorageWrapper<>(getComponent(), storage);
    }

    @Override
    public LazyOptional<H> getStorage(ICapabilityProvider capabilityProvider, @Nullable Direction facing) {
        return capabilityProvider.getCapability(this.handlerCapability, facing).cast();
    }

    @Override
    public IngredientComponent<S, Integer> getComponent() {
        return this.ingredientComponent;
    }

    public static class ComponentStorageWrapper<C extends Chemical<C>, S extends ChemicalStack<C>, H extends IChemicalHandler<C, S>> implements IIngredientComponentStorage<S, Integer> {

        private final IngredientComponent<S, Integer> ingredientComponent;
        private final H storage;

        public ComponentStorageWrapper(IngredientComponent<S, Integer> ingredientComponent, H storage) {
            this.ingredientComponent = ingredientComponent;
            this.storage = storage;
        }

        @Override
        public IngredientComponent<S, Integer> getComponent() {
            return this.ingredientComponent;
        }

        @Override
        public Iterator<S> iterator() {
            return new ChemicalHandlerChemicalStackIterator<>(storage);
        }

        @Override
        public Iterator<S> iterator(@Nonnull S prototype, Integer matchFlags) {
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
        public S insert(@Nonnull S ingredient, boolean simulate) {
            // Don't continue if stack is empty
            if (ingredient.isEmpty()) {
                return getComponent().getMatcher().getEmptyInstance();
            }

            return storage.insertChemical(ingredient, simulateToChemicalAction(simulate));
        }

        @Override
        public S extract(@Nonnull S prototype, Integer matchFlags, boolean simulate) {
            // Don't continue if stack is empty
            if (prototype.isEmpty()) {
                return getComponent().getMatcher().getEmptyInstance();
            }

            // Optimize if ANY condition
            if (matchFlags == ChemicalMatch.ANY) {
                // Drain as much as possible
                return storage.extractChemical(prototype.getAmount(), simulateToChemicalAction(simulate));
            }

            // Optimize if AMOUNT condition
            if (matchFlags == ChemicalMatch.AMOUNT) {
                // Drain the exact given amount
                S drainedSimulated = storage.extractChemical(prototype.getAmount(), Action.SIMULATE);
                if (drainedSimulated.isEmpty() || drainedSimulated.getAmount() != prototype.getAmount()) {
                    return getComponent().getMatcher().getEmptyInstance();
                }
                return simulate ? drainedSimulated : storage.extractChemical(prototype.getAmount(), Action.EXECUTE);
            }

            // In all other cases, we have to iterate over the tank contents,
            // and drain based on their contents.
            for (int i = 0; i < storage.getTanks(); i++) {
                S contents = storage.getChemicalInTank(i);
                if (!contents.isEmpty()
                        && ChemicalMatch.areStacksEqual(contents, prototype, matchFlags & ~ChemicalMatch.AMOUNT)) {
                    S toDrain = (S) contents.copy();
                    toDrain.setAmount(prototype.getAmount());
                    S drained = storage.extractChemical(toDrain, simulateToChemicalAction(simulate));
                    if (ChemicalMatch.areStacksEqual(drained, prototype, matchFlags)) {
                        return drained;
                    }
                }
            }

            return getComponent().getMatcher().getEmptyInstance();
        }

        @Override
        public S extract(long maxQuantity, boolean simulate) {
            return storage.extractChemical(maxQuantity, simulateToChemicalAction(simulate));
        }
    }

    public static class ChemicalStorageWrapper<C extends Chemical<C>, S extends ChemicalStack<C>> implements IChemicalHandler<C, S> {

        private final IIngredientComponentStorage<S, Integer> storage;

        public ChemicalStorageWrapper(IIngredientComponentStorage<S, Integer> storage) {
            this.storage = storage;
        }

        @Override
        public int getTanks() {
            // +1 so that at least one slot appears empty, for when others want to insert
            return Iterators.size(storage.iterator()) + 1;
        }

        @Nonnull
        @Override
        public S getChemicalInTank(int tank) {
            return Iterables.get(this.storage, tank, getEmptyStack());
        }

        @Override
        public long getTankCapacity(int tank) {
            return Long.MAX_VALUE;
        }

        @Override
        public void setChemicalInTank(int i, S stack) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isValid(int tank, @Nonnull S stack) {
            return false;
        }

        @Override
        public S insertChemical(int i, S stack, Action action) {
            return stack;
        }

        @Override
        public S extractChemical(int i, long l, Action action) {
            return getEmptyStack();
        }

        @Override
        public S insertChemical(S resource, Action action) {
            return storage.insert(resource, chemicalActionToSimulate(action));
        }

        @Override
        public S extractChemical(S resource, Action action) {
            return storage.extract(resource, ChemicalMatch.EXACT, chemicalActionToSimulate(action));
        }

        @Override
        public S extractChemical(long maxDrain, Action action) {
            return storage.extract(maxDrain, chemicalActionToSimulate(action));
        }

        @Nonnull
        @Override
        public S getEmptyStack() {
            return storage.getComponent().getMatcher().getEmptyInstance();
        }
    }

    public static class ChemicalStorageWrapperSlotted<C extends Chemical<C>, S extends ChemicalStack<C>> extends ChemicalStorageWrapper<C, S> {

        private final IIngredientComponentStorageSlotted<S, Integer> storage;

        public ChemicalStorageWrapperSlotted(IIngredientComponentStorageSlotted<S, Integer> storage) {
            super(storage);
            this.storage = storage;
        }

        @Override
        public int getTanks() {
            return this.storage.getSlots();
        }

        @Nonnull
        @Override
        public S getChemicalInTank(int tank) {
            int tanks = getTanks();
            if (tank < 0 || tank >= tanks) {
                throw new IndexOutOfBoundsException("Tank " + tank + " not in valid range - [0," + tanks + ")");
            }
            return this.storage.getSlotContents(tank);
        }

        @Override
        public void setChemicalInTank(int i, S stack) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isValid(int tank, @Nonnull S stack) {
            return true;
        }

        @Override
        public S insertChemical(int i, S stack, Action action) {
            return this.storage.insert(i, stack, chemicalActionToSimulate(action));
        }

        @Override
        public S extractChemical(int i, long l, Action action) {
            return this.storage.extract(i, l, chemicalActionToSimulate(action));
        }
    }

}
