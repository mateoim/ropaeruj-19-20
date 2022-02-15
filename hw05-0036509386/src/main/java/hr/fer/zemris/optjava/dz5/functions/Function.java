package hr.fer.zemris.optjava.dz5.functions;

import hr.fer.zemris.optjava.dz5.solutions.Solution;

/**
 * An interface that models function
 * used to calculate fitness of {@link Solution}s.
 *
 * @param <T> type of solution used.
 *
 * @author Mateo Imbrišak
 */

public interface Function<T extends Solution> {

    /**
     * Calculates the fitness of the given {@link Solution}
     * and assigns it using {@link Solution#setFitness(double)}.
     *
     * @param solution whose fitness will be calculated.
     */
    void calculateFitness(T solution);
}
