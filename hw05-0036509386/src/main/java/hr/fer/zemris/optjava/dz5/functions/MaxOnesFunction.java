package hr.fer.zemris.optjava.dz5.functions;

import hr.fer.zemris.optjava.dz5.solutions.BitVectorSolution;

/**
 * A function sued to calculate fitness of {@link BitVectorSolution}s.
 *
 * @author Mateo Imbrišak
 */

public class MaxOnesFunction implements Function<BitVectorSolution> {

    @Override
    public void calculateFitness(BitVectorSolution solution) {
        int k = solution.countOnes();
        int n = solution.size();
        double fitness;

        if (k <= 0.8 * n) {
            fitness = k / (double) n;
        } else if (k > 0.8 * n && k <= 0.9 * n) {
            fitness = 0.8;
        } else {
            fitness = (2 * k / (double) n) - 1;
        }

        solution.setFitness(fitness);
    }
}
