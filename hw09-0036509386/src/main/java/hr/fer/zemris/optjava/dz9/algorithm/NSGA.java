package hr.fer.zemris.optjava.dz9.algorithm;

import hr.fer.zemris.optjava.dz9.crossover.BLXAlphaCrossover;
import hr.fer.zemris.optjava.dz9.mutator.Mutator;
import hr.fer.zemris.optjava.dz9.problem.MOOPProblem;
import hr.fer.zemris.optjava.dz9.selection.RouletteWheelSelection;

import java.util.*;
import java.util.function.Supplier;

/**
 * An implementation of Non-Dominated Sorting Genetic Algorithm.
 *
 * @author Mateo Imbrišak
 */

public class NSGA {

    /**
     * Used to prevent negative fitness.
     */
    private static final double EPSILON_FAILSAFE_FACTOR = 100;

    /**
     * Size of population used in the algorithm.
     */
    private final int populationSize;

    /**
     * Maximum number of iterations to perform.
     */
    private final int maxIter;

    /**
     * Defines the problem being optimized.
     */
    private final MOOPProblem problem;

    /**
     * Used to select parents.
     */
    private final RouletteWheelSelection selection;

    /**
     * Used to cross parents.
     */
    private final BLXAlphaCrossover crossover;

    /**
     * Used to mutate children.
     */
    private final Mutator mutator;

    /**
     * Keeps track of current population.
     */
    private double[][] solutions;

    /**
     * Keeps track of population's values.
     */
    private double[][] values;

    /**
     * A {@link Supplier} used in {@link #calculateFitness()}, defined in constructor by {@code type}.
     */
    private final Supplier<Integer> sizeGetter;

    /**
     * A {@link Supplier} used in {@link #calculateFitness()}, defined in constructor by {@code type}.
     */
    private final Supplier<double[][]> shareTarget;

    /**
     * Keeps all current fronts.
     */
    private List<List<Integer>> fronts;

    /**
     * Keeps the number of solutions by which each solution is dominated.
     */
    private int[] dominatedByCounter;

    /**
     * Keeps a {@link List} of solutions dominated by every solution.
     */
    private List<List<Integer>> dominatesList;

    /**
     * Determines range in which solutions share fitness.
     */
    private final double sigmaShare;

    /**
     * Factor used for share calculation.
     */
    private final double alpha;

    /**
     * Used to control fitness for fronts.
     */
    private final double epsilon;

    /**
     * Keeps fitness used for selection.
     */
    private double[] fitness;

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns all values.
     *
     * @param populationSize size of population used.
     * @param maxIter number of iterations to perform.
     * @param problem being optimized.
     * @param selection used to select parents.
     * @param crossover used to cross parents.
     * @param mutator used to mutate children.
     * @param type used to configure {@link Supplier}s.
     * @param sigmaShare range in which solutions share fitness.
     * @param alpha factor used for share calculation.
     * @param epsilon used to control fitness for fronts.
     * @param rand used to generate random values.
     */
    public NSGA(int populationSize, int maxIter, MOOPProblem problem, RouletteWheelSelection selection,
                BLXAlphaCrossover crossover, Mutator mutator, String type,
                double sigmaShare, double alpha, double epsilon, Random rand) {
        this.populationSize = populationSize;
        this.maxIter = maxIter;
        this.problem = problem;
        this.selection = selection;
        this.crossover = crossover;
        this.mutator = mutator;
        this.sigmaShare = sigmaShare;
        this.alpha = alpha;
        this.epsilon = epsilon;
        this.rand = rand;

        if (type.equals("decision-space")) {
            sizeGetter = this.problem::getSolutionDimension;
            shareTarget = () -> solutions;
        } else if (type.equals("objective-space")) {
            sizeGetter = this.problem::getNumberOfObjectives;
            shareTarget = () -> values;
        } else {
            throw new RuntimeException("Unknown type.");
        }
    }

