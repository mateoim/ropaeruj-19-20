package hr.fer.zemris.optjava.dz9.crossover;

import java.util.Random;

/**
 * An implementation of Crossover that
 * uses BLX-alpha algorithm to cross and modify solutions.
 *
 * @author Mateo Imbrišak
 */

public class BLXAlphaCrossover {

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
     * @param rand used to generate random values.
     */
    public BLXAlphaCrossover(double alpha, Random rand) {
        this.alpha = alpha;
        this.rand = rand;
    }

    /**
     * Creates a child based in the given parents.
     *
     * @param firstParent used to generate a child.
     * @param secondParent used to generate a child.
     *
     * @return a new {@code array} created from both parents.
     */
    public double[] cross(double[] firstParent, double[] secondParent) {
        int size = firstParent.length;
        double[] values = new double[size];

        for (int i = 0; i < size; i++) {
            double min = Math.min(firstParent[i], secondParent[i]);
            double max = Math.max(firstParent[i], secondParent[i]);

            double I = max - min;

            values[i] = (min - I * alpha) + rand.nextDouble() * ((max + I * alpha) - (min - I * alpha));
        }

        return values;
    }
}
