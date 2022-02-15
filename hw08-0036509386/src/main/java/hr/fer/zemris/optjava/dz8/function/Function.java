package hr.fer.zemris.optjava.dz8.function;

import hr.fer.zemris.optjava.dz8.network.NeuralNetwork;

/**
 * An interface that models a transfer function used in {@link NeuralNetwork}.
 *
 * @author Mateo Imbrišak
 */

public interface Function {

    /**
     * Calculates this function's value ath the given {@code x}.
     *
     * @param x point used to calculate the value.
     * @return value at the given point.
     */
    double valueAt(double x);
}
