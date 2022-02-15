package hr.fer.zemris.optjava.dz5.functions;

import hr.fer.zemris.optjava.dz5.solutions.PermutationSolution;
import hr.fer.zemris.optjava.dz5.solutions.Solution;

/**
 * A {@link Function} used to calculate fitness of
 * {@link Solution}s of Quadratic Assignment Problem.
 *
 * @author Mateo Imbrišak
 */

public class QAPFunction implements Function<PermutationSolution> {

    /**
     * A matrix that keeps distances between locations.
     */
    private final int[][] distances;

    /**
     * A matrix that keeps transport costs between factories.
     */
    private final int[][] resources;

    /**
     * Default constructor that assigns all matrices.
     *
     * @param distances matrix.
     * @param resources matrix.
     */
    public QAPFunction(int[][] distances, int[][] resources) {
        this.distances = distances;
        this.resources = resources;
    }

    @Override
    public void calculateFitness(PermutationSolution solution) {
        double sum = 0;
        int dimension = distances.length;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int c = resources[solution.get(i) - 1][solution.get(j) - 1];
                int d = distances[i][j];

                sum += c * d;
            }
        }

        solution.setFitness(-sum);
    }
}
