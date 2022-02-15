package hr.fer.zemris.trisat;

import java.util.Arrays;
import java.util.Random;

/**
 * A class that represents a solution as an immutable bit vector.
 *
 * @author Mateo Imbrišak
 */

public class BitVector {

    /**
     * Keeps the bits.
     */
    boolean[] bits;

    /**
     * Constructor that creates a vector of size {@code numberOfBits}
     * filled with random bits.
     *
     * @param rand used to generate bits.
     * @param numberOfBits in the vector.
     */
    public BitVector(Random rand, int numberOfBits) {
        bits = new boolean[numberOfBits];

        for (int i = 0; i < numberOfBits; i++) {
            bits[i] = rand.nextBoolean();
        }
    }

    /**
     * Default constructor that assigns the bits.
     *
     * @param bits to be assigned.
     */
    public BitVector(boolean ... bits) {
        this.bits = bits;
    }

    /**
     * Constructor that creates a vector of {@code n} zero bits.
     *
     * @param n size of the vector.
     */
    public BitVector(int n) {
        this.bits = new boolean[n];
    }

    /**
     * Provides the bit at requested {@code index}.
     *
     * @param index of the requested bit.
     *
     * @return bit at requested position.
     */
    public boolean get(int index) {
        return bits[index];
    }

    /**
     * Provides the size of this {@code BitVector}.
     *
     * @return size of this {@code BitVector}.
     */
    public int getSize() {
        return bits.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (boolean bit : bits) {
            if (bit) {
                sb.append(1);
            } else {
                sb.append(0);
            }
        }

        return sb.toString();
    }

    /**
     * Provides a modifiable copy of this {@code BitVector}.
     *
     * @return copy of this {@code BitVector} as a {@link MutableBitVector}.
     */
    public MutableBitVector copy() {
        return new MutableBitVector(Arrays.copyOf(bits, bits.length));
    }
}
