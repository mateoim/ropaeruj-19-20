package hr.fer.zemris.optjava.dz3.decoders;

import hr.fer.zemris.optjava.dz3.solutions.DoubleArraySolution;

import java.util.Arrays;

/**
 * An implementation of {@link IDecoder} used
 * to decode {@link DoubleArraySolution}s.
 *
 * @author Mateo Imbrišak
 */

public class PassThroughDecoder implements IDecoder<DoubleArraySolution> {

    /**
     * Default constructor.
     */
    public PassThroughDecoder() {}

    @Override
    public double[] decode(DoubleArraySolution solution) {
        return Arrays.copyOf(solution.values, solution.values.length);
    }

    @Override
    public void decode(DoubleArraySolution solution, double[] destination) {
        for (int i = 0, size = solution.values.length; i < size; i++) {
            destination[i] = solution.values[i];
        }
    }
}
