package org.cyclops.commoncapabilities.capability.recipehandler;

import net.minecraftforge.fluids.FluidStack;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;

/**
 * Matcher for FluidStacks.
 * @author rubensworks
 */
public class IngredientMatcherFluidStack implements IIngredientMatcher<FluidStack, Integer> {
    @Override
    public boolean matches(FluidStack a, FluidStack b, Integer matchCondition) {
        return FluidMatch.areFluidStacksEqual(a, b, matchCondition);
    }

    @Override
    public boolean matchesExactly(FluidStack a, FluidStack b) {
        return a.equals(b);
    }

    @Override
    public int hash(FluidStack instance) {
        return instance.hashCode();
    }
}
