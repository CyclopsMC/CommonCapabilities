package org.cyclops.commoncapabilities.modcompat.mekanism;

import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.gas.Gas;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;

/**
 * Matcher for ChemicalStacks.
 * @author rubensworks
 */
public abstract class IngredientMatcherChemicalStack<S extends ChemicalStack<C>, C extends Chemical<C>> implements IIngredientMatcher<S, Integer> {

    @Override
    public boolean isInstance(Object object) {
        return object instanceof ChemicalStack;
    }

    @Override
    public Integer getAnyMatchCondition() {
        return ChemicalMatch.ANY;
    }

    @Override
    public Integer getExactMatchCondition() {
        return ChemicalMatch.EXACT;
    }

    @Override
    public Integer getExactMatchNoQuantityCondition() {
        return ChemicalMatch.TYPE;
    }

    @Override
    public Integer withCondition(Integer matchCondition, Integer with) {
        return matchCondition | with;
    }

    @Override
    public Integer withoutCondition(Integer matchCondition, Integer without) {
        return matchCondition & ~without;
    }

    @Override
    public boolean hasCondition(Integer matchCondition, Integer searchCondition) {
        return (matchCondition & searchCondition) > 0;
    }

    @Override
    public int hash(S instance) {
        if (instance.isEmpty()) {
            return 0;
        }

        int code = 1;
        code = 31 * code + instance.getType().hashCode();
        code = 31 * code + Long.hashCode(instance.getAmount());
        return code;
    }

    @Override
    public S copy(S instance) {
        if (instance.isEmpty()) {
            return getEmptyInstance();
        }
        return (S) instance.copy();
    }

    @Override
    public long getQuantity(S instance) {
        return instance.getAmount();
    }

    @Override
    public boolean matches(S a, S b, Integer matchCondition) {
        return ChemicalMatch.areStacksEqual(a, b, matchCondition);
    }

    protected abstract IForgeRegistry<C> getRegistry();
    protected abstract S createChemicalStack(C type, long quantity);

    @Override
    public S withQuantity(S instance, long quantity) {
        if (quantity == 0) {
            return getEmptyInstance();
        }
        if (instance.isEmpty()) {
            C someType = getRegistry().getValues().iterator().next();
            return createChemicalStack(someType, quantity);
        }
        if (instance.getAmount() == quantity) {
            return instance;
        }
        S copy = (S) instance.copy();
        copy.setAmount(quantity);
        return copy;
    }

    @Override
    public long getMaximumQuantity() {
        return Long.MAX_VALUE;
    }

    @Override
    public int conditionCompare(Integer a, Integer b) {
        return Integer.compare(a, b);
    }

    @Override
    public String localize(S instance) {
        return instance.getTextComponent().getString();
    }

    @Override
    public IFormattableTextComponent getDisplayName(S instance) {
        return (IFormattableTextComponent) instance.getTextComponent();
    }

    @Override
    public int compare(S o1, S o2) {
        if (o1.isEmpty()) {
            if (o2.isEmpty()) {
                return 0;
            } else {
                return -1;
            }
        } else if (o2.isEmpty()) {
            return 1;
        } else if (o1.getType() == o2.getType()) {
            return (int) (o1.getAmount() - o2.getAmount());
        }
        return o1.getType().getRegistryName().compareTo(o2.getType().getRegistryName());
    }

}
