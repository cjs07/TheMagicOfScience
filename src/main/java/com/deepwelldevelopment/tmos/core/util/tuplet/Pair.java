package com.deepwelldevelopment.tmos.core.util.tuplet;

import com.deepwelldevelopment.tmos.core.util.tuplet.valueintf.IValue0;
import com.deepwelldevelopment.tmos.core.util.tuplet.valueintf.IValue1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public final class Pair<A, B> extends Tuple implements IValue0<A>, IValue1<B> {
    private static final long serialVersionUID = 2438099850625502138L;
    private static final int SIZE = 2;

    private final A val0;
    private final B val1;

    public static <A, B> Pair<A, B> with(final A value0, final B value1) {
        return new Pair<>(value0, value1);
    }

    //region fromXYZ

    /**
     * <p>
     * Create tuple from array. Array has to have exactly two elements.
     * </p>
     *
     * @param <X>   the array component type
     * @param array the array to be converted to a tuple
     * @return the tuple
     */
    public static <X> Pair<X, X> fromArray(final X[] array) {
        if( array == null ) {
            throw new IllegalArgumentException("Array cannot be null");
        }

        if( array.length != 2 ) {
            throw new IllegalArgumentException(String.format("Array must have exactly 2 elements in order to create a Pair. Size is %d", array.length));
        }

        return new Pair<>(array[0], array[1]);
    }

    /**
     * <p>
     * Create tuple from collection. Collection has to have exactly two elements.
     * </p>
     *
     * @param <X>        the collection component type
     * @param collection the collection to be converted to a tuple
     * @return the tuple
     */
    public static <X> Pair<X, X> fromCollection(final Collection<X> collection) {
        return fromIterable(collection);
    }

    /**
     * <p>
     * Create tuple from iterable. Iterable has to have exactly two elements.
     * </p>
     *
     * @param <X>      the iterable component type
     * @param iterable the iterable to be converted to a tuple
     * @return the tuple
     */
    public static <X> Pair<X, X> fromIterable(final Iterable<X> iterable) {
        return fromIterable(iterable, 0, true);
    }

    /**
     * <p>
     * Create tuple from iterable, starting from the specified index. Iterable
     * can have more (or less) elements than the tuple to be created.
     * </p>
     *
     * @param <X>      the iterable component type
     * @param iterable the iterable to be converted to a tuple
     * @return the tuple
     */
    public static <X> Pair<X, X> fromIterable(final Iterable<X> iterable, int index) {
        return fromIterable(iterable, index, false);
    }

    private static <X> Pair<X, X> fromIterable(final Iterable<X> iterable, int index, final boolean checkSize) {
        if( iterable == null ) {
            throw new IllegalArgumentException("Iterable cannot be null");
        }

        X element;
        ArrayList<X> elements = new ArrayList<>(2);
        final Iterator<X> iter = iterable.iterator();
        int lastIndex = index + SIZE - 1;

        for( int i = 0; i <= lastIndex; i++ ) {
            if( iter.hasNext() ) {
                element = iter.next();
                if( i >= index ) {
                    if( checkSize && i == lastIndex && iter.hasNext() ) {
                        throw new IllegalArgumentException("Iterable must have exactly 2 elements in order to create a Pair.");
                    }

                    elements.add(element);
                }
            } else {
                if( i < index ) {
                    throw new IllegalArgumentException(String.format("Iterable has not enough elements to grab a value from index %d", index));
                } else {
                    throw new IllegalArgumentException(String.format("Not enough elements for creating a Pair (2 needed, %d given)", i));
                }
            }
        }

        return new Pair<>(elements.get(0), elements.get(1));
    }

    public Pair(final A value0, final B value1) {
        super(value0, value1);
        this.val0 = value0;
        this.val1 = value1;
    }

    @Override
    public A getValue0() {
        return this.val0;
    }

    @Override
    public B getValue1() {
        return this.val1;
    }

    @Override
    public int getSize() {
        return SIZE;
    }
}