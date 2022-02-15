package hr.fer.zemris.optjava.dz7;

/**
 * A class that represents a Sigmoid {@link ITransferFunction}
 * used to calculate outputs on neurons in {@link FFANN}.
 *
 * @author Mateo Imbrišak
 */

public class SigmoidTransferFunction implements ITransferFunction {

    @Override
    public double valueAt(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}
