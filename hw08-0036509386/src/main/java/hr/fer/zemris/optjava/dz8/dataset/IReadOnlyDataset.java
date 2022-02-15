package hr.fer.zemris.optjava.dz8.dataset;

import hr.fer.zemris.optjava.dz8.network.NeuralNetwork;

/**
 * An interface that models a dataset used to train a {@link NeuralNetwork}.
 *
 * @author Mateo Imbrišak
 */

public interface IReadOnlyDataset {

    /**
     * Provides input at requested {@code index}.
     *
     * @param index of requested input.
     *
     * @return input at the given {@code index}.
     */
    double[] getInput(int index);

    /**
     * Provides output at requested {@code index}.
     *
     * @param index of requested output.
     *
     * @return output at the given {@code index}.
     */
    double getOutput(int index);

    /**
     * Provides the size of this dataset.
     *
     * @return size of this dataset.
     */
    int getSize();
}
