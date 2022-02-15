package hr.fer.zemris.optjava.dz3.functions;

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
    public double valueAt(double[] point) {
        double sum = 0;

        for (int i = 0, size = results.getDimension(); i < size; i++) {
            double result = (point[0] * variables.getEntry(i, 0) // a*x1
                    + point[1] * Math.pow(variables.getEntry(i, 0), 3) * variables.getEntry(i, 1) // + b*x1^3*x2
                    + point[2] * Math.exp(point[3] * variables.getEntry(i, 2)) // + c*e(d*x3)
                    * (1 + Math.cos(point[4] * variables.getEntry(i, 3))) // * (1+cos(e*x4))
                    + point[5] * variables.getEntry(i, 3) * Math.pow(variables.getEntry(i, 4), 2)) // + f*x4*x5^2
                    - results.getEntry(i);
            sum += result * result;
        }

        return sum;
    }
}
