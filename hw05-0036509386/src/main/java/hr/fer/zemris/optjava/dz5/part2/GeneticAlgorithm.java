package hr.fer.zemris.optjava.dz5.part2;

import hr.fer.zemris.optjava.dz5.algorithm.Algorithm;
import hr.fer.zemris.optjava.dz5.algorithm.SASEGASA;
import hr.fer.zemris.optjava.dz5.crossover.Crossover;
import hr.fer.zemris.optjava.dz5.crossover.PartiallyMappedCrossover;
import hr.fer.zemris.optjava.dz5.functions.Function;
import hr.fer.zemris.optjava.dz5.functions.QAPFunction;
import hr.fer.zemris.optjava.dz5.mutators.Mutator;
import hr.fer.zemris.optjava.dz5.mutators.SwapMutator;
import hr.fer.zemris.optjava.dz5.selection.Selection;
import hr.fer.zemris.optjava.dz5.selection.TournamentSelection;
import hr.fer.zemris.optjava.dz5.solutions.PermutationSolution;
import hr.fer.zemris.optjava.dz5.solutions.Solution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

/**
 * A program that attempts to find the best solution for {@link QAPFunction}.
 *
 * @author Mateo Imbrišak
 */

public class GeneticAlgorithm {

    /**
     * Maximum allowed selection pressure for {@link SASEGASA}.
     */
    private static final double MAXIMUM_SELECTION_PRESSURE = 25;

    /**
     * Don't let anyone instantiate this class.
     */
    private GeneticAlgorithm() {}

    /**
     * Used to start the program.
     *
     * @param args exactly 3 arguments
     *             path to the file containing the problem configuration
     *             number of solutions in total population
     *             number of populations at start
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Program takes exactly 3 arguments.");
            return;
        }

        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(args[0]));
        } catch (IOException exc) {
            System.out.println("Error while reading the file.");
            return;
        }

        int dimension = Integer.parseInt(lines.get(0).strip());

        int[][] dimensions = new int[dimension][dimension];
        int[][] resources = new int[dimension][dimension];

        lines.remove(0);
        lines.remove(0);

        int i = 0, j = 0;

        int[][] matrixToFill = dimensions;

        for (String line : lines) {
            if (line.isBlank()) {
                matrixToFill = resources;
                i = 0;
                continue;
            }

            line = line.strip();

            String[] parts = line.split("\\s+");

            for (String part : parts) {
                matrixToFill[i][j] = Integer.parseInt(part.strip());
                j++;

                if (j == dimension) {
                    i++;
                    j = 0;
                }
            }
        }

        Random rand = new Random();

        Function<PermutationSolution> function = new QAPFunction(dimensions, resources);

        int totalPopulation = Integer.parseInt(args[1]);
        int initialNumberOfPopulations = Integer.parseInt(args[2]);

        Selection<Solution> selection = new TournamentSelection(3);
        Crossover<PermutationSolution> crossover = new PartiallyMappedCrossover(rand);
        Mutator<PermutationSolution> mutator = new SwapMutator(rand);

        Algorithm<PermutationSolution> algorithm = new SASEGASA(totalPopulation, initialNumberOfPopulations,
                MAXIMUM_SELECTION_PRESSURE, function, selection, crossover, mutator, dimension);

        PermutationSolution solution = algorithm.run();

        System.out.println("Best solution: " + solution + ", with cost: " + (-1 * solution.getFitness()));
    }
}
