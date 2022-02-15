package hr.fer.zemris.optjava.dz8.network;

import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataset;

/**
 * An interface that models a neural network,
 *
 * @author Mateo Imbrišak
 */

public interface NeuralNetwork {

    /**
     * Provides the number of weights in this network.
     *
     * @return number of weights in this network.
     */
    int getWeightsCount();

    /**
     * Calculate outputs of this network and write them to {@code outputs} array.
     *
     * @param dataset used to get inputs.
     * @param weights used to calculate {@code outputs}.
     * @param outputs array used to store calculated outputs.
     */
    void calcOutputs(IReadOnlyDataset dataset, double[] weights, double[] outputs);
}
