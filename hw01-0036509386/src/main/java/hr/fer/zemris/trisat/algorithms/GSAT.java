package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.SATFormula;

import java.util.List;
import java.util.Random;

/**
 * A class that represents a Multistart Local Search algorithm.
 *
 * @author Mateo Imbrišak
 */

public class GSAT {

    /**
     * Number of times a single iteration is used.
     */
    private static final int MAX_FLIPS = 100_000;

    /**
     * Maximum number of tries the whole algorithm is run.
     */
    private static final int MAX_TRIES = 30;

    /**
     * Don't let anyone instantiate this class.
     */
    private GSAT() {}

    /**
     * Attempts to find a solution for the given {@code formula}.
     *
     * @param formula being checked for a solution.
     *
     * @return a {@link BitVector} representing a solution
     * if it is found, otherwise {@code null}.
     */
    public static BitVector search(SATFormula formula) {
        Random rand = new Random();

        for (int i = 0; i < MAX_TRIES; i++) {
            BitVector current = new BitVector(rand, formula.getNumberOfVariables());

            for (int t = 0; t < MAX_FLIPS; t++) {
                if (formula.isSatisfied(current)) {
                    return current;
                }

                List<BitVector> bestNeighbours = Util.selectBestNeighbours(current, formula);

                current = bestNeighbours.get(rand.nextInt(bestNeighbours.size()));
            }
        }

        return null;
    }
}
