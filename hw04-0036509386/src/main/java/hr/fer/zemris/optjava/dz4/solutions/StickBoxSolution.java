package hr.fer.zemris.optjava.dz4.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@link Solution} that represents a configuration of
 * a box of 1-dimensional sticks.
 *
 * @author Mateo Imbrišak
 */

public class StickBoxSolution implements Solution {

    /**
     * Maximum height of boxes.
     */
    private static final int MAXIMUM_HEIGHT = 20;

    /**
     * Keeps the sticks in the box.
     */
    private List<Integer> sticks;

    /**
     * Length of the box.
     */
    private double fitness;

    /**
     * Default constructor that adds all values from the given {@code array}
     * to the configuration.
     *
     * @param sticks to be added.
     */
    public StickBoxSolution(int[] sticks) {
        this.sticks = Arrays.stream(sticks).boxed().collect(Collectors.toList());
        calculateFitness();
    }

    /**
     * Default constructor that adds all values from the given {@link List}
     * to the configuration.
     *
     * @param sticks to be added.
     */
    public StickBoxSolution(List<Integer> sticks) {
        this.sticks = new ArrayList<>(sticks);
        calculateFitness();
    }

    /**
     * Constructor used for {@link #duplicate()} to skip {@link #fitness} calculation.
     *
     * @param sticks to be assigned.
     * @param fitness to be assigned.
     */
    private StickBoxSolution(List<Integer> sticks, double fitness) {
        this.sticks = sticks;
        this.fitness = fitness;
    }

    /**
     * Duplicates this {@code StickBoxSolution}.
     *
     * @return duplicate of this solution.
     */
    public StickBoxSolution duplicate() {
        List<Integer> copy = new ArrayList<>(sticks);

        return new StickBoxSolution(copy, fitness);
    }

    /**
     * Provides the total number of sticks.
     *
     * @return total number of sticks.
     */
    public int getNumberOfSticks() {
        return sticks.size();
    }

    /**
     * Provides the column of sticks at specified {@code index}.
     *
     * @param index of requested column.
     *
     * @return {@code array} representing the column of sticks ak the requested {@code index}.
     */
    public int[] getStickColumn(int index) {
        if (fitness <= index) {
            throw new IndexOutOfBoundsException("Index cannot be greater than current fitness.");
        }

        int currentIndex = 0, counter = -1, sum = 0;

        if (index != 0) {
            while (currentIndex != index) {
                counter++;
                int stick = sticks.get(counter);

                sum += counter;

                if (sum > MAXIMUM_HEIGHT) {
                    currentIndex++;
                    sum = stick;
                }
            }
        } else {
            counter = 0;
        }

        List<Integer> column = new ArrayList<>();
        sum = 0;

        for (; counter < sticks.size(); counter++) {
            int stick = sticks.get(counter);

            sum += stick;

            if (sum > MAXIMUM_HEIGHT) {
                break;
            }

            column.add(sticks.get(counter));
        }

        return convertToArray(column);
    }

    /**
     * Inserts given column of sticks to the backs of this configuration
     * and removes sticks in the column from their previous positions.
     *
     * @param column to be inserted.
     */
    public void insertColumn(int[] column) {
        for (Integer stick : column) {
            sticks.remove(stick);
            sticks.add(stick);
        }

        calculateFitness();
    }

    /**
     * Provides the stick configuration as a {@link List}.
     *
     * @return {@link List} representing the current stick configuration.
     */
    public List<Integer> toList() {
        return new ArrayList<>(sticks);
    }

    /**
     * Converts a {@link List} of {@link Integer} objects
     * to an array of primitive {@code int} types.
     *
     * @param list to be converted.
     *
     * @return an array of {@code int}s based in the given {@link List}.
     */
    private int[] convertToArray(List<Integer> list) {
        int size = list.size();
        int[] ret = new int[size];

        for (int i = 0; i < size; i++) {
            ret[i] = list.get(i);
        }

        return ret;
    }

    /**
     * Used internally to calculate {@link #fitness} after
     * initialization and changes.
     */
    private void calculateFitness() {
        fitness = 1;
        int sum = 0;

        for (int stick : sticks) {
            sum += stick;

            if (sum > MAXIMUM_HEIGHT) {
                fitness++;
                sum = stick;
            }
        }
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StickBoxSolution that = (StickBoxSolution) o;
        return sticks.equals(that.sticks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sticks);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int sum = 0;

        for (int stick : sticks) {
            sum += stick;

            if (sum > MAXIMUM_HEIGHT) {
                sb.append("\n").append(stick).append(" ");
                sum = stick;
            } else {
                sb.append(stick).append(" ");
            }
        }

        return sb.toString();
    }
}
