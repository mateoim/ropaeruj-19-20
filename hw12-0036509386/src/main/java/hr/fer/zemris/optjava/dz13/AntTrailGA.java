package hr.fer.zemris.optjava.dz13;

import hr.fer.zemris.optjava.dz13.actions.AntAction;
import hr.fer.zemris.optjava.dz13.algorithms.GeneticAlgorithm;
import hr.fer.zemris.optjava.dz13.crossover.Crossover;
import hr.fer.zemris.optjava.dz13.gui.AntTrailBoard;
import hr.fer.zemris.optjava.dz13.mutator.Mutator;
import hr.fer.zemris.optjava.dz13.selection.TournamentSelection;
import hr.fer.zemris.optjava.dz13.solutions.Ant;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

/**
 * A program that attempts to find solution to Ant Trail problem.
 *
 * @author Mateo Imbrišak
 */

public class AntTrailGA {

    /**
     * Recommended number of iterations.
     */
    private static final int NUMBER_OF_ITERATIONS = 100;

    /**
     * Recommended population size.
     */
    private static final int POPULATION_SIZE = 500;

    /**
     * Used to initialize {@link TournamentSelection}.
     */
    private static final int TOURNAMENT_SELECTION = 7;

    /**
     * Don't let anyone instantiate this class.
     */
    private AntTrailGA() {}

    /**
     * Used to start the program.
     *
     * @param args exactly five arguments
     *             path to the file with a map
     *             maximum number of iterations
     *             population size
     *             error threshold used to stop the algorithm.
     *             path to the file where best solution will be written
     */
    public static void main(String[] args) {
        if (args.length != 5) {
            System.err.println("Program takes exactly 5 arguments.");
            return;
        }

        List<String> lines;

        try {
            lines = Files.readAllLines(Path.of(args[0]));
        } catch (IOException exc) {
            System.err.println("Error while reading input file.");
            return;
        }

        boolean[][] map = parseMap(lines);

        Ant ant = new Ant(map);

        int maxIter = Integer.parseInt(args[1]);
        int populationSize = Integer.parseInt(args[2]);
        double error = Double.parseDouble(args[3]);

        Random rand = new Random();
        Mutator mutator = new Mutator(rand);
        TournamentSelection selection = new TournamentSelection(TOURNAMENT_SELECTION, rand);
        Crossover crossover = new Crossover(rand);

        GeneticAlgorithm algorithm = new GeneticAlgorithm(ant, populationSize, maxIter, error,
                mutator, selection, crossover, rand);

        AntAction solution = algorithm.run();

        ant.run(solution);

        SwingUtilities.invokeLater(() -> new AntTrailBoard(ant));

        try {
            Files.write(Path.of(args[4]), solution.toString().getBytes());
        } catch (IOException e) {
            System.err.println("Error writing best solution.");
        }
    }

    /**
     * Parses the {@code lines} into a map of the given world.
     *
     * @param lines from the file containing world configuration.
     *
     * @return a matrix representing the map of the given world.
     */
    private static boolean[][] parseMap(List<String> lines) {
        String[] size = lines.get(0).split("x");
        boolean[][] map = new boolean[Integer.parseInt(size[0])][Integer.parseInt(size[1])];

        lines.remove(0);

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int index = 0;
            if (line.isBlank()) {
                continue;
            }

            for (char c : line.toCharArray()) {
                map[i][index++] = c == '1';
            }
        }

        return map;
    }
}
