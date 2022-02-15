package hr.fer.zemris.optjava.crossover;

import hr.fer.zemris.generic.ga.ArrayGASolution;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * A {@link Crossover} that selects rectangles form
 * {@code firstParent} or {@code secondParent} and
 * passes them to the child.
 *
 * @author Mateo Imbrišak
 */

public class DiscreteCrossover implements Crossover<GASolution<int[]>> {

    @Override
    public GASolution<int[]> cross(GASolution<int[]> firstParent, GASolution<int[]> secondParent) {
        IRNG rand = RNG.getRNG();
        int solutionLength = firstParent.getData().length;
        int[] firstValues = firstParent.getData();
        int[] secondValues = secondParent.getData();

        int[] childValues = new int[solutionLength];

        if (rand.nextBoolean()) {
            childValues[0] = firstValues[0];
        } else {
            childValues[0] = secondValues[0];
        }

        int index = 1;

        for (int i = 0, size = (solutionLength - 1) / 5; i < size; i++) {
            if (rand.nextBoolean()) {
                childValues[index] = firstValues[index];
                childValues[index + 1] = firstValues[index + 1];
                childValues[index + 2] = firstValues[index + 2];
                childValues[index + 3] = firstValues[index + 3];
                childValues[index + 4] = firstValues[index + 4];
            } else {
                childValues[index] = secondValues[index];
                childValues[index + 1] = secondValues[index + 1];
                childValues[index + 2] = secondValues[index + 2];
                childValues[index + 3] = secondValues[index + 3];
                childValues[index + 4] = secondValues[index + 4];
            }

            index += 5;
        }

        return new ArrayGASolution(childValues);
    }
}
