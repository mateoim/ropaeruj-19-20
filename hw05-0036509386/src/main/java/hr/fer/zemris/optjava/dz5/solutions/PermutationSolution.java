package hr.fer.zemris.optjava.dz5.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A {@link Solution} used to model a permutation.
 *
 * @author Mateo Imbrišak
 */

public class PermutationSolution implements Solution {

    /**
     * Keeps the configuration of this {@link Solution}.
     */
    private int[] permutation;

    /**
     * Current fitness of this solution.
     */
    private double fitness;

    /**
     * Default constructor that assigns {@link #permutation} values.
     *
     * @param permutation values that represent this {@link Solution}.
     */
    public PermutationSolution(int[] permutation) {
        this.permutation = permutation;
    }

    /**
     * Private constructor used for {@link #duplicate()}.
     *
     * @param permutation to be assigned.
     * @param fitness to be assigned.
     */
    private PermutationSolution(int[] permutation, double fitness) {
        this.permutation = permutation;
        this.fitness = fitness;
    }

    /**
     * Creates a new random {@code PermutationSolution} consisting of numbers
     * form {@code 0} up to and including {@code size}.
     *
     * @param size of the solution and the highest number in the permutation.
     * @param rand used to generate random values.
     *
     * @return new randomized {@code PermutationSolution}.
     */
    public static PermutationSolution randomize(int size, Random rand) {
        int[] values = new int[size];
        List<Integer> list = new ArrayList<>(size);

        for (int i = 1; i <= size; i++) {
            list.add(i);
        }

        for (int i = 0; i < size - 1; i++) {
            values[i] = list.remove(rand.nextInt(list.size()));
        }

        values[size - 1] = list.remove(0);

        return new PermutationSolution(values);
    }

    /**
     * Creates an identical copy of this {@code PermutationSolution}.
     *
     * @return identical copy of this {@code PermutationSolution}.
     */
    public PermutationSolution duplicate() {
        return new PermutationSolution(Arrays.copyOf(permutation, permutation.length), fitness);
    }

    /**
     * Provides the value at the requested index.
     *
     * @param index whose value is requested.
     *
     * @return value at the requested index.
     */
    public int get(int index) {
        return permutation[index];
    }

    /**
     * Swaps the elements at given indexes.
     *
     * @param firstIndex to be swapped.
     * @param secondIndex to be swapped.
     */
    public void swap(int firstIndex, int secondIndex) {
        int temp = permutation[firstIndex];
        permutation[firstIndex] = permutation[secondIndex];
        permutation[secondIndex] = temp;
    }

    /**
     * Provides the size of this {@link Solution}.
     *
     * @return size of this solution.
     */
    public int size() {
        return permutation.length;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        return Arrays.toString(permutation);
    }
}
