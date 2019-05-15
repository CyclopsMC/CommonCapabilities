package org.cyclops.commoncapabilities.api.ingredient;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * @author rubensworks
 */
public class IsEqualIngredient<T, M> extends BaseMatcher<T> {

    private final IngredientComponent<T, M> component;
    private final T expectedValue;

    public IsEqualIngredient(IngredientComponent<T, M> component, T equalArg) {
        this.component = component;
        expectedValue = equalArg;
    }

    @Override
    public boolean matches(Object item) {
        return component.getMatcher().matchesExactly(expectedValue, (T) item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expectedValue);
    }
}
