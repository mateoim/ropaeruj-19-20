package hr.fer.zemris.optjava.dz4.solutions;

import java.util.Arrays;
import java.util.Random;

/**
 * A class that defines a solution represented by
 * a {@code double} array {@link #values}.
 *
 * @author Mateo Imbrišak
 */

public class DoubleArraySolution implements Solution {

    /**
     * Keeps the values of this solution.
     */
    private final double[] values;

    /**
     * Keeps calculated value of fitness function.
     */
    private double fitness;

    /**
     * Constructor that assigns an array for {@link #values}.
     *
     * @param values array to be used for values.
     */
    public DoubleArraySolution(double[] values) {
        this.values = values;
    }

    /**
     * Constructor that generates a random {@code DoubleArraySolution}.
     *
     * @param size of the internal {@link #values} array.
     * @param lowBound used to generate values.
     * @param highBound used to generate values.
     */
    public DoubleArraySolution(int size, int lowBound, int highBound) {
        this.values = new double[size];
        Random rand = new Random();

        for (int i = 0; i < size; i ++) {
            values[i] = lowBound + rand.nextDouble() * (highBound - lowBound);
        }
    }

    /**
     * Provides the length of this solution.
     *
     * @return length of the solution.
     */
    public int getLength() {
        return values.length;
    }

    /**
     * Provides the value at requested index.
     *
     * @param index of the requested value.
     *
     * @return value at requested index.
     */
    public double getValue(int index) {
        return values[index];
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    /**
     * Assigns new {@code fitness} to the solution.
     *
     * @param fitness assigned to the solution.
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleArraySolution that = (DoubleArraySolution) o;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}
