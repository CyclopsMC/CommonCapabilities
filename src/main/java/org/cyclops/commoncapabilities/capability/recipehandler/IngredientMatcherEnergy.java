package org.cyclops.commoncapabilities.capability.recipehandler;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;

/**
 * Exact matcher for a void match condition.
 * @author rubensworks
 */
public class IngredientMatcherEnergy implements IIngredientMatcher<Integer, Void> {
    @Override
    public boolean matches(Integer a, Integer b, Void matchCondition) {
        return a.intValue() == b.intValue();
    }

    @Override
    public boolean matchesExactly(Integer a, Integer b) {
        return matches(a, b, null);
    }

    @Override
    public boolean isEmpty(Integer instance) {
        return instance == 0;
    }

    @Override
    public int hash(Integer instance) {
        return instance.hashCode();
    }
}
