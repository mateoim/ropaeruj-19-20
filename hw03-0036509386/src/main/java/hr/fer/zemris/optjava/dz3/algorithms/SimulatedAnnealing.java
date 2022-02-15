package hr.fer.zemris.optjava.dz3.algorithms;

import hr.fer.zemris.optjava.dz3.functions.IFunction;
import hr.fer.zemris.optjava.dz3.cooling.ITempSchedule;
import hr.fer.zemris.optjava.dz3.decoders.IDecoder;
import hr.fer.zemris.optjava.dz3.neighbourhoods.INeighbourhood;
import hr.fer.zemris.optjava.dz3.solutions.SingleObjectiveSolution;

import java.util.Arrays;
import java.util.Random;

/**
 * An {@link IOptAlgorithm} based on metal annealing.
 *
 * @author Mateo Imbrišak
 */

public class SimulatedAnnealing<T extends SingleObjectiveSolution> implements IOptAlgorithm<T> {

    /**
     * Used to decode the current solution.
     */
    private IDecoder<T> decoder;

    /**
     * Used to generate random neighbours.
     */
    private INeighbourhood<T> neighbourhood;

    /**
     * Initial solution.
     */
    private T startsWith;

    /**
     * Function being optimized.
     */
    private IFunction function;

    /**
     * Used to simulate cooling.
     */
    private ITempSchedule schedule;

    /**
     * Used to check whether the function is being minimised.
     */
    private final boolean minimize;

    /**
     * Used to generate random values.
     */
    private Random rand;

    /**
     * Default constructor that assigns all values.
     *
     * @param decoder used to decode solutions.
     * @param neighbourhood used to generate neighbours.
     * @param startsWith initial solution.
     * @param function being optimized.
     * @param schedule used for cooling.
     * @param minimize whether the function is being minimised.
     */
    public SimulatedAnnealing(IDecoder<T> decoder, INeighbourhood<T> neighbourhood, T startsWith,
                              IFunction function, ITempSchedule schedule, boolean minimize) {
        this.decoder = decoder;
        this.neighbourhood = neighbourhood;
        this.startsWith = startsWith;
        this.function = function;
        this.schedule = schedule;
        this.minimize = minimize;
        this.rand = new Random();
    }

    @Override
    public T run() {
        T solution = startsWith;
        initializeSolution(solution);

        for (int outer = 0, outerSize = schedule.getOuterLoopCounter(); outer < outerSize; outer++) {
            double temperature = schedule.getNextTemperature();
            for (int inner = 0, innerSize = schedule.getInnerLoopCounter(); inner < innerSize; inner++) {
                T neighbour = neighbourhood.randomNeighbour(solution);
                initializeSolution(neighbour);
                double delta = solution.fitness - neighbour.fitness;

                if (delta <= 0) {
                    solution = neighbour;
                    System.out.println("Accepted solution: " + Arrays.toString(decoder.decode(solution)) +
                            " with error: " + solution.value + " at temperature: " + temperature);
                } else {
                    if (rand.nextDouble() <= Math.exp(-delta / temperature)) {
                        solution = neighbour;
                        System.out.println("Accepted solution: " + Arrays.toString(decoder.decode(solution)) +
                                " with error: " + solution.value + " at temperature: " + temperature);
                    }
                }
            }
        }

        return solution;
    }

    /**
     * Initializes the given {@code solution} by
     *  calculating {@link SingleObjectiveSolution#fitness}
     *  and {@link SingleObjectiveSolution#value}.
     *
     * @param solution to be initialized.
     */
    private void initializeSolution(T solution) {
        solution.value = function.valueAt(decoder.decode(solution));
        solution.fitness = minimize ? -solution.value : solution.value;
    }
}
