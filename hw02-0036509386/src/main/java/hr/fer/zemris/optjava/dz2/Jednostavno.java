package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.functions.Function1;
import hr.fer.zemris.optjava.dz2.functions.Function2;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * A program that attempts to find the minimum value
 * of a function.
 *
 * @author Mateo Imbrišak
 */

public class Jednostavno {

    /**
     * Used to start the program.
     *
     * @param args 2 or 4 arguments
     *             which function and algorithm is being used.
     *             maximum number of iterations
     *             x coordinate of starting point
     *             y coordinate of starting point
     */
    public static void main(String[] args) {
        if (args.length != 2 && args.length != 4) {
            System.err.println("Program takes exactly 2 or 4 arguments.");
            return;
        }

        RealVector point = null;

        if (args.length == 4) {
            try {
                double x1 = Double.parseDouble(args[2]);
                double x2 = Double.parseDouble(args[3]);
                double[] values = {x1, x2};

                point = new ArrayRealVector(values);
            } catch (NumberFormatException exc) {
                System.out.println("Arguments 3 and 4 must be point coordinates.");
                return;
            }
        }

        int iterations;

        try {
            iterations = Integer.parseInt(args[1]);
        } catch (NumberFormatException exc) {
            System.out.println("Argument 2 must be the number of maximum iterations.");
            return;
        }

        RealVector solution;
        double value;

        switch (args[0]) {
            case "1a":
                IHFunction f1a = new Function1();
                solution = NumOptAlgorithms.gradientMinimum(f1a, iterations, point);
                value = f1a.calculateValue(solution);
                break;
            case "1b":
                IHFunction f1b = new Function1();
                solution = NumOptAlgorithms.newtonMinimum(f1b, iterations, point);
                value = f1b.calculateValue(solution);
                break;
            case "2a":
                IHFunction f2a = new Function2();
                solution = NumOptAlgorithms.gradientMinimum(f2a, iterations, point);
                value = f2a.calculateValue(solution);
                break;
            case "2b":
                IHFunction f2b = new Function2();
                solution = NumOptAlgorithms.newtonMinimum(f2b, iterations, point);
                value = f2b.calculateValue(solution);
                break;
            default:
                System.out.println("Unknown command.");
                return;
        }

        System.out.println("\nSolution: " + solution + ", value: " + value);
    }
}
