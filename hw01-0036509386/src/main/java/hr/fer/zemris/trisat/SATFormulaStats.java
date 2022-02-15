package hr.fer.zemris.trisat;

/**
 * A class that keeps additional statistics about a {@link SATFormula} used in
 * {@link hr.fer.zemris.trisat.algorithms.WeightedIteratedSearch} algorithm.
 *
 * @author Mateo Imbrišak
 */

public class SATFormulaStats {

    /**
     * Multiplication factor used when a {@link Clause} is satisfied.
     */
    private static final double PERCENTAGE_CONSTANT_UP = 0.1;

    /**
     * Multiplication factor used when a {@link Clause} is not satisfied.
     */
    private static final double PERCENTAGE_CONSTANT_DOWN = 0.1;

    /**
     * Used when calculating {@link #percentageBonus}.
     */
    private static final int PERCENTAGE_UNIT_AMOUNT = 50;

    /**
     * Keeps the {@link SATFormula} used to calculate the statistics.
     */
    private SATFormula formula;

    /**
     * Keeps the number of satisfied {@link Clause}s
     * from last {@link #setAssignment(BitVector, boolean)} run.
     */
    private int numberOfSatisfied;

    /**
     * Keeps track of whether the {@link #formula} was satisfied
     * during last {@link #setAssignment(BitVector, boolean)} run.
     */
    private boolean isSatisfied;

    /**
     * Keeps the sum of all corrections.
     */
    private double percentageBonus;

    /**
     * Keeps the correction for each clause.
     */
    private double[] post;

    /**
     * Default constructor that assigns the {@link SATFormula}.
     *
     * @param formula to be assigned.
     */
    public SATFormulaStats(SATFormula formula) {
        this.formula = formula;
        this.post = new double[formula.getNumberOfClauses()];
    }

    /**
     * Updates all parameters for the given {@link BitVector}.
     *
     * @param assignment used to calculate new statistics.
     * @param updatePercentages whether correction percentages will be updated.
     */
    public void setAssignment(BitVector assignment, boolean updatePercentages) {
        int satisfied = 0;

        if (updatePercentages) {
            for (int i = 0, size = formula.getNumberOfClauses(); i < size; i++) {
                if (formula.getClause(i).isSatisfied(assignment)) {
                    satisfied++;
                    post[i] += (1 - post[i]) * PERCENTAGE_CONSTANT_UP;
                } else {
                    post[i] += (0 - post[i]) * PERCENTAGE_CONSTANT_DOWN;
                }
            }
        } else {
            double correction = 0;

            for (int i = 0, size = formula.getNumberOfClauses(); i < size; i++) {
                if (formula.getClause(i).isSatisfied(assignment)) {
                    satisfied++;
                    correction += PERCENTAGE_UNIT_AMOUNT * (1 - post[i]);
                } else {
                    correction -= PERCENTAGE_UNIT_AMOUNT * (1 - post[i]);
                }
            }

            percentageBonus = correction;
        }

        numberOfSatisfied = satisfied;
        isSatisfied = numberOfSatisfied == formula.getNumberOfClauses();
    }

    /**
     * Provides the number of {@link Clause}s satisfied
     * by the last given {@link BitVector}.
     *
     * @return number of {@link Clause}s satisfied by
     * last {@link #setAssignment(BitVector, boolean)} run.
     */
    public int getNumberOfSatisfied() {
        return numberOfSatisfied;
    }

    /**
     * Checks whether the last used {@link BitVector}
     * satisfied the {@link #formula}.
     *
     * @return {@code true} if the {@link #formula}
     * was satisfied, otherwise {@code false}.
     */
    public boolean isSatisfied() {
        return isSatisfied;
    }

    /**
     * Provides the sum of all corrections calculated
     * in {@link #setAssignment(BitVector, boolean)}.
     *
     * @return sum of all corrections.
     */
    public double getPercentageBonus() {
        return percentageBonus;
    }

    /**
     * Provides the correction at the given {@code index}.
     *
     * @param index of the requested correction.
     *
     * @return correction at the given location.
     */
    public double getPercentage(int index) {
        return post[index];
    }
}
