package hr.fer.zemris.optjava.dz8.function;

import hr.fer.zemris.optjava.dz8.network.NeuralNetwork;

/**
 * A class that represents a Sigmoid {@link Function}
 * used to calculate outputs on neurons in {@link NeuralNetwork}.
 *
 * @author Mateo Imbrišak
 */

public class SigmoidTransferFunction implements Function {

    @Override
    public double valueAt(double x) {
        double exp = Math.exp(-x);
        return (1 - exp) / (1 + exp);
    }
}
