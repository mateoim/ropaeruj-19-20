package hr.fer.zemris.optjava.dz8.network;

import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataset;
import hr.fer.zemris.optjava.dz8.function.Function;

import java.util.Arrays;

/**
 * A class that models an Elman {@link NeuralNetwork}.
 *
 * @author Mateo Imbrišak
 */

public class ElmanNN implements NeuralNetwork {

    /**
     * Keeps information about the layers of neural network.
     */
    private final int[] layers;

    /**
     * Keeps {@link Function} used to convert calculated
     * values to output used by the neurons.
     */
    private final Function function;

    /**
     * Keeps the network's calculated weights count.
     */
    private final int weightsCount;

    /**
     * Keeps the values of the context layer.
     */
    private double[] contextLayer;

    /**
     * Default constructor that assigns all values.
     *
     * @param layers provides information about layer configuration.
     * @param function used to calculate output of a neuron in a given layer.
     */
    public ElmanNN(int[] layers, Function function) {
        this.layers = layers;
        this.function = function;

        weightsCount = calculateWeightsCount();
    }

    @Override
    public int getWeightsCount() {
        return weightsCount;
    }

    @Override
    public void calcOutputs(IReadOnlyDataset dataset, double[] weights, double[] outputs) {
        int outputIndex = 0;
        int contextLength = layers[1];
        contextLayer = new double[contextLength];

        System.arraycopy(weights, weights.length - contextLength, contextLayer, 0, contextLength);

        for (int i = 0, size = dataset.getSize(); i < size; i++) {
            double[] currentInput = Arrays.copyOf(dataset.getInput(i), dataset.getInput(i).length);


            for (int index = 0; index < layers.length; index++) {
                currentInput = calculateLayer(index, weights, currentInput);

                if (index == 1) {
                    contextLayer = Arrays.copyOf(currentInput, currentInput.length);
                }
            }

            for (double v : currentInput) {
                outputs[outputIndex++] = v;
            }
        }
    }

    /**
     * Calculates the output of the given layer.
     *
     * @param index of the layer.
     * @param weights used to calculate the output.
     * @param input being fed to the layer.
     *
     * @return output of the requested layer.
     */
    private double[] calculateLayer(int index, double[] weights, double[] input) {
        if (index == 0) {
            return input;
        }

        int size = layers[index];
        double[] ret = new double[size];

        int previousSize = layers[index - 1];
        int baseIndex = calculateBaseIndex(index);

        for (int i = 0; i < previousSize; i++) {
            for (int j = 0; j < size; j++) {
                ret[j] += input[i] * weights[baseIndex++];
            }
        }

        if (index == 1) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    ret[j] += contextLayer[i] * weights[baseIndex++];
                }
            }
        }

        for (int i = 0; i < size; i++) {
            ret[i] = function.valueAt(ret[i] + weights[i]);
        }

        return ret;
    }

    /**
     * Calculates the base index of the given layer.
     *
     * @param index of the layer whose base index is being calculated.
     *
     * @return base index of requested layer.
     */
    private int calculateBaseIndex(int index) {
        if (index == 1) {
            return 0;
        }

        int base = layers[1] * layers[1];

        for (int i = 1; i < index; i++) {
            base += layers[i] * layers[i - 1] + layers[i];
        }

        return base;
    }

    /**
     * Used internally to initialize {@link #weightsCount}.
     *
     * @return value used for {@link #weightsCount}.
     */
    private int calculateWeightsCount() {
        int count = layers[1] * layers[1];

        for (int i = 0; i < layers.length - 1; i++) {
            count += layers[i] * layers[i + 1] + (i == 0 ? 0 : layers[i]);
        }

        return count + layers[layers.length - 1] + layers[1];
    }
}
