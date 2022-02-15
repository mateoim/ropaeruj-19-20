package hr.fer.zemris.optjava.dz5.algorithm;

import hr.fer.zemris.optjava.dz5.solutions.Solution;

/**
 * An interface that models algorithms.
 *
 * @author Mateo Imbrišak
 */

public interface Algorithm<T extends Solution> {

    /**
     * Executes the {@code Algorithm} and attempts to find the best {@link Solution}.
     *
     * @return best found {@link Solution}.
     */
    T run();
}
