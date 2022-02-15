package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.dz3.algorithms.IOptAlgorithm;
import hr.fer.zemris.optjava.dz3.algorithms.SimulatedAnnealing;
import hr.fer.zemris.optjava.dz3.cooling.GeometricTempSchedule;
import hr.fer.zemris.optjava.dz3.cooling.ITempSchedule;
import hr.fer.zemris.optjava.dz3.decoders.IDecoder;
import hr.fer.zemris.optjava.dz3.decoders.NaturalBinaryDecoder;
import hr.fer.zemris.optjava.dz3.decoders.PassThroughDecoder;
import hr.fer.zemris.optjava.dz3.functions.Function4;
import hr.fer.zemris.optjava.dz3.functions.IFunction;
import hr.fer.zemris.optjava.dz3.neighbourhoods.BitvectorNeighbourhood;
import hr.fer.zemris.optjava.dz3.neighbourhoods.DoubleArrayUnifNeighbourhood;
import hr.fer.zemris.optjava.dz3.solutions.BitvectorSolution;
import hr.fer.zemris.optjava.dz3.solutions.DoubleArraySolution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A program that attempts to find
 * constants for a function with
 * readings of 5 variables and solutions.
 *
 * @author Mateo Imbrišak
 */

public class RegresijaSustava {

    /**
     * Keeps the number of variables in the system.
     */
    private static final int NUMBER_OF_VARIABLES = 5;

    /**
     * Number of constants in the system.
     */
    private static final int NUMBER_OF_CONSTANTS = 6;

    /**
     * Minimum value for binary in command line.
     */
    private static final int MIN_BINARY = 5;

    /**
     * Maximum value for binary in command line.
     */
    private static final int MAX_BINARY = 30;

    /**
     * Minimum value for generated initial solution.
     */
    private static final double MIN_VALUE = -10;

    /**
     * Maximum value for generated initial solution.
     */
    private static final double MAX_VALUE = 10;

    /**
     * Don't let anyone instantiate this class.
     */
    private RegresijaSustava() {}

    /**
     * Used to start the program.
     *
     * @param args three arguments
     *             grad or newton to select the algorithm
     *             maximum number of iterations
     *             path to the file containing the readings
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Program takes exactly two arguments.");
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

        IFunction function = new Function4(variableMatrix, resultVector);
        ITempSchedule schedule = new GeometricTempSchedule(0.995, 100, 1000, 1500);

        if (args[1].toLowerCase().equals("decimal")) {
            IDecoder<DoubleArraySolution> decoder = new PassThroughDecoder();
            DoubleArraySolution solution = new DoubleArraySolution(NUMBER_OF_CONSTANTS);
            solution.randomize(new Random(), MIN_VALUE, MAX_VALUE);

            IOptAlgorithm<DoubleArraySolution> alg = new SimulatedAnnealing<>(
                    decoder,
                    new DoubleArrayUnifNeighbourhood(new double[] {-1, 1}),
                    solution,
                    function,
                    schedule,
                    true);
            solution = alg.run();

            System.out.println("Solution: " + Arrays.toString(decoder.decode(solution)));
            System.out.println("Error: " + solution.value);
        } else if (args[1].toLowerCase().startsWith("binary:")) {
            String value = args[1].replace("binary:", "");

            int bit = Integer.parseInt(value);

            if (bit < MIN_BINARY || bit > MAX_BINARY) {
                System.out.println("Number of bits must be between " + MIN_BINARY + " and " + MAX_BINARY + ".");
                return;
            }

            IDecoder<BitvectorSolution> decoder = new NaturalBinaryDecoder(MIN_VALUE, MAX_VALUE,
                    bit, bit * NUMBER_OF_CONSTANTS);
            BitvectorSolution solution = new BitvectorSolution(bit * NUMBER_OF_CONSTANTS);
            solution.randomize(new Random());

            IOptAlgorithm<BitvectorSolution> alg = new SimulatedAnnealing<>(
                    decoder,
                    new BitvectorNeighbourhood(),
                    solution,
                    function,
                    schedule,
                    true);
            solution = alg.run();

            System.out.println("Solution: " + Arrays.toString(decoder.decode(solution)));
            System.out.println("Error: " + solution.value);
        } else {
            System.out.println("Unknown command.");
        }
    }
}
