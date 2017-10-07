package org.cyclops.commoncapabilities.core;

import java.util.AbstractList;
import java.util.List;

/**
 * A simple composite list implementation.
 * @author rubensworks
 */
public class CompositeList<E> extends AbstractList<E> {

    private final List<List<E>> lists;

    public CompositeList(List<List<E>> lists) {
        this.lists = lists;
    }

    @Override
    public E get(int index) {
        for (List<E> list : lists) {
            if (index > list.size()) {
                index -= list.size();
            } else {
                return list.get(index);
            }
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int size() {
        return lists.stream().mapToInt(List::size).sum();
    }
}
