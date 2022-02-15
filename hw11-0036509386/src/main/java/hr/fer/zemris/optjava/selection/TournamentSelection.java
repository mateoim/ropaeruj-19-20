package hr.fer.zemris.optjava.selection;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.List;

/**
 * An implementation of {@link Selection} that
 * selects the best solution from {@code n} randomly
 * picked solutions.
 *
 * @author Mateo Imbrišak
 */

public class TournamentSelection implements Selection<GASolution<int[]>> {

    /**
     * Amount of solutions used in each selection.
     */
    private final int n;

    /**
     * Default constructor that assigns {@link #n} number
     * of solutions used in each selection.
     *
     * @param n number of solutions used in each selection.
     */
    public TournamentSelection(int n) {
        this.n = n;
    }

    @Override
    public GASolution<int[]> select(List<GASolution<int[]>> population) {
        int size = population.size();
        IRNG rand = RNG.getRNG();

        int toGet = rand.nextInt(0, size - 1);

        GASolution<int[]> bestSolution = population.get(toGet);

        for (int i = 0; i < n - 1; i++) {
            GASolution<int[]> selectedSolution = population.get(rand.nextInt(0, size - 1));

            if (selectedSolution.fitness >= bestSolution.fitness) {
                bestSolution = selectedSolution;
            }
        }

        return bestSolution;
    }
}
