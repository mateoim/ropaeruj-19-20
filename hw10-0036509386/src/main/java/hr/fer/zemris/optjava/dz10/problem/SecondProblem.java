package hr.fer.zemris.optjava.dz10.problem;

/**
 * A simple multi-objective optimization problem.
 *
 * @author Mateo Imbrišak
 */

public class SecondProblem implements MOOPProblem {

    /**
     * Constraint for all dimensions.
     */
    private static final double[][] CONSTRAINTS = new double[][] {{0.1, 1}, {0, 5}};

    /**
     * Keeps value used by {@link #getNumberOfObjectives()}.
     */
    private static final int NUMBER_OF_OBJECTIVES = 2;

    /**
     * Keeps value used by {@link #getSolutionDimension()}.
     */
    private static final int NUMBER_OF_DIMENSIONS = 2;

    @Override
    public int getNumberOfObjectives() {
        return NUMBER_OF_OBJECTIVES;
    }

    @Override
    public void evaluateSolution(double[] solution, double[] values) {
        values[0] = solution[0];
        values[1] = (1 + solution[1]) / solution[0];
    }

    @Override
    public int getSolutionDimension() {
        return NUMBER_OF_DIMENSIONS;
    }

    @Override
    public double[] getConstraint(int index) {
        return CONSTRAINTS[index];
    }
}
