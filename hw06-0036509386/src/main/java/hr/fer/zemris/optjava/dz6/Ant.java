package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.List;

/**
 * A solution used by {@link TSPSolver}.
 *
 * @author Mateo Imbrišak
 */

public class Ant {

    /**
     * Total distance traveled by this ant.
     */
    private double distanceTraveled;

    /**
     * List of cities visited so far.
     */
    private List<City> visitedCities;

    /**
     * A matrix that keeps the distances between the cities.
     */
    private final double[][] distances;

    /**
     * Default constructor that assigns the distances between the cities.
     *
     * @param distances between the cities.
     */
    public Ant(double[][] distances) {
        this.distances = distances;
        this.visitedCities = new ArrayList<>();
    }

    /**
     * Visits the given {@link City}.
     *
     * @param city to be visited.
     */
    public void visitCity(City city) {
        if (visitedCities.isEmpty()) {
            visitedCities.add(city);
            return;
        }

        City lastVisited = visitedCities.get(visitedCities.size() - 1);

        visitedCities.add(city);
        distanceTraveled += distances[lastVisited.getIndex() - 1][city.getIndex() - 1];
    }

    /**
     * Returns this ant to its starting {@link City}.
     */
    public void returnToStart() {
        City lastVisited = visitedCities.get(visitedCities.size() - 1);
        City first = visitedCities.get(0);

        visitedCities.add(first);
        distanceTraveled += distances[lastVisited.getIndex() - 1][first.getIndex() - 1];
    }

    /**
     * Checks whether the given {@link City} has been visited.
     *
     * @param city to be checked.
     *
     * @return {@code true} if the given {@code city} has been visited,
     * otherwise {@code false.}
     */
    public boolean isVisited(City city) {
        return visitedCities.contains(city);
    }

    /**
     * Provides the city in which this ant started.
     *
     * @return city in which this ant started.
     */
    public City getStartingCity() {
        return visitedCities.get(0);
    }

    /**
     * Provides total distance traveled.
     *
     * @return {@link #distanceTraveled}.
     */
    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    /**
     * Provides a {@link List} of {@link #visitedCities}.
     *
     * @return a {@link List} of {@link #visitedCities}.
     */
    public List<City> getVisitedCities() {
        return visitedCities;
    }

    @Override
    public String toString() {
        if (visitedCities.size() == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (City current : visitedCities) {
            sb.append(current.getIndex()).append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");

        return sb.toString();
    }
}
