package hr.fer.zemris.optjava.dz10.problem;

/**
 * A problem that has four functions {@code x * x} that it attempts to minimize.
 *
 * @author Mateo Imbrišak
 */

public class FirstProblem implements MOOPProblem {

    /**
     * Constraint for all dimensions.
     */
    private static final double[] CONSTRAINT = new double[] {-5, 5};

    /**
     * Keeps value used by {@link #getNumberOfObjectives()}.
     */
    private static final int NUMBER_OF_OBJECTIVES = 4;

    /**
     * Keeps value used by {@link #getSolutionDimension()}.
     */
    private static final int NUMBER_OF_DIMENSIONS = 4;

    @Override
    public int getNumberOfObjectives() {
        return NUMBER_OF_OBJECTIVES;
    }

    @Override
    public void evaluateSolution(double[] solution, double[] values) {
        for (int i = 0; i < NUMBER_OF_DIMENSIONS; i++) {
            double value = solution[i];
            values[i] = value * value;
        }
    }

    @Override
    public int getSolutionDimension() {
        return NUMBER_OF_DIMENSIONS;
    }

    @Override
    public double[] getConstraint(int index) {
        if (index < 0 || index > 3) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }

        return CONSTRAINT;
    }
}
