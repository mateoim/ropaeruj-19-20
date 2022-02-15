package hr.fer.zemris.optjava.dz3.neighbourhoods;

import hr.fer.zemris.optjava.dz3.solutions.BitvectorSolution;

import java.util.Random;

/**
 * An implementation of {@link INeighbourhood}
 * that generates neighbours for {@link BitvectorSolution}s.
 *
 * @author Mateo Imbrišak
 */

public class BitvectorNeighbourhood implements INeighbourhood<BitvectorSolution> {

    /**
     * Probability to mutate a bit.
     */
    private static final double MUTATION_PROBABILITY = 0.1;

    /**
     * Used to randomise mutated bit.
     */
    Random rand;

    /**
     * Default constructor that initializes {@link #rand}.
     */
    public BitvectorNeighbourhood() {
        rand = new Random();
    }

    @Override
    public BitvectorSolution randomNeighbour(BitvectorSolution solution) {
        BitvectorSolution ret = solution.duplicate();
        boolean changed = false;

        for (int i = 0, size = solution.bits.length; i < size; i++) {
            if (rand.nextDouble() <= MUTATION_PROBABILITY) {
                ret.bits[i] = !ret.bits[i];

                changed = true;
            }
        }

        if (!changed) {
            int position = rand.nextInt(ret.bits.length - 1);
            ret.bits[position] = !ret.bits[position];
        }

        return ret;
    }
}
