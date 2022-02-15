package hr.fer.zemris.optjava.dz7;

/**
 * @author Mateo Imbrišak
 */

public interface IReadOnlyDataset {

    double[] getInputs();

    double[] getOutputs();

    int getInputDimension();

    int getOutputDimension();
}
