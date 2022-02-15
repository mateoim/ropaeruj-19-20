package hr.fer.zemris.optjava.dz2.functions;

import hr.fer.zemris.optjava.dz2.IFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * A class representing a function with 5 sampled variables
 * and solutions, used to find the values of constants.
 *
 * @author Mateo Imbrišak
 */

public class Function4 implements IFunction {

    /**
     * Equation that represents this system.
     */
    private RealMatrix variables;

    /**
     * Results of the sampling.
     */
    private RealVector results;

    /**
     * Default constructor that assigns all values.
     *
     * @param variables collected during sampling
     * @param results of the sampling.
     */
    public Function4(RealMatrix variables, RealVector results) {
        this.variables = variables;
        this.results = results;
    }

    @Override
    public int getNumberOfVariables() {
        return 6;
    }

    @Override
    public double calculateValue(RealVector point) {
        double sum = 0;

        for (int i = 0, size = results.getDimension(); i < size; i++) {
            double result = solve(point, i);
            sum += result * result;
        }

        return sum;
    }

    @Override
    public RealVector calculateGradient(RealVector point) {
        int size = results.getDimension();
        double[] values = new double[getNumberOfVariables()];

        for (int i = 0; i < size; i++) {
            double current = solve(point, i);

            values[0] += 2 * variables.getEntry(i, 0) * current; // d/da
            values[1] += 2 * Math.pow(variables.getEntry(i, 0), 3) * variables.getEntry(i, 1) * current; // d/db
            values[2] += 2 * Math.exp(point.getEntry(3) * variables.getEntry(i, 2)) *
                    (1 + Math.cos(point.getEntry(4) * variables.getEntry(i, 3))) * current; // d/dc
            values[3] += 2 * point.getEntry(2) * variables.getEntry(i, 2) *
                    Math.exp(variables.getEntry(i, 2) * point.getEntry(3)) *
                    (1 + Math.cos(point.getEntry(4) * variables.getEntry(i, 3))) * current; // d/dd
            values[4] += -2 * point.getEntry(2) * variables.getEntry(i, 3) *
                    Math.exp(point.getEntry(3) * variables.getEntry(i, 2)) *
                    Math.sin(point.getEntry(4) * variables.getEntry(i, 3)) * current; // d/de
            values[5] += 2 * variables.getEntry(i, 3) * Math.pow(variables.getEntry(i, 4), 2) * current; // d/df

        }

        return (new ArrayRealVector(values)).unitVector();
    }

    /**
     * Provides the solution of the error equation.
     *
     * @param point in which the solution is calculated.
     * @param i reading which is used for variables.
     *
     * @return solved error equation in the given {@code point}
     *          for the given {@code i} sample.
     */
    private double solve(RealVector point, int i) {
        return (point.getEntry(0) * variables.getEntry(i, 0) // a*x1
                + point.getEntry(1) * Math.pow(variables.getEntry(i, 0), 3) * variables.getEntry(i, 1) // + b*x1^3*x2
                + point.getEntry(2) * Math.exp(point.getEntry(3) * variables.getEntry(i, 2)) // + c*e(d*x3)
                * (1 + Math.cos(point.getEntry(4) * variables.getEntry(i, 3))) // * (1+cos(e*x4))
                + point.getEntry(5) * variables.getEntry(i, 3) * Math.pow(variables.getEntry(i, 4), 2)) // + f*x4*x5^2
                - results.getEntry(i);
    }
}
