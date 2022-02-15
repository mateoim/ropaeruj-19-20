package hr.fer.zemris.optjava.dz13.actions;

import hr.fer.zemris.optjava.dz13.solutions.Ant;

/**
 * An interface that represents a node in {@link Ant}'s AI tree.
 *
 * @author Mateo Imbrišak
 */

public interface AntAction extends Comparable<AntAction> {

    /**
     * Executes this {@link Ant}'s AI for this subtree.
     *
     * @param ant used in the simulation.
     */
    void execute(Ant ant);

    /**
     * Provides the number of children.
     * Does not count this node.
     *
     * @return total number of children.
     */
    int getNumberOfChildren();

    /**
     * Provides the number of children directly owned by this node.
     *
     * @return number of direct children.
     */
    int getDirectChildren();

    /**
     * Provides depth level of this node.
     *
     * @return depth level of this node.
     */
    int getDepth();

    /**
     * Assigns depth to this node and calculates depths of it's children.
     *
     * @param depth starting depth for this node.
     */
    void setDepth(int depth);

    /**
     * Provides total depth for this subtree.
     *
     * @return total depth for this subtree.
     */
    int getTotalDepth();

    /**
     * Assigns the given {@code child} to the given {@code index}.
     *
     * @param index at which the child will be assigned.
     * @param child which will be assigned.
     */
    default void setChild(int index, AntAction child) {
        throw new UnsupportedOperationException("Terminal symbols cannot have children.");
    }

    /**
     * Provides the child at the requested index.
     *
     * @param index of the requested child.
     *
     * @return child at the requested index.
     */
    default AntAction getChild(int index) {
        throw new UnsupportedOperationException("Terminal symbols cannot have children.");
    }

    /**
     * Creates a copy of this node and calls this method for all it's children.
     *
     * @return copy of this node and it's subtree.
     */
    AntAction duplicate();

    /**
     * Provides the fitness of this solution.
     *
     * @return solution's current fitness.
     */
    double getFitness();

    /**
     * Assigns the given fitness to this solution.
     *
     * @param fitness to be assigned.
     */
    void setFitness(double fitness);

    /**
     * Provides the total amount of food found by this solution.
     *
     * @return total amount of food found.
     */
    int getFood();

    /**
     * Assigns amount of food found.
     * Used when duplicating a parent.
     *
     * @param food to be assigned.
     */
    void setFood(int food);

    /**
     * Calculates the total number of children of this node
     * and its children.
     */
    default void calculateChildren() {}

    @Override
    default int compareTo(AntAction o) {
        return -1 * Double.compare(getFitness(), o.getFitness());
    }
}
