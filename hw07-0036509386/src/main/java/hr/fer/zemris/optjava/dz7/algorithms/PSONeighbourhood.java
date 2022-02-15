package hr.fer.zemris.optjava.dz7.algorithms;

import hr.fer.zemris.optjava.dz7.FFANN;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Mateo Imbrišak
 */
public class PSONeighbourhood {

    private final int neighbours;

    private final FFANN network;

    private final int populationSize;

    private final double vMin;

    private final double vMax;

    private final double xMin;

    private final double xMax;

    private final double wMin;

    private final double wMax;

    private double[][] velocity;

    private double[][] values;

    private double[][] personalBest;

    private double[] globalBest;

    private double globalBestValue = Double.NEGATIVE_INFINITY;

    private double[] function;

    private final double c1;

    private final double c2;

    private final int maxIter;

    private final double minError;

    private final Random rand;

    public PSONeighbourhood(FFANN network, int populationSize, double vMin, double vMax, double xMin, double xMax,
               double wMin, double wMax, double c1, double c2, int maxIter,
                            double minError, int neighbours, Random rand) {
        this.network = network;
        this.populationSize = populationSize;
        this.vMin = vMin;
        this.vMax = vMax;
        this.xMin = xMin;
        this.xMax = xMax;
        this.wMin = wMin;
        this.wMax = wMax;
        this.c1 = c1;
        this.c2 = c2;
        this.maxIter = maxIter;
        this.minError = minError;
        this.neighbours = neighbours;
        this.rand = rand;
    }

    public double[] run() {
        initializePopulation();
        int dimension = network.getWeightsCount();
        int solutionSize = network.getDataset().getOutputs().length;

        function = new double[dimension];
        Arrays.fill(function, Double.NEGATIVE_INFINITY);

        personalBest = new double[populationSize][dimension];

        for (int i = 0; i < maxIter; i++) {
            if (-1 * globalBestValue <= minError) {
                return globalBest;
            }

            double[] localFunction = new double[dimension];
            double w = (double) i / maxIter * (wMax - wMin) + wMax;

            for (int j = 0; j < populationSize; j++) {
                double[] solution = new double[solutionSize];

                network.calcOutputs(network.getDataset().getInputs(), values[j], solution);

                localFunction[j] = -1 * network.calculateError(solution);
            }

            for (int j = 0; j < populationSize; j++) {
                if (localFunction[j] > function[j]) {
                    double value = localFunction[j];
                    double[] unit = Arrays.copyOf(values[j], values[j].length);

                    function[j] = value;
                    personalBest[j] = unit;

                    if (value > globalBestValue) {
                        globalBest = Arrays.copyOf(unit, unit.length);
                        globalBestValue = value;
                    }
                }
            }

            for (int j = 0; j < populationSize; j++) {
                double[] bestNeighbour = values[findLocalBest(j)];

                for (int k = 0; k < dimension; k++) {
                    velocity[j][k] = w * velocity[j][k] + c1 * rand.nextDouble() * (personalBest[j][k] - values[j][k])
                            + c2 * rand.nextDouble() * (bestNeighbour[k] - values[j][k]);

                    if (velocity[j][k] > vMax) {
                        velocity[j][k] = vMax;
                    } else if (velocity[j][k] < vMin) {
                        velocity[j][k] = vMin;
                    }

                    values[j][k] += velocity[j][k];
                }
            }

            System.out.println("Iteration: " + (i + 1) + " best value: " + (-1 * globalBestValue));
        }

        return globalBest;
    }

    private void initializePopulation() {
        int dimension = network.getWeightsCount();
        velocity = new double[populationSize][dimension];
        values = new double[populationSize][dimension];

        for (int i = 0; i < populationSize; i++) {
            double[] currentVelocity = new double[dimension];
            double[] currentValues = new double[dimension];

            for (int j = 0; j < dimension; j++) {
                currentVelocity[j] = vMin + rand.nextDouble() * (vMax - vMin);
                currentValues[j] = xMin + rand.nextDouble() * (xMax - xMin);
            }

            velocity[i] = currentVelocity;
            values[i] = currentValues;
        }
    }

    /**
     * Finds the index of best local neighbour.
     *
     * @param index of the unit
     * @return index of best local neighbour.
     */
    private int findLocalBest(int index) {
        double best = function[index];
        int found = index;

        for (int i = 0, currentIndex = index + 1; i < neighbours; i++, currentIndex++) {
            if (currentIndex >= function.length - 1) {
                currentIndex = 0;
            }

            if (function[currentIndex] > best) {
                best = function[index];
                found = currentIndex;
            }
        }

        for (int i = 0, currentIndex = index - 1; i < neighbours; i++, currentIndex--) {
            if (currentIndex < 0) {
                currentIndex = function.length - 1;
            }

            if (function[currentIndex] > best) {
                best = function[index];
                found = currentIndex;
            }
        }

        return found;
    }
}
