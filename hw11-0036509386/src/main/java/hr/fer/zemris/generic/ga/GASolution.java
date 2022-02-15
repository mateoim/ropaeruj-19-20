package hr.fer.zemris.generic.ga;

/**
 * A class that represents a generic solution.
 *
 * @author marcupic
 */

public abstract class GASolution<T> implements Comparable<GASolution<T>> {

    /**
     * Keeps the data for this solution.
     */
    protected T data;

    /**
     * Keeps the fitness of this solution.
     */
    public double fitness;

    /**
     * Default constructor.
     */
    public GASolution() {}

    /**
     * Provides current data;
     *
     * @return current {@link #data}.
     */
    public T getData() {
        return data;
    }

    /**
     * Creates a copy of this solution with same {@link #data} and {@link #fitness}.
     *
     * @return new instance with same {@link #data} and {@link #fitness} values.
     */
    public abstract GASolution<T> duplicate();

    @Override
    public int compareTo(GASolution<T> o) {
        return -Double.compare(this.fitness, o.fitness);
    }
}
