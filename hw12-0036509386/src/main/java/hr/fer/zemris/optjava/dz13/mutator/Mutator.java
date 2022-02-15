package hr.fer.zemris.optjava.dz13.mutator;

import hr.fer.zemris.optjava.dz13.actions.*;

import java.util.Random;
import java.util.function.Function;

/**
 * A mutator that generates random subtrees.
 *
 * @author Mateo Imbrišak
 */

public class Mutator {

    /**
     * Total number of terminal symbols.
     */
    private static final int NUMBER_OF_TERMINALS = 3;

    /**
     * Total number of nonterminal symbols.
     */
    private static final int NUMBER_OF_NONTERMINALS = 3;

    /**
     * Used to generate random values.
     */
    private final Random rand;

    /**
     * Default constructor that assigns random value generator.
     *
     * @param rand used to generate random values.
     */
    public Mutator(Random rand) {
        this.rand = rand;
    }

    /**
     * Creates a random tree using full method.
     *
     * @param maxDepth of the tree being created.
     *
     * @return generated tree's root node.
     */
    public AntAction fullTree(int maxDepth) {
        if (maxDepth == 1) {
            return generateTerminal();
        }

        return generateNonterminal(maxDepth, this::fullTree);
    }

    /**
     * Creates a random tree using grow method.
     *
     * @param maxDepth of the tree being created.
     *
     * @return generated tree's root node.
     */
    public AntAction growTree(int maxDepth) {
        if (maxDepth == 1) {
            return generateTerminal();
        }

        if (rand.nextBoolean()) {
            return generateNonterminal(maxDepth, this::growTree);
        } else {
            return generateTerminal();
        }
    }

    /**
     * Generates a random terminal symbol.
     *
     * @return generated terminal symbol.
     */
    private AntAction generateTerminal() {
        int generatedIndex = rand.nextInt(NUMBER_OF_TERMINALS);

        switch (generatedIndex) {
            case 0:
                return new Left();
            case 1:
                return new Right();
            case 2:
                return new Move();
            default:
                throw new RuntimeException("Unexpected generated index.");
        }
    }

    /**
     * Generates a random nonterminal symbol.
     *
     * @param maxDepth of the parent tree.
     * @param function that called this function.
     *
     * @return generated nonterminal symbol.
     */
    private AntAction generateNonterminal(int maxDepth, Function<Integer, AntAction> function) {
        int generatedIndex = rand.nextInt(NUMBER_OF_NONTERMINALS);

        AntAction first;
        AntAction second;
        AntAction third;

        switch (generatedIndex) {
            case 0:
                first = function.apply(maxDepth - 1);
                second = function.apply(maxDepth - 1);

                return new IfFoodAhead(first, second);
            case 1:
                first = function.apply(maxDepth - 1);
                second = function.apply(maxDepth - 1);

                return new Prog2(first, second);
            case 2:
                first = function.apply(maxDepth - 1);
                second = function.apply(maxDepth - 1);
                third = function.apply(maxDepth - 1);

                return new Prog3(first, second, third);
            default:
                throw new RuntimeException("Unexpected generated index.");
        }
    }
}
