package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in the TSP problem to be visited by an {@link Ant}.
 *
 * @author Mateo Imbrišak
 */

public class City {

    /**
     * Index used to represent the city.
     */
    private final int index;

    /**
     * X coordinate of the city.
     */
    private final double x;

    /**
     * Y coordinate of the city.
     */
    private final double y;

    /**
     * Keeps a given amount of closest neighbours.
     */
    private List<City> closestNeighbours;

    /**
     * Default constructor that assigns all values.
     *
     * @param index used to represent the city.
     * @param x coordinate of the city.
     * @param y coordinate of the city.
     */
    public City(int index, double x, double y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }

    /**
     * Initializes {@link #closestNeighbours} by finding {@code size} closest neighbours.
     *
     * @param size to be used for {@link #closestNeighbours}.
     * @param distances between the given cities.
     * @param cities to be considered for the list.
     */
    public void findClosestNeighbours(int size, double[][] distances, List<City> cities) {
        List<City> copy = new ArrayList<>(cities);
        final double[] distanceArray = distances[index - 1];

        copy.sort((o1, o2) -> {
            double o1d = distanceArray[o1.index - 1];
            double o2d = distanceArray[o2.index - 1];

            return Double.compare(o1d, o2d);
        });

        closestNeighbours = copy.subList(1, size + 1);
    }

    /**
     * Calculates the distance between this city and given {@code city}.
     *
     * @param city used to calculate the distance.
     *
     * @return distance between the cities.
     */
    public double calculateDistance(City city) {
        if (index == city.getIndex()) {
            return  0;
        } else {
            double deltaX = x - city.x;
            double deltaY = y - city.y;

            return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        }
    }

    /**
     * Provides the {@link List} of closest neighbours.
     *
     * @return {@link #closestNeighbours}.
     */
    public List<City> getClosestNeighbours() {
        return closestNeighbours;
    }

    /**
     * Provides the {@link #index} of this city.
     *
     * @return {@link #index}.
     */
    public int getIndex() {
        return index;
    }
}
