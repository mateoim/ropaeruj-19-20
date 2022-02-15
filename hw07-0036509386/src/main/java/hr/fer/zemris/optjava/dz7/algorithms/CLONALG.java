package hr.fer.zemris.optjava.dz7.algorithms;

import hr.fer.zemris.optjava.dz7.FFANN;

import java.util.*;

/**
 * @author Mateo Imbrišak
 */

public class CLONALG {

    private final FFANN network;

    private double[][] population;

    private final double xMin;

    private final double xMax;

    private double[] function;

    private Integer[] sortedIndexes;

    private final int populationSize;

    private final int beta;

    private final double ro;

    private final int randomToGenerate;

    private final int maxIter;

    private final double maxError;

    private final Random rand;

    public CLONALG(FFANN network, double xMin, double xMax, int populationSize, int beta, double ro,
                   int randomToGenerate, int maxIter, double maxError, Random rand) {
        this.network = network;
        this.xMin = xMin;
        this.xMax = xMax;
        this.populationSize = populationSize;
        this.beta = beta;
        this.ro = ro;
        this.randomToGenerate = randomToGenerate;
        this.maxIter = maxIter;
        this.maxError = maxError;
        this.rand = rand;
    }

    public double[] run() {
        initializePopulation();
        double bestValue = Double.NEGATIVE_INFINITY;

        int i = 0;
        while (i < maxIter && bestValue < maxError) {
            function = evaluate(population);
            sort(sortedIndexes, function);

            double[][] cloned = clonePopulation();

            hyperMutate(cloned);

            double[] clonedFunction = evaluate(cloned);

            Integer[] clonedIndexes = initIndexes(cloned.length);
            sort(clonedIndexes, clonedFunction);

            for (int j = 0; j < populationSize; j++) {
                population[j] = cloned[clonedIndexes[j]];
            }

            addNewSolutions();

            bestValue = clonedFunction[clonedIndexes[0]];
            i++;

            System.out.println("Iteration :" + i + " best value: " + (-1 * bestValue));
        }

        sort(sortedIndexes, function);

        return population[sortedIndexes[0]];
    }

    private Integer[] initIndexes(int size) {
        Integer[] ret = new Integer[size];

        for (int i = 0; i < size; i++) {
            ret[i] = i;
        }

        return ret;
    }

    private double[] evaluate(double[][] population) {
        int solutionSize = network.getDataset().getOutputs().length;
        double[] ret = new double[population.length];

        for (int j = 0, size = population.length; j < size; j++) {
            double[] solution = new double[solutionSize];

            network.calcOutputs(network.getDataset().getInputs(), population[j], solution);

            ret[j] = -1 * network.calculateError(solution);
        }

        return ret;
    }

    private void sort(Integer[] indexes, double[] function) {
        Arrays.sort(indexes, (o1, o2) -> {
            double value1 = function[o1];
            double value2 = function[o2];

            return -1 * Double.compare(value1, value2);
        });
    }

    private double[][] clonePopulation() {
        List<double[]> clonePopulation = new ArrayList<>();
        int size = network.getWeightsCount();

        for (int i = 0; i < populationSize; i++) {
            double[] unit = population[i];

            int toGenerate = Math.floorDiv(beta * populationSize, i + 1);

            for (int j = 0; j < toGenerate; j++) {
                clonePopulation.add(Arrays.copyOf(unit, unit.length));
            }
        }

        return clonePopulation.toArray(new double[clonePopulation.size()][size]);
    }

    private void hyperMutate(double[][] toMutate) {
        int index = 0;
        int normalizedIndex = sortedIndexes[index];
        double[] original = population[normalizedIndex];
        double value = function[normalizedIndex];
        double elements = population[0].length;

        for (int i = 0, size = population.length; i < size; i++) {
            double[] current = toMutate[i];

            if (!Arrays.equals(current, original)) {
                index++;
                normalizedIndex = sortedIndexes[index];
                original = population[normalizedIndex];
                value = function[normalizedIndex];
            }

            double probability = Math.exp(-1 * ro * value);
            for (int j = 0; j < elements; j++) {
                if (rand.nextDouble() < probability) {
                    current[j] += xMin + rand.nextDouble() * (xMax - xMin);
                }
            }

        }
    }

    private void addNewSolutions() {
        for (int i = populationSize - randomToGenerate; i < populationSize; i++) {
            population[i] = generateRandom();
        }
    }

    private void initializePopulation() {
        int dimension = network.getWeightsCount();
        population = new double[populationSize][dimension];
        sortedIndexes = new Integer[populationSize];

        for (int i = 0; i < populationSize; i++) {
            sortedIndexes[i] = i;
            population[i] = generateRandom();
        }
    }

    private double[] generateRandom() {
        int dimension = network.getWeightsCount();
        double[] solution = new double[dimension];

        for (int j = 0; j < dimension; j++) {
            solution[j] = xMin + rand.nextDouble() * (xMax - xMin);
        }

        return solution;
    }
}
