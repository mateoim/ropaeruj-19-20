package hr.fer.zemris.optjava.dz7;

/**
 * An interface that models a transfer function used in {@link FFANN}.
 *
 * @author Mateo Imbrišak
 */

public interface ITransferFunction {

    /**
     * Calculates this function's value ath the given {@code x}.
     *
     * @param x point used to calculate the value.
     * @return value at the given point.
     */
    double valueAt(double x);
}
