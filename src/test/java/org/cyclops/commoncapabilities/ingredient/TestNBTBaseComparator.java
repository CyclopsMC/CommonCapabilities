package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.component.DyedItemColor;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestNBTBaseComparator {

    private static DataComparator COMP;

    private static DataComponentMap EMPTY;
    private static DataComponentMap GLINT_TRUE;
    private static DataComponentMap GLINT_TRUE_;
    private static DataComponentMap GLINT_FALSE;

    private static DataComponentMap GLINT_TRUE_MAXDAMAGE_64;
    private static DataComponentMap GLINT_TRUE_MAXDAMAGE_32;

    private static DataComponentMap GLINT_TRUE_DAMAGE_64;

    private static DataComponentMap GLINT_FALSE_DYED_C1;
    private static DataComponentMap GLINT_FALSE_DYED_C2;
    private static DataComponentMap GLINT_FALSE_DYED_D;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();

        COMP = new DataComparator(null);

        EMPTY = DataComponentMap.builder()
                .build();

        GLINT_TRUE = DataComponentMap.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .build();
        GLINT_TRUE_ = DataComponentMap.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .build();
        GLINT_FALSE = DataComponentMap.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
                .build();

        GLINT_TRUE_MAXDAMAGE_64 = DataComponentMap.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponents.MAX_DAMAGE, 64)
                .build();
        GLINT_TRUE_MAXDAMAGE_32 = DataComponentMap.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponents.MAX_DAMAGE, 32)
                .build();

        GLINT_TRUE_DAMAGE_64 = DataComponentMap.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponents.DAMAGE, 64)
                .build();

        DyedItemColor color = new DyedItemColor(1, false);
        GLINT_FALSE_DYED_C1 = DataComponentMap.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
                .set(DataComponents.DYED_COLOR, color)
                .build();
        GLINT_FALSE_DYED_C2 = DataComponentMap.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
                .set(DataComponents.DYED_COLOR, color)
                .build();
        GLINT_FALSE_DYED_D = DataComponentMap.builder()
                .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
                .set(DataComponents.DYED_COLOR, new DyedItemColor(2, false))
                .build();
    }

    @Test
    public void testIdentical() {
        assertThat(COMP.compare(GLINT_TRUE, GLINT_TRUE), is(0));
    }

    @Test
    public void testEqual() {
        assertThat(COMP.compare(GLINT_TRUE, GLINT_TRUE_), is(0));

        assertThat(COMP.compare(GLINT_FALSE_DYED_C1, GLINT_FALSE_DYED_C2), is(0));
    }

    @Test
    public void testDifferentKeys() {
        assertThat(COMP.compare(EMPTY, GLINT_TRUE), is(-1));
        assertThat(COMP.compare(GLINT_TRUE, EMPTY), is(1));

        assertThat(COMP.compare(GLINT_TRUE, GLINT_TRUE_MAXDAMAGE_64), is(-1));
        assertThat(COMP.compare(GLINT_TRUE_MAXDAMAGE_64, GLINT_TRUE), is(1));

        assertThat(COMP.compare(GLINT_TRUE_MAXDAMAGE_64, GLINT_TRUE_DAMAGE_64), is(1));
        assertThat(COMP.compare(GLINT_TRUE_DAMAGE_64, GLINT_TRUE_MAXDAMAGE_64), is(-1));
    }

    @Test
    public void testDifferentValues() {
        assertThat(COMP.compare(GLINT_TRUE, GLINT_FALSE), is(1));
        assertThat(COMP.compare(GLINT_FALSE, GLINT_TRUE), is(-1));

        assertThat(COMP.compare(GLINT_TRUE_MAXDAMAGE_64, GLINT_TRUE_MAXDAMAGE_32), is(1));
        assertThat(COMP.compare(GLINT_TRUE_MAXDAMAGE_32, GLINT_TRUE_MAXDAMAGE_64), is(-1));

        assertThat(COMP.compare(GLINT_FALSE_DYED_C1, GLINT_FALSE_DYED_D), is(-1));
        assertThat(COMP.compare(GLINT_FALSE_DYED_D, GLINT_FALSE_DYED_C1), is(1));
    }
}
