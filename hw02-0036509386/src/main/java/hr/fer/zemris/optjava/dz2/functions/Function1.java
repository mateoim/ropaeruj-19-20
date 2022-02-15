package hr.fer.zemris.optjava.dz2.functions;

import hr.fer.zemris.optjava.dz2.IHFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * A class that represents function
 * f(x1, x2) = x1^2 + (x2 - 1)^2
 *
 * @author Mateo Imbrišak
 */

public class Function1 implements IHFunction {

    @Override
    public RealMatrix getHesseMatrix(RealVector point) {
        double[][] values = {{2, 0}, {0, 2}};
        return new Array2DRowRealMatrix(values);
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }

    @Override
    public double calculateValue(RealVector point) {
        return point.getEntry(0) * point.getEntry(0) + Math.pow(point.getEntry(1) - 1, 2);
    }

    @Override
    public RealVector calculateGradient(RealVector point) {
        double[] values = {2 * point.getEntry(0), 2 * (point.getEntry(1) - 1)};
        return new ArrayRealVector(values);
    }
}
