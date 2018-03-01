package org.cyclops.commoncapabilities.ingredient;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.UnsignedBytes;
import net.minecraft.nbt.*;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

/**
 * A comparator implementation for NBT tags.
 * @author rubensworks
 */
public class NBTBaseComparator implements Comparator<NBTBase> {

    private static final Field FIELD_NBT_LONG_ARRAY_DATA = ReflectionHelper.findField(NBTTagLongArray.class, "field_193587_b", "data");

    @Override
    public int compare(NBTBase o1, NBTBase o2) {
        if (o1.getId() == o2.getId()) {
            switch (o1.getId()) {
                case 0:
                    return 0;
                case 1:
                    return Byte.compare(((NBTTagByte) o1).getByte(), ((NBTTagByte) o2).getByte());
                case 2:
                    return Short.compare(((NBTTagShort) o1).getShort(), ((NBTTagShort) o2).getShort());
                case 3:
                    return Integer.compare(((NBTTagInt) o1).getInt(), ((NBTTagInt) o2).getInt());
                case 4:
                    return Long.compare(((NBTTagLong) o1).getLong(), ((NBTTagLong) o2).getLong());
                case 5:
                    return Float.compare(((NBTTagFloat) o1).getFloat(), ((NBTTagFloat) o2).getFloat());
                case 6:
                    return Double.compare(((NBTTagDouble) o1).getDouble(), ((NBTTagDouble) o2).getDouble());
                case 7:
                    return UnsignedBytes.lexicographicalComparator().compare(((NBTTagByteArray) o1).getByteArray(),
                            ((NBTTagByteArray) o2).getByteArray());
                case 8:
                    return ((NBTTagString) o1).getString().compareTo(((NBTTagString) o2).getString());
                case 9:
                    NBTTagList l1 = (NBTTagList) o1;
                    NBTTagList l2 = (NBTTagList) o2;
                    if (l1.getTagType() != l2.getTagType()) {
                        return l1.getTagType() - l2.getTagType();
                    }
                    if (l1.tagCount() != l2.tagCount()) {
                        return l1.tagCount() - l2.tagCount();
                    }
                    Iterator<NBTBase> it1 = l1.iterator();
                    Iterator<NBTBase> it2 = l2.iterator();
                    while (it1.hasNext()) {
                        int comp = this.compare(it1.next(), it2.next());
                        if (comp != 0) {
                            return comp;
                        }
                    }
                    return 0;
                case 10:
                    NBTTagCompound t1 = (NBTTagCompound) o1;
                    NBTTagCompound t2 = (NBTTagCompound) o2;
                    Set<String> k1 = t1.getKeySet();
                    Set<String> k2 = t2.getKeySet();
                    int k1s = k1.size();
                    int k2s = k2.size();
                    if (k1s != k2s) {
                        return k1s - k2s;
                    }
                    for (String key : k1) {
                        int comp = this.compare(t1.getTag(key), t2.getTag(key));
                        if (comp != 0) {
                            return comp;
                        }
                    }
                    return 0;
                case 11:
                    return Ints.lexicographicalComparator().compare(((NBTTagIntArray) o1).getIntArray(),
                            ((NBTTagIntArray) o2).getIntArray());
                case 12:
                    return Longs.lexicographicalComparator().compare(getLongArray(((NBTTagLongArray) o1)),
                            getLongArray(((NBTTagLongArray) o2)));
                default:
                    return 0;
            }
        }
        return o1.getId() - o2.getId();
    }

    private static long[] getLongArray(NBTTagLongArray nbt) {
        try {
            return (long[]) FIELD_NBT_LONG_ARRAY_DATA.get(nbt);
        } catch (IllegalAccessException e) {
            return new long[0];
        }
    }

}
