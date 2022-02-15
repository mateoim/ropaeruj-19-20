package hr.fer.zemris.optjava.dz4.selection;

import hr.fer.zemris.optjava.dz4.solutions.Solution;

import java.util.Random;

/**
 * An implementation of {@link Selection} that
 * selects the best solution from {@code n} randomly
 * picked solutions.
 *
 * @author Mateo Imbrišak
 */

public class TournamentSelection implements Selection<Solution> {

    /**
     * Amount of solutions used in each selection.
     */
    private final int n;

    /**
     * Used to randomly select solutions.
     */
    private final Random rand;

    /**
     * Default constructor that assigns {@link #n} number
     * of solutions used in each selection.
     *
     * @param n number of solutions used in each selection.
     */
    public TournamentSelection(int n) {
        this.n = n;
        this.rand = new Random();
    }

    @Override
    public Solution select(Solution[] population) {
        return select(population, true);
    }

    /**
     * Selects the worst out of {@link #n} {@link Solution}s.
     *
     * @param population from which the {@link Solution} will be selected.
     *
     * @return worst {@link Solution} from the given {@code population}.
     */
    public Solution selectWorst(Solution[] population) {
        return select(population, false);
    }

    /**
     * Used internally to select best or worst {@link Solution}
     * from the given {@code population}.
     *
     * @param population from which the {@link Solution} will be selected.
     * @param best whether the selected solution is best or worst.
     *
     * @return best or worst {@link Solution} based on {@code best}.
     */
    private Solution select(Solution[] population, final boolean best) {
        int size = population.length;

        Solution bestSolution = population[rand.nextInt(size - 1)];

        for (int i = 0; i < n - 1; i++) {
            Solution selectedSolution = population[rand.nextInt(size - 1)];

            if (best && selectedSolution.getFitness() >= bestSolution.getFitness()) {
                bestSolution = selectedSolution;
            } else if (!best && selectedSolution.getFitness() <= bestSolution.getFitness()) {
                bestSolution = selectedSolution;
            }
        }

        return bestSolution;
    }
}
