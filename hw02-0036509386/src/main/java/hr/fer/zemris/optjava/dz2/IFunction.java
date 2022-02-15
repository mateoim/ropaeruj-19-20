package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.RealVector;

/**
 * An interface that represents a function
 * and can provide the number of variables and
 * calculate the function's gradient and value
 * for a given {@link RealVector}.
 *
 * @author Mateo Imbrišak
 */

public interface IFunction {

    /**
     * Provides the number of variables in this function.
     *
     * @return number of variables.
     */
    int getNumberOfVariables();

    /**
     * Calculates the function's value in the given {@link RealVector}.
     *
     * @param point used to calculate the value.
     *
     * @return function's value in the given {@code point}.
     */
    double calculateValue(RealVector point);

    /**
     * Calculates the function's gradient in the given {@link RealVector}.
     *
     * @param point used to calculate the gradient.
     *
     * @return function's gradient in the given {@code point}.
     */
    RealVector calculateGradient(RealVector point);
}
