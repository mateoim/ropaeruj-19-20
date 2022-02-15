package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.SATFormula;

import java.util.List;
import java.util.Random;

/**
 * A class that represents Iterated Local Search algorithm.
 *
 * @author Mateo Imbrišak
 */

public class IteratedSearch {

    /**
     * Maximum number of times the algorithm is performed.
     */
    private static final int MAX_ITERATIONS = 100_000;

    /**
     * Don't let anyone instantiate this class.
     */
    private IteratedSearch() {}

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

        while (t < MAX_ITERATIONS) {
            List<BitVector> bestNeighbours = Util.selectBestNeighbours(current, formula);

            current = bestNeighbours.get(rand.nextInt(bestNeighbours.size()));

            if (Util.fit(current, formula) <= Util.fit(solution, formula)) {
                return solution;
            } else {
                solution = current;
            }

            t++;
        }

        return solution;
    }
}