    /**
     * Executes the algorithm.
     */
    public void run() {
        initializePopulation();

        int dimension = problem.getSolutionDimension();

        for (int iteration = 0; iteration < maxIter; iteration++) {
            double[][] children = new double[populationSize][dimension];
            calculateFronts();

            calculateFitness();

            for (int i = 0; i < populationSize; i++) {
                double[] firstParent = solutions[selection.select(fitness)];
                double[] secondParent;

                do {
                    secondParent = solutions[selection.select(fitness)];
                } while (Arrays.equals(firstParent, secondParent));


                double[] child = crossover.cross(firstParent, secondParent);
                mutator.mutate(child);

                checkChild(child);

                children[i] = child;
                problem.evaluateSolution(child, values[i]);
            }

            solutions = children;

            System.out.println("Current iteration: " + (iteration + 1) + ", number of fronts: " + fronts.size() +
                    ", size of best front: " + fronts.get(0).size());
        }

        calculateFronts();

        for (int i = 0; i < fronts.size(); i++) {
            List<Integer> front = fronts.get(i);
            System.out.println("Front: " + (i + 1) + ", number of solutions: " + front.size());
        }
    }

    /**
     * Provides last generated {@link #solutions}.
     *
     * @return {@link #solutions}.
     */
    public double[][] getSolutions() {
        return solutions;
    }

    /**
     * Provides {@link #values} for last generated {@link #solutions}.
     *
     * @return {@link #values}.
     */
    public double[][] getValues() {
        return values;
    }

    /**
     * Provides solutions from Pareto-front.
     *
     * @return solutions from Pareto-front.
     */
    public double[][] getBestSolutions() {
        List<Integer> front = fronts.get(0);
        int frontSize = front.size();
        int solutionSize = solutions[0].length;

        double[][] bestSolutions = new double[frontSize][solutionSize];

        for (int i = 0; i < front.size(); i++) {
            int index = front.get(i);
            bestSolutions[i] = solutions[index];
        }

        return bestSolutions;
    }

    /**
     * Checks the child for violations of hard constraints and corrects it if any are found.
     *
     * @param child being checked.
     */
    private void checkChild(double[] child) {
        for (int i = 0, size = child.length; i < size; i++) {
            double currentValue = child[i];
            double[] constraint = problem.getConstraint(i);

            if (currentValue < constraint[0]) {
                child[i] = constraint[0];
            } else if (currentValue > constraint[1]) {
                child[i] = constraint[1];
            }
        }
    }

    /**
     * Calculates {@link #fronts} for the current population.
     */
    private void calculateFronts() {
        fronts.clear();

        for (int i = 0; i < populationSize; i++) {
            for (int j = i + 1; j < populationSize; j++) {
                Optional<Boolean> dominates = dominates(i, j);

                if (dominates.isPresent()) {
                    if (dominates.get()) {
                        dominatedByCounter[j] = dominatedByCounter[j] + 1;
                        dominatesList.get(i).add(j);
                    } else {
                        dominatedByCounter[i] = dominatedByCounter[i] + 1;
                        dominatesList.get(j).add(i);
                    }
                }
            }
        }

        List<Integer> toProcess = new ArrayList<>();
        List<Integer> currentFront = new ArrayList<>();
        fronts.add(currentFront);

        int[] oldDominatedBy = Arrays.copyOf(dominatedByCounter, dominatedByCounter.length);

        for (int i = 0; i < populationSize; i++) {
            if (oldDominatedBy[i] == 0) {
                currentFront.add(i);

                for (int index : dominatesList.get(i)) {
                    dominatedByCounter[index] = dominatedByCounter[index] - 1;
                }

                dominatesList.get(i).clear();
            } else {
                toProcess.add(i);
            }
        }

        while (!toProcess.isEmpty()) {
            currentFront = new ArrayList<>();
            fronts.add(currentFront);

            oldDominatedBy = Arrays.copyOf(dominatedByCounter, dominatedByCounter.length);

            for (Integer index : toProcess) {
                if (oldDominatedBy[index] == 0) {
                    currentFront.add(index);

                    for (int current : dominatesList.get(index)) {
                        dominatedByCounter[current] = dominatedByCounter[current] - 1;
                    }

                    dominatesList.get(index).clear();
                }
            }

            toProcess.removeAll(currentFront);
        }
    }

