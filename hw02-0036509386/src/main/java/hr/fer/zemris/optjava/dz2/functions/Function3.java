package hr.fer.zemris.optjava.dz2.functions;

import hr.fer.zemris.optjava.dz2.IHFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * A function the represents a linear system.
 *
 * @author Mateo Imbrišak
 */

public class Function3 implements IHFunction {

    /**
     * Equation that represents this system.
     */
    private RealMatrix equation;

    /**
     * Solution of the {@link #equation}.
     */
    private RealVector solution;

    /**
     * Default constructor that assigns all values.
     *
     * @param equation used for this system.
     * @param solution of this system.
     */
    public Function3(RealMatrix equation, RealVector solution) {
        this.equation = equation;
        this.solution = solution;
    }

    @Override
    public RealMatrix getHesseMatrix(RealVector point) {
        int size = getNumberOfVariables();
        double[][] values = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    values[i][j] += 2 * (equation.getEntry(i, k) * equation.getEntry(j, k));
                }
            }
        }

        return new Array2DRowRealMatrix(values);
    }

    @Override
    public int getNumberOfVariables() {
        return solution.getDimension();
    }

    /**
     * Calculates the error for the given {@code point}.
     *
     * @param point used to calculate the value.
     *
     * @return total error of this system for the given point.
     */
    @Override
    public double calculateValue(RealVector point) {
        int size = getNumberOfVariables();
        double sum = 0;

        for (int i = 0; i < size; i++) {
            double currentSum = 0;

            for (int j = 0; j < size; j++) {
                currentSum += equation.getEntry(i, j) * point.getEntry(j);
            }
            currentSum -= solution.getEntry(i);

            sum += currentSum * currentSum;
        }

        return sum;
    }

    @Override
    public RealVector calculateGradient(RealVector point) {
        RealMatrix A = equation.copy();
        RealMatrix b = new Array2DRowRealMatrix(solution.toArray());
        RealMatrix x = new Array2DRowRealMatrix(point.toArray());

        RealMatrix gradientMatrix = A.scalarMultiply(2).transpose().multiply(A.multiply(x).subtract(b));

        return new ArrayRealVector(gradientMatrix.getColumnVector(0));
    }
}
