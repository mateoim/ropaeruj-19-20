package hr.fer.zemris.optjava.dz3.neighbourhoods;

/**
 * An interface used to generate
 * neighbours of an object.
 *
 * @author Mateo Imbrišak
 */

public interface INeighbourhood<T> {

    /**
     * Generates a random neighbour.
     *
     * @param solution used to generate the neighbour.
     *
     * @return a random neighbour.
     */
    T randomNeighbour(T solution);
}
