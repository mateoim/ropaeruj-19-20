package hr.fer.zemris.optjava.dz5.algorithm;

import hr.fer.zemris.optjava.dz5.crossover.Crossover;
import hr.fer.zemris.optjava.dz5.functions.Function;
import hr.fer.zemris.optjava.dz5.mutators.Mutator;
import hr.fer.zemris.optjava.dz5.selection.Selection;
import hr.fer.zemris.optjava.dz5.solutions.BitVectorSolution;
import hr.fer.zemris.optjava.dz5.solutions.Solution;

import java.util.*;

/**
 * A genetic algorithm that generates children as long as they are
 * better than at least one of their parents and uses a population
 * of variable size.
 *
 * @author Mateo Imbrišak
 */

public class RAPGA implements Algorithm<BitVectorSolution> {

    /**
     * Maximum number of iterations to perform.
     */
    private static final int MAX_ITERATIONS = 1000;

    /**
     * Minimum population under which the algorithm stops.
     */
    private final int minPopulationSize;

    /**
     * Maximum population that can be used in an iteration.
     */
    private final int maxPopulationSize;

    /**
     * Selection pressure used to stop the algorithm.
     */
    private final double maxSelPress;

    /**
     * Used to calculate {@link Solution} fitness.
     */
    private final Function<BitVectorSolution> function;

    /**
     * Used to select first parent.
     */
    private final Selection<Solution> firstSelection;

    /**
     * Used to select second parent.
     */
    private final Selection<Solution> secondSelection;

    /**
     * Used to cross the parents.
     */
    private final Crossover<BitVectorSolution> crossover;

    /**
     * Used to mutate the child.
     */
    private final Mutator<BitVectorSolution> mutator;

    /**
     * Size of solutions used.
     */
    private final int n;

    /**
     * Maximum number of children to generate per iteration.
     */
    private final int maxChildrenToGenerate;

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns all values.
     *
     * @param minPopulationSize population under which the algorithm stops.
     * @param maxPopulationSize that can be used in an iteration.
     * @param maxSelPress used to stop the algorithm if passed.
     * @param function used to calculate fitness.
     * @param firstSelection used to select first parent.
     * @param secondSelection used to select second parent.
     * @param crossover used to cross the parents.
     * @param mutator used to mutate the child.
     * @param n size of solutions used.
     * @param maxChildrenToGenerate per iteration.
     * @param rand used to generate random values.
     */
    public RAPGA(int minPopulationSize, int maxPopulationSize, double maxSelPress,
                 Function<BitVectorSolution> function, Selection<Solution> firstSelection,
                 Selection<Solution> secondSelection, Crossover<BitVectorSolution> crossover,
                 Mutator<BitVectorSolution> mutator, int n, int maxChildrenToGenerate, Random rand) {
        this.minPopulationSize = minPopulationSize;
        this.maxPopulationSize = maxPopulationSize;
        this.maxSelPress = maxSelPress;
        this.function = function;
        this.firstSelection = firstSelection;
        this.secondSelection = secondSelection;
        this.crossover = crossover;
        this.mutator = mutator;
        this.n = n;
        this.maxChildrenToGenerate = maxChildrenToGenerate;
        this.rand = rand;
    }

    @Override
    public BitVectorSolution run() {
        double compFactor = 0;
        double compFactorIncrement = 1d / MAX_ITERATIONS;

        int initialSize = (int) Math.round(minPopulationSize
                + rand.nextDouble() * (maxPopulationSize - minPopulationSize));
        BitVectorSolution[] population = new BitVectorSolution[initialSize];

        for (int i = 0; i < initialSize; i++) {
            population[i] = BitVectorSolution.random(n, rand);
            function.calculateFitness(population[i]);
        }

        int i = 0;
        double actSelPress = 0;
        while (i < MAX_ITERATIONS && actSelPress < maxSelPress) {
            Set<BitVectorSolution> nextPopulation = new HashSet<>();
            int oldSize = population.length;
            int generatedChildren = 0;

            while (nextPopulation.size() < maxPopulationSize && generatedChildren < maxChildrenToGenerate) {
                BitVectorSolution firstParent = (BitVectorSolution) firstSelection.select(population);
                BitVectorSolution secondParent;

                do {
                    secondParent = (BitVectorSolution) secondSelection.select(population);
                } while (firstParent.equals(secondParent));

                BitVectorSolution child = mutator.mutate(crossover.cross(firstParent, secondParent));
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
                }

                generatedChildren++;
            }

            int nextSize = nextPopulation.size();
            if (nextSize < minPopulationSize) {
                System.out.println("Cannot find better children.");
                break;
            }

            population = new BitVectorSolution[nextSize];
            int j = 0;
            for (BitVectorSolution current : nextPopulation) {
                population[j++] = current;
            }

            actSelPress = (double) generatedChildren / oldSize;
            compFactor += compFactorIncrement;
            i++;

            Arrays.sort(population, Collections.reverseOrder());

            System.out.println("Population " + i + ", actSelPres: " + actSelPress + ", size: "
                    + nextSize + ", best fitness: " + population[0].getFitness());
        }

        return population[0];
    }
}
