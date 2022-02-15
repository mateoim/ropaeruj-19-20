package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * An algorithm that attempts to find the solution of a TSP problem.
 *
 * @author Mateo Imbrišak
 */

public class MaxMinAntSystem {

    /**
     * Used to reinitialize the {@link #tauMatrix} when passed.
     */
    private static final int REINITIALIZATION_THRESHOLD = 1000;

    /**
     * Maximum number of iterations.
     */
    private final int maxIter;

    /**
     * Number of {@link Ant}s per iteration.
     */
    private final int numberOfAnts;

    /**
     * List of cities in the problem.
     */
    private final List<City> cities;

    /**
     * Keeps the distances between the cities.
     */
    private final double[][] distances;

    /**
     * Alpha parameter used for trails.
     */
    private final double alpha;

    /**
     * Beta parameter used for trails.
     */
    private final double beta;

    /**
     * Used to calculate maximum trail.
     */
    private final double ro;

    /**
     * Parameter used to calculate minimum trail.
     */
    private double a;

    /**
     * Keeps the current minimum trail.
     */
    private double tauMin;

    /**
     * Keeps the current maximum trail.
     */
    private double tauMax;

    /**
     * Used to determine the next city to visit.
     */
    private double[][] tauMatrix;

    /**
     * Used to determine the next city to visit.
     */
    private double[][] heuristicMatrix;

    /**
     * Keeps the {@link Ant} with the shortest travel distance.
     */
    private Ant globalBestAnt;

    /**
     * Best solution found so far.
     */
    private double shortestDistance;

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns the constants and initializes auxiliary arrays.
     *
     * @param maxIter maximum number of iterations to perform.
     * @param numberOfAnts used in every iteration.
     * @param cities to visit.
     * @param distances between the cities.
     * @param alpha used to calculate probabilities.
     * @param beta used to calculate {@link #heuristicMatrix}.
     * @param ro used to calculate maximum trail.
     * @param a used to calculate minimum trail.
     * @param rand used to generate random values.
     */
    public MaxMinAntSystem(int maxIter, int numberOfAnts, List<City> cities, double[][] distances,
                           double alpha, double beta, double ro, double a, Random rand) {
        this.maxIter = maxIter;
        this.numberOfAnts = numberOfAnts;
        this.cities = cities;
        this.distances = distances;
        this.alpha = alpha;
        this.beta = beta;
        this.ro = ro;
        this.a = a;
        this.rand = rand;

        shortestDistance = findInitialRoute();
        updateTau();

        tauMatrix = new double[distances.length][distances.length];
        initializeHeuristicMatrix();
        reinitializeTrails();
    }

    /**
     * Executes the algorithm.
     *
     * @return {@link Ant} with best found path.
     */
    public Ant run() {
        int i = 0;
        int stagnationCounter = 0;

        while (i < maxIter) {
            Ant localBestAnt = null;

            for (int j = 0; j < numberOfAnts; j++) {
                City last = cities.get(rand.nextInt(cities.size() - 1));
                Ant currentAnt = new Ant(distances);

                currentAnt.visitCity(last);

                for (int c = 0, size = cities.size() - 1; c < size; c++) {
                    List<City> potentialNext = findNextList(currentAnt, last.getClosestNeighbours());

                    if (potentialNext.isEmpty()) {
                        potentialNext = findNextList(currentAnt, cities);
                    }

                    double[] probabilities = calculateProbabilities(potentialNext, last);

                    double sum = 0;
                    double roll = rand.nextDouble();
                    City next = null;

                    for (int k = 0; k < probabilities.length; k++) {
                        sum += probabilities[k];

                        if (roll < sum) {
                            next = potentialNext.get(k);
                            break;
                        }
                    }

                    if (next == null) {
                        next = potentialNext.get(potentialNext.size() - 1);
                    }

                    currentAnt.visitCity(next);

                    last = next;
                }

                currentAnt.returnToStart();

                if (localBestAnt == null) {
                    localBestAnt = currentAnt;
                } else if (currentAnt.getDistanceTraveled() < localBestAnt.getDistanceTraveled()) {
                    localBestAnt = currentAnt;
                }
            }

            evaporateTrails();

            if (i < maxIter / 2) {
                updateTrails(localBestAnt);
            } else {
                updateTrails(globalBestAnt);
            }

            if (localBestAnt.getDistanceTraveled() < globalBestAnt.getDistanceTraveled()) {
                globalBestAnt = localBestAnt;
                shortestDistance = globalBestAnt.getDistanceTraveled();
                updateTau();
                stagnationCounter = 0;
            } else {
                stagnationCounter++;
            }

            if (stagnationCounter == REINITIALIZATION_THRESHOLD) {
                reinitializeTrails();
            }

            i++;
            System.out.println("Iteration " + i + " best distance " + shortestDistance);
            System.out.println("Best solution: " + globalBestAnt);
        }

        return globalBestAnt;
    }

    /**
     * Finds all {@code cities} unvisited by the given {@link Ant}.
     *
     * @param ant being checked for visited cities.
     * @param cities in this problem.
     *
     * @return a {@link List} of unvisited cities.
     */
    private List<City> findNextList(Ant ant, List<City> cities) {
        List<City> potentialNext = new ArrayList<>();

        for (City city : cities) {
            if (!ant.isVisited(city)) {
                potentialNext.add(city);
            }
        }

        return potentialNext;
    }

    /**
     * Used to initialize {@link #heuristicMatrix}.
     */
    private void initializeHeuristicMatrix() {
        int size = distances.length;
        double[][] values = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                double value = Math.pow(1 / distances[i][j], beta);

                values[i][j] = value;
                values[j][i] = value;
            }
        }

