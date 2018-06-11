package org.cyclops.commoncapabilities.ingredient.storage;

import com.google.common.collect.Iterators;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageWrapperHandler;
import org.cyclops.cyclopscore.ingredient.collection.FilteredIngredientCollectionIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;

/**
 * Energy storage wrapper handler for {@link IEnergyStorage}.
 * @author rubensworks
 */
public class IngredientComponentStorageWrapperHandlerEnergyStorage implements
        IIngredientComponentStorageWrapperHandler<Integer, Boolean, IEnergyStorage> {

    private final IngredientComponent<Integer, Boolean> ingredientComponent;

    public IngredientComponentStorageWrapperHandlerEnergyStorage(IngredientComponent<Integer, Boolean> ingredientComponent) {
        this.ingredientComponent = Objects.requireNonNull(ingredientComponent);
    }

    @Override
    public IIngredientComponentStorage<Integer, Boolean> wrapComponentStorage(IEnergyStorage storage) {
        return new ComponentStorageWrapper(getComponent(), storage);
    }

    @Override
    public IEnergyStorage wrapStorage(IIngredientComponentStorage<Integer, Boolean> componentStorage) {
        return new EnergyStorageWrapper(componentStorage);
    }

    @Nullable
    @Override
    public IEnergyStorage getStorage(ICapabilityProvider capabilityProvider, @Nullable EnumFacing facing) {
        if (capabilityProvider.hasCapability(CapabilityEnergy.ENERGY, facing)) {
            return capabilityProvider.getCapability(CapabilityEnergy.ENERGY, facing);
        }
        return null;
    }

    @Override
    public IngredientComponent<Integer, Boolean> getComponent() {
        return this.ingredientComponent;
    }

    public static class ComponentStorageWrapper implements IIngredientComponentStorage<Integer, Boolean> {

        private final IngredientComponent<Integer, Boolean> ingredientComponent;
        private final IEnergyStorage storage;

        public ComponentStorageWrapper(IngredientComponent<Integer, Boolean> ingredientComponent, IEnergyStorage storage) {
            this.ingredientComponent = ingredientComponent;
            this.storage = storage;
        }

        @Override
        public IngredientComponent<Integer, Boolean> getComponent() {
            return this.ingredientComponent;
        }

        @Override
        public Iterator<Integer> iterator() {
            return Iterators.forArray(storage.getEnergyStored());
        }

        @Override
        public Iterator<Integer> iterator(@Nonnull Integer prototype, Boolean matchFlags) {
            return new FilteredIngredientCollectionIterator<>(iterator(), getComponent().getMatcher(), prototype, matchFlags);
        }

        @Override
        public long getMaxQuantity() {
            return storage.getMaxEnergyStored();
        }

        @Override
        public Integer insert(@Nonnull Integer ingredient, boolean simulate) {
            return ingredient - storage.receiveEnergy(ingredient, simulate);
        }

        @Override
        public Integer extract(@Nonnull Integer prototype, Boolean matchFlags, boolean simulate) {
            if (matchFlags) {
                int extractable = storage.extractEnergy(prototype, true);
                if (extractable != prototype) {
                    return 0;
                }
            } else {
                prototype = Integer.MAX_VALUE;
            }
            return storage.extractEnergy(prototype, simulate);
        }

        @Override
        public Integer extract(long maxQuantity, boolean simulate) {
            return storage.extractEnergy((int) maxQuantity, simulate);
        }
    }

    public static class EnergyStorageWrapper implements IEnergyStorage {

        private final IIngredientComponentStorage<Integer, Boolean> storage;

        public EnergyStorageWrapper(IIngredientComponentStorage<Integer, Boolean> storage) {
            this.storage = storage;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return maxReceive - storage.insert(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return storage.extract(maxExtract, simulate);
        }

        @Override
        public int getEnergyStored() {
            int total = 0;
            for (Integer stored : storage) {
                total = Math.addExact(total, stored);
            }
            return total;
        }

        @Override
        public int getMaxEnergyStored() {
            return (int) storage.getMaxQuantity();
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return true;
        }
    }
}
