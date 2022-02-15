package hr.fer.zemris.optjava.selection;

import java.util.List;

/**
 * An interface used to model selections
 * used to select parents from a given population.
 *
 * @author Mateo Imbrišak
 */

public interface Selection<T> {

    /**
     * Selects a solution from the given {@code population} to be used as a parent.
     *
     * @param population used as pool from which to select the parent.
     *
     * @return selected parent.
     */
    T select(List<T> population);
}
