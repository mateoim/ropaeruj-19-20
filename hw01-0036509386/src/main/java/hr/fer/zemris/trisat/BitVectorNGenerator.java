package hr.fer.zemris.trisat;

import java.util.Iterator;

/**
 * A class that generates neighbourhood of the
 * {@link BitVector} given to it during initialization.
 *
 * @author Mateo Imbrišak
 */

public class BitVectorNGenerator implements Iterable<MutableBitVector> {

    /**
     * Keeps the given {@link BitVector}.
     */
    private BitVector assigment;

    /**
     * Default constructor that takes a {@link BitVector}
     * whose neighbours will be generated.
     *
     * @param assignment used to generate neighbours.
     */
    public BitVectorNGenerator(BitVector assignment) {
        this.assigment = assignment;

    }

    @Override
    public Iterator<MutableBitVector> iterator() {
        return new Iterator<>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < assigment.getSize();
            }

            @Override
            public MutableBitVector next() {
                MutableBitVector next = assigment.copy();
                next.set(index, !assigment.get(index));
                index++;

                return next;
            }
        };
    }

    /**
     * Provides the complete neighbourhood as an array.
     *
     * @return an array of neighbours.
     */
    public MutableBitVector[] createNeighborhood() {
        MutableBitVector[] ret = new MutableBitVector[assigment.getSize()];
        Iterator<MutableBitVector> iter = iterator();

        for (int i = 0, size = assigment.getSize(); i < size; i++) {
            ret[i] = iter.next();
        }

        return ret;
    }
}
