package hr.fer.zemris.optjava.dz3.algorithms;

/**
 * An interface used to model
 * optimization algorithms.
 *
 * @author Mateo Imbrišak
 */

public interface IOptAlgorithm<T> {

    /**
     * Executes the algorithm.
     *
     * @return best found solution.
     */
    T run();
}
