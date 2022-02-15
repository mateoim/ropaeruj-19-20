package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.functions.Function4;
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

public class Prijenosna {

    /**
     * Keeps the number of variables in the system.
     */
    private static final int NUMBER_OF_VARIABLES = 5;

    /**
     * Used to start the program.
     *
     * @param args three arguments
     *             grad or newton to select the algorithm
     *             maximum number of iterations
     *             path to the file containing the readings
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Program takes exactly 3 arguments.");
            return;
        }

        int iterations;

        try {
            iterations = Integer.parseInt(args[1]);
        } catch (NumberFormatException exc) {
            System.out.println("Argument 2 must be the number of maximum iterations.");
            return;
        }

        List<String[]> values = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(args[2]));

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
        RealVector solution;

        if ("grad".equals(args[0])) {
            solution = NumOptAlgorithms.gradientMinimum(function, iterations, null);
        } else {
            System.out.println("Unknown command.");
            return;
        }

        System.out.println("\nSolution: " + solution);
        System.out.println("Error: " + function.calculateValue(solution));
    }
}
