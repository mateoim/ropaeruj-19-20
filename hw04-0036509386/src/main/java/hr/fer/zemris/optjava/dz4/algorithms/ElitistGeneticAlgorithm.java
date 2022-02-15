package hr.fer.zemris.optjava.dz4.algorithms;

import hr.fer.zemris.optjava.dz4.crossover.Crossover;
import hr.fer.zemris.optjava.dz4.functions.Function;
import hr.fer.zemris.optjava.dz4.mutators.Mutator;
import hr.fer.zemris.optjava.dz4.selection.Selection;
import hr.fer.zemris.optjava.dz4.solutions.DoubleArraySolution;
import hr.fer.zemris.optjava.dz4.solutions.Solution;

import java.util.Arrays;
import java.util.Collections;

/**
 * An elitist genetic algorithm that attempts to minimise
 * the given {@link Function}.
 *
 * @author Mateo Imbrišak
 */

public class ElitistGeneticAlgorithm {

    /**
     * Amount of values in array used when generating solutions.
     */
    private static final int SOLUTION_SIZE = 6;

    /**
     * Maximum value used when generating random solutions.
     */
    private static final int MAXIMUM_SOLUTION = 10;

    /**
     * Minimum value used when generating random solutions.
     */
    private static final int MINIMUM_SOLUTION = -10;

    /**
     * Amount of best solutions to be included in the next population.
     */
    private static final int BEST_TO_KEEP = 2;

    /**
     * Number of solutions used in each population.
     */
    private final int populationSize;

    /**
     * Error threshold used to stop the algorithm when reached.
     */
    private final double stopThreshold;

    /**
     * Maximum number of iterations to be performed unless {@link #stopThreshold} is reached.
     */
    private final int maximumIterations;

    /**
     * Used to perform selection on the population.
     */
    private final Selection<Solution> selection;

    /**
     * Used to mutate newly created children.
     */
    private final Mutator<DoubleArraySolution> mutator;

    /**
     * Used to cross two parents and create a child.
     */
    private final Crossover<DoubleArraySolution> crossover;

    /**
     * Used to calculate a solution's fitness.
     */
    private final Function function;

    /**
     * Default constructor that assigns all values.
     *
     * @param populationSize used to create new population every iteration.
     * @param stopThreshold stops the algorithm if this value is reached.
     * @param maximumIterations to be performed.
     * @param selection used to select parents.
     * @param mutator used to mutate new children.
     * @param crossover used to cross tho parents and create a child.
     * @param function used to calculate the fitness of every solution.
     */
    public ElitistGeneticAlgorithm(int populationSize, double stopThreshold, int maximumIterations,
                                   Selection<Solution> selection, Mutator<DoubleArraySolution> mutator,
                                   Crossover<DoubleArraySolution> crossover, Function function) {
        this.populationSize = populationSize;
        this.stopThreshold = stopThreshold;
        this.maximumIterations = maximumIterations;
        this.selection = selection;
        this.mutator = mutator;
        this.crossover = crossover;
        this.function = function;
    }

    /**
     * Performs the algorithm.
     *
     * @return best found solution.
     */
    public DoubleArraySolution run() {
        DoubleArraySolution[] population = new DoubleArraySolution[populationSize];

        for (int i = 0; i < populationSize; i++) {
            population[i] = new DoubleArraySolution(SOLUTION_SIZE, MINIMUM_SOLUTION, MAXIMUM_SOLUTION);
            function.calculateFitness(population[i]);
        }

        Arrays.sort(population, Collections.reverseOrder());
        int i = 0;

        printBest(population, i);

        while (stopThreshold > population[0].getFitness() && i < maximumIterations) {
            DoubleArraySolution[] newPopulation = new DoubleArraySolution[populationSize];

            if (BEST_TO_KEEP < populationSize) {
                System.arraycopy(population, 0, newPopulation, 0, BEST_TO_KEEP);
            } else {
                throw new IllegalArgumentException("Number of best kept cannot be greater than population size.");
            }

            int currentSize = BEST_TO_KEEP;

            while (currentSize < populationSize) {
                DoubleArraySolution firstParent = (DoubleArraySolution) selection.select(population);
                DoubleArraySolution secondParent;

                do {
                    secondParent = (DoubleArraySolution) selection.select(population);
                } while (firstParent.equals(secondParent));

                DoubleArraySolution child = mutator.mutate(crossover.cross(firstParent, secondParent));

                newPopulation[currentSize] = child;
                currentSize++;
            }

            population = newPopulation;
            evaluate(population);
            Arrays.sort(population, Collections.reverseOrder());
            i++;

            printBest(population, i);
        }

        return population[0];
    }

    /**
     * Evaluates the given {@code population} by calling
     * {@link Function#calculateFitness(DoubleArraySolution)}
     * on each solution.
     *
     * @param population being evaluated.
     */
    private void evaluate(DoubleArraySolution[] population) {
        for (int i = 0; i < populationSize; i++) {
            function.calculateFitness(population[i]);
        }
    }

    /**
     * Prints the best solution from the given {@code population},
     * ti's value and the number of iteration {@code index}.
     *
     * @param population whose best solution will be printed.
     * @param index represents the number of iteration.
     */
    private void printBest(DoubleArraySolution[] population, int index) {
        System.out.println("Iteration " + index + " best solution: "
                + population[0] + " with error: " + (-1 * population[0].getFitness()));
    }
}
