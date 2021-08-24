package org.cyclops.commoncapabilities.modcompat.mekanism;

import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator over all slots in a chemical handler.
 * @author rubensworks
 */
public class ChemicalHandlerChemicalStackIterator<C extends Chemical<C>, S extends ChemicalStack<C>> implements Iterator<S> {

    private final IChemicalHandler<C, S> chemicalHandler;
    private int slot;

    public ChemicalHandlerChemicalStackIterator(IChemicalHandler<C, S> chemicalHandler, int offset) {
        this.chemicalHandler = chemicalHandler;
        this.slot = offset;
    }

    public ChemicalHandlerChemicalStackIterator(IChemicalHandler<C, S> chemicalHandler) {
        this(chemicalHandler, 0);
    }

    @Override
    public boolean hasNext() {
        return slot < chemicalHandler.getTanks();
    }

    @Override
    public S next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Slot out of bounds");
        }
        return chemicalHandler.getChemicalInTank(slot++);
    }
}
