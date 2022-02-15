package hr.fer.zemris.optjava.dz5.crossover;

import hr.fer.zemris.optjava.dz5.solutions.BitVectorSolution;

/**
 * An implementation of {@link Crossover} that
 * crosses the selected parents using or operator on their bits.
 *
 * @author Mateo Imbrišak
 */

public class OrCrossover implements Crossover<BitVectorSolution> {

    @Override
    public BitVectorSolution cross(BitVectorSolution firstParent, BitVectorSolution secondParent) {
        int size = firstParent.size();
        boolean[] childValues = new boolean[size];

        for (int i = 0; i < size; i++) {
            childValues[i] = firstParent.getBit(i) || secondParent.getBit(i);
        }

        return new BitVectorSolution(childValues);
    }
}
