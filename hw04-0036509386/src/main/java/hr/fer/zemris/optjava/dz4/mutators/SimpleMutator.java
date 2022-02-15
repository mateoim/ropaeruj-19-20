package hr.fer.zemris.optjava.dz4.mutators;

import hr.fer.zemris.optjava.dz4.solutions.DoubleArraySolution;

import java.util.Random;

/**
 * A {@link Mutator} that adds a normally distributed
 * random number between {@code 0} and {@link #upperBound}
 * to every value in the given {@code solution}.
 *
 * @author Mateo Imbrišak
 */

public class SimpleMutator implements Mutator<DoubleArraySolution> {

    /**
     * Upper bound for random value added to mutated solutions.
     */
    private final double upperBound;

    /**
     * Used to generate random values added to mutated solutions.
     */
    private final Random rand;

    /**
     * Default constructor that assigns {@link #upperBound}
     * used for mutating solutions.
     *
     * @param upperBound for random number to be added to the
     *                   mutated solution.
     */
    public SimpleMutator(double upperBound) {
        this.upperBound = upperBound;
        this.rand = new Random();
    }

    @Override
    public DoubleArraySolution mutate(DoubleArraySolution solution) {
        int size = solution.getLength();
        double[] ret = new double[size];

        for (int i = 0; i < size; i++) {
            ret[i] = solution.getValue(i) + rand.nextGaussian() * upperBound;
        }

        return new DoubleArraySolution(ret);
    }
}
