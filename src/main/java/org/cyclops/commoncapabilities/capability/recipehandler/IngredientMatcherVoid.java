package org.cyclops.commoncapabilities.capability.recipehandler;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;

/**
 * Exact matcher for a void match condition.
 * @author rubensworks
 */
public class IngredientMatcherVoid<T> implements IIngredientMatcher<T, Void> {
    @Override
    public boolean matches(T a, T b, Void matchCondition) {
        return a.equals(b);
    }

    @Override
    public boolean matchesExactly(T a, T b) {
        return matches(a, b, null);
    }

    @Override
    public int hash(T instance) {
        return instance.hashCode();
    }
}
