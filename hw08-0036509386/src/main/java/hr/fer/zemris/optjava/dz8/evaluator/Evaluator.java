package hr.fer.zemris.optjava.dz8.evaluator;

import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataset;
import hr.fer.zemris.optjava.dz8.network.NeuralNetwork;

/**
 * A class used by algorithms to evaluate error for a given set of weights.
 *
 * @author Mateo Imbrišak
 */

public class Evaluator {

    /**
     * Keeps the {@link NeuralNetwork} being evaluated.
     */
    private final NeuralNetwork network;

    /**
     * Keeps the dataset used to train the network.
     */
    private final IReadOnlyDataset dataset;

    /**
     * Default constructor that assigns all values.
     *
     * @param network to be evaluated.
     * @param dataset used to train the network.
     */
    public Evaluator(NeuralNetwork network, IReadOnlyDataset dataset) {
        this.network = network;
        this.dataset = dataset;
    }

    /**
     * Calculates the error for the given {@code weights}.
     *
     * @param weights used by the {@link #network}.
     *
     * @return calculated error.
     */
    public double calculateError(double[] weights) {
        double[] outputs = new double[dataset.getSize()];

        network.calcOutputs(dataset, weights, outputs);

        double error = 0;

        for (int i = 0, size = outputs.length; i < size; i++) {
            double delta = dataset.getOutput(i) - outputs[i];
            error += delta * delta;
        }

        return error / dataset.getSize();
    }

    /**
     * Provides {@link #network}'s weights count.
     *
     * @return {@link #network}'s weights count.
     */
    public int getWeightsCount() {
        return network.getWeightsCount();
    }
}