        heuristicMatrix = values;
    }

    /**
     * Used to initialize and reinitialize {@link #tauMatrix}.
     */
    private void reinitializeTrails() {
        for (double[] array : tauMatrix) {
            Arrays.fill(array, tauMax);
        }
    }

    /**
     * Used to evaporate trails after each iteration.
     */
    private void evaporateTrails() {
        int size = tauMatrix.length;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tauMatrix[i][j] *= (1 - ro);

                if (tauMatrix[i][j] < tauMin) {
                    tauMatrix[i][j] = tauMin;
                }
            }
        }
    }

    /**
     * Used to approximate a good initial route.
     *
     * @return total distance traveled.
     */
    private double findInitialRoute() {
        Ant ant = new Ant(distances);
        City city = cities.get(rand.nextInt(cities.size() - 1));
        ant.visitCity(city);

        for (int c = 0, size = cities.size() - 1; c < size; c++) {
            List<City> potentialNext = findNextList(ant, city.getClosestNeighbours());

            if (potentialNext.isEmpty()) {
                potentialNext = findNextList(ant, cities);
            }

            final double[] distanceArray = distances[city.getIndex() - 1];

            potentialNext.sort((o1, o2) -> {
                double o1d = distanceArray[o1.getIndex() - 1];
                double o2d = distanceArray[o2.getIndex() - 1];

                return Double.compare(o1d, o2d);
            });

            city = potentialNext.get(0);
            ant.visitCity(city);
        }

        ant.returnToStart();

        globalBestAnt = ant;
        return ant.getDistanceTraveled();
    }

    /**
     * Calculates probabilities to pick each city from the given list.
     *
     * @param cities to be considered.
     * @param currentCity from which the {@link Ant} will be visiting.
     *
     * @return an {@code array} of probabilities to visit a given city form {@code cities}.
     */
    private double[] calculateProbabilities(List<City> cities, City currentCity) {
        int size = cities.size();
        int index = currentCity.getIndex() - 1;
        double[] ret = new double[size];

        double sum = 0;

        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            double value = Math.pow(tauMatrix[index][city.getIndex() - 1], alpha)
                    * heuristicMatrix[index][city.getIndex() - 1];

            sum += value;
            ret[i] = value;
        }

        for (int i = 0; i < size; i ++) {
            ret[i] /= sum;
        }

        return ret;
    }

    /**
     * Used to update {@link #tauMax} and {@link #tauMin}
     * after {@link #shortestDistance} has changed.
     */
    private void updateTau() {
        tauMax = 1 / (ro * shortestDistance);
        tauMin = tauMax / a;
    }

    /**
     * Used internally by {@link #run()} to update
     * {@link #tauMatrix} each iteration.
     *
     * @param ant used to update visited paths.
     */
    private void updateTrails(Ant ant) {
        List<City> visited = ant.getVisitedCities();
        double value = 1 / ant.getDistanceTraveled();

        City first = ant.getStartingCity();

        for (int i = 1, size = visited.size(); i < size; i++) {
            City second = visited.get(i);

            tauMatrix[first.getIndex() - 1][second.getIndex() - 1] += value;
            tauMatrix[second.getIndex() - 1][first.getIndex() - 1] += value;

            first = second;
        }
    }
}
