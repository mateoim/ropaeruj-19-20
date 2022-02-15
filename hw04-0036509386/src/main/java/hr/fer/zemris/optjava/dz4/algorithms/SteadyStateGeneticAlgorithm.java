package hr.fer.zemris.optjava.dz4.algorithms;

import hr.fer.zemris.optjava.dz4.crossover.Crossover;
import hr.fer.zemris.optjava.dz4.mutators.Mutator;
import hr.fer.zemris.optjava.dz4.selection.Selection;
import hr.fer.zemris.optjava.dz4.selection.TournamentSelection;
import hr.fer.zemris.optjava.dz4.solutions.Solution;
import hr.fer.zemris.optjava.dz4.solutions.StickBoxSolution;

import java.util.Arrays;

/**
 * A genetic algorithm that attempts to
 * find optimal box configuration to transport sticks.
 *
 * @author Mateo Imbrišak
 */

public class SteadyStateGeneticAlgorithm {

    /**
     * Population used in iterations.
     */
    private StickBoxSolution[] population;

    /**
     * Error threshold used to stop the algorithm when reached.
     */
    private final int stopThreshold;

    /**
     * Maximum number of iterations to be performed unless {@link #stopThreshold} is reached.
     */
    private final int maximumIterations;

    /**
     * Used to perform selection on the population.
     */
    private final Selection<Solution> selection;

    /**
     * Used to select solution to be replaced.
     */
    private final TournamentSelection worstSelection;

    /**
     * Used to mutate newly created children.
     */
    private final Mutator<StickBoxSolution> mutator;

    /**
     * Used to cross two parents and create a child.
     */
    private final Crossover<StickBoxSolution> crossover;

    /**
     * {@code false} if the generated child unconditionally replaces
     * the loser selected by {@link #worstSelection}, {@code true} if
     * the child replaces it only if the child is better.
     */
    private final boolean p;

    /**
     * Default constructor that assigns all values.
     *
     * @param population used in iterations.
     * @param stopThreshold stops the algorithm if this value is reached.
     * @param maximumIterations to be performed.
     * @param selection used to select parents.
     * @param worstSelection used to select solution to be replaced.
     * @param p determines conditions under which the loser of {@link #worstSelection} is replaced.
     * @param mutator used to mutate new children.
     * @param crossover used to cross tho parents and create a child.
     */
    public SteadyStateGeneticAlgorithm(StickBoxSolution[] population, int stopThreshold, int maximumIterations,
                                       Selection<Solution> selection, TournamentSelection worstSelection, boolean p,
                                       Mutator<StickBoxSolution> mutator, Crossover<StickBoxSolution> crossover) {
        this.population = population;
        this.stopThreshold = stopThreshold;
        this.maximumIterations = maximumIterations;
        this.selection = selection;
        this.worstSelection = worstSelection;
        this.mutator = mutator;
        this.crossover = crossover;
        this.p = p;
    }

    /**
     * Executes the algorithm.
     *
     * @return best found solution.
     */
    public StickBoxSolution run() {
        Arrays.sort(population);

        StickBoxSolution bestSolution = population[0];

        System.out.println("Found new best solution. Length: " + bestSolution.getFitness());
        int i = 0;

        while (stopThreshold <= bestSolution.getFitness() && i < maximumIterations) {
            StickBoxSolution firstParent = (StickBoxSolution) selection.select(population);
            StickBoxSolution secondParent;

            do {
                secondParent = (StickBoxSolution) selection.select(population);
            } while (firstParent.equals(secondParent));

            StickBoxSolution child = mutator.mutate(crossover.cross(firstParent, secondParent));

            StickBoxSolution loser = (StickBoxSolution) worstSelection.select(population);

            if (!p) {
                swap(population, child, loser);
            } else {
                if (child.getFitness() >= loser.getFitness()) {
                    swap(population, child, loser);
                }
            }

            if (child.getFitness() < bestSolution.getFitness()) {
                bestSolution = child;
                System.out.println("Found new best solution. Length: " + bestSolution.getFitness());
            }

            Arrays.sort(population);
            i++;
        }

        return bestSolution;
    }

    /**
     * Swaps the {@code loser} with {@code winner} in the given {@code population}.
     *
     * @param population where {@code winner} will be inserted.
     * @param winner being inserted.
     * @param loser being replaced.
     */
    private void swap(StickBoxSolution[] population, StickBoxSolution winner, StickBoxSolution loser) {
        for (int i = 0, size = population.length; i < size; i++) {
            if (population[i].equals(loser)) {
                population[i] = winner;
                return;
            }
        }
    }
}
