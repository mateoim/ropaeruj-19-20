package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.algorithms.CLONALG;
import hr.fer.zemris.optjava.dz7.algorithms.PSO;
import hr.fer.zemris.optjava.dz7.algorithms.PSONeighbourhood;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Mateo Imbrišak
 */

public class ANNTrainer {

    private static final double C = 2;

    private static final double MAX_VELOCITY = 5;

    private static final double MIN_VELOCITY = -5;

    private static final double MAX_X = 1;

    private static final double MIN_X = -1;

    private static final double MAX_INERTIA = 0.9;

    private static final double MIN_INERTIA = 0.01;

    private static final int BETA = 5;

    private static final double RO = 0.25;

    private static final double TO_GENERATE = 0.1;

    public static void main(String[] args) {
        IReadOnlyDataset dataset = loadData(args[0]);

        FFANN ffann = new FFANN(
                new int[] {4, 5, 3, 3},
                new ITransferFunction[] {
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction()
                },
                dataset
        );

        double[] weights = new double[ffann.getWeightsCount()];
        Arrays.fill(weights, 0.1);
        double[] out = new double[dataset.getOutputs().length];

        ffann.calcOutputs(dataset.getInputs(), weights, out);

        System.out.println(ffann.calculateError(out));

        int maxIter = Integer.parseInt(args[4]);
        int populationSize = Integer.parseInt(args[2]);
        double error = Double.parseDouble(args[3]);

        double[] solution;

        if (args[1].equals("pso-a")) {
            PSO algorithm = new PSO(ffann, populationSize, MIN_VELOCITY, MAX_VELOCITY, MIN_X, MAX_X,
                    MIN_INERTIA, MAX_INERTIA, C, C, maxIter, error, new Random());
            solution = algorithm.run();;
        } else if (args[1].startsWith("pso-b")) {
            int neighbours = Integer.parseInt(args[1].split("-")[2]);

            PSONeighbourhood algorithm = new PSONeighbourhood(ffann, populationSize, MIN_VELOCITY, MAX_VELOCITY,
                    MIN_X, MAX_X, MIN_INERTIA, MAX_INERTIA, C, C, maxIter, error, neighbours, new Random());
            solution = algorithm.run();
        } else if (args[1].equals("clonalg")) {
            CLONALG algorithm = new CLONALG(ffann, MIN_X, MAX_X, populationSize, BETA, RO,
                    (int) Math.round(TO_GENERATE * populationSize), maxIter, error, new Random());
            solution = algorithm.run();
        } else {
            System.err.println("Unknown algorithm. Exiting...");
            return;
        }

        System.out.println(Arrays.toString(solution));
        printStatistics(ffann, solution);
    }

    private static IReadOnlyDataset loadData(String path) {
        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException exc) {
            throw new RuntimeException("Error while reading data.");
        }

        double[] inputArray = null;
        double[] outputArray = null;

        int inputDimension = 0;
        int outputDimension = 0;

        int inputIndex = 0;
        int outputIndex = 0;

        for (String line : lines) {
            String[] parts = line.strip().replace("(", "")
                    .replace(")", "").split(":");

            String[] inputParts = parts[0].split(",");
            String[] outputParts = parts[1].split(",");

            if (inputArray == null) {
                inputArray = new double[lines.size() * inputParts.length];
                outputArray = new double[lines.size() * outputParts.length];

                inputDimension = inputParts.length;
                outputDimension = outputParts.length;
            }

            for (String input : inputParts) {
                inputArray[inputIndex++] = Double.parseDouble(input);
            }

            for (String output : outputParts) {
                outputArray[outputIndex++] = Double.parseDouble(output);
            }
        }

        return new Dataset(inputDimension, outputDimension, inputArray, outputArray);
    }

    private static void printStatistics(FFANN network, double[] weights) {
        int positive = 0;
        int negative = 0;

        int size = network.getDataset().getOutputs().length;
        double[] solution = new double[size];
        double[] outputs = network.getDataset().getOutputs();
        int increment = network.getDataset().getOutputDimension();

        network.calcOutputs(network.getDataset().getInputs(), weights, solution);

        for (int i = 0; i < size / increment; i ++) {
            double[] current = new double[increment];
            double[] expected = new double[increment];

            for (int j = 0; j < increment; j++) {
                expected[j] = outputs[i * increment + j];
                current[j] = Math.round(solution[i * increment + j]);
            }

            System.out.println("Solution classified as: " + Arrays.toString(current) +
                    " expected: " + Arrays.toString(expected));

            if (Arrays.equals(expected, current)) {
                positive++;
                System.out.println("PASSED");
            } else {
                negative++;
                System.out.println("FAILED");
            }
        }

        System.out.println("Correctly classified: " + positive + ", incorrectly classified: " + negative);
    }
}
