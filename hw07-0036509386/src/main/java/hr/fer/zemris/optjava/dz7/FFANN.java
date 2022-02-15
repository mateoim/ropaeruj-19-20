package hr.fer.zemris.optjava.dz7;

/**
 * A class that models a simple neural network.
 *
 * @author Mateo Imbrišak
 */

public class FFANN {

    /**
     * Keeps information about the layers of neural network.
     */
    private final int[] layers;

    /**
     * Keeps {@link ITransferFunction} used to convert calculated
     * values to output used by the neurons.
     */
    private final ITransferFunction[] functions;

    /**
     * Keeps the dataset used by this neural network.
     */
    private final IReadOnlyDataset dataset;

    /**
     * Keeps the network's calculated weights count.
     */
    private final int weightsCount;

    /**
     * Default constructor that assigns all values.
     *
     * @param layers provides information about layer configuration.
     * @param functions used to calculate output of a neuron in a given layer.
     * @param dataset used to train the network.
     */
    public FFANN(int[] layers, ITransferFunction[] functions, IReadOnlyDataset dataset) {
        this.layers = layers;
        this.functions = functions;
        this.dataset = dataset;

        weightsCount = calculateWeightsCount();
    }

    /**
     * Provides this network's {@link #weightsCount}.
     *
     * @return network's {@link #weightsCount}
     */
    public int getWeightsCount() {
        return weightsCount;
    }

    public void calcOutputs(double[] inputs, double[] weights, double[] outputs) {
        int increment = dataset.getInputDimension();
        int outputIndex = 0;

        for (int outer = 0; outer < dataset.getInputs().length; outer += increment) {
            double[] currentInput = new double[increment];
            System.arraycopy(inputs, outer, currentInput, 0, increment);

            for (int index = 0; index < layers.length; index++) {
                currentInput = calculateLayer(index, weights, currentInput);
            }

            for (double v : currentInput) {
                outputs[outputIndex++] = v;
            }
        }
    }

    public double calculateError(double[] outputs) {
        double error = 0;
        double[] data = dataset.getOutputs();

        for (int i = 0, size = outputs.length; i < size; i++) {
            double delta = data[i] - outputs[i];
            error += delta * delta;
        }

        return error / ((double) dataset.getOutputs().length / dataset.getOutputDimension());
    }

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

        ITransferFunction function = functions[index - 1];

        for (int i = 0; i < size; i++) {
            ret[i] = function.valueAt(ret[i] + weights[i]);
        }

        return ret;
    }

//    private double[] calculateLayer(int index, double[] weights, double[] input) {
//        int size = index == layers.length - 1 ? layers[layers.length - 1] : layers[index + 1];
//        double[] ret = new double[size];
//
//        if (index == 0) {
//            for (int i = 0; i < layers[index]; i++) {
//                for (int j = 0; j < size; j++) {
//                    ret[j] += weights[i * size + j] * input[i];
//                }
//            }
//
//            return ret;
//        }
//
//        int baseIndex = calculateBaseIndex(index);
//        ITransferFunction transferFunction = functions[index - 1];
//
//        int increment = (index == layers.length - 1) ? 0 : layers[index + 1];
//
//        for (int i = 0; i < size; i++) {
//            double value = input[i] + weights[baseIndex++]; // neti
//            value = transferFunction.valueAt(value); // yi
//
//            if (index == layers.length - 1) {
//                ret[i] = value;
//                continue;
//            }
//
//            for (int j = 0; j < increment; j++) {
//                ret[j] += value * weights[baseIndex++]; // yi * w(i, j)
//            }
//        }
//
//        return ret;
//    }

    private int calculateBaseIndex(int index) {
        if (index == 1) {
            return 0;
        }

        int base = 0;

        for (int i = 1; i < index; i++) {
            base += layers[i] * layers[i - 1] + layers[i];
        }

        return base;
    }

    private int calculateWeightsCount() {
        int count = 0;

        for (int i = 0; i < layers.length - 1; i++) {
            count += layers[i] * layers[i + 1] + (i == 0 ? 0 : layers[i]);
        }

        return count + layers[layers.length - 1];
    }

    public IReadOnlyDataset getDataset() {
        return dataset;
    }
}
