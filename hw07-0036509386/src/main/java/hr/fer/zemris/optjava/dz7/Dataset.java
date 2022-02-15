package hr.fer.zemris.optjava.dz7;

/**
 * @author Mateo Imbrišak
 */
public class Dataset implements IReadOnlyDataset {

    private final int inputDimension;

    private final int outputDimension;

    private final double[] inputs;

    private final double[] outputs;

    public Dataset(int inputDimension, int outputDimension, double[] inputs, double[] outputs) {
        this.inputDimension = inputDimension;
        this.outputDimension = outputDimension;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    @Override
    public double[] getInputs() {
        return inputs;
    }

    @Override
    public double[] getOutputs() {
        return outputs;
    }

    @Override
    public int getInputDimension() {
        return inputDimension;
    }

    @Override
    public int getOutputDimension() {
        return outputDimension;
    }
}
