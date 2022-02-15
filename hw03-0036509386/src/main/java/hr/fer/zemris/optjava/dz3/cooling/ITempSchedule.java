package hr.fer.zemris.optjava.dz3.cooling;

/**
 * An interface that models cooling strategies
 * used by {@link hr.fer.zemris.optjava.dz3.algorithms.SimulatedAnnealing}
 * algorithm.
 *
 * @author Mateo Imbrišak
 */

public interface ITempSchedule {

    /**
     * Calculates next temperature.
     *
     * @return next temperature.
     */
    double getNextTemperature();

    /**
     * Provides the number of iterations in
     * the inner loop.
     *
     * @return number of iterations in the inner loop.
     */
    int getInnerLoopCounter();

    /**
     * Provides the number of iterations in
     * the outer loop.
     *
     * @return number of iterations in the outer loop.
     */
    int getOuterLoopCounter();
}
