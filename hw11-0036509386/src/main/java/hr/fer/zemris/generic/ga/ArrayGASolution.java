package hr.fer.zemris.generic.ga;

import java.util.Arrays;

/**
 * @author Mateo Imbrišak
 */

public class ArrayGASolution extends GASolution<int[]> {

    public ArrayGASolution(int size) {
        data = new int[size];
    }

    public ArrayGASolution(int[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    private ArrayGASolution(int[] data, double fitness) {
        this.fitness = fitness;
        this.data = Arrays.copyOf(data, data.length);
    }

    @Override
    public GASolution<int[]> duplicate() {
        return new ArrayGASolution(data, fitness);
    }
}
