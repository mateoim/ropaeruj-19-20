package hr.fer.zemris.optjava.rng.rngimpl;

import hr.fer.zemris.optjava.rng.IRNG;

import java.util.Random;

/**
 * An implementation of {@link IRNG} that user {@link Random}
 * to generate random values.
 *
 * @author Mateo Imbrišak
 */

public class RNGRandomImpl implements IRNG {

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that initializes {@link #rand}.
     */
    public RNGRandomImpl() {
        rand = new Random();
    }

    @Override
    public double nextDouble() {
        return rand.nextDouble();
    }

    @Override
    public double nextDouble(double min, double max) {
        return min + rand.nextDouble() * (max - min);
    }

    @Override
    public float nextFloat() {
        return rand.nextFloat();
    }

    @Override
    public float nextFloat(float min, float max) {
        return min + rand.nextFloat() * (max - min);
    }

    @Override
    public int nextInt() {
        return rand.nextInt();
    }

    @Override
    public int nextInt(int min, int max) {
        return min + rand.nextInt(max - min);
    }

    @Override
    public boolean nextBoolean() {
        return rand.nextBoolean();
    }

    @Override
    public double nextGaussian() {
        return rand.nextGaussian();
    }
}
