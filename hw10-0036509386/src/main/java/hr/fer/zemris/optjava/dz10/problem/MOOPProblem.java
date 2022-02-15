package hr.fer.zemris.optjava.dz10.problem;

/**
 * An interface that defines a multi-objective optimization problem.
 *
 * @author Mateo Imbrišak
 */

public interface MOOPProblem {

    /**
     * Provides the number of functions being optimized.
     *
     * @return number of functions being optimized.
     */
    int getNumberOfObjectives();

    /**
     * Evaluates the given {@code solution}.
     *
     * @param solution to be evaluated.
     * @param values solution's fitness for every function being optimized.
     */
    void evaluateSolution(double[] solution, double[] values);

    /**
     * Provides the dimension of a solution.
     *
     * @return number of points in a solution.
     */
    int getSolutionDimension();

    /**
     * Provides the constraint for {@code index} point in a solution.
     * Constraint is represented as an {@code array} of size 2, first number represents lower bound,
     * second number represents upper bound.
     *
     * @param index at which the requested point is in an array.
     *
     * @return an array representing constraints for the point at {@code index} location.
     */
    double[] getConstraint(int index);
}
