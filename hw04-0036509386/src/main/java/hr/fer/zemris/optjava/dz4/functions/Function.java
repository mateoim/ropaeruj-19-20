package hr.fer.zemris.optjava.dz4.functions;

import hr.fer.zemris.optjava.dz4.solutions.DoubleArraySolution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * A class representing a function with 5 sampled variables
 * and solutions, used to find the values of constants.
 *
 * @author Mateo Imbrišak
 */

public class Function {

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
    public Function(RealMatrix variables, RealVector results) {
        this.variables = variables;
        this.results = results;
    }

    /**
     * Calculates and assigns fitness for the given {@link DoubleArraySolution}.
     *
     * @param solution whose fitness is being calculated.
     */
    public void calculateFitness(DoubleArraySolution solution) {
        double sum = 0;

        for (int i = 0, size = results.getDimension(); i < size; i++) {
            double result = (solution.getValue(0) * variables.getEntry(i, 0) // a*x1
                    + solution.getValue(1) * Math.pow(variables.getEntry(i, 0), 3) * variables.getEntry(i, 1) // + b*x1^3*x2
                    + solution.getValue(2) * Math.exp(solution.getValue(3) * variables.getEntry(i, 2)) // + c*e(d*x3)
                    * (1 + Math.cos(solution.getValue(4) * variables.getEntry(i, 3))) // * (1+cos(e*x4))
                    + solution.getValue(5) * variables.getEntry(i, 3) * Math.pow(variables.getEntry(i, 4), 2)) // + f*x4*x5^2
                    - results.getEntry(i);
            sum += result * result;
        }

        solution.setFitness(-sum);
    }
}
