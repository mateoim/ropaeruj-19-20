package hr.fer.zemris.optjava.dz8;

import hr.fer.zemris.optjava.dz8.algorithms.DifferentialEvolution;
import hr.fer.zemris.optjava.dz8.dataset.ElmanDataset;
import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataset;
import hr.fer.zemris.optjava.dz8.dataset.TDDataset;
import hr.fer.zemris.optjava.dz8.evaluator.Evaluator;
import hr.fer.zemris.optjava.dz8.function.SigmoidTransferFunction;
import hr.fer.zemris.optjava.dz8.network.ElmanNN;
import hr.fer.zemris.optjava.dz8.network.NeuralNetwork;
import hr.fer.zemris.optjava.dz8.network.TDNN;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A program that attempts to train a {@link NeuralNetwork}.
 *
 * @author Mateo Imbrišak
 */

public class ANNTrainer {

    /**
     * Size of the sample used.
     */
    private static final int SAMPLE_SIZE = 600;

    /**
     * Lower bound used for generating initial population.
     */
    private static final double MIN_INTERVAL = -1d;

    /**
     * Upper bound used for generating initial population.
     */
    private static final double MAX_INTERVAL = 1d;

    /**
     * Probability used while generating trial vectors.
     */
    private static final double MUTATION_PROBABILITY = 0.02;

    /**
     * Factor used to scale the mutant solution/
     */
    private static final double SCALE_FACTOR = 0.9;

    /**
     * Used to start the program.
     *
     * @param args exactly 5 arguments:
     *             path to the file containing the samples
     *             description of network architecture used (elman or tdnn)
     *             population size
     *             error threshold used to stop the algorithm
     *             maximum number of iterations
     */
    public static void main(String[] args) {
        if (args.length != 5) {
            System.err.println("Program takes exactly 5 arguments.");
            return;
        }

        double[] normalized = loadNormalizedData(args[0]);

        String[] arh = args[1].split("-");
        String[] layersString = arh[1].split("x");
        int[] layers = new int[layersString.length];

        for (int i = 0; i < layersString.length; i++) {
            layers[i] = Integer.parseInt(layersString[i].strip());
        }

        NeuralNetwork network;
        IReadOnlyDataset dataset;

        if (arh[0].equalsIgnoreCase("tdnn")) {
            if (layers[layers.length - 1] != 1) {
                System.err.println("Invalid network configuration.");
                return;
            }

            network = new TDNN(layers, new SigmoidTransferFunction());
            dataset = new TDDataset(normalized, layers[0]);
        } else if (arh[0].equalsIgnoreCase("elman")) {
            if (layers[0] != 1 || layers[layers.length - 1] != 1 || layers.length < 3) {
                System.err.println("Invalid network configuration.");
                return;
            }

            network = new ElmanNN(layers, new SigmoidTransferFunction());
            dataset = new ElmanDataset(normalized);
        } else {
            System.err.println("Unknown network architecture.");
            return;
        }

        Evaluator evaluator = new Evaluator(network, dataset);

        int populationSize = Integer.parseInt(args[2]);
        double errorThreshold = Double.parseDouble(args[3]);
        int maxIter = Integer.parseInt(args[4]);

        DifferentialEvolution algorithm = new DifferentialEvolution(populationSize, maxIter, errorThreshold, evaluator,
                MIN_INTERVAL, MAX_INTERVAL, SCALE_FACTOR, MUTATION_PROBABILITY, new Random());

        double[] solution = algorithm.run();

        System.out.println("Best solution: " + Arrays.toString(solution));
        System.out.println("Error: " + evaluator.calculateError(solution));
    }

    /**
     * Loads data from the given {@code path} and normalizes it to range [-1, 1].
     *
     * @param path to the file containing the data.
     *
     * @return an array of normalized inputs size of {@link #SAMPLE_SIZE}.
     */
    private static double[] loadNormalizedData(String path) {
        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException("Error while reading data.");
        }

        int size = lines.size();
        int[] parsed = new int[size];

        int number = Integer.parseInt(lines.get(0).strip());
        int min = number;
        int max = number;

        for (int i = 0; i < size; i++) {
            number = Integer.parseInt(lines.get(i).strip());
            parsed[i] = number;

            if (number < min) {
                min = number;
            }

            if (number > max) {
                max = number;
            }
        }

        int delta = max - min;

        int index = 0;
        double[] normalized = new double[SAMPLE_SIZE];

        for (int input : parsed) {
            if (index == SAMPLE_SIZE) {
                break;
            }

            normalized[index++] = 2d * (input - min) / delta - 1;
        }

        return normalized;
    }
}
