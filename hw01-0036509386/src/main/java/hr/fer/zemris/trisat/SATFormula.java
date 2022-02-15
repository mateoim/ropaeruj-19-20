package hr.fer.zemris.trisat;

/**
 * A class that represents a formula containing multiple {@link Clause}s.
 *
 * @author Mateo Imbrišak
 */

public class SATFormula {

    /**
     * Keeps the number pf variables in this formula.
     */
    private int numberOfVariables;

    /**
     * Keeps the {@link Clause}s.
     */
    private Clause[] clauses;

    /**
     * Default constructor that assigns all values.
     *
     * @param numberOfVariables used to solve this formula.
     * @param clauses that are used in the formula.
     */
    public SATFormula(int numberOfVariables, Clause[] clauses) {
        this.numberOfVariables = numberOfVariables;
        this.clauses = clauses;
    }

    /**
     * Provides the number of variables used to solve the formula.
     *
     * @return number of variables.
     */
    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    /**
     * Provides the number of {@link Clause}s in the formula.
     *
     * @return number of {@link Clause}s.
     */
    public int getNumberOfClauses() {
        return clauses.length;
    }

    /**
     * Provides the {@link Clause} at the requested {@code index}.
     *
     * @param index of the requested {@link Clause}.
     *
     * @return {@link Clause} at the requested location.
     */
    public Clause getClause(int index) {
        return clauses[index];
    }

    /**
     * Checks whether the given {@link BitVector} satisfies the formula.
     *
     * @param assignment used to check the formula.
     *
     * @return {@code true} if the {@code assignment} satisfies the formula,
     * otherwise {@code false}.
     */
    public boolean isSatisfied(BitVector assignment) {
        for (Clause clause : clauses) {
            if (!clause.isSatisfied(assignment)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Clause clause : clauses) {
            sb.append(clause).append("\n");
        }

        return sb.toString();
    }
}
