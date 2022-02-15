package hr.fer.zemris.optjava.dz4.crossover;

import hr.fer.zemris.optjava.dz4.solutions.DoubleArraySolution;

import java.util.Random;

/**
 * An implementation of {@link Crossover} that
 * uses BLX-alpha algorithm to cross and modify solutions.
 *
 * @author Mateo Imbrišak
 */

public class BLXAlphaCrossover implements Crossover<DoubleArraySolution> {

    /**
     * Used to modify selection.
     */
    private final double alpha;

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns {@link #alpha} value
     * and initializes {@link #rand} random number generator.
     *
     * @param alpha used to modify parts of the generated child.
     */
    public BLXAlphaCrossover(double alpha) {
        this.alpha = alpha;
        this.rand = new Random();
    }

    @Override
    public DoubleArraySolution cross(DoubleArraySolution firstParent, DoubleArraySolution secondParent) {
        int size = firstParent.getLength();
        double[] values = new double[size];

        for (int i = 0; i < size; i++) {
            double min = Math.min(firstParent.getValue(i), secondParent.getValue(i));
            double max = Math.max(firstParent.getValue(i), secondParent.getValue(i));

            double I = max - min;

            values[i] = (min - I * alpha) + rand.nextDouble() * ((max + I * alpha) - (min - I * alpha));
        }

        return new DoubleArraySolution(values);
    }
}
