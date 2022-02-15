package hr.fer.zemris.optjava.dz13.solutions;

import hr.fer.zemris.optjava.dz13.actions.AntAction;

import java.util.Arrays;

/**
 * A class that runs Ant Trail simulations.
 *
 * @author Mateo Imbrišak
 */

public class Ant {

    /**
     * Number of moves allowed in an evaluation.
     */
    private static final int INITIAL_MOVES_REMAINING = 600;

    /**
     * Used to scale solutions that behave like their parents.
     */
    private static final double PLAGIARISM_SCALING_FACTOR = 0.9;

    /**
     * Keeps track of current x coordinate.
     */
    private int x;

    /**
     * Keeps track of current y coordinate.
     */
    private int y;

    /**
     * Keeps the direction in which the ant is facing.
     */
    private Direction direction;

    /**
     * Keeps track of all visited x coordinates.
     */
    private int[] xArray;

    /**
     * Keeps track of all visited y coordinates.
     */
    private int[] yArray;

    /**
     * Keeps track of all {@link Direction}s used in the simulation.
     */
    private Direction[] directionArray;

    /**
     * Keeps the root of currently evaluated tree.
     */
    private AntAction root;

    /**
     * Keeps total food found in this iteration.
     */
    private int totalFood;

    /**
     * Keeps the map of the world.
     */
    private final boolean[][] originalWorldMap;

    /**
     * Copy of {@link #originalWorldMap} that can be modified.
     */
    private boolean[][] worldMap;

    /**
     * Keeps track of remaining moves.
     */
    private int movesRemaining;

    /**
     * Default constructor that assigns world map used in simulations.
     *
     * @param worldMap used to train and evaluate solutions.
     */
    public Ant(boolean[][] worldMap) {
        this.originalWorldMap = worldMap;
        this.direction = Direction.EAST;
    }

    /**
     * Executes the simulation.
     *
     * @param action used as root of the simulation.
     */
    public void run(AntAction action) {
        this.root = action;
        this.xArray = new int[INITIAL_MOVES_REMAINING + 1];
        this.yArray = new int[INITIAL_MOVES_REMAINING + 1];
        this.directionArray = new Direction[INITIAL_MOVES_REMAINING + 1];

        int size = originalWorldMap.length;
        this.worldMap = new boolean[size][originalWorldMap[0].length];

        for (int i = 0; i < size; i++) {
            worldMap[i] = Arrays.copyOf(originalWorldMap[i], originalWorldMap[i].length);
        }

        movesRemaining = INITIAL_MOVES_REMAINING;
        totalFood = 0;
        x = 0;
        y = 0;
        direction = Direction.EAST;

        xArray[0] = x;
        yArray[0] = y;
        directionArray[0] = direction;

        checkForFood();

        while (hasRemainingMoves()) {
            root.execute(this);
        }
    }

    /**
     * Checks the current location for food.
     */
    public void checkForFood() {
        if (worldMap[x][y]) {
            worldMap[x][y] = false;
            totalFood++;
        }
    }

    /**
     * Checks if this ant has any remaining moves.
     *
     * @return {@code true} if this ant has remaining moves,
     * otherwise {@code false}.
     */
    public boolean hasRemainingMoves() {
        return movesRemaining != 0;
    }

    /**
     * Moves the ant in the direction it is facing.
     */
    public void move() {
        switch (direction) {
            case EAST:
                x = (x + 1) % worldMap[0].length;
                break;
            case WEST:
                x = x > 0 ? (x - 1) : worldMap[0].length - 1;
                break;
            case NORTH:
                y = (y + 1) % worldMap.length;
                break;
            case SOUTH:
                y = y > 0 ? (y - 1) : worldMap.length - 1;
        }

        checkForFood();

        xArray[INITIAL_MOVES_REMAINING - movesRemaining + 1] = x;
        yArray[INITIAL_MOVES_REMAINING - movesRemaining + 1] = y;
        directionArray[INITIAL_MOVES_REMAINING - movesRemaining + 1] = direction;

        movesRemaining--;
    }

    /**
     * Rotates this ant to the left by {@code 90} degrees.
     */
    public void rotateLeft() {
        switch (direction) {
            case EAST:
                direction = Direction.NORTH;
                break;
            case WEST:
                direction = Direction.SOUTH;
                break;
            case NORTH:
                direction = Direction.WEST;
                break;
            case SOUTH:
                direction = Direction.EAST;
        }

        xArray[INITIAL_MOVES_REMAINING - movesRemaining + 1] = x;
        yArray[INITIAL_MOVES_REMAINING - movesRemaining + 1] = y;
        directionArray[INITIAL_MOVES_REMAINING - movesRemaining + 1] = direction;

        movesRemaining--;
    }

    /**
     * Rotates this ant to the right by {@code 90} degrees.
     */
    public void rotateRight() {
        switch (direction) {
            case EAST:
                direction = Direction.SOUTH;
                break;
            case WEST:
                direction = Direction.NORTH;
                break;
            case NORTH:
                direction = Direction.EAST;
                break;
            case SOUTH:
                direction = Direction.WEST;
        }

        xArray[INITIAL_MOVES_REMAINING - movesRemaining + 1] = x;
        yArray[INITIAL_MOVES_REMAINING - movesRemaining + 1] = y;
        directionArray[INITIAL_MOVES_REMAINING - movesRemaining + 1] = direction;

        movesRemaining--;
    }

    /**
     * Checks if there is food on the block that this ant is currently facing.
     *
     * @return {@code true} if food is directly ahead of this and,
     * otherwise {@code false}.
     */
    public boolean foodAhead() {
        switch (direction) {
            case EAST:
                return worldMap[y][(x + 1) % worldMap[0].length];
            case WEST:
                return worldMap[y][x > 0 ? (x - 1) : worldMap[0].length - 1];
            case NORTH:
                return worldMap[(y + 1) % worldMap.length][x];
            case SOUTH:
                return worldMap[y > 0 ? (y - 1) : worldMap.length - 1][x];
        }

        return false;
    }

    /**
     * Provides total food found in last simulation.
     *
     * @return total food found ina last simulation.
     */
    public int getTotalFood() {
        return totalFood;
    }

    /**
     * Provides the original map used in simulations.
     *
     * @return original map used in simulations.
     */
    public boolean[][] getWorldMap() {
        return originalWorldMap;
    }

    /**
     * Checks if this ant behaves the same way as their parent
     * and scales its fitness if it does.
     *
     * @param parentRoot parent used to generate {@link #root}.
     */
    public void checkForPlagiarism(AntAction parentRoot) {
        root.setFood(totalFood);

        if (parentRoot.getFood() == totalFood) {
            root.setFitness(totalFood * PLAGIARISM_SCALING_FACTOR);
        } else {
            root.setFitness(totalFood);
        }
    }

    /**
     * Provides all visited x coordinates.
     *
     * @return {@code array} of all visited x coordinates.
     */
    public int[] getxArray() {
        return xArray;
    }

    /**
     * Provides all visited y coordinates.
     *
     * @return {@code array} of all visited y coordinates.
     */
    public int[] getyArray() {
        return yArray;
    }

    /**
     * Provides all {@link Direction}s used.
     *
     * @return {@code array} of all used {@link Direction}s.
     */
    public Direction[] getDirectionArray() {
        return directionArray;
    }
}
