package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.*;

import java.util.Random;

/**
 * A class that provides methods used to
 * perform numeric optimisation algorithms.
 *
 * @author Mateo Imbrišak
 */

public class NumOptAlgorithms {

    /**
     * Low bound used when generating initial solution.
     */
    private static final double LOW_BOUND = -5;

    /**
     * High bound used when generating initial solution.
     */
    private static final double HIGH_BOUND = 5;

    /**
     * Threshold for double tolerance.
     */
    private static final double THRESHOLD = 1E-10;

    /**
     * Don't let anyone instantiate this class.
     */
    private NumOptAlgorithms() {}

    /**
     * Gradient descent algorithm used to find the minimum value of the given {@link IFunction}.
     *
     * @param function being minimised.
     * @param iterations maximum number of iterations.
     * @param point used as initial solution, {@code null} if it is random.
     *
     * @return found local minimum.
     */
    public static RealVector gradientMinimum(IFunction function, int iterations, RealVector point) {
        if (point == null) {
            point = generateRandomSolution(function);
        }

        for (int k = 0; k < iterations; k++) {
            if (checkSolution(point, function)) {
                return point;
            }

            System.out.println(point);

            RealVector d = function.calculateGradient(point).mapMultiply(-1);
            double lambda = calculateLambda(function, point, iterations);
            point = point.add(d.mapMultiply(lambda));
        }

        return point;
    }

    /**
     * Newton's algorithm used to find the minimum value of the given {@link IFunction}.
     *
     * @param function being minimised.
     * @param iterations maximum number of iterations.
     * @param point used as initial solution, {@code null} if it is random.
     *
     * @return found local minimum.
     */
    public static RealVector newtonMinimum(IHFunction function, int iterations, RealVector point) {
        if (point == null) {
            point = generateRandomSolution(function);
        }

        for (int k = 0; k < iterations; k++) {
            if (checkSolution(point, function)) {
                return point;
            }

            System.out.println(point);

            RealMatrix inverse = new LUDecomposition(function.getHesseMatrix(point)).getSolver().getInverse();
            RealMatrix tau = inverse.scalarMultiply(-1).multiply(
                    new Array2DRowRealMatrix(function.calculateGradient(point).toArray())
            );
            double lambda = calculateLambda(function, point, iterations);

            point = point.add(tau.getColumnVector(0).mapMultiply(lambda));
        }

        return point;
    }

    /**
     * Generates a random point to be used as initial solution.
     *
     * @param function used to determine the number of variables.
     *
     * @return a {@link RealVector} with random values between
     *         {@link #LOW_BOUND} and {@link #HIGH_BOUND}.
     */
    private static RealVector generateRandomSolution(IFunction function) {
        double span = HIGH_BOUND - LOW_BOUND;
        Random rand = new Random();
        double[] values = new double[function.getNumberOfVariables()];

        for (int i = 0, size = function.getNumberOfVariables(); i < size; i++) {
            values[i] = rand.nextDouble() * span + LOW_BOUND;
        }

        return new ArrayRealVector(values);
    }

    /**
     * Finds the upper lambda value to be used.
     *
     * @param function being used to calculate upper bound.
     * @param solution for which the bound is being calculated.
     *
     * @return upper lambda bound.
     */
    private static double findLambdaUpper(IFunction function, RealVector solution) {
        double lambdaUpper = 0.5;

        RealVector dVector = function.calculateGradient(solution).mapMultiply(-1);
        RealMatrix d = new Array2DRowRealMatrix(dVector.toArray());
        double derivation;

        do {
            lambdaUpper *= 2;

            RealVector functionArg = solution.add(dVector.mapMultiply(lambdaUpper));
            RealVector gradientVector = function.calculateGradient(functionArg);
            RealMatrix gradientMatrix = new Array2DRowRealMatrix(gradientVector.toArray());

            derivation = gradientMatrix.transpose().multiply(d).getEntry(0, 0);
        } while (derivation < 0);

        return lambdaUpper;
    }

    /**
     * Checks if the given {@code point} is the solution.
     *
     * @param point being checked.
     * @param function used to check the {@code point}.
     *
     * @return {@code true} if the {@code point} is the solution,
     * otherwise {@code false}.
     */
    private static boolean checkSolution(RealVector point, IFunction function) {
        RealVector gradient = function.calculateGradient(point);

        for (int i = 0, size = gradient.getDimension(); i < size; i++) {
            if (Math.abs(gradient.getEntry(i)) > THRESHOLD) {
                return false;
            }
        }

        return true;
    }

    /**
     * Finds the lambda value to be used as a step in the next iteration.
     *
     * @param function being minimised.
     * @param point representing the current solution.
     * @param iterations maximum number of iterations to be performed.
     *
     * @return found lambda value.
     */
    private static double calculateLambda(IFunction function, RealVector point, int iterations) {
        double lambdaLower = 0;
        double lambdaUpper = findLambdaUpper(function, point);

        RealVector solution = point.copy();

        RealVector dVector = function.calculateGradient(point).mapMultiply(-1);
        RealMatrix d = new Array2DRowRealMatrix(dVector.toArray());
        double lambda = 0.01;
        int k = 0;

        while (k < iterations) {
            lambda = (lambdaLower + lambdaUpper) / 2;

            RealVector functionArg = solution.add(dVector.mapMultiply(lambdaUpper));
            RealVector gradientVector = function.calculateGradient(functionArg);
            RealMatrix gradientMatrix = new Array2DRowRealMatrix(gradientVector.toArray());

            double derivation = gradientMatrix.transpose().multiply(d).getEntry(0, 0);

            if (Math.abs(derivation) < THRESHOLD) {
                return lambda;
            }

            k++;

            if (derivation > 0) {
                lambdaUpper = lambda;
            } else {
                lambdaLower = lambda;
            }
        }

        return lambda;
    }
}
