package hr.fer.zemris.optjava.dz8.dataset;

import hr.fer.zemris.optjava.dz8.network.TDNN;

/**
 * A {@link IReadOnlyDataset} used to train {@link TDNN}.
 *
 * @author Mateo Imbrišak
 */

public class TDDataset implements IReadOnlyDataset {

    /**
     * Keeps the inputs of this dataset.
     */
    private final double[][] inputs;

    /**
     * Keeps the outputs of this dataset.
     */
    private final double[] outputs;

    /**
     * Default constructor that initializes {@link #inputs} and {@link #outputs}
     * based on the given {@code data} and {@code length} of inputs.
     *
     * @param data used to create the set.
     * @param length of the inputs in the dataset.
     */
    public TDDataset(double[] data, int length) {
        int size = data.length - length;
        double[] out = new double[size];
        double[][] in = new double[size][length];

        System.arraycopy(data, length, out, 0, data.length - length);

        for (int i = 0; i < size; i++) {
            double[] input = new double[length];
            System.arraycopy(data, i, input, 0, length);

            in[i] = input;
        }

        inputs = in;
        outputs = out;
    }

    @Override
    public double[] getInput(int index) {
        return inputs[index];
    }

    @Override
    public double getOutput(int index) {
        return outputs[index];
    }

    @Override
    public int getSize() {
        return inputs.length;
    }
}
