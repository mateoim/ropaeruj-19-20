package hr.fer.zemris.optjava.dz5.algorithm;

import hr.fer.zemris.optjava.dz5.crossover.Crossover;
import hr.fer.zemris.optjava.dz5.functions.Function;
import hr.fer.zemris.optjava.dz5.mutators.Mutator;
import hr.fer.zemris.optjava.dz5.selection.Selection;
import hr.fer.zemris.optjava.dz5.solutions.PermutationSolution;
import hr.fer.zemris.optjava.dz5.solutions.Solution;

import java.util.*;

/**
 * A genetic algorithm that uses multiple populations
 * and slowly merges them over time.
 *
 * @author Mateo Imbrišak
 */

public class SASEGASA implements Algorithm<PermutationSolution> {

    /**
     * Maximum number of iterations to perform per population;
     */
    private static final int MAX_ITERATIONS = 1000;

    /**
     * Amount of {@link Solution}s used in each iteration.
     */
    private final int initialPopulationSize;

    /**
     * Number of populations at the start.
     */
    private int numberOfPopulations;

    /**
     * Selection pressure used to stop the algorithm.
     */
    private final double maxSelPress;

    /**
     * Used to calculate {@link Solution} fitness.
     */
    private final Function<PermutationSolution> function;

    /**
     * Used to select parents.
     */
    private final Selection<Solution> selection;

    /**
     * Used to cross the parents.
     */
    private final Crossover<PermutationSolution> crossover;

    /**
     * Used to mutate the child.
     */
    private final Mutator<PermutationSolution> mutator;

    /**
     * Size of solutions used.
     */
    private final int solutionSize;

    /**
     * Default constructor that assigns all values.
     *
     * @param initialPopulationSize total population.
     * @param numberOfPopulations at the start of the algorithm.
     * @param maxSelPress used to stop the algorithm.
     * @param function used to calculate {@link Solution} fitness.
     * @param selection used to select parents.
     * @param crossover used to cross the parents.
     * @param mutator used to mutate the child.
     * @param solutionSize size of solutions used.
     */
    public SASEGASA(int initialPopulationSize, int numberOfPopulations, double maxSelPress,
                    Function<PermutationSolution> function, Selection<Solution> selection,
                    Crossover<PermutationSolution> crossover, Mutator<PermutationSolution> mutator, int solutionSize) {
        this.initialPopulationSize = initialPopulationSize;
        this.numberOfPopulations = numberOfPopulations;
        this.maxSelPress = maxSelPress;
        this.function = function;
        this.selection = selection;
        this.crossover = crossover;
        this.mutator = mutator;
        this.solutionSize = solutionSize;
    }

    @Override
    public PermutationSolution run() {
        Random rand = new Random();

        PermutationSolution[] totalPopulation = new PermutationSolution[initialPopulationSize];

        for (int i = 0; i < initialPopulationSize; i++) {
            totalPopulation[i] = PermutationSolution.randomize(solutionSize, rand);
            function.calculateFitness(totalPopulation[i]);
        }

        while (numberOfPopulations != 0) {
            List<PermutationSolution[]> populationCollection = new ArrayList<>(numberOfPopulations);
            int minPopulationSize = Math.floorDiv(initialPopulationSize, numberOfPopulations);
            int delta = initialPopulationSize - numberOfPopulations * minPopulationSize;

            for (int i = 0, deltaCounter = 0; i < numberOfPopulations; i++) {
                PermutationSolution[] population;

                if (deltaCounter < delta) {
                    population = new PermutationSolution[minPopulationSize + 1];
                    System.arraycopy(totalPopulation, i * minPopulationSize + deltaCounter, population,
                            0, minPopulationSize + 1);
                    deltaCounter++;
                } else {
                    population = new PermutationSolution[minPopulationSize];
                    System.arraycopy(totalPopulation, i * minPopulationSize + deltaCounter, population,
                            0, minPopulationSize);
                }

                populationCollection.add(population);
            }

            for (int i = 0; i < numberOfPopulations; i++) {
                PermutationSolution[] population = populationCollection.get(i);

                double compFactor = 0;
                double compFactorIncrement = 1d / MAX_ITERATIONS;

                int j = 0;
                double actSelPress = 0;
                double successRatio = 0.5;

                while (j < MAX_ITERATIONS && actSelPress < maxSelPress) {
                    Set<PermutationSolution> nextPopulation = new HashSet<>();
                    Set<PermutationSolution> pool = new TreeSet<>();
                    int oldSize = population.length;
                    int generatedChildren = 0;

                    while (nextPopulation.size() < oldSize * successRatio &&
                            (nextPopulation.size() + pool.size()) < oldSize * maxSelPress) {
                        PermutationSolution firstParent = (PermutationSolution) selection.select(population);
                        PermutationSolution secondParent;

                        do {
                            secondParent = (PermutationSolution) selection.select(population);
                        } while (firstParent.equals(secondParent));

                        PermutationSolution child = mutator.mutate(crossover.cross(firstParent, secondParent));
                        function.calculateFitness(child);

                        double worse, better;

                        if (firstParent.getFitness() < secondParent.getFitness()) {
                            worse = firstParent.getFitness();
                            better = secondParent.getFitness();
                        } else {
                            worse = secondParent.getFitness();
                            better = firstParent.getFitness();
                        }

                        double fitnessThreshold = worse + compFactor * (better - worse);

                        if (child.getFitness() >= fitnessThreshold) {
                            nextPopulation.add(child);
                        } else {
                            pool.add(child);
                        }

                        generatedChildren++;
                    }

                    actSelPress = generatedChildren / (double) oldSize;


                    for (PermutationSolution child : pool) {
                        if (nextPopulation.size() >= oldSize) {
                            break;
                        }

                        nextPopulation.add(child);
                    }

                    while (nextPopulation.size() < oldSize) {
                        nextPopulation.add(generateChild(population));
                    }

                    compFactor += compFactorIncrement;
                    j++;

                    System.out.println("Number of populations: " + numberOfPopulations +
                            ", population: " + i + ", iteration: " + j);
                }
            }

            int returnCounter = 0;
            for (PermutationSolution[] population : populationCollection) {
                for (PermutationSolution unit : population) {
                    totalPopulation[returnCounter] = unit;
                    returnCounter++;
                }
            }

            numberOfPopulations--;
        }

        Arrays.sort(totalPopulation, Collections.reverseOrder());

        return totalPopulation[0];
    }

    /**
     * Generates a child using parents from the given {@code population}.
     *
     * @param population from which the parents will be selected.
     *
     * @return a new {@link PermutationSolution}.
     */
    private PermutationSolution generateChild(PermutationSolution[] population) {
        PermutationSolution firstParent = (PermutationSolution) selection.select(population);
        PermutationSolution secondParent;

        do {
            secondParent = (PermutationSolution) selection.select(population);
        } while (firstParent.equals(secondParent));

        PermutationSolution child = mutator.mutate(crossover.cross(firstParent, secondParent));
        function.calculateFitness(child);

        return child;
    }
}
