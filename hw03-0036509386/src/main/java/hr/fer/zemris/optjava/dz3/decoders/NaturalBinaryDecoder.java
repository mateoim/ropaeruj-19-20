package hr.fer.zemris.optjava.dz3.decoders;

import hr.fer.zemris.optjava.dz3.solutions.BitvectorSolution;

/**
 * A {@link IDecoder} that decodes binary vectors.
 *
 * @author Mateo Imbrišak
 */

public class NaturalBinaryDecoder extends BitvectorDecoder {

    public NaturalBinaryDecoder(double[] mins, double[] maxs, int n, int totalBits) {
        super(mins, maxs, n, totalBits);
    }

    public NaturalBinaryDecoder(double min, double max, int n, int totalBits) {
        super(min, max, n, totalBits);
    }

    @Override
    public double[] decode(BitvectorSolution solution) {
        int numberOfVariables = totalBits / n;
        double[] variables = new double[numberOfVariables];

        fillArray(solution, variables);

        return variables;
    }

    @Override
    public void decode(BitvectorSolution solution, double[] destination) {
        fillArray(solution, destination);
    }

    /**
     * Fills the given {@code array} with decoded {@code solution}.
     *
     * @param solution being decoded.
     * @param destination where the decoded solution is being stored.
     */
    private void fillArray(BitvectorSolution solution, double[] destination) {
        int numberOfVariables = totalBits / n;
        bits = new int[solution.bits.length];

        for (int i = 0, size = bits.length; i < size; i++) {
            bits[i] = solution.bits[i] ? 1 : 0;
        }

        for (int i = 1; i <= numberOfVariables; i++) {
            double value = 0;
            for (int currentPower = n - 1; currentPower >= 0; currentPower--) {
                value += bits[i * n + (currentPower - n)] * Math.pow(2, currentPower);
            }

            destination[i - 1] = mins[i - 1] + value / (Math.pow(2, n) - 1) * (maxs[i - 1] - mins[i - 1]);
        }
    }
}
