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
public class TagComparator implements Comparator<Tag> {

    /**
     * A comparator for NBT tags. (This is set in GeneralConfig)
     */
    public static Comparator<Tag> INSTANCE = new TagComparator(null);

    private final INbtPathNavigation ignoreNbtNavigation;

    public TagComparator(@Nullable INbtPathNavigation ignoreNbtNavigation) {
        this.ignoreNbtNavigation = ignoreNbtNavigation;
    }

    @Override
    public int compare(Tag o1, Tag o2) {
        return this.compare(o1, o2, this.ignoreNbtNavigation);
    }

    protected int compare(Tag o1, Tag o2, @Nullable INbtPathNavigation ignoreNbtNavigation) {
        if (o1.getId() == o2.getId()) {
            switch (o1.getId()) {
                case 0:
                    return 0;
                case 1:
                    return Byte.compare(((ByteTag) o1).getAsByte(), ((ByteTag) o2).getAsByte());
                case 2:
                    return Short.compare(((ShortTag) o1).getAsShort(), ((ShortTag) o2).getAsShort());
                case 3:
                    return Integer.compare(((IntTag) o1).getAsInt(), ((IntTag) o2).getAsInt());
                case 4:
                    return Long.compare(((LongTag) o1).getAsLong(), ((LongTag) o2).getAsLong());
                case 5:
                    return Float.compare(((FloatTag) o1).getAsFloat(), ((FloatTag) o2).getAsFloat());
                case 6:
                    return Double.compare(((DoubleTag) o1).getAsDouble(), ((DoubleTag) o2).getAsDouble());
                case 7:
                    return UnsignedBytes.lexicographicalComparator().compare(((ByteArrayTag) o1).getAsByteArray(),
                            ((ByteArrayTag) o2).getAsByteArray());
                case 8:
                    return ((StringTag) o1).getAsString().compareTo(((StringTag) o2).getAsString());
                case 9:
                    ListTag l1 = (ListTag) o1;
                    ListTag l2 = (ListTag) o2;
                    if (l1.getElementType() != l2.getElementType()) {
                        return l1.getElementType() - l2.getElementType();
                    }
                    if (l1.size() != l2.size()) {
                        return l1.size() - l2.size();
                    }
                    Iterator<Tag> it1 = l1.iterator();
                    Iterator<Tag> it2 = l2.iterator();
                    while (it1.hasNext()) {
                        int comp = this.compare(it1.next(), it2.next(), null);
                        if (comp != 0) {
                            return comp;
                        }
                    }
                    return 0;
                case 10:
                    CompoundTag t1 = (CompoundTag) o1;
                    CompoundTag t2 = (CompoundTag) o2;
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
                    return Ints.lexicographicalComparator().compare(((IntArrayTag) o1).getAsIntArray(),
                            ((IntArrayTag) o2).getAsIntArray());
                case 12:
                    return Longs.lexicographicalComparator().compare(((LongArrayTag) o1).data,
                            ((LongArrayTag) o2).data);
                default:
                    return 0;
            }
        }
        return o1.getId() - o2.getId();
    }

}
