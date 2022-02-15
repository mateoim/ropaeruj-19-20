package hr.fer.zemris.optjava.dz4.part2;

import hr.fer.zemris.optjava.dz4.algorithms.SteadyStateGeneticAlgorithm;
import hr.fer.zemris.optjava.dz4.crossover.StickBoxCrossover;
import hr.fer.zemris.optjava.dz4.mutators.StickBoxMutator;
import hr.fer.zemris.optjava.dz4.selection.TournamentSelection;
import hr.fer.zemris.optjava.dz4.solutions.StickBoxSolution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A program that attempts to find optimal box configuration
 * of a box to transport 1-dimensional sticks.
 *
 * @author Mateo Imbrišak
 */

public class BoxFilling {

    /**
     * Don't let anyone instantiate this class.
     */
    private BoxFilling() {}

    /**
     * Used to start the program.
     *
     * @param args exactly 8 arguments.
     *             path to the file containing stick sizes
     *             population size
     *             number of participants in tournament used to select parents
     *             number of participants in tournament used to select solution to be replaced
     *             whether the loser is replaced unconditionally or only if the child is better
     *             maximum number of iterations
     *             acceptable length at which the algorithm stops
     *             percentage of sticks to move when mutating solutions
     */
    public static void main(String[] args) {
        if (args.length != 8) {
            System.out.println("Program takes exactly 8 parameters.");
            return;
        }

        List<Integer> input = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(args[0]));

            String[] parts = lines.get(0).replace("[", "")
                    .replace("]", "").split(", ");

            for (String number : parts) {
                input.add(Integer.parseInt(number));
            }
        } catch (IOException exc) {
            System.err.println("Error while opening file.");
            return;
        }

        int populationSize = Integer.parseInt(args[1]);
        int n = Integer.parseInt(args[2]);
        int m = Integer.parseInt(args[3]);
        boolean p = Boolean.parseBoolean(args[4]);
        int maxIter = Integer.parseInt(args[5]);
        int acceptableLength = Integer.parseInt(args[6]);
        double mutationFactor = Double.parseDouble(args[7]);

        if (n < 2) {
            System.err.println("Parameter 3 must be at least 2.");
            return;
        } else if (m < 2) {
            System.err.println("Parameter 4 must be at least 2.");
            return;
        }

        StickBoxSolution[] population = new StickBoxSolution[populationSize];
        Random rand = new Random();

        for (int i = 0; i < populationSize; i++) {
            population[i] = generateSolution(input, rand);
        }

        SteadyStateGeneticAlgorithm algorithm = new SteadyStateGeneticAlgorithm(population, acceptableLength,
                maxIter, new TournamentSelection(n), new TournamentSelection(m), p, new StickBoxMutator(mutationFactor),
                new StickBoxCrossover());
        StickBoxSolution solution = algorithm.run();

        System.out.println("Solution:\n" + solution + "\nLength:" + (int) solution.getFitness());
    }

    /**
     * Used internally to generate a random {@link StickBoxSolution}.
     *
     * @param numbers a {@link List} containing sticks to be used.
     * @param rand used to generate random values.
     *
     * @return new randomly generated {@link StickBoxSolution}.
     */
    private static StickBoxSolution generateSolution(List<Integer> numbers, Random rand) {
        List<Integer> copy = new ArrayList<>(numbers);
        int[] values = new int[numbers.size()];

        for (int i = 0, size = numbers.size(); i < size - 1; i++) {
            int number = copy.remove(rand.nextInt(copy.size() - 1));
            values[i] = number;
        }

        values[numbers.size() - 1] = copy.remove(0);

        return new StickBoxSolution(values);
    }
}
