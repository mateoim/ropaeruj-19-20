package hr.fer.zemris.optjava.dz2.functions;

import hr.fer.zemris.optjava.dz2.IHFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * A class that represents function
 * f(x1, x2) = (x1 - 1)^2 + 10 * (x2 - 2)^2
 *
 * @author Mateo Imbrišak
 */

public class Function2 implements IHFunction {

    @Override
    public RealMatrix getHesseMatrix(RealVector point) {
        double[][] values = {{2, 0}, {0, 20}};
        return new Array2DRowRealMatrix(values);
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }

    @Override
    public double calculateValue(RealVector point) {
        return Math.pow(point.getEntry(0) - 1, 2) + 10 * Math.pow(point.getEntry(1) - 2, 2);
    }

    @Override
    public RealVector calculateGradient(RealVector point) {
        double[] values = {2 * (point.getEntry(0) - 1), 20 * (point.getEntry(1) - 2)};
        return new ArrayRealVector(values);
    }
}
