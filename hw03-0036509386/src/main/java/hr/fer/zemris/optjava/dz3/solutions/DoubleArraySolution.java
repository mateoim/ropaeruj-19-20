package hr.fer.zemris.optjava.dz3.solutions;

import java.util.Arrays;
import java.util.Random;

/**
 * A {@link SingleObjectiveSolution} represented as
 * an {@code array} of {@code double}s.
 *
 * @author Mateo Imbrišak
 */

public class DoubleArraySolution extends SingleObjectiveSolution {

    /**
     * Keeps the values of this solution.
     */
    public double[] values;

    /**
     * Default constructor that initializes the
     * {@link #values} array with the given {@code size}.
     *
     * @param size used to initialize {@link #values}.
     */
    public DoubleArraySolution(int size) {
        values = new double[size];
    }

    /**
     * Constructor used internally to copy {@link #values}.
     *
     * @param values used in the new solution.
     */
    private DoubleArraySolution(double[] values) {
        this.values = values;
    }

    /**
     * Creates a copy of this solution.
     *
     * @return copy of this solution.
     */
    public DoubleArraySolution newLikeThis() {
        return new DoubleArraySolution(Arrays.copyOf(values, values.length));
    }

    /**
     * Creates a copy of this solution.
     *
     * @return copy of this solution.
     */
    public DoubleArraySolution duplicate() {
        return newLikeThis();
    }

    /**
     * Randomizes this solution.
     *
     * @param rand used to generate random values.
     * @param low bound used for values.
     * @param high bound used for values.
     */
    public void randomize(Random rand, double low, double high) {
        for (int i = 0, size = values.length; i < size; i++) {
            values[i] = low + rand.nextDouble() * (high - low);
        }
    }
}
