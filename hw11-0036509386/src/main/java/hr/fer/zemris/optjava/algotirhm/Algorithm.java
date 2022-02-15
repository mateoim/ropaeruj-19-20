package hr.fer.zemris.optjava.algotirhm;

import hr.fer.zemris.generic.ga.GASolution;

/**
 * An interface that models an optimization algorithm.
 *
 * @author Mateo Imbrišak
 */

public interface Algorithm {

    /**
     * Executes the algorithm.
     *
     * @return best found solution.
     */
    GASolution<int[]> run();

    /**
     * Provides the best found solution.
     *
     * @return best found solution.
     */
    GASolution<int[]> getBestSolution();
}