    /**
     * Checks whether {@code first} dominates {@code second} solution.
     *
     * @param first solution being checked.
     * @param second solution being checked.
     *
     * @return {@code true} if {@code first} dominates {@code second},
     *         {@code false} if {@code second} dominates {@code first},
     *         {@code null} if there is no relation.
     */
    private Optional<Boolean> dominates(int first, int second) {
        boolean dominatesFirst = false;
        boolean dominatesSecond = false;

        double[] firstValues = values[first];
        double[] secondValues = values[second];

        for (int i = 0, size = firstValues.length; i < size; i++) {
            if (firstValues[i] > secondValues[i]) {
                dominatesSecond = true;

                if (dominatesFirst) {
                    return Optional.empty();
                }
            } else if (firstValues[i] < secondValues[i]) {
                dominatesFirst = true;

                if (dominatesSecond) {
                    return Optional.empty();
                }
            }
        }

        if (dominatesFirst) {
            return Optional.of(true);
        } else if (dominatesSecond) {
            return Optional.of(false);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Calculates shared {@link #fitness} for current population.
     */
    private void calculateFitness() {
        double fMin = populationSize + epsilon;
        int size = sizeGetter.get();

        double[] min = new double[size];
        double[] max = new double[size];

        Arrays.fill(min, Double.POSITIVE_INFINITY);
        Arrays.fill(max, Double.NEGATIVE_INFINITY);

        for (double[] unit : shareTarget.get()) {
            for (int i = 0; i < size; i++) {
                if (unit[i] < min[i]) {
                    min[i] = unit[i];
                } else if (unit[i] > max[i]) {
                    max[i] = unit[i];
                }
            }
        }

        double[] share = new double[populationSize];

        for (List<Integer> front : fronts) {
            int frontSize = front.size();
            double currentF = fMin - (fMin / EPSILON_FAILSAFE_FACTOR);
            double currentMin = Double.POSITIVE_INFINITY;

            for (int j = 0; j < frontSize; j++) {
                int index = front.get(j);
                double[] solution = shareTarget.get()[index];

                for (int k = j + 1; k < frontSize; k++) {
                    int secondIndex = front.get(k);
                    double distance = 0;
                    double[] target = shareTarget.get()[secondIndex];

                    for (int l = 0; l < size; l++) {
                        distance += (solution[l] - target[l]) / (max[l] - min[l]);
                        distance *= distance;
                    }

                    distance = Math.sqrt(distance);

                    if (distance < sigmaShare) {
                        distance = 1 - Math.pow(distance / sigmaShare, alpha);
                    } else {
                        distance = 0;
                    }

                    share[index] = share[index] + distance;
                    share[secondIndex] = share[secondIndex] + distance;
                }

                if (share[index] == 0) {
                    fitness[index] = currentF;
                } else {
                    fitness[index] = currentF / share[index];
                }

                if (fitness[index] < currentMin) {
                    currentMin = fitness[index];
                }
            }

            fMin = currentMin;
        }
    }

    /**
     * Generates initial population to be used in the algorithm.
     */
    private void initializePopulation() {
        int dimension = problem.getSolutionDimension();
        int objectives = problem.getNumberOfObjectives();

        solutions = new double[populationSize][dimension];
        values = new double[populationSize][objectives];
        fronts = new ArrayList<>(populationSize);
        dominatedByCounter = new int[populationSize];
        dominatesList = new ArrayList<>(populationSize);
        fitness = new double[populationSize];

        for (int i = 0; i < populationSize; i++) {
            double[] solution = new double[dimension];

            for (int j = 0; j < dimension; j++) {
                double[] bound = problem.getConstraint(j);
                solution[j] = bound[0] + rand.nextDouble() * (bound[1] - bound[0]);
            }

            solutions[i] = solution;
            values[i] = new double[objectives];
            problem.evaluateSolution(solutions[i], values[i]);
            dominatesList.add(new ArrayList<>());
        }
    }
}
