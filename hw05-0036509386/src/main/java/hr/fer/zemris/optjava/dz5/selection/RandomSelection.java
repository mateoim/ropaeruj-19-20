package hr.fer.zemris.optjava.dz5.selection;

import hr.fer.zemris.optjava.dz5.solutions.Solution;

import java.util.Random;

/**
 * An implementation of {@link Selection} that selects
 * parents completely randomly.
 *
 * @author Mateo Imbrišak
 */

public class RandomSelection implements Selection<Solution> {

    /**
     * Used to perform selection.
     */
    private final Random rand;

    /**
     * Default constructor that assigns the random value generator.
     *
     * @param rand used to perform selection.
     */
    public RandomSelection(Random rand) {
        this.rand = rand;
    }

    @Override
    public Solution select(Solution[] population) {
        return population[rand.nextInt(population.length - 1)];
    }
}
