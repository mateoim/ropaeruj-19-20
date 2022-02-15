package hr.fer.zemris.optjava.dz4.mutators;

import hr.fer.zemris.optjava.dz4.solutions.StickBoxSolution;

import java.util.List;
import java.util.Random;

/**
 * A {@link Mutator} that mutates {@link StickBoxSolution}
 * by moving sticks around.
 *
 * @author Mateo Imbrišak
 */

public class StickBoxMutator implements Mutator<StickBoxSolution> {

    /**
     * Determines percentage of sticks to move.
     */
    private final double mutationFactor;

    /**
     * Used to generate random indexes.
     */
    private final Random rand;

    /**
     * Default constructor that assigns {@link #mutationFactor}.
     *
     * @param mutationFactor determines percentage of sticks to move.
     */
    public StickBoxMutator(double mutationFactor) {
        this.mutationFactor = mutationFactor;
        this.rand = new Random();
    }

    @Override
    public StickBoxSolution mutate(StickBoxSolution solution) {
        int size = solution.getNumberOfSticks();
        List<Integer> mutationList = solution.toList();

        int toMutate = (int) Math.round(mutationFactor * size);

        for (int i = 0; i < toMutate; i++) {
            int toRemove = rand.nextInt(size - 1);
            int toInsert = rand.nextInt(size - 1);

            int removed = mutationList.remove(toRemove);
            mutationList.add(toInsert, removed);
        }

        return new StickBoxSolution(mutationList);
    }
}
