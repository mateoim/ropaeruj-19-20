package hr.fer.zemris.optjava.dz3.cooling;

/**
 * An implementation of {@link ITempSchedule}
 * that represents geometric cooling.
 *
 * @author Mateo Imbrišak
 */

public class GeometricTempSchedule implements ITempSchedule {

    /**
     * Used to scale temperature.
     */
    private final double alpha;

    /**
     * Initial assigned temperature.
     */
    private final double tInitial;

    /**
     * Counter used when calculating {@link #getNextTemperature()}.
     */
    private double tCurrent;

    /**
     * Number of iterations with a single temperature.
     */
    private final int innerLimit;

    /**
     * Number of times temperature should change.
     */
    private final int outerLimit;

    /**
     * Default constructor that assigns all values.
     *
     * @param alpha used to scale temperature.
     * @param tInitial to be assigned.
     * @param innerLimit iterations with a single temperature.
     * @param outerLimit total iterations.
     */
    public GeometricTempSchedule(double alpha, double tInitial, int innerLimit, int outerLimit) {
        this.alpha = alpha;
        this.tInitial = tInitial;
        this.innerLimit = innerLimit;
        this.outerLimit = outerLimit;
        tCurrent = 0;
    }

    @Override
    public double getNextTemperature() {
        return tInitial * Math.pow(alpha, tCurrent++);
    }

    @Override
    public int getInnerLoopCounter() {
        return innerLimit;
    }

    @Override
    public int getOuterLoopCounter() {
        return outerLimit;
    }
}
