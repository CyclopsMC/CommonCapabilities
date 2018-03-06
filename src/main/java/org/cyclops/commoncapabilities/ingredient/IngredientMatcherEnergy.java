package org.cyclops.commoncapabilities.ingredient;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;

/**
 * Exact matcher for a void match condition.
 * @author rubensworks
 */
public class IngredientMatcherEnergy implements IIngredientMatcher<Integer, Void> {
    @Override
    public boolean isInstance(Object object) {
        return object instanceof Integer;
    }

    @Override
    public Void getExactMatchCondition() {
        return null;
    }

    @Override
    public boolean matches(Integer a, Integer b, Void matchCondition) {
        return a.intValue() == b.intValue();
    }

    @Override
    public Integer getEmptyInstance() {
        return 0;
    }

    @Override
    public boolean isEmpty(Integer instance) {
        return instance == 0;
    }

    @Override
    public int hash(Integer instance) {
        return instance;
    }

    @Override
    public Integer copy(Integer instance) {
        return instance;
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        return o1 - o2;
    }
}
