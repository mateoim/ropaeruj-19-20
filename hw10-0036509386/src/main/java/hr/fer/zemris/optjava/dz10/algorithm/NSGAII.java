package hr.fer.zemris.optjava.dz10.algorithm;

import hr.fer.zemris.optjava.dz10.crossover.BLXAlphaCrossover;
import hr.fer.zemris.optjava.dz10.mutator.Mutator;
import hr.fer.zemris.optjava.dz10.problem.MOOPProblem;
import hr.fer.zemris.optjava.dz10.selection.CrowdedTournamentSelection;

import java.util.*;

/**
 * An implementation of Non-Dominated Sorting Genetic Algorithm.
 *
 * @author Mateo Imbrišak
 */

public class NSGAII {

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
    private final CrowdedTournamentSelection selection;

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
     * Keeps distances between solutions in a front.
     */
    private double[] distances;

    /**
     * Keeps all current fronts.
     */
    private List<List<Integer>> fronts;

    /**
     * Keeps the number of solutions by which each solution is dominated.
     */
    private int[] globalDominatedByCounter;

    /**
     * Keeps the number of solutions by which each solution in the union is dominated.
     */
    private int[] unionDominatedByCounter;

    /**
     * Keeps a {@link List} of solutions dominated by every solution.
     */
    private List<List<Integer>> dominatesList;

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
     * @param rand used to generate random values.
     */
    public NSGAII(int populationSize, int maxIter, MOOPProblem problem, CrowdedTournamentSelection selection,
                  BLXAlphaCrossover crossover, Mutator mutator, Random rand) {
        this.populationSize = populationSize;
        this.maxIter = maxIter;
        this.problem = problem;
        this.selection = selection;
        this.crossover = crossover;
        this.mutator = mutator;
        this.rand = rand;
    }

    /**
     * Executes the algorithm.
     */
    public void run() {
        initializePopulation();

        int dimension = problem.getSolutionDimension();
        int objectives = problem.getNumberOfObjectives();

        calculateFronts(values, globalDominatedByCounter);

        for (List<Integer> front : fronts) {
            crowdingSort(front, values);
        }

        double[][] union = new double[2 * populationSize][dimension];
        double[][] unionValues = new double[2 * populationSize][dimension];

        for (int i = populationSize; i < 2 * populationSize; i++) {
            unionValues[i] = new double[objectives];
        }

        for (int iteration = 0; iteration < maxIter; iteration++) {
            System.arraycopy(solutions, 0, union, 0, populationSize);
            System.arraycopy(values, 0, unionValues, 0, populationSize);

            for (int i = populationSize; i < 2 * populationSize; i++) {
                double[] firstParent = solutions[selection.select(globalDominatedByCounter, distances)];
                double[] secondParent;

                do {
                    secondParent = solutions[selection.select(globalDominatedByCounter, distances)];
                } while (Arrays.equals(firstParent, secondParent));


                double[] child = crossover.cross(firstParent, secondParent);
                mutator.mutate(child);

                checkChild(child);

                union[i] = child;
                problem.evaluateSolution(child, unionValues[i]);
            }

            calculateFronts(unionValues, unionDominatedByCounter);
            int counter = 0;
            List<Integer> nextFront = new ArrayList<>();

            for (int i = 0; i < fronts.size(); i++) {
                List<Integer> front = fronts.get(i);

                if (counter + front.size() < populationSize) {
                    for (int index : front) {
                        nextFront.add(counter);
                        solutions[counter] = union[index];
                        values[counter++] = unionValues[index];
                    }

                    front.clear();
                    front.addAll(nextFront);
                    nextFront.clear();
                } else {
                    crowdingSort(front, unionValues);

                    for (int index : front) {
                        if (counter == populationSize) {
                            break;
                        }

                        nextFront.add(counter);
                        solutions[counter] = union[index];
                        values[counter++] = unionValues[index];
                    }

                    fronts = fronts.subList(0, i);
                    fronts.add(nextFront);

                    break;
                }
            }

            for (List<Integer> front : fronts) {
                crowdingSort(front, values);
            }

            System.out.println("Current iteration: " + (iteration + 1) + ", number of fronts: " + fronts.size() +
                    ", size of best front: " + fronts.get(0).size());
        }

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
     *
     * @param values used to calculate {@link #dominates(int, int, double[][])}.
     * @param globalDominatedByCounter keeps info about {@link #dominates(int, int, double[][])} and population size.
     */
    private void calculateFronts(double[][] values, int[] globalDominatedByCounter) {
        fronts.clear();
        Arrays.fill(globalDominatedByCounter, 0);
        int populationSize = globalDominatedByCounter.length;

        for (int i = 0; i < populationSize; i++) {
            for (int j = i + 1; j < populationSize; j++) {
                Optional<Boolean> dominates = dominates(i, j, values);

                if (dominates.isPresent()) {
                    if (dominates.get()) {
                        globalDominatedByCounter[j] = globalDominatedByCounter[j] + 1;
                        dominatesList.get(i).add(j);
                    } else {
                        globalDominatedByCounter[i] = globalDominatedByCounter[i] + 1;
                        dominatesList.get(j).add(i);
                    }
                }
            }
        }

        int[] dominatedByCounter = Arrays.copyOf(globalDominatedByCounter, globalDominatedByCounter.length);
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
     * @param values used to check solution relations.
     *
     * @return {@code true} if {@code first} dominates {@code second},
     *         {@code false} if {@code second} dominates {@code first},
     *         {@code null} if there is no relation.
     */
    private Optional<Boolean> dominates(int first, int second, double[][] values) {
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
     * Performs sorting for the given {@code front}.
     *
     * @param front being sorted.
     * @param values used to sort the elements, size must be greater
     *               than highest index in the given {@code front}.
     */
    private void crowdingSort(List<Integer> front, double[][] values) {
        for (int index : front) {
            distances[index] = 0;
        }

        int frontSize = front.size();
        int numberOfObjectives = problem.getNumberOfObjectives();

        for (int i = 0; i < numberOfObjectives; i++) {
            int finalI = i;
            front.sort((o1, o2) -> {
                double delta = values[o1][finalI] - values[o2][finalI];
                if (delta > 0) {
                    return 1;
                } else if (delta == 0) {
                    return 0;
                } else {
                    return -1;
                }
            });

            distances[front.get(0)] = Double.POSITIVE_INFINITY;
            distances[front.get(frontSize - 1)] = Double.POSITIVE_INFINITY;

            double min = values[front.get(0)][i];
            double max = values[front.get(frontSize - 1)][i];
            double delta = max - min;

            for (int j = 1; j < frontSize - 1; j++) {
                int index = front.get(j);

                distances[index] += (values[front.get(j + 1)][i] - values[front.get(j - 1)][i]) / delta;
            }
        }

        front.sort((o1, o2) -> Double.compare(distances[o2], distances[o1]));
    }

    /**
     * Generates initial population to be used in the algorithm.
     */
    private void initializePopulation() {
        int dimension = problem.getSolutionDimension();
        int objectives = problem.getNumberOfObjectives();

        solutions = new double[populationSize][dimension];
        values = new double[populationSize][objectives];
        distances = new double[2 * populationSize];
        fronts = new ArrayList<>(populationSize);
        globalDominatedByCounter = new int[populationSize];
        unionDominatedByCounter = new int[2 * populationSize];
        dominatesList = new ArrayList<>(2 * populationSize);

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
            dominatesList.add(new ArrayList<>());
        }
    }
}
