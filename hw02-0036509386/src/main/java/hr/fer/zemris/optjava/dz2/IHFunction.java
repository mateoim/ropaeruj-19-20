package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * An interface that represents a function
 * and can calculate the function's gradient,
 * value and Hesse matrix for a given {@link RealVector}.
 *
 * @author Mateo Imbrišak
 */

public interface IHFunction extends IFunction {

    /**
     * Calculates Hesse matrix for the given {@link RealVector}.
     *
     * @param point used to calculate the matrix.
     *
     * @return a Hesse {@link RealMatrix} calculated from
     * the given {@code point}.
     */
    RealMatrix getHesseMatrix(RealVector point);
}
