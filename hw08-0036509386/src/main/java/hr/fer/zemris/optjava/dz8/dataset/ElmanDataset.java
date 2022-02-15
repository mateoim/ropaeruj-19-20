package hr.fer.zemris.optjava.dz8.dataset;

import hr.fer.zemris.optjava.dz8.network.ElmanNN;

import java.util.Arrays;

/**
 * A {@link IReadOnlyDataset} used to train {@link ElmanNN}.
 *
 * @author Mateo Imbrišak
 */

public class ElmanDataset implements IReadOnlyDataset {

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
     * based on the given {@code data}.
     *
     * @param data used to create the set.
     */
    public ElmanDataset(double[] data) {
        outputs = Arrays.copyOf(data, data.length - 1);
        double[][] out = new double[data.length - 1][1];

        for (int i = 1; i < data.length; i++) {
            out[i - 1] = new double[] {data[i]};
        }

        inputs = out;
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
