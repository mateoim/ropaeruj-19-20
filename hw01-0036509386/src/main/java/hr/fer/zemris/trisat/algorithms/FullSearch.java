package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.SATFormula;

/**
 * A class that represents a simple algorithm
 * that checks all 2^m possible solutions.
 *
 * @author Mateo Imbrišak
 */

public class FullSearch {

    /**
     * Don't let anyone instantiate this class.
     */
    private FullSearch() {}

    /**
     * Attempts to find a solution for the given {@code formula}.
     *
     * @param formula being checked for a solution.
     *
     * @return a {@link BitVector} representing a solution
     * if it is found, otherwise {@code null}.
     */
    public static BitVector search(SATFormula formula) {
        double maxIterations = Math.pow(2, formula.getNumberOfVariables());
        BitVector solution = null;

        for (int index = 0; index < maxIterations; index++) {
            boolean[] bits = new boolean[formula.getNumberOfVariables()];

            for (int j = formula.getNumberOfVariables() - 1; j >= 0; j--) {
                bits[j] = (index & (1 << j)) != 0;
            }

            BitVector vector = new BitVector(bits);

            if (formula.isSatisfied(vector)) {
                solution = vector;
                System.out.println(vector);
            }
        }

        return solution;
    }
}
