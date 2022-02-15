package hr.fer.zemris.optjava.dz13.algorithms;

import hr.fer.zemris.optjava.dz13.actions.AntAction;
import hr.fer.zemris.optjava.dz13.crossover.Crossover;
import hr.fer.zemris.optjava.dz13.mutator.Mutator;
import hr.fer.zemris.optjava.dz13.selection.TournamentSelection;
import hr.fer.zemris.optjava.dz13.solutions.Ant;

import java.util.Arrays;
import java.util.Random;

/**
 * A Genetic Algorithm that attempts to find the best solution
 * using genetic programming.
 *
 * @author Mateo Imbrišak
 */

public class GeneticAlgorithm {

    /**
     * Maximum depth of initially generated solutions.
     */
    private static final int MAX_INITIAL_DEPTH = 6;

    /**
     * Maximum depth os a solution.
     */
    private static final int MAX_TOTAL_DEPTH = 20;

    /**
     * Highest number of child nodes allowed.
     */
    private static final int MAX_NUMBER_OF_NODES = 200;

    /**
     * Probability of solution reproduction.
     */
    private static final double REPRODUCTION_PROBABILITY = 0.01;

    /**
     * Probability of a mutation.
     */
    private static final double MUTATION_PROBABILITY = 0.14;

    /**
     * Probability of a crossover.
     */
    private static final double CROSSOVER_PROBABILITY = 0.85;

    /**
     * {@link Ant} used to run simulations.
     */
    private final Ant ant;

    /**
     * Size of the population used.
     */
    private final int populationSize;

    /**
     * Highest number of iterations.
     */
    private final int maxIter;

    /**
     * Used to stop the algorithm if reached.
     */
    private final double errorThreshold;

    /**
     * Used to perform mutations.
     */
    private final Mutator mutator;

    /**
     * Used to select parents.
     */
    private final TournamentSelection selection;

    /**
     * Used to cross solutions.
     */
    private final Crossover crossover;

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Population in the current iteration.
     */
    private AntAction[] population;

    /**
     * Default constructor that assigns all values.
     *
     * @param ant used to run the simulations.
     * @param populationSize size of population used.
     * @param maxIter maximum number of iterations.
     * @param errorThreshold threshold used to stop the algorithm.
     * @param mutator used to mutate solutions.
     * @param selection used to select parents.
     * @param crossover used to cross solutions.
     * @param rand used to generate random values.
     */
    public GeneticAlgorithm(Ant ant, int populationSize, int maxIter, double errorThreshold, Mutator mutator,
                            TournamentSelection selection, Crossover crossover, Random rand) {
        this.ant = ant;
        this.populationSize = populationSize;
        this.maxIter = maxIter;
        this.errorThreshold = errorThreshold;
        this.mutator = mutator;
        this.selection = selection;
        this.crossover = crossover;
        this.rand = rand;
    }

    /**
     * Executes the algorithm and attempts to find the best solution.
     *
     * @return best found solution.
     */
    public AntAction run() {
        initializePopulation();

        int iter = 0;
        AntAction bestSolution = population[0];
        AntAction[] children = new AntAction[populationSize];

        while (iter < maxIter && bestSolution.getFitness() < errorThreshold) {
            children[0] = bestSolution;

            for (int i = 1; i < populationSize; i++) {
                double roll = rand.nextDouble();

                if (roll <= REPRODUCTION_PROBABILITY) {
                    AntAction parent = selection.select(population);
                    AntAction child = parent.duplicate();

                    child.setDepth(1);
                    child.setFitness(parent.getFitness());
                    child.setFood(parent.getFood());

                    children[i] = child;
                } else if (roll <= REPRODUCTION_PROBABILITY + MUTATION_PROBABILITY) {
                    AntAction originalParent = selection.select(population);
                    AntAction parent = originalParent.duplicate();
                    parent.setDepth(1);

                    int index = rand.nextInt(parent.getTotalDepth() - 1) + 1;

                    AntAction connector = parent;

                    for (int j = 0; j < index; j++) {
                        int current = rand.nextInt(connector.getDirectChildren());

                        AntAction child = connector.getChild(current);

                        if (child.getDirectChildren() == 0) {
                            if (rand.nextBoolean()) {
                                connector.setChild(current,
                                        mutator.growTree(MAX_TOTAL_DEPTH - connector.getDepth()));
                            }

                            break;
                        }

                        connector = child;
                    }

                    if (parent.getNumberOfChildren() > MAX_NUMBER_OF_NODES) {
                        parent = originalParent.duplicate();
                    }

                    children[i] = parent;
                    parent.setDepth(1);
                    ant.run(parent);
                    ant.checkForPlagiarism(originalParent);
                } else {
                    AntAction firstParent = selection.select(population);
                    AntAction secondParent;

                    do {
                        secondParent = selection.select(population);
                    } while (firstParent == secondParent);

                    AntAction[] cross = crossover.cross(firstParent, secondParent);

                    if (cross[0].getDirectChildren() > MAX_NUMBER_OF_NODES) {
                        AntAction child = firstParent.duplicate();

                        child.setFitness(firstParent.getFitness());
                        child.setFood(firstParent.getFood());
                        child.setDepth(1);

                        children[i] = child;
                    } else {
                        cross[0].setDepth(1);

                        if (cross[1].getTotalDepth() > MAX_TOTAL_DEPTH) {
                            AntAction child = firstParent.duplicate();

                            child.setFitness(firstParent.getFitness());
                            child.setFood(firstParent.getFood());
                            child.setDepth(1);

                            children[i] = child;
                        }

                        children[i] = cross[0];
                        ant.run(cross[0]);
                        ant.checkForPlagiarism(firstParent);
                    }

                    i++;

                    if (i == populationSize) {
                        continue;
                    }

                    if (cross[1].getDirectChildren() > MAX_NUMBER_OF_NODES) {
                        AntAction child = secondParent.duplicate();

                        child.setFitness(secondParent.getFitness());
                        child.setFood(secondParent.getFood());
                        child.setDepth(1);

                        children[i] = child;
                    } else {
                        cross[1].setDepth(1);

                        if (cross[1].getTotalDepth() > MAX_TOTAL_DEPTH) {
                            AntAction child = secondParent.duplicate();

                            child.setFitness(secondParent.getFitness());
                            child.setFood(secondParent.getFood());
                            child.setDepth(1);

                            children[i] = child;
                        }

                        children[i] = cross[1];
                        ant.run(cross[1]);
                        ant.checkForPlagiarism(secondParent);
                    }
                }
            }

            System.arraycopy(children, 0, population, 0, populationSize);
            Arrays.sort(population);

            bestSolution = population[0];

            iter++;

            System.out.println("Iteration " + iter + ", best fitness: " + bestSolution.getFitness() + ", best food: "
                    + bestSolution.getFood());
        }

        return bestSolution;
    }

    /**
     * Generates initial population using ramped-half-and-half method.
     */
    private void initializePopulation() {
        population = new AntAction[populationSize];

        int maxDepth = 0;

        for (int i = 0; i < populationSize; i++) {
            if (i % 2 == 0) {
                maxDepth %= (MAX_INITIAL_DEPTH - 1);

                population[i] = mutator.fullTree(maxDepth + 2);
            } else {
                AntAction child = mutator.growTree(maxDepth + 2);

                if (child.getDirectChildren() == 0) {
                    i--;
                    continue;
                }

                population[i] = child;
                maxDepth++;
            }

            population[i].setDepth(1);
            ant.run(population[i]);
            population[i].setFood(ant.getTotalFood());
            population[i].setFitness(population[i].getFood());
        }

        Arrays.sort(population);
    }
}
