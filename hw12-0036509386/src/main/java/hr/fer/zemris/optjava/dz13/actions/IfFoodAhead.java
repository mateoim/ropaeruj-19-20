package hr.fer.zemris.optjava.dz13.actions;

import hr.fer.zemris.optjava.dz13.solutions.Ant;

/**
 * A node with two children that executes the first one if {@link Ant#foodAhead()}
 * is {@code true} or the second one if it is {@code false}.
 *
 * @author Mateo Imbrišak
 */

public class IfFoodAhead implements AntAction {

    /**
     * Keeps the children of this node.
     */
    private AntAction[] children;

    /**
     * Fitness for this node.
     */
    private double fitness;

    /**
     * Food found using this tree.
     */
    private int food;

    /**
     * Keeps the total number of children.
     */
    private int numberOfChildren = 0;

    /**
     * Keeps the depth of this node.
     */
    private int depth;

    /**
     * Default constructor that assigns all children.
     *
     * @param firstChild child executed if food is ahead.
     * @param secondChild child executed if no food is ahead.
     */
    public IfFoodAhead(AntAction firstChild, AntAction secondChild) {
        children = new AntAction[2];

        children[0] = firstChild;
        children[1] = secondChild;
    }

    @Override
    public void execute(Ant ant) {
        if (!ant.hasRemainingMoves()) {
            return;
        }

        if (ant.foodAhead()) {
            children[0].execute(ant);
        } else {
            children[1].execute(ant);
        }
    }

    @Override
    public int getNumberOfChildren() {
        if (numberOfChildren == 0) {
            calculateChildren();
        }

        return numberOfChildren;
    }

    @Override
    public int getDirectChildren() {
        return 2;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public void setDepth(int depth) {
        this.depth = depth;

        for (AntAction child : children) {
            child.setDepth(depth + 1);
        }
    }

    @Override
    public int getTotalDepth() {
        int totalDepth = 0;

        for (AntAction child : children) {
            int childDepth = child.getTotalDepth();

            totalDepth = Math.max(childDepth, totalDepth);
        }

        return totalDepth;
    }

    @Override
    public void setChild(int index, AntAction child) {
        children[index] = child;
    }

    @Override
    public AntAction getChild(int index) {
        return children[index];
    }

    @Override
    public AntAction duplicate() {
        AntAction first = children[0].duplicate();
        AntAction second = children[1].duplicate();

        return new IfFoodAhead(first, second);
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public int getFood() {
        return food;
    }

    @Override
    public void setFood(int food) {
        this.food = food;
    }

    @Override
    public void calculateChildren() {
        numberOfChildren = 2 + children[0].getNumberOfChildren() + children[1].getNumberOfChildren();
    }

    @Override
    public String toString() {
        return "IfFoodAhead(" + children[0].toString() + ", " + children[1].toString() + ")";
    }
}
