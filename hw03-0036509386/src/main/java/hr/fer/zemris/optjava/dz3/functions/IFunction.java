package hr.fer.zemris.optjava.dz3.functions;

/**
 * An interface that represents a function.
 *
 * @author Mateo Imbrišak
 */
public interface IFunction {

    /**
     * Provides the value at the given point.
     *
     * @param point used to calculate the value.
     *
     * @return value at the given point.
     */
    double valueAt(double[] point);
}
