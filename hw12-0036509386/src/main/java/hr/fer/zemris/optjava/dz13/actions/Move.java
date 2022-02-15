package hr.fer.zemris.optjava.dz13.actions;

import hr.fer.zemris.optjava.dz13.solutions.Ant;

/**
 * An {@link AntAction} that moves the {@link Ant} in the direction it is facing.
 *
 * @author Mateo Imbrišak
 */

public class Move implements AntAction {

    /**
     * Fitness for this node.
     */
    private double fitness;

    /**
     * Food found using this tree.
     */
    private int food;

    /**
     * Keeps the depth of this node.
     */
    private int depth;

    @Override
    public void execute(Ant ant) {
        if (ant.hasRemainingMoves()) {
            ant.move();
        }
    }

    @Override
    public int getNumberOfChildren() {
        return 0;
    }

    @Override
    public int getDirectChildren() {
        return 0;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public int getTotalDepth() {
        return depth;
    }

    @Override
    public AntAction duplicate() {
        return new Move();
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
    public String toString() {
        return "Move";
    }
}
