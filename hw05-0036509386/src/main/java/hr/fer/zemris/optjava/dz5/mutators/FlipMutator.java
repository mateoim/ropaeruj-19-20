package hr.fer.zemris.optjava.dz5.mutators;

import hr.fer.zemris.optjava.dz5.solutions.BitVectorSolution;

import java.util.Random;

/**
 * An implementation of {@link Mutator} that
 * mutates {@link BitVectorSolution}s by flipping bits
 * with probability of {@link #PROBABILITY_TO_FLIP}.
 *
 * @author Mateo Imbrišak
 */

public class FlipMutator implements Mutator<BitVectorSolution> {

    /**
     * Probability to flip a single bit while mutating.
     */
    private static final double PROBABILITY_TO_FLIP = 0.1;

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns the random value generator.
     *
     * @param rand used to generate random values.
     */
    public FlipMutator(Random rand) {
        this.rand = rand;
    }

    @Override
    public BitVectorSolution mutate(BitVectorSolution solution) {
        boolean flipped = false;
        BitVectorSolution copy = solution.duplicate();
        int size = solution.size();

        for (int i = 0; i < size; i++) {
            if (rand.nextDouble() <= PROBABILITY_TO_FLIP) {
                copy.flipBit(i);
                flipped = true;
            }
        }

        if (!flipped) {
            copy.flipBit(rand.nextInt(size));
        }

        return copy;
    }
}
