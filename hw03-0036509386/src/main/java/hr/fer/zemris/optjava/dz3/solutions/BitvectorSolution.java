package hr.fer.zemris.optjava.dz3.solutions;

import java.util.Arrays;
import java.util.Random;

/**
 * A {@link SingleObjectiveSolution} represented as an
 * {@code array} of bits.
 *
 * @author Mateo Imbrišak
 */

public class BitvectorSolution extends SingleObjectiveSolution{

    /**
     * Keeps the bits used to represent this solution.
     */
    public boolean[] bits;

    /**
     * Default constructor that initializes the
     * {@link #bits} array with the given {@code size}.
     *
     * @param size used to initialize {@link #bits}.
     */
    public BitvectorSolution(int size) {
        bits = new boolean[size];
    }

    /**
     * Constructor used internally to copy {@link #bits}.
     *
     * @param values used in the new solution.
     */
    private BitvectorSolution(boolean[] values) {
        bits = values;
    }

    /**
     * Creates a copy of this solution.
     *
     * @return copy of this solution.
     */
    public BitvectorSolution newLikeThis() {
        return new BitvectorSolution(Arrays.copyOf(bits, bits.length));
    }

    /**
     * Creates a copy of this solution.
     *
     * @return copy of this solution.
     */
    public BitvectorSolution duplicate() {
        return newLikeThis();
    }

    /**
     * Randomizes this solution.
     *
     * @param rand used to generate random values.
     */
    public void randomize(Random rand) {
        for (int i = 0, size = bits.length; i < size; i++) {
            bits[i] = rand.nextBoolean();
        }
    }
}
