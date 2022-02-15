package hr.fer.zemris.trisat;

/**
 * A class that represents a solution as a mutable bit vector.
 *
 * @author Mateo Imbrišak
 */

public class MutableBitVector extends BitVector {

    /**
     * Default constructor that assigns the bits.
     *
     * @param bits to be assigned.
     */
    public MutableBitVector(boolean ... bits) {
        super(bits);
    }

    /**
     * Constructor that creates a vector of {@code n} zero bits.
     *
     * @param n size of the vector.
     */
    public MutableBitVector(int n) {
        super(n);
    }

    /**
     * Changes the bit at {@code index} to {@code value}.
     *
     * @param index to be changed.
     * @param value to be set.
     */
    public void set(int index, boolean value) {
        bits[index] = value;
    }
}
