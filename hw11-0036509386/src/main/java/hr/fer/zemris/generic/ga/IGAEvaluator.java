package hr.fer.zemris.generic.ga;

/**
 * An interface that models evaluators for {@link GASolution}s.
 *
 * @author marcupic
 */

public interface IGAEvaluator<T> {

    /**
     * Evaluates the given solution.
     *
     * @param p solution to be evaluated.
     */
    void evaluate(GASolution<T> p);
}
