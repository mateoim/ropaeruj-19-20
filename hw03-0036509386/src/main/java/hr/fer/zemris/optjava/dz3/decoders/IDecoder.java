package hr.fer.zemris.optjava.dz3.decoders;

/**
 * An interface used to model solution decoders.
 *
 * @author Mateo Imbrišak
 */

public interface IDecoder<T> {

    /**
     * Decodes the given object.
     *
     * @param solution to be decoded.
     *
     * @return an {@code array} representing the decoded solution.
     */
    double[] decode(T solution);

    /**
     * Decodes the given object and puts
     * the result in the given {@code array}.
     *
     * @param solution to be decoded.
     * @param destination to be filled with the decoded solution.
     */
    void decode(T solution, double[] destination);
}
