package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.functions.Function3;
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
 * A program that attempts to solve a linear system.
 *
 * @author Mateo Imbrišak
 */

public class Sustav {

    /**
     * Used to start the program.
     *
     * @param args three arguments
     *             grad or newton to select the algorithm
     *             maximum number of iterations
     *             path to the file containing the system
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

        int size = 0;

        if (!values.isEmpty()) {
            size = values.get(0).length;
        }

        double[][] valuesA = new double[size - 1][size - 1];
        double[] valuesB = new double[size - 1];

        for (int i = 0; i < values.size(); i++) {
            String[] valueArray = values.get(i);

            for (int j = 0; j < size - 1; j++) {
                valuesA[i][j] = Double.parseDouble(valueArray[j]);
            }

            valuesB[i] = Double.parseDouble(valueArray[size - 1]);
        }

        RealMatrix A = new Array2DRowRealMatrix(valuesA);
        RealVector b = new ArrayRealVector(valuesB);

        IHFunction function = new Function3(A, b);
        RealVector solution;

        switch (args[0]) {
            case "grad":
                solution = NumOptAlgorithms.gradientMinimum(function, iterations, null);
                break;
            case "newton":
                solution = NumOptAlgorithms.newtonMinimum(function, iterations, null);
                break;
            default:
                System.out.println("Unknown command.");
                return;
        }

        System.out.println("\nSolution: " + solution);
        System.out.println("Error: " + function.calculateValue(solution));
    }
}
