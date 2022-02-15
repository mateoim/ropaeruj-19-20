package hr.fer.zemris.optjava.dz6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A program that attempts to find
 * the optimal solution to TSP problem.
 *
 * @author Mateo Imbrišak
 */

public class TSPSolver {

    /**
     * Alpha parameter used for trails.
     */
    private static final double ALPHA = 0.995;

    /**
     * Beta parameter used for trails.
     */
    private static final double BETA = 3.5;

    /**
     * Parameter used to calculate minimum trail.
     */
    private static final double A = 10;

    /**
     * Used to calculate maximum trail.
     */
    private static final double RO = 0.02;

    /**
     * Don't let anyone instantiate this class.
     */
    private TSPSolver() {}

    /**
     * Used to start the algorithm.
     *
     * @param args exactly 4 arguments
     *             path to the file with the problem
     *             size of the list of neighbours to generate for each city
     *             number of ants used in each iteration
     *             maximum number of iterations
     */
    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Program takes exactly 4 parameters.");
            return;
        }

        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(args[0]));
        } catch (IOException exc) {
            System.err.println("Error while reading the file.");
            return;
        }

        List<City> cities = new ArrayList<>();

        boolean skip = true;
        for (String line : lines) {
            line = line.strip();

            if (line.equalsIgnoreCase("EOF")) {
                break;
            }

            if (skip) {
                if (line.startsWith("NODE_COORD_SECTION") || line.startsWith("DISPLAY_DATA_SECTION")) {
                    skip = false;
                }

                continue;
            }

            String[] parts = line.split("\\s+");
            cities.add(new City(Integer.parseInt(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2])));
        }

        double[][] distanceMatrix = initializeDistances(cities);

        int listSize = Integer.parseInt(args[1]);
        int numberOfAnts = Integer.parseInt(args[2]);
        int maxIter = Integer.parseInt(args[3]);

        for (City current : cities) {
            current.findClosestNeighbours(listSize, distanceMatrix, cities);
        }

        MaxMinAntSystem algorithm = new MaxMinAntSystem(maxIter, numberOfAnts, cities, distanceMatrix,
                ALPHA, BETA, RO, A, new Random());

        Ant solution = algorithm.run();

        System.out.println("Found solution: " + solution);
        System.out.println("Distance traveled: " + solution.getDistanceTraveled());
    }

    /**
     * Used internally to calculate the distances between cities.
     *
     * @param cities whose distances will be represented in the matrix.
     *
     * @return a matrix that keeps the distances between the given {@code cities}.
     */
    private static double[][] initializeDistances(List<City> cities) {
        int size = cities.size();
        double[][] values = new double[size][size];

        for (int i = 0; i < size; i++) {
            City cityI = cities.get(i);
            for (int j = i; j < size; j++) {
                City cityJ = cities.get(j);

                double distance = cityI.calculateDistance(cityJ);

                values[i][j] = distance;
                values[j][i] = distance;
            }
        }

        return values;
    }
}
