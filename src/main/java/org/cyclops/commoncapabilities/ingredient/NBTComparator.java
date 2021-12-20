package org.cyclops.commoncapabilities.ingredient;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.UnsignedBytes;
import net.minecraft.nbt.*;
import org.cyclops.cyclopscore.nbt.path.navigate.INbtPathNavigation;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A comparator implementation for NBT tags.
 * @author rubensworks
 */
public class NBTComparator implements Comparator<INBT> {

    /**
     * A comparator for NBT tags. (This is set in GeneralConfig)
     */
    public static Comparator<INBT> INSTANCE = new NBTComparator(null);

    private final INbtPathNavigation ignoreNbtNavigation;

    public NBTComparator(@Nullable INbtPathNavigation ignoreNbtNavigation) {
        this.ignoreNbtNavigation = ignoreNbtNavigation;
    }

    @Override
    public int compare(INBT o1, INBT o2) {
        return this.compare(o1, o2, this.ignoreNbtNavigation);
    }

    protected int compare(INBT o1, INBT o2, @Nullable INbtPathNavigation ignoreNbtNavigation) {
        if (o1.getId() == o2.getId()) {
            switch (o1.getId()) {
                case 0:
                    return 0;
                case 1:
                    return Byte.compare(((ByteNBT) o1).getAsByte(), ((ByteNBT) o2).getAsByte());
                case 2:
                    return Short.compare(((ShortNBT) o1).getAsShort(), ((ShortNBT) o2).getAsShort());
                case 3:
                    return Integer.compare(((IntNBT) o1).getAsInt(), ((IntNBT) o2).getAsInt());
                case 4:
                    return Long.compare(((LongNBT) o1).getAsLong(), ((LongNBT) o2).getAsLong());
                case 5:
                    return Float.compare(((FloatNBT) o1).getAsFloat(), ((FloatNBT) o2).getAsFloat());
                case 6:
                    return Double.compare(((DoubleNBT) o1).getAsDouble(), ((DoubleNBT) o2).getAsDouble());
                case 7:
                    return UnsignedBytes.lexicographicalComparator().compare(((ByteArrayNBT) o1).getAsByteArray(),
                            ((ByteArrayNBT) o2).getAsByteArray());
                case 8:
                    return ((StringNBT) o1).getAsString().compareTo(((StringNBT) o2).getAsString());
                case 9:
                    ListNBT l1 = (ListNBT) o1;
                    ListNBT l2 = (ListNBT) o2;
                    if (l1.getElementType() != l2.getElementType()) {
                        return l1.getElementType() - l2.getElementType();
                    }
                    if (l1.size() != l2.size()) {
                        return l1.size() - l2.size();
                    }
                    Iterator<INBT> it1 = l1.iterator();
                    Iterator<INBT> it2 = l2.iterator();
                    while (it1.hasNext()) {
                        int comp = this.compare(it1.next(), it2.next(), null);
                        if (comp != 0) {
                            return comp;
                        }
                    }
                    return 0;
                case 10:
                    CompoundNBT t1 = (CompoundNBT) o1;
                    CompoundNBT t2 = (CompoundNBT) o2;
                    Set<String> k1 = t1.getAllKeys();
                    Set<String> k2 = t2.getAllKeys();
                    if (ignoreNbtNavigation != null) {
                        k1 = k1.stream().filter(k -> !ignoreNbtNavigation.isLeafKey(k)).collect(Collectors.toSet());
                        k2 = k2.stream().filter(k -> !ignoreNbtNavigation.isLeafKey(k)).collect(Collectors.toSet());
                    }
                    if (!k1.equals(k2)) {
                        String[] k1a = k1.toArray(new String[0]);
                        String[] k2a = k2.toArray(new String[0]);
                        Arrays.sort(k1a);
                        Arrays.sort(k2a);
                        int minLength = Math.min(k1a.length, k2a.length);
                        for (int i = 0; i < minLength; i++) {
                            int result = k1a[i].compareTo(k2a[i]);
                            if (result != 0) {
                                return result;
                            }
                        }
                        return k1a.length - k2a.length;
                    }
                    for (String key : k1) {
                        int comp = this.compare(t1.get(key), t2.get(key),
                                ignoreNbtNavigation != null ? ignoreNbtNavigation.getNext(key) : null);
                        if (comp != 0) {
                            return comp;
                        }
                    }
                    return 0;
                case 11:
                    return Ints.lexicographicalComparator().compare(((IntArrayNBT) o1).getAsIntArray(),
                            ((IntArrayNBT) o2).getAsIntArray());
                case 12:
                    return Longs.lexicographicalComparator().compare(((LongArrayNBT) o1).data,
                            ((LongArrayNBT) o2).data);
                default:
                    return 0;
            }
        }
        return o1.getId() - o2.getId();
    }

}
