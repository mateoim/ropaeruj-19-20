package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.MutableBitVector;
import hr.fer.zemris.trisat.SATFormula;

import java.util.List;
import java.util.Random;

/**
 * A class that represents Iterated Local Search algorithm
 * that mutates a percentage of bits when it gets stuck in local
 * optimum.
 *
 * @author Mateo Imbrišak
 */

public class IteratedLocalSearch {

    /**
     * Maximum number of times the algorithm is performed.
     */
    private static final int MAX_FLIPS = 100_000;

    /**
     * Percentage of bits mutated when stuck.
     */
    private static final double CHANGE = 0.3;

    /**
     * Don't let anyone instantiate this class.
     */
    private IteratedLocalSearch() {}

    /**
     * Attempts to find a solution for the given {@code formula}.
     *
     * @param formula being checked for a solution.
     *
     * @return a {@link BitVector} representing best found solution.
     */
    public static BitVector search(SATFormula formula) {
        Random rand = new Random();

        BitVector current = new BitVector(rand, formula.getNumberOfVariables());
        BitVector solution = current;

        int t = 0;

        while (t < MAX_FLIPS) {
            if (formula.isSatisfied(solution)) {
                return solution;
            }

            List<BitVector> bestNeighbours = Util.selectBestNeighbours(current, formula);

            current = bestNeighbours.get(rand.nextInt(bestNeighbours.size()));

            if (Util.fit(current, formula) <= Util.fit(solution, formula)) {
                int bitsToChange = (int) Math.round(formula.getNumberOfVariables() * CHANGE);
                MutableBitVector copy = current.copy();

                for (int i = 0; i < bitsToChange; i++) {
                    int bit = rand.nextInt(current.getSize());
                    copy.set(bit, !copy.get(bit));
                }

                current = copy;
            }

            solution = current;
            t++;
        }

        return solution;
    }
}
