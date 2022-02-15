package hr.fer.zemris.optjava.mutator;

import hr.fer.zemris.generic.ga.ArrayGASolution;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * A {@link Mutator} that mutates rectangles in {@link ArrayGASolution}s.
 *
 * @author Mateo Imbrišak
 */

public class RectangleMutator implements Mutator<GASolution<int[]>> {

    /**
     * Probability to mutate a rectangle.
     */
    private static final double PROBABILITY_TO_MUTATE = 0.15;

    /**
     * Lower bound used to mutate bytes.
     */
    private static final int BYTE_LOWER_BOUND = -5;

    /**
     * Upper bound used to mutate bytes.
     */
    private static final int BYTE_UPPER_BOUND = 5;

    /**
     * Lower bound used to mutate integers.
     */
    private static final int INT_LOWER_BOUND = -10;

    /**
     * Upper bound used to mutate integers.
     */
    private static final int INT_UPPER_BOUND = 10;

    /**
     * Height of the image.
     */
    private final int height;

    /**
     * Width of the image.
     */
    private final int width;

    /**
     * Default constructor that assigns constraints.
     *
     * @param height of the image.
     * @param width of the image.
     */
    public RectangleMutator(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public GASolution<int[]> mutate(GASolution<int[]> solution) {
        GASolution<int[]> child = solution.duplicate();
        IRNG rand = RNG.getRNG();
        int[] childValues = child.getData();
        int solutionSize = childValues.length;

        boolean changed = false;

        if (rand.nextDouble() <= PROBABILITY_TO_MUTATE) {
            childValues[0] += rand.nextInt(BYTE_LOWER_BOUND, BYTE_UPPER_BOUND);
            changed = true;
        }

        int index = 1;

        for (int i = 0, size = (solutionSize - 1) / 5; i < size; i++) {
            if (rand.nextDouble() <= PROBABILITY_TO_MUTATE) {
                changed = true;
                int toChange = rand.nextInt(0, 5);

                if (toChange == 4) {
                    int value = childValues[index + toChange] + rand.nextInt(BYTE_LOWER_BOUND, BYTE_UPPER_BOUND);
                    childValues[index + toChange] = changeByte(value);
                } else {
                    int value = childValues[index + toChange] + rand.nextInt(INT_LOWER_BOUND, INT_UPPER_BOUND);
                    childValues[index + toChange] = changeInt(value, toChange);
                }
            }

            index += 5;
        }

        if (!changed) {
            int toChange = rand.nextInt(0, solutionSize);

            if (toChange == 0 || (toChange - 1) % 5 == 4) {
                int value = childValues[index + toChange] + rand.nextInt(BYTE_LOWER_BOUND, BYTE_UPPER_BOUND);
                childValues[index + toChange] = changeByte(value);
            } else {
                int value = childValues[index + toChange] + rand.nextInt(INT_LOWER_BOUND, INT_UPPER_BOUND);
                childValues[index + toChange] = changeInt(value, toChange);
            }
        }

        return child;
    }

    /**
     * Checks constraints for bytes.
     *
     * @param value being checked.
     *
     * @return value to assign.
     */
    private int changeByte(int value) {
        if (value > Byte.MAX_VALUE) {
            return Byte.MAX_VALUE;
        } else if (value < Byte.MIN_VALUE) {
            return Byte.MIN_VALUE;
        }

        return value;
    }

    /**
     * Checks constraints for integers.
     *
     * @param value being checked.
     * @param position in the rectangle.
     *
     * @return value to assign.
     */
    private int changeInt(int value, int position) {
        if (value < 0) {
            return 0;
        }

        if (position == 0 && value > height) {
            return height;
        } else if (position == 1 && value > width) {
            return width;
        }

        return value;
    }
}
