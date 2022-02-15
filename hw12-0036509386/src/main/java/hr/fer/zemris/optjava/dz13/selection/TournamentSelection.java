package hr.fer.zemris.optjava.dz13.selection;

import hr.fer.zemris.optjava.dz13.actions.AntAction;

import java.util.Random;

/**
 * Used to select a solution. Selects the best solution
 * out of {@link #n} randomly picked ones.
 *
 * @author Mateo Imbrišak
 */

public class TournamentSelection {

    /**
     * Number of selections to perform.
     */
    private final int n;

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns number of
     * selections and random value generator.
     *
     * @param n number of selections to perform every time.
     * @param rand used to generate random values.
     */
    public TournamentSelection(int n, Random rand) {
        this.n = n;
        this.rand = rand;
    }

    /**
     * Selects a solution from the given population.
     *
     * @param population from which the solution will be selected.
     *
     * @return best solution out of {@link #n} selected.
     */
    public AntAction select(AntAction[] population) {
        int size = population.length;

        AntAction winner = population[rand.nextInt(size)];

        for (int i = 1; i < n; i++) {
            AntAction selected = population[rand.nextInt(size)];

            if (selected.getFitness() >= winner.getFitness()) {
                winner = selected;
            }
        }

        return winner;
    }
}
