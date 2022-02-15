package hr.fer.zemris.optjava.dz9.selection;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;

/**
 * A Selection based on a Roulette-Wheel.
 *
 * @author Mateo Imbrišak
 */

public class RouletteWheelSelection {

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Keeps cached copy of last given array.
     */
    private double[] copy;

    /**
     * Keeps the last calculated proportions.
     */
    private double[] proportions;

    /**
     * Default constructor that assigns random value generator.
     *
     * @param rand used to generate random values.
     */
    public RouletteWheelSelection(Random rand) {
        this.rand = rand;
    }

    /**
     * Selects an index to be used as a parent based on the given array.
     *
     * @param fitness used to perform the selection.
     *
     * @return index of selected parent.
     */
    public int select(double[] fitness) {
        if (!Arrays.equals(fitness, copy)) {
            copy = Arrays.copyOf(fitness, fitness.length);
            proportions = Arrays.copyOf(fitness, fitness.length);

            double min = Double.POSITIVE_INFINITY;
            int size = fitness.length;

            for (double current : fitness) {
                min = Math.min(current, min);
            }

            for (int i = 0; i < size; i++) {
                proportions[i] = proportions[i] - min;
            }

            double sum = DoubleStream.of(proportions).sum();

            for (int i = 0; i < size; i++) {
                proportions[i] = proportions[i] / sum;
            }
        }

        double roll = rand.nextDouble();
        double sum = 0;

        for (int i = 0, size = fitness.length; i < size; i++) {
            sum += proportions[i];

            if (sum >= roll) {
                return i;
            }
        }

        return fitness.length - 1;
    }
}
