package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.BitVectorNGenerator;
import hr.fer.zemris.trisat.SATFormula;

import java.util.ArrayList;
import java.util.List;

/**
 * A class containing utilities for working
 * with {@link BitVector}s across algorithms.
 *
 * @author Mateo Imbrišak
 */

public class Util {

    /**
     * Don't let anyone instantiate this class.
     */
    private Util() {}

    /**
     * Calculates the number of {@link hr.fer.zemris.trisat.Clause}s
     * satisfied by the given {@link BitVector} for the given {@link SATFormula}.
     *
     * @param vector whose fitness function is being calculated.
     * @param formula used to calculate the fitness function.
     *
     * @return number of {@link hr.fer.zemris.trisat.Clause}s
     * satisfied by the given {@link BitVector}.
     */
    public static int fit(BitVector vector, SATFormula formula) {
        int fit = 0;

        for (int i = 0, size = formula.getNumberOfClauses(); i < size; i++) {
            if (formula.getClause(i).isSatisfied(vector)) {
                fit++;
            }
        }

        return fit;
    }

    /**
     * Finds the {@link BitVector}s that satisfy the most {@link hr.fer.zemris.trisat.Clause}s.
     *
     * @param vector whose neighbours are used to check the {@code formula}.
     * @param formula being checked.
     *
     * @return a {@link List} of neighbours that satisfy the most {@link hr.fer.zemris.trisat.Clause}s.
     */
    public static List<BitVector> selectBestNeighbours(BitVector vector, SATFormula formula) {
        int maxFit = 0;
        List<BitVector> bestNeighbours = new ArrayList<>();

        for (BitVector current : new BitVectorNGenerator(vector).createNeighborhood()) {
            int currentFit = Util.fit(current, formula);

            if (currentFit > maxFit) {
                maxFit = currentFit;
                bestNeighbours.clear();
                bestNeighbours.add(current);
            } else if (maxFit == currentFit) {
                bestNeighbours.add(current);
            }
        }

        return bestNeighbours;
    }
}
