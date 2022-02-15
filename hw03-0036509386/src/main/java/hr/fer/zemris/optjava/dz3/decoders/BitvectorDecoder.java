package hr.fer.zemris.optjava.dz3.decoders;

import hr.fer.zemris.optjava.dz3.solutions.BitvectorSolution;

import java.util.Arrays;

/**
 * An abstract implementation
 * of {@link IDecoder} used to
 * decode {@link BitvectorSolution}s.
 *
 * @author Mateo Imbrišak
 */

public abstract class BitvectorDecoder implements IDecoder<BitvectorSolution> {

    /**
     * Contains minimum value for each variable.
     */
    protected double[] mins;

    /**
     * Contains maximum value for each variable.
     */
    protected double[] maxs;

    /**
     * Keeps currently processed bits.
     */
    protected int[] bits;

    /**
     * Amount of bits used for a single variable.
     */
    protected int n;

    /**
     * Total number of bits.
     */
    protected int totalBits;

    /**
     * constructor that assigns all values.
     *
     * @param mins minimum value for each variable.
     * @param maxs maximum value for each variable.
     * @param n amount of bits used for a single variable.
     * @param totalBits total number of bits.
     */
    public BitvectorDecoder(double[] mins, double[] maxs, int n, int totalBits) {
        this.mins = mins;
        this.maxs = maxs;
        this.totalBits = totalBits;
        this.n = n;
    }

    /**
     * constructor that assigns all values.
     *
     * @param min minimum value used for all variable.
     * @param max maximum value used for all variable.
     * @param n amount of bits used for a single variable.
     * @param totalBits total number of bits.
     */
    public BitvectorDecoder(double min, double max, int n, int totalBits) {
        this.mins = new double[totalBits / n];
        Arrays.fill(mins, min);
        this.maxs = new double[totalBits / n];
        Arrays.fill(maxs, max);
        this.n = n;
        this.totalBits = totalBits;
    }

    /**
     * Provides the {@link #totalBits}.
     *
     * @return {@link #totalBits}.
     */
    public int getTotalBits() {
        return totalBits;
    }

    /**
     * Provides the dimension {@link #n}.
     *
     * @return dimension {@link #totalBits}.
     */
    public int getDimensions() {
        return n;
    }
}
