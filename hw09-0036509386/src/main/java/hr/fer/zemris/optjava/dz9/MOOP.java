package hr.fer.zemris.optjava.dz9;

import hr.fer.zemris.optjava.dz9.algorithm.NSGA;
import hr.fer.zemris.optjava.dz9.crossover.BLXAlphaCrossover;
import hr.fer.zemris.optjava.dz9.mutator.Mutator;
import hr.fer.zemris.optjava.dz9.problem.FirstProblem;
import hr.fer.zemris.optjava.dz9.problem.MOOPProblem;
import hr.fer.zemris.optjava.dz9.problem.SecondProblem;
import hr.fer.zemris.optjava.dz9.selection.RouletteWheelSelection;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

/**
 * A program that attempts to solve a multi-objective optimization problem.
 *
 * @author Mateo Imbrišak
 */

public class MOOP {

    /**
     * Keeps path to output file for {@code decision-space}.
     */
    private static final Path DEC = Paths.get("izlaz-dec.txt");

    /**
     * Keeps path to output file for {@code objective-space}.
     */
    private static final Path OBJ = Paths.get("izlaz-obj.txt");

    /**
     * Factor used for share calculation.
     */
    private static final double ALPHA = 2d;

    /**
     * Used to control fitness for fronts.
     */
    private static final double EPSILON = 0.0002;

    /**
     * Used to control share range.
     */
    private static final double SIGMA_SHARE = 0.95;

    /**
     * Factor used in {@link BLXAlphaCrossover}.
     */
    private static final double BLX_FACTOR = 0.1;

    /**
     * Bound used by {@link Mutator}.
     */
    private static final double UPPER_BOUND = 0.25;

    /**
     * Bound used by {@link Mutator}.
     */
    private static final double LOWER_BOUND = -0.25;

    /**
     * Don't let anyone instantiate this class.
     */
    private MOOP() {}

    /**
     * Used to start the program.
     *
     * @param args exactly four arguments:
     *             number of problem being solved {@code 1} or {@code 2}
     *             population size
     *             type used to calculate share fitness {@code decision-space} or {@code objective-space}
     *             number of iterations to perform
     */
    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Program takes exactly 4 arguments");
            return;
        }

        MOOPProblem problem;

        if (args[0].equals("1")) {
            problem = new FirstProblem();
        } else if (args[0].equals("2")) {
            problem = new SecondProblem();
        } else {
            System.err.println("Unknown problem.");
            return;
        }

        int populationSize = Integer.parseInt(args[1]);
        int maxIter = Integer.parseInt(args[3]);

        Random rand = new Random();

        Mutator mutator = new Mutator(LOWER_BOUND, UPPER_BOUND, rand);
        RouletteWheelSelection selection = new RouletteWheelSelection(rand);
        BLXAlphaCrossover crossover = new BLXAlphaCrossover(BLX_FACTOR, rand);

        NSGA algorithm = new NSGA(populationSize, maxIter, problem, selection, crossover, mutator, args[2],
                SIGMA_SHARE, ALPHA, EPSILON, rand);

        algorithm.run();

        StringBuilder objBuilder = new StringBuilder();
        StringBuilder decBuilder = new StringBuilder();

        double[][] solutions = algorithm.getSolutions();
        double[][] values = algorithm.getValues();

        for (int i = 0; i < populationSize; i++) {
            objBuilder.append(Arrays.toString(values[i])).append("\n");
            decBuilder.append(Arrays.toString(solutions[i])).append("\n");
        }

        try {
            Files.write(DEC, decBuilder.toString().getBytes());
            Files.write(OBJ, objBuilder.toString().getBytes());
        } catch (IOException exc) {
            System.err.println("Error writing files.");
            return;
        }

        if (args[0].equals("2")) {
            XYSeries series = new XYSeries("Data");

            for (double[] dataPoint : values) {
                series.add(dataPoint[0], dataPoint[1]);
            }

            DefaultXYDataset dataset = new DefaultXYDataset();
            dataset.addSeries(series.getKey(), series.toArray());

            Plot plot = new XYPlot(dataset, new NumberAxis("f1"), new NumberAxis("f2"),
                    new XYShapeRenderer());

            JFreeChart chart = new JFreeChart(args[2], JFreeChart.DEFAULT_TITLE_FONT, plot, false);

            try {
                if (args[2].equals("decision-space")) {
                    Path test = Paths.get("dec.png");
                    ChartUtilities.saveChartAsPNG(test.toFile(), chart, 500, 300);
                } else {
                    Path test = Paths.get("obj.png");
                    ChartUtilities.saveChartAsPNG(test.toFile(), chart, 500, 300);
                }
            } catch (IOException exc) {
                System.err.println("Error writing image.");
            }
        }
    }
}
