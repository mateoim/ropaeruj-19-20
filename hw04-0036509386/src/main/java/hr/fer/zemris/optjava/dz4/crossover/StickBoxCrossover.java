package hr.fer.zemris.optjava.dz4.crossover;

import hr.fer.zemris.optjava.dz4.solutions.StickBoxSolution;

import java.util.Random;

/**
 * A {@link Crossover} that crossed stick columns from one
 * parent into a child based on the other parent.
 *
 * @author Mateo Imbrišak
 */

public class StickBoxCrossover implements Crossover<StickBoxSolution> {

    /**
     * Percentage of stick columns to copy from {@code secondParent}
     * to child based on {@code firstParent}.
     */
    private static final double PERCENTAGE_TO_CROSS = 0.3;

    /**
     * Used to generate random column indexes.
     */
    private final Random rand;

    /**
     * Default constructor that initializes {@link #rand}
     * random value generator.
     */
    public StickBoxCrossover() {
        this.rand = new Random();
    }

    @Override
    public StickBoxSolution cross(StickBoxSolution firstParent, StickBoxSolution secondParent) {
        StickBoxSolution copy = firstParent.duplicate();

        int toCross = (int) Math.round(PERCENTAGE_TO_CROSS * secondParent.getFitness());

        for (int i = 0; i < toCross; i++) {
            int[] column = secondParent.getStickColumn(rand.nextInt((int) secondParent.getFitness() - 1));

            copy.insertColumn(column);
        }

        return copy;
    }
}
