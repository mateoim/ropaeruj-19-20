package hr.fer.zemris.optjava.dz4.selection;

import hr.fer.zemris.optjava.dz4.solutions.Solution;

import java.util.Arrays;
import java.util.Random;

/**
 * An implementation of {@link Selection} that
 * selects {@link Solution}s based on roulette
 * wheel algorithm.
 *
 * @author Mateo Imbrišak
 */

public class RouletteWheelSelection implements Selection<Solution> {

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that initializes {@link #rand} random
     * number generator.
     */
    public RouletteWheelSelection() {
        this.rand = new Random();
    }

    @Override
    public Solution select(Solution[] population) {
        int size = population.length;
        Solution[] copy = Arrays.copyOf(population, size);

        Solution worst = copy[size - 1];
        double[] effectiveValues = new double[size];

        for (int i = 0; i < size; i++) {
            effectiveValues[i] = copy[i].getFitness() - worst.getFitness();
        }

        double sum = sum(effectiveValues);

        for (int i = 0; i < size; i++) {
            effectiveValues[i] /= sum;
        }

        double roll = rand.nextDouble();
        double currentSum = 0;

        for (int i = 0; i < size; i++) {
            currentSum += effectiveValues[i];

            if (roll <= currentSum) {
                return copy[i];
            }
        }

        return worst;
    }

    /**
     * Calculates the sum of values in the given array.
     *
     * @param values being summed.
     *
     * @return sum of all values in the given array.
     */
    private double sum(double[] values) {
        double sum = 0;

        for (double value : values) {
            sum += value;
        }

        return sum;
    }
}
