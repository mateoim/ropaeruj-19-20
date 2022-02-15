package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.BitVectorNGenerator;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.SATFormulaStats;

import java.util.*;

/**
 * A class that represents an Iterated Local Search algorithm
 * that determines next iteration based on weighted scores.
 *
 * @author Mateo Imbrišak
 */

public class WeightedIteratedSearch {

    /**
     * Maximum number of iterations performed by the algorithm.
     */
    private static final int MAX_ITERATIONS = 100_000;

    /**
     * Number of best neighbours used to select next iteration.
     */
    private static final int NUMBER_OF_BEST = 2;

    /**
     * Don't let anyone instantiate this class.
     */
    private WeightedIteratedSearch() {}

    /**
     * Attempts to find a solution for the given {@code formula}.
     *
     * @param formula being checked for a solution.
     *
     * @return a {@link BitVector} representing a solution
     * if it is found, otherwise {@code null}.
     */
    public static BitVector search(SATFormula formula) {
        SATFormulaStats stats = new SATFormulaStats(formula);
        Random rand = new Random();

        BitVector current = new BitVector(rand, formula.getNumberOfVariables());

        int t = 0;

        while (t < MAX_ITERATIONS && !formula.isSatisfied(current)) {
            stats.setAssignment(current, true);
            List<BitVector> bestNeighbours = selectBestNeighbours(current, stats);

            current = bestNeighbours.get(rand.nextInt(bestNeighbours.size()));

            t++;
        }

        return current;
    }

    /**
     * Used internally to find {@link #NUMBER_OF_BEST} best neighbours.
     *
     * @param vector whose neighbours are being selected.
     * @param stats used to calculate weighted scores.
     *
     * @return a sorted {@link List} of {@link #NUMBER_OF_BEST} best neighbours.
     */
    private static List<BitVector> selectBestNeighbours(BitVector vector, SATFormulaStats stats) {
        Map<BitVector, Double> neighbours = new HashMap<>();

        for (BitVector current : new BitVectorNGenerator(vector)) {
            stats.setAssignment(current, false);
            double value = stats.getNumberOfSatisfied() + stats.getPercentageBonus();

            neighbours.put(current, value);
        }

        List<BitVector> neighbourList = new ArrayList<>(vector.getSize());

        neighbours.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach((current) -> neighbourList.add(current.getKey()));

        return neighbourList.subList(0, Math.min(NUMBER_OF_BEST, neighbourList.size()));
    }
}
