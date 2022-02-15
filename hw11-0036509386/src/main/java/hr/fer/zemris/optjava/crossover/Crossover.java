package hr.fer.zemris.optjava.crossover;

/**
 * An interface that models crossover operators.
 *
 * @param <T> type of solution used.
 *
 * @author Mateo Imbrišak
 */

public interface Crossover<T> {

    /**
     * Creates a child based in the given parents.
     *
     * @param firstParent used to generate a child.
     * @param secondParent used to generate a child.
     *
     * @return a new {@code Object} type {@code T}
     * created from both parents.
     */
    T cross(T firstParent, T secondParent);
}
