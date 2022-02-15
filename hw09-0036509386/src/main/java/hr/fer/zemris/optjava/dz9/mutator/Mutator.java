package hr.fer.zemris.optjava.dz9.mutator;

import java.util.Random;

/**
 * A {@link Mutator} that adds a normally distributed
 * random number between {@link #lowerBound} and {@link #upperBound}
 * to every value in the given {@code child}.
 *
 * @author Mateo Imbrišak
 */

public class Mutator {

    /**
     * Lower bound used to modify the solutions.
     */
    private final double lowerBound;

    /**
     * Lower bound used to modify the solutions.
     */
    private final double upperBound;

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns all values.
     *
     * @param lowerBound used to modify the solutions.
     * @param upperBound used to modify the solutions.
     * @param rand used to generate random values.
     */
    public Mutator(double lowerBound, double upperBound, Random rand) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.rand = rand;
    }

    /**
     * Mutates the given child by altering the given {@code array}.
     *
     * @param child to be mutated.
     */
    public void mutate(double[] child) {
        double delta = upperBound - lowerBound;

        for (int i = 0, size = child.length; i < size; i++) {
            child[i] += lowerBound + rand.nextDouble() * delta;
        }
    }
}
