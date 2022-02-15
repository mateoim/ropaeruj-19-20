package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.Evaluator;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.algotirhm.Algorithm;
import hr.fer.zemris.optjava.algotirhm.EvaluationParallelizationGA;
import hr.fer.zemris.optjava.crossover.Crossover;
import hr.fer.zemris.optjava.crossover.DiscreteCrossover;
import hr.fer.zemris.optjava.mutator.Mutator;
import hr.fer.zemris.optjava.mutator.RectangleMutator;
import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.selection.Selection;
import hr.fer.zemris.optjava.selection.TournamentSelection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A program that runs {@link EvaluationParallelizationGA}.
 *
 * @author Mateo Imbrišak
 */

public class Pokretac1 {

    /**
     * Used to initialize {@link TournamentSelection}.
     */
    private static final int TOURNAMENT_SELECTION_N = 3;

    /**
     * Used to start the program.
     *
     * @param args exactly 7 arguments:
     *             path to the original image
     *             number of rectangles to use
     *             population size
     *             maximum number of iterations
     *             error threshold
     *             path to the best solution output
     *             path to the generated image approximation
     */
    public static void main(String[] args) {
        if (args.length != 7) {
            System.err.println("Program takes exactly 7 arguments.");
            return;
        }

        Path imagePath = Paths.get(args[0]);
        GrayScaleImage originalImage;

        try {
            originalImage = GrayScaleImage.load(imagePath.toFile());
        } catch (IOException e) {
            System.err.println("Error loading image.");
            return;
        }

        Evaluator evaluator = new Evaluator(originalImage);

        int numberOfRectangles = Integer.parseInt(args[1]);
        int populationSize = Integer.parseInt(args[2]);
        int maxIter = Integer.parseInt(args[3]);
        double errorThreshold = Double.parseDouble(args[4]);

        Path textOutput = Paths.get(args[5]);
        Path imageOutput = Paths.get(args[6]);

        int height = originalImage.getHeight();
        int width = originalImage.getWidth();
        int solutionSize = 1 + 5 * numberOfRectangles;

        Mutator<GASolution<int[]>> mutator = new RectangleMutator(height, width);
        Selection<GASolution<int[]>> selection = new TournamentSelection(TOURNAMENT_SELECTION_N);
        Crossover<GASolution<int[]>> crossover = new DiscreteCrossover();

        Algorithm algorithm = new EvaluationParallelizationGA(populationSize, errorThreshold, maxIter, evaluator,
                selection, mutator, crossover, solutionSize, height, width);

        Thread thread = new EVOThread(algorithm::run);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GASolution<int[]> bestSolution = algorithm.getBestSolution();

        int[] data = bestSolution.getData();

        StringBuilder sb = new StringBuilder();

        for (int value : data) {
            sb.append(value).append("\n");
        }

        try {
            Files.write(textOutput, sb.toString().getBytes());
            evaluator.draw(bestSolution, null).save(imageOutput.toFile());
        } catch (IOException e) {
            System.err.println("Error writing best solution.");
        }
    }
}
