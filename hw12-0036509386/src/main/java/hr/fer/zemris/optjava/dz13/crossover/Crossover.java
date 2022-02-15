package hr.fer.zemris.optjava.dz13.crossover;

import hr.fer.zemris.optjava.dz13.actions.AntAction;

import java.util.Random;

/**
 * A class that crosses two parents and creates two children.
 *
 * @author Mateo Imbrišak
 */

public class Crossover {

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns random number generator.
     *
     * @param rand used to generate random values.
     */
    public Crossover(Random rand) {
        this.rand = rand;
    }

    /**
     * Crosses the parents at a random point and generates two children.
     *
     * @param firstParent used to generate children.
     * @param secondParent used to generate children.
     *
     * @return array of two children.
     */
    public AntAction[] cross(AntAction firstParent, AntAction secondParent) {
        AntAction firstCopy = firstParent.duplicate();
        AntAction secondCopy = secondParent.duplicate();

        int firstRolls = rand.nextInt(firstParent.getTotalDepth() - 1) + 1;
        int secondRolls = rand.nextInt(secondParent.getTotalDepth() - 1) + 1;

        AntAction firstToConnect = firstCopy;
        AntAction secondToConnect = secondCopy;
        AntAction firstSubtree = null;
        AntAction secondSubtree = null;

        int firstRoll = 0;
        int secondRoll = 0;

        for (int i = 0; i < firstRolls; i++) {
            firstRoll = rand.nextInt(firstToConnect.getDirectChildren());

            firstSubtree = firstToConnect.getChild(firstRoll);

            if (firstSubtree.getDirectChildren() == 0) {
                break;
            }

            firstToConnect = firstSubtree;
        }

        for (int i = 0; i < secondRolls; i++) {
            secondRoll = rand.nextInt(secondToConnect.getDirectChildren());

            secondSubtree = secondToConnect.getChild(secondRoll);

            if (secondSubtree.getDirectChildren() == 0) {
                break;
            }

            secondToConnect = secondSubtree;
        }

        if (firstSubtree == null || secondSubtree == null) {
            return new AntAction[] {firstCopy, secondCopy};
        }

        if (firstToConnect == firstSubtree) {
            firstRoll = rand.nextInt(firstToConnect.getDirectChildren());
            firstSubtree = firstToConnect.getChild(firstRoll);
        }

        if (secondToConnect == secondSubtree) {
            secondRoll = rand.nextInt(secondToConnect.getDirectChildren());
            secondSubtree = secondToConnect.getChild(secondRoll);
        }

        try {
            firstToConnect.setChild(firstRoll, secondSubtree);
            secondToConnect.setChild(secondRoll, firstSubtree);
        } catch (IndexOutOfBoundsException exc) {
            System.out.println();
        }

        return new AntAction[] {firstCopy, secondCopy};
    }
}
