package hr.fer.zemris.optjava.dz3.neighbourhoods;

import hr.fer.zemris.optjava.dz3.solutions.DoubleArraySolution;

import java.util.Random;

/**
 * An implementation of {@link INeighbourhood}
 * that generates neighbours for {@link DoubleArraySolution}s.
 *
 * @author Mateo Imbrišak
 */

public class DoubleArrayUnifNeighbourhood implements INeighbourhood<DoubleArraySolution> {

    /**
     * Probability that a value will be mutated.
     */
    private static final double MUTATION_PROBABILITY = 0.4;

    /**
     * Keeps minimum and maximum value to be generated.
     */
    private double[] deltas;

    /**
     * Used to randomize the next neighbour.
     */
    Random rand;

    /**
     * Default constructor that assigns {@link #deltas} and
     * initializes {@link #rand}.
     *
     * @param deltas to be assigned.
     */
    public DoubleArrayUnifNeighbourhood(double[] deltas) {
        if (deltas.length != 2) {
            throw new IllegalArgumentException("Size of the array must be exactly two.");
        }

        this.deltas = deltas;
        rand = new Random();
    }

    @Override
    public DoubleArraySolution randomNeighbour(DoubleArraySolution solution) {
        DoubleArraySolution ret = solution.duplicate();
        boolean changed = false;

        for (int i = 0, size = solution.values.length; i < size; i++) {
            if (rand.nextDouble() <= MUTATION_PROBABILITY) {
                mutateValue(ret, i);

                changed = true;
            }
        }

        if (!changed) {
            int position = rand.nextInt(ret.values.length - 1);
            mutateValue(ret, position);
        }

        return ret;
    }

    /**
     * Mutates value in the given {@link DoubleArraySolution}
     * at the given {@code position}.
     *
     * @param solution to be mutated.
     * @param position which will be mutated.
     */
    private void mutateValue(DoubleArraySolution solution, int position) {
        double delta = deltas[0] + rand.nextDouble() * (deltas[1] - deltas[0]);
        solution.values[position] += delta;
    }
}
