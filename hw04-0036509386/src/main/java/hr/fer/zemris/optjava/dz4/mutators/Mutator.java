package hr.fer.zemris.optjava.dz4.mutators;

/**
 * An interface that models a mutator used to
 * mutate solutions.
 *
 * @param <T> type of solution used.
 *
 * @author Mateo Imbrišak
 */

public interface Mutator<T> {

    /**
     * Creates a mutation of the given {@code solution}.
     *
     * @param solution used to create the mutation.
     *
     * @return mutated solution.
     */
    T mutate(T solution);
}
