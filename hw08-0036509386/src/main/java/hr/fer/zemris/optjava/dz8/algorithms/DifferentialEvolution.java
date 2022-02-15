package hr.fer.zemris.optjava.dz8.algorithms;

import hr.fer.zemris.optjava.dz8.evaluator.Evaluator;

import java.util.Random;

/**
 * A class that represents a differential evolution optimization algorithm.
 *
 * @author Mateo Imbrišak
 */

public class DifferentialEvolution {

    /**
     * Size of population used in the algorithm.
     */
    private final int populationSize;

    /**
     * Maximum number of iterations used.
     */
    private final int maxIter;

    /**
     * Population used while running the algorithm.
     */
    private double[][] population;

    /**
     * Keeps calculated values for {@link #population}.
     */
    private double[] values;

    /**
     * Keeps track of the best value so far.
     */
    private double bestValue = Double.POSITIVE_INFINITY;

    /**
     * Threshold used to stop the algorithm.
     */
    private final double errorThreshold;

    /**
     * Used to calculate values.
     */
    private final Evaluator evaluator;

    /**
     * Lower bound used for generating initial population.
     */
    private final double minInterval;

    /**
     * Upper bound used for generating initial population.
     */
    private final double maxInterval;

    /**
     * Used to scale mutant vectors.
     */
    private final double scaleFactor;

    /**
     * Used when generating trial vectors.
     */
    private final double probability;

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns all values.
     *
     * @param populationSize of solutions.
     * @param maxIter maximum iterations to perform.
     * @param errorThreshold used to stop the algorithm if reached.
     * @param evaluator used to calculate solution values.
     * @param minInterval lower bound used for generating initial population.
     * @param maxInterval upper bound used for generating initial population.
     * @param scaleFactor used to scale mutator vector.
     * @param probability used when generating trial vectors.
     * @param rand used to generate random values.
     */
    public DifferentialEvolution(int populationSize, int maxIter, double errorThreshold,
                                 Evaluator evaluator, double minInterval, double maxInterval,
                                 double scaleFactor, double probability, Random rand) {
        this.populationSize = populationSize;
        this.maxIter = maxIter;
        this.errorThreshold = errorThreshold;
        this.evaluator = evaluator;
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        this.scaleFactor = scaleFactor;
        this.probability = probability;
        this.rand = rand;
    }

    /**
     * Executes the algorithm.
     *
     * @return best found solution.
     */
    public double[] run() {
        initializePopulation();
        int i = 0;
        int dimension = evaluator.getWeightsCount();

        double[][] nextPopulation = new double[populationSize][dimension];
        while (i < maxIter && bestValue > errorThreshold) {
            for (int j = 0; j < populationSize; j++) {
                int r0 = findNextIndex(j);
                int r1 = findNextIndex(j, r0);
                int r2 = findNextIndex(j, r0, r1);

                double[] mutant = new double[dimension];
                double[] jVector = population[j];
                double[] r0Vector = population[r0];
                double[] r1Vector = population[r1];
                double[] r2Vector = population[r2];

                for (int k = 0; k < dimension; k++) {
                    mutant[k] = r0Vector[k] + scaleFactor * (r1Vector[k] - r2Vector[k]);
                }

                int start = rand.nextInt(dimension);

                for (int k = 0; k < dimension; k++) {
                    if (!(rand.nextDouble() <= probability || k == start)) {
                        mutant[k] = jVector[k];
                    }
                }

                double value = evaluator.calculateError(mutant);
                if (checkFitness(values[j], value)) {
                    values[j] = value;
                    nextPopulation[j] = mutant;
                } else {
                    nextPopulation[j] = jVector;
                }
            }

            population = nextPopulation;

            i++;
            System.out.println("Iteration: " + i + ", smallest error: " + bestValue);
        }

        return findBest();
    }

    /**
     * Generates the initial population with size {@link #populationSize}.
     */
    private void initializePopulation() {
        int size = evaluator.getWeightsCount();
        population = new double[populationSize][size];
        values = new double[populationSize];
        double delta = maxInterval - minInterval;

        for (int i = 0; i < populationSize; i++) {
            double[] unit = new double[size];

            for (int j = 0; j < size; j++) {
                unit[j] = minInterval + rand.nextDouble() * delta;
            }

            population[i] = unit;
            double value = evaluator.calculateError(unit);
            checkFitness(value);
            values[i] = value;
        }
    }

    /**
     * Checks if {@code newValue} is better than {@code oldValue}.
     *
     * @param oldValue value of the base vector.
     * @param newValue value of the trial vector.
     *
     * @return {@code true} if {@code newValue} is greater than or equal to
     * {@code oldValue}, otherwise {@code false}.
     */
    private boolean checkFitness(double oldValue, double newValue) {
        if (newValue <= oldValue) {
            checkFitness(newValue);
            return true;
        }

        return false;
    }

    /**
     * Updates {@link #bestValue} if the given value is better.
     *
     * @param value being checked.
     */
    private void checkFitness(double value) {
        if (value <= bestValue) {
            bestValue = value;
        }
    }

    /**
     * Finds an index that hasn't already been selected.
     *
     * @param indexes that have already been selected.
     *
     * @return new index.
     */
    private int findNextIndex(int ... indexes) {
        int i = rand.nextInt(populationSize);

        for (int index : indexes) {
            if (index == i) {
                return findNextIndex(indexes);
            }
        }

        return i;
    }

    /**
     * Finds the best solution based on {@link #bestValue}.
     *
     * @return best solution if found, or first in the {@link #population}
     * if an error occurred.
     */
    private double[] findBest() {
        for (int i = 0; i < populationSize; i++) {
            if (values[i] == bestValue) {
                return population[i];
            }
        }

        return population[0];
    }
}
