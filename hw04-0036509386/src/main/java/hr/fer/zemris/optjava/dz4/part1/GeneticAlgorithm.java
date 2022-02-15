package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.algorithms.ElitistGeneticAlgorithm;
import hr.fer.zemris.optjava.dz4.crossover.BLXAlphaCrossover;
import hr.fer.zemris.optjava.dz4.functions.Function;
import hr.fer.zemris.optjava.dz4.mutators.SimpleMutator;
import hr.fer.zemris.optjava.dz4.selection.RouletteWheelSelection;
import hr.fer.zemris.optjava.dz4.selection.Selection;
import hr.fer.zemris.optjava.dz4.selection.TournamentSelection;
import hr.fer.zemris.optjava.dz4.solutions.DoubleArraySolution;
import hr.fer.zemris.optjava.dz4.solutions.Solution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * A program that attempts to find
 * constants for a function with
 * readings of 5 variables and solutions.
 *
 * @author Mateo Imbrišak
 */

public class GeneticAlgorithm {

    /**
     * Keeps the number of variables in the system.
     */
    private static final int NUMBER_OF_VARIABLES = 5;

    /**
     * Alpha value used for {@link BLXAlphaCrossover}.
     */
    private static final double DEFAULT_ALPHA = 0.1;

    /**
     * Don't let anyone instantiate this class.
     */
    private GeneticAlgorithm() {}

    /**
     * Used to start the program.
     *
     * @param args exactly 6 arguments.
     *             path to the file containing the readings
     *             size of population to be used
     *             maximum error for which the algorithm will stop when reached
     *             maximum number of iterations performed if defined error value is not reached
     *             type of selection: "rouletteWheel" or "tournament:n" where n is the number of participants
     *             sigma used for {@link SimpleMutator}
     */
    public static void main(String[] args) {
        if (args.length != 6) {
            System.err.println("Program takes exactly six arguments.");
            return;
        }

        List<String[]> values = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(args[0]));

            for (String line : lines) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }

                line = line.replace("[", "").replace("]", "");
                values.add(line.split(",\\s+"));
            }
        } catch (IOException exc) {
            System.out.println("Couldn't open file.");
            return;
        }

        int size = values.size();

        double[][] variables = new double[size][NUMBER_OF_VARIABLES];
        double[] results = new double[size];

        for (int i = 0; i < size; i++) {
            String[] valueArray = values.get(i);

            for (int j = 0; j < NUMBER_OF_VARIABLES; j++) {
                variables[i][j] = Double.parseDouble(valueArray[j]);
            }

            results[i] = Double.parseDouble(valueArray[NUMBER_OF_VARIABLES]);
        }

        RealMatrix variableMatrix = new Array2DRowRealMatrix(variables);
        RealVector resultVector = new ArrayRealVector(results);

        Function function = new Function(variableMatrix, resultVector);

        int populationSize = Integer.parseInt(args[1]);
        double maxError = -1 * Double.parseDouble(args[2]);
        int maxIterations = Integer.parseInt(args[3]);
        double sigma = Double.parseDouble(args[5]);

        SimpleMutator mutator = new SimpleMutator(sigma);
        BLXAlphaCrossover crossover = new BLXAlphaCrossover(DEFAULT_ALPHA);
        Selection<Solution> selection;

        if (args[4].startsWith("tournament:")) {
            String value = args[4].replace("tournament:", "");
            int n = Integer.parseInt(value);

            if (n < 2) {
                System.err.println("Number of solutions in the tournament must be at least two.");
                return;
            }

            selection = new TournamentSelection(n);
        } else if (args[4].equalsIgnoreCase("rouletteWheel")) {
            selection = new RouletteWheelSelection();
        } else {
            System.err.println("Unknown selection operator.");
            return;
        }

        ElitistGeneticAlgorithm algorithm = new ElitistGeneticAlgorithm(populationSize, maxError, maxIterations,
                selection, mutator, crossover, function);
        DoubleArraySolution bestSolution = algorithm.run();

        System.out.println("Found solution: " + bestSolution + " with error " + (-1 * bestSolution.getFitness()));
    }
}
