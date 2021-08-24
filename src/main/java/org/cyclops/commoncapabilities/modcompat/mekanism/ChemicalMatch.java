package org.cyclops.commoncapabilities.modcompat.mekanism;

import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;

/**
 * Gas matching flags.
 * @author rubensworks
 */
public final class ChemicalMatch {

    /**
     * Convenience value matching any chemical stack.
     */
    public static final int ANY = 0;
    /**
     * Match chemical stack types.
     */
    public static final int TYPE = 1;
    /**
     * Match chemical stacks amounts.
     */
    public static final int AMOUNT = 2;
    /**
     * Convenience value matching chemical stacks exactly by type and amount.
     */
    public static final int EXACT = TYPE | AMOUNT;

    public static <C extends Chemical<C>> boolean areStacksEqual(ChemicalStack<C> a, ChemicalStack<C> b, int matchFlags) {
        if (matchFlags == ANY) {
            return true;
        }
        boolean fluid  = (matchFlags & TYPE) > 0;
        boolean amount = (matchFlags & AMOUNT) > 0;
        return a == b || a.isEmpty() && b.isEmpty() ||
                (!a.isEmpty() && !b.isEmpty()
                        && (!fluid || a.getType() == b.getType())
                        && (!amount || a.getAmount() == b.getAmount()));
    }

}
