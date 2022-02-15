package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.Clause;
import hr.fer.zemris.trisat.MutableBitVector;
import hr.fer.zemris.trisat.SATFormula;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class that represents a Multistart Local Search algorithm
 * that doesn't utilize deterministic greed.
 *
 * @author Mateo Imbrišak
 */

public class RandomWalkSAT {

    /**
     * Number of times a single iteration is used.
     */
    private static final int MAX_FLIPS = 100_000;

    /**
     * Maximum number of tries the whole algorithm is run.
     */
    private static final int MAX_TRIES = 30;

    /**
     * Probability to switch a bit in an unsatisfied {@link Clause}.
     */
    private static final double FLIP_PROBABILITY = 0.3;

    /**
     * Don't let anyone instantiate this class.
     */
    private RandomWalkSAT() {}

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

                List<Clause> unsatisfied = findUnsatisfiedClauses(current, formula);
                Clause selected = unsatisfied.get(rand.nextInt(unsatisfied.size()));

                if (rand.nextDouble() <= FLIP_PROBABILITY) {
                    current = flipBit(current, selected, rand);
                }

                if (rand.nextDouble() <= 1 - FLIP_PROBABILITY) {
                    List<BitVector> bestNeighbours = Util.selectBestNeighbours(current, formula);
                    current = bestNeighbours.get(rand.nextInt(bestNeighbours.size()));
                }
            }
        }

        return null;
    }

    /**
     * Used internally to find all {@link Clause}s unsatisfied
     * by the given {@code vector}.
     *
     * @param vector used to check the clauses.
     * @param formula containing the clauses.
     *
     * @return a {@link List} of all unsatisfied {@link Clause}s.
     */
    private static List<Clause> findUnsatisfiedClauses(BitVector vector, SATFormula formula) {
        List<Clause> unsatisfied = new ArrayList<>();

        for (int i = 0, size = formula.getNumberOfClauses(); i < size; i++) {
            Clause clause = formula.getClause(i);

            if (!clause.isSatisfied(vector)) {
                unsatisfied.add(clause);
            }
        }

        return unsatisfied;
    }

    /**
     * Used internally to flip a random bit
     * from the given {@code clause}.
     *
     * @param vector whose bit is being flipped.
     * @param clause from which the bit is selected.
     * @param rand used to generate the random position.
     *
     * @return the given {@code vector} with flipped bit.
     */
    private static BitVector flipBit(BitVector vector, Clause clause, Random rand) {
        int flipped = clause.getLiteral(rand.nextInt(clause.getSize()));
        MutableBitVector copy = vector.copy();
        copy.set(Math.abs(flipped) - 1, !copy.get(Math.abs(flipped) - 1));
        return copy;
    }
}
