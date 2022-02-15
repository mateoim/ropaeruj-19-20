package hr.fer.zemris.optjava.dz5.mutators;

import hr.fer.zemris.optjava.dz5.solutions.PermutationSolution;

import java.util.Random;

/**
 * An implementation of {@link Mutator} that swaps a {@link #PERCENTAGE_TO_MUTATE}
 * elements of {@link PermutationSolution}s.
 *
 * @author Mateo Imbrišak
 */

public class SwapMutator implements Mutator<PermutationSolution> {

    /**
     * Percentage of elements that will be swapped.
     */
    private static final double PERCENTAGE_TO_MUTATE = 0.1;

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns the random value generator.
     *
     * @param rand used to generate random values.
     */
    public SwapMutator(Random rand) {
        this.rand = rand;
    }

    @Override
    public PermutationSolution mutate(PermutationSolution solution) {
        PermutationSolution copy = solution.duplicate();
        int size = solution.size();
        int numberToMutate = (int) Math.round(PERCENTAGE_TO_MUTATE * size);

        for (int i = 0; i < numberToMutate; i++) {
            copy.swap(rand.nextInt(size - 1), rand.nextInt(size - 1));
        }

        return copy;
    }
}
