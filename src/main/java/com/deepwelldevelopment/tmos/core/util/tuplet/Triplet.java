package com.deepwelldevelopment.tmos.core.util.tuplet;

import com.deepwelldevelopment.tmos.core.util.tuplet.valueintf.IValue0;
import com.deepwelldevelopment.tmos.core.util.tuplet.valueintf.IValue1;
import com.deepwelldevelopment.tmos.core.util.tuplet.valueintf.IValue2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class Triplet<A, B, C> extends Tuple implements IValue0<A>, IValue1<B>, IValue2<C>
{
    private static final long serialVersionUID = -1877265551599483740L;
    private static final int SIZE = 3;

    private final A val0;
    private final B val1;
    private final C val2;

    public static <A, B, C> Triplet<A, B, C> with(final A value0, final B value1, final C value2) {
        return new Triplet<>(value0, value1, value2);
    }

    /**
     * <p>
     * Create tuple from array. Array has to have exactly three elements.
     * </p>
     *
     * @param <X>   the array component type
     * @param array the array to be converted to a tuple
     * @return the tuple
     */
    public static <X> Triplet<X, X, X> fromArray(final X[] array) {
        if( array == null ) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        if( array.length != 3 ) {
            throw new IllegalArgumentException("Array must have exactly 3 elements in order to create a Triplet. Size is " + array.length);
        }
        return new Triplet<>(array[0], array[1], array[2]);
    }

    /**
     * <p>
     * Create tuple from collection. Collection has to have exactly three elements.
     * </p>
     *
     * @param <X>        the collection component type
     * @param collection the collection to be converted to a tuple
     * @return the tuple
     */
    public static <X> Triplet<X, X, X> fromCollection(final Collection<X> collection) {
        return fromIterable(collection);
    }

    /**
     * <p>
     * Create tuple from iterable. Iterable has to have exactly three elements.
     * </p>
     *
     * @param <X>      the iterable component type
     * @param iterable the iterable to be converted to a tuple
     * @return the tuple
     */
    public static <X> Triplet<X, X, X> fromIterable(final Iterable<X> iterable) {
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
    public static <X> Triplet<X, X, X> fromIterable(final Iterable<X> iterable, int index) {
        return fromIterable(iterable, index, false);
    }

    private static <X> Triplet<X, X, X> fromIterable(final Iterable<X> iterable, int index, final boolean checkSize) {
        if( iterable == null ) {
            throw new IllegalArgumentException("Iterable cannot be null");
        }

        X element;
        ArrayList<X> elements = new ArrayList<>(3);
        final Iterator<X> iter = iterable.iterator();
        int lastIndex = index + SIZE - 1;

        for( int i = 0; i <= lastIndex; i++ ) {
            if( iter.hasNext() ) {
                element = iter.next();
                if( i >= index ) {
                    if( checkSize && i == lastIndex && iter.hasNext() ) {
                        throw new IllegalArgumentException("Iterable must have exactly 3 elements in order to create a Triplet.");
                    }

                    elements.add(element);
                }
            } else {
                if( i < index ) {
                    throw new IllegalArgumentException(String.format("Iterable has not enough elements to grab a value from index %d", index));
                } else {
                    throw new IllegalArgumentException(String.format("Not enough elements for creating a Triplet (3 needed, %d given)", i));
                }
            }
        }

        return new Triplet<>(elements.get(0), elements.get(1), elements.get(2));
    }

    public Triplet(final A value0, final B value1, final C value2) {
        super(value0, value1, value2);
        this.val0 = value0;
        this.val1 = value1;
        this.val2 = value2;
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
    public C getValue2() {
        return this.val2;
    }

    @Override
    public int getSize() {
        return SIZE;
    }
}