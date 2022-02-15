package hr.fer.zemris.optjava.dz10.selection;

import java.util.Random;

/**
 * A selection based on solution distances.
 *
 * @author Mateo Imbrišak
 */

public class CrowdedTournamentSelection {

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns random value generator.
     *
     * @param rand used to generate random values.
     */
    public CrowdedTournamentSelection(Random rand) {
        this.rand = rand;
    }

    /**
     * Randomly selects an index of a solution to be used as a parent.
     *
     * @param dominatedByCounter used to determine the front of a solution and number os solutions used.
     * @param distances used to pick a solution if both are in the same front.
     *
     * @return index of selected solution.
     */
    public int select(int[] dominatedByCounter, double[] distances) {
        int size = dominatedByCounter.length;

        int first = rand.nextInt(size);
        int second;

        do {
            second = rand.nextInt(size);
        } while (first == second);

        if (dominatedByCounter[first] == dominatedByCounter[second]) {
            return select(first, second, distances);
        } else {
            return dominatedByCounter[first] < dominatedByCounter[second] ? first : second;
        }
    }

    /**
     * Performs selection when the solutions are on the same front.
     *
     * @param first index of first picked solution.
     * @param second index of second picked solution.
     * @param distances distances between solutions.
     *
     * @return index of solution with greater distance.
     */
    public int select(int first, int second, double[] distances) {
        return distances[first] > distances[second] ? first : second;
    }
}
