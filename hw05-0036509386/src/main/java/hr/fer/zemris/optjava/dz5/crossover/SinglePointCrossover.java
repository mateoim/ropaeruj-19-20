package hr.fer.zemris.optjava.dz5.crossover;

import hr.fer.zemris.optjava.dz5.solutions.BitVectorSolution;

import java.util.Random;

/**
 * An implementation of {@link Crossover} that
 * randomly picks a point up to which genetic material
 * of first parent is used and after which genetic material
 * of second parent is used.
 *
 * @author Mateo Imbrišak
 */

public class SinglePointCrossover implements Crossover<BitVectorSolution> {

    /**
     * Used to generate crossing point.
     */
    private final Random rand;

    /**
     * Default constructor that assigns the random number generator.
     *
     * @param rand used to generate crossing point.
     */
    public SinglePointCrossover(Random rand) {
        this.rand = rand;
    }

    @Override
    public BitVectorSolution cross(BitVectorSolution firstParent, BitVectorSolution secondParent) {
        int size = firstParent.size();
        int crossingPoint = rand.nextInt(size - 1);
        boolean[] childValues = new boolean[size];

        int i = 0;
        while (i < crossingPoint) {
            childValues[i] = firstParent.getBit(i);

            i++;
        }

        for (; i < size; i++) {
            childValues[i] = secondParent.getBit(i);
        }

        return new BitVectorSolution(childValues);
    }
}
