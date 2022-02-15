package hr.fer.zemris.optjava.algotirhm;

import hr.fer.zemris.generic.ga.ArrayGASolution;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.generic.ga.IGAEvaluator;
import hr.fer.zemris.optjava.crossover.Crossover;
import hr.fer.zemris.optjava.mutator.Mutator;
import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;
import hr.fer.zemris.optjava.selection.Selection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A Genetic Algorithm that uses parallelization to calculate solution fitness.
 *
 * @author Mateo Imbrišak
 */

public class EvaluationParallelizationGA implements Algorithm {

    /**
     * Population used in iterations.
     */
    private List<GASolution<int[]>> population;

    /**
     * Size of population used.
     */
    private final int populationSize;

    /**
     * Error threshold used to stop the algorithm when reached.
     */
    private final double stopThreshold;

    /**
     * Maximum number of iterations to be performed unless {@link #stopThreshold} is reached.
     */
    private final int maximumIterations;

    /**
     * Used to evaluate solutions.
     */
    private final IGAEvaluator<int[]> evaluator;

    /**
     * Used to perform selection on the population.
     */
    private final Selection<GASolution<int[]>> selection;

    /**
     * Used to mutate newly created children.
     */
    private final Mutator<GASolution<int[]>> mutator;

    /**
     * Used to cross two parents and create a child.
     */
    private final Crossover<GASolution<int[]>> crossover;

    /**
     * Keeps the number of threads used.
     */
    private final int numberOfThreads;

    /**
     * Size of a single solution.
     */
    private final int solutionSize;

    /**
     * Height of the image.
     */
    private final int height;

    /**
     * Width of the image.
     */
    private final int width;

    /**
     * Used to stop worker threads.
     */
    private final GASolution<int[]> killPill;

    /**
     * Contains children that need to be evaluated.
     */
    private BlockingQueue<GASolution<int[]>> toEvaluate = new LinkedBlockingQueue<>();

    /**
     * Contains evaluated children.
     */
    private BlockingQueue<GASolution<int[]>> evaluated = new LinkedBlockingQueue<>();

    /**
     * Best solution found so far.
     */
    private GASolution<int[]> bestSolution;

    /**
     * Default constructor that assigns all values.
     *
     * @param populationSize size of population used in the algorithm.
     * @param stopThreshold used to stop the algorithm.
     * @param maximumIterations maximum number of iterations to perform.
     * @param evaluator used to evaluate solutions.
     * @param selection used to select parents.
     * @param mutator used to mutate children.
     * @param crossover used to cross the parents.
     * @param solutionSize size of the solution arrays.
     * @param height of the image.
     * @param width of the image.
     */
    public EvaluationParallelizationGA(int populationSize, double stopThreshold, int maximumIterations,
                                       IGAEvaluator<int[]> evaluator, Selection<GASolution<int[]>> selection,
                                       Mutator<GASolution<int[]>> mutator, Crossover<GASolution<int[]>> crossover,
                                       int solutionSize, int height, int width) {
        this.populationSize = populationSize;
        this.stopThreshold = stopThreshold;
        this.maximumIterations = maximumIterations;
        this.evaluator = evaluator;
        this.selection = selection;
        this.mutator = mutator;
        this.crossover = crossover;
        this.numberOfThreads = Runtime.getRuntime().availableProcessors();
        this.solutionSize = solutionSize;
        this.height = height;
        this.width = width;
        this.killPill = new ArrayGASolution(0);
    }

    @Override
    public GASolution<int[]> run() {
        initializePopulation();

        List<GASolution<int[]>> children = new ArrayList<>(populationSize);
        bestSolution = population.get(0);
        int iter = 0;
        while (iter < maximumIterations && bestSolution.fitness < stopThreshold) {
            children.add(bestSolution);

            for (int i = 0; i < populationSize - 1; i++) {
                GASolution<int[]> firstParent = selection.select(population);
                GASolution<int[]> secondParent;

                do {
                    secondParent = selection.select(population);
                } while (firstParent == secondParent);

                GASolution<int[]> child = crossover.cross(firstParent, secondParent);
                child = mutator.mutate(child);

                try {
                    toEvaluate.put(child);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < populationSize - 1; i++) {
                try {
                    children.add(evaluated.take());
                } catch (InterruptedException e) {
                    i--;
                    e.printStackTrace();
                }
            }

            population.clear();
            population.addAll(children);
            children.clear();

            population.sort(GASolution::compareTo);
            bestSolution = population.get(0);

            iter++;

            System.out.println("Current iteration: " + iter + ", best fitness: " + bestSolution.fitness);
        }

        for (int i = 0; i < numberOfThreads; i++) {
            try {
                toEvaluate.put(killPill);
            } catch (InterruptedException exc) {
                i--;
            }
        }

        bestSolution = population.get(0);
        return bestSolution;
    }

    /**
     * Creates starting population.
     */
    private void initializePopulation() {
        IRNG rand = RNG.getRNG();
        population = new ArrayList<>(populationSize);

        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = new EVOThread(() -> {
                while (true) {
                    try {
                        GASolution<int[]> solution = toEvaluate.take();

                        if (solution == killPill) {
                            break;
                        }

                        evaluator.evaluate(solution);

                        evaluated.put(solution);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }

        for (int i = 0; i < populationSize; i++) {
            try {
                int[] solutionArray = new int[solutionSize];

                solutionArray[0] = rand.nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE);

                int index = 1;

                for (int j = 0, size = (solutionSize - 1) / 5; j < size; j++) {
                    solutionArray[index] = rand.nextInt(0, height);
                    solutionArray[index + 1] = rand.nextInt(0, width);
                    solutionArray[index + 2] = rand.nextInt(1, height);
                    solutionArray[index + 3] = rand.nextInt(1, width);
                    solutionArray[index + 4] = rand.nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE);

                    index += 5;
                }

                toEvaluate.put(new ArrayGASolution(solutionArray));
            } catch (InterruptedException exc) {
                i--;
                exc.printStackTrace();
            }
        }

        for (int i = 0; i < populationSize; i++) {
            try {
                population.add(evaluated.take());
            } catch (InterruptedException exc) {
                i--;
                exc.printStackTrace();
            }
        }

        population.sort(GASolution::compareTo);
    }

    @Override
    public GASolution<int[]> getBestSolution() {
        return bestSolution;
    }
}
