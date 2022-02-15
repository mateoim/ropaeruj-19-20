package hr.fer.zemris.optjava.dz3.solutions;

/**
 * A class that represents a solution
 * to a problem with single objective.
 *
 * @author Mateo Imbrišak
 */

public class SingleObjectiveSolution implements Comparable<SingleObjectiveSolution> {

    /**
     * Keeps the fitness value of this solution.
     */
    public double fitness;

    /**
     * Keeps the calculated value of this solution.
     */
    public double value;

    /**
     * Default constructor.
     */
    public SingleObjectiveSolution() {}

    @Override
    public int compareTo(SingleObjectiveSolution o) {
        return Double.compare(fitness, o.fitness);
    }
}
