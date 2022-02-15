package hr.fer.zemris.optjava.dz5.crossover;

import hr.fer.zemris.optjava.dz5.solutions.PermutationSolution;
import hr.fer.zemris.optjava.dz5.solutions.Solution;

import java.util.Random;

/**
 * An implementation of {@link Crossover} used to
 * cross permutation based {@link Solution}s.
 *
 * @author Mateo Imbrišak
 */

public class PartiallyMappedCrossover implements Crossover<PermutationSolution> {

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns the random value generator.
     *
     * @param rand used to generate random values.
     */
    public PartiallyMappedCrossover(Random rand) {
        this.rand = rand;
    }

    @Override
    public PermutationSolution cross(PermutationSolution firstParent, PermutationSolution secondParent) {
        int size = firstParent.size();
        int low = rand.nextInt(size - 1);
        int high = rand.nextInt(size -1);

        if (low > high) {
            int temp = low;
            low = high;
            high = temp;
        }

        int[] values = new int[size];

        for (int i = low; i <= high; i++) {
            values[i] = secondParent.get(i);
        }

        for (int i = 0; i < size; i++) {
            if (i == low) {
                i = high;
                continue;
            }

            int current = firstParent.get(i);

            for (int j = low; j <= high; j++) {
                if (current == secondParent.get(j)) {
                    current = firstParent.get(i);
                    break;
                }
            }

            values[i] = current;
        }

        return new PermutationSolution(values);
    }
}
