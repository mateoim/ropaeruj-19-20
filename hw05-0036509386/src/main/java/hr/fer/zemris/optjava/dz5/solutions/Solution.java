package hr.fer.zemris.optjava.dz5.solutions;

/**
 * An interface that models a solution
 * for an algorithm.
 *
 * @author Mateo Imbrišak
 */

public interface Solution extends Comparable<Solution> {

    /**
     * Provides current fitness of the solution.
     *
     * @return current fitness.
     */
    double getFitness();

    /**
     * Assigns given {@code fitness} to this {@code Solution}.
     *
     * @param fitness to be assigned.
     */
    void setFitness(double fitness);

    @Override
    default int compareTo(Solution o) {
        return Double.compare(this.getFitness(), o.getFitness());
    }
}
