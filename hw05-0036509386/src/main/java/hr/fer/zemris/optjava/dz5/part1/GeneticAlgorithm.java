package hr.fer.zemris.optjava.dz5.part1;

import hr.fer.zemris.optjava.dz5.algorithm.Algorithm;
import hr.fer.zemris.optjava.dz5.algorithm.RAPGA;
import hr.fer.zemris.optjava.dz5.crossover.Crossover;
import hr.fer.zemris.optjava.dz5.crossover.OrCrossover;
import hr.fer.zemris.optjava.dz5.functions.Function;
import hr.fer.zemris.optjava.dz5.functions.MaxOnesFunction;
import hr.fer.zemris.optjava.dz5.mutators.FlipMutator;
import hr.fer.zemris.optjava.dz5.mutators.Mutator;
import hr.fer.zemris.optjava.dz5.selection.RandomSelection;
import hr.fer.zemris.optjava.dz5.selection.Selection;
import hr.fer.zemris.optjava.dz5.selection.TournamentSelection;
import hr.fer.zemris.optjava.dz5.solutions.BitVectorSolution;
import hr.fer.zemris.optjava.dz5.solutions.Solution;

import java.util.Random;

/**
 * A program that attempts to find a {@link BitVectorSolution}
 * that has most values {@code true} by using {@link MaxOnesFunction}
 * to calculate {@link BitVectorSolution#getFitness()}.
 *
 * @author Mateo Imbrišak
 */

public class GeneticAlgorithm {

    /**
     * Minimum allowed population for {@link RAPGA}.
     */
    private static final int MINIMUM_POPULATION_SIZE = 2;

    /**
     * Maximum allowed population for {@link RAPGA}.
     */
    private static final int MAXIMUM_POPULATION_SIZE = 1000;

    /**
     * Maximum allowed selection pressure for {@link RAPGA}.
     */
    private static final double MAXIMUM_SELECTION_PRESSURE = 500;

    /**
     * Maximum number of children that can be generated by {@link RAPGA}.
     */
    private static final int MAXIMUM_CHILDREN_TO_GENERATE = 2500;

    /**
     * Defines the number of participants in {@link TournamentSelection}.
     */
    private static final int K = 5;

    /**
     * Don't let anyone instantiate this class.
     */
    private GeneticAlgorithm() {}

    /**
     * Used to start the program.
     *
     * @param args single argument
     *             number of bits used in {@link BitVectorSolution}s.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Program takes exactly one parameter.");
            return;
        }

        int n = Integer.parseInt(args[0]);

        Random rand = new Random();
        Function<BitVectorSolution> function = new MaxOnesFunction();
        Selection<Solution> firstSelection = new TournamentSelection(K);
//        Selection<Solution> secondSelection = new TournamentSelection(K);
        Selection<Solution> secondSelection = new RandomSelection(rand);
//        Crossover<BitVectorSolution> crossover = new SinglePointCrossover(rand);
        Crossover<BitVectorSolution> crossover = new OrCrossover();
        Mutator<BitVectorSolution> mutator = new FlipMutator(rand);

        Algorithm<BitVectorSolution> algorithm = new RAPGA(MINIMUM_POPULATION_SIZE, MAXIMUM_POPULATION_SIZE,
                MAXIMUM_SELECTION_PRESSURE, function, firstSelection, secondSelection,
                crossover, mutator, n, MAXIMUM_CHILDREN_TO_GENERATE, rand);

        BitVectorSolution solution = algorithm.run();

        System.out.println("Best found solution with fitness: " + solution.getFitness());
    }
}
