package hr.fer.zemris.optjava.dz5.solutions;

import java.util.Arrays;
import java.util.Random;

/**
 * An implementation of {@link Solution} that
 * uses an array of {@code boolean}s to represent solutions.
 *
 * @author Mateo Imbrišak
 */

public class BitVectorSolution implements Solution {

    /**
     * Keeps the bits that represent the solution.
     */
    private boolean[] bits;

    /**
     * Current fitness of this {@link Solution}.
     */
    private double fitness;

    /**
     * Default constructor that assigns an array as {@link #bits}.
     *
     * @param bits to be used for values.
     */
    public BitVectorSolution(boolean[] bits) {
        this.bits = bits;
    }

    /**
     * Constructor used to {@link #duplicate()} this solution.
     *
     * @param bits in this solution.
     * @param fitness of this solution.
     */
    private BitVectorSolution(boolean[] bits, double fitness) {
        this.bits = Arrays.copyOf(bits, bits.length);
        this.fitness = fitness;
    }

    /**
     * Creates a random {@code BitVectorSolution}.
     *
     * @param size of the new solution.
     * @param rand used to generate random values.
     *
     * @return new randomized solution size {@code n}.
     */
    public static BitVectorSolution random(int size, Random rand) {
        boolean[] bits = new boolean[size];

        for (int i = 0; i < size; i++) {
            bits[i] = rand.nextBoolean();
        }

        return new BitVectorSolution(bits);
    }

    /**
     * Provides a copy of this solution.
     *
     * @return copy of this solution.
     */
    public BitVectorSolution duplicate() {
        return new BitVectorSolution(bits, fitness);
    }

    /**
     * Provides the size of this {@code BitVectorSolution}.
     *
     * @return size of this {@code BitVectorSolution}.
     */
    public int size() {
        return bits.length;
    }

    /**
     * Calculates the number of {@code true} bits in
     * the {@link #bits} array.
     *
     * @return number of {@code true} values in {@link #bits}.
     */
    public int countOnes() {
        int ones = 0;

        for (boolean bit : bits) {
            if (bit) {
                ones++;
            }
        }

        return ones;
    }

    /**
     * Provides the bit at the given {@code index}.
     *
     * @param index of the requested bit.
     *
     * @return bit at the given {@code index}.
     */
    public boolean getBit(int index) {
        return bits[index];
    }

    /**
     * Flips the bit at the given {@code index}.
     *
     * @param index at which the bit will be flipped.
     */
    public void flipBit(int index) {
        bits[index] = !bits[index];
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitVectorSolution that = (BitVectorSolution) o;
        return Arrays.equals(bits, that.bits);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bits);
    }
}
