package hr.fer.zemris.trisat;

/**
 * A class that represents a single clause in a {@link SATFormula}.
 *
 * @author Mateo Imbrišak
 */

public class Clause {

    /**
     * Keeps the indexes and complements of this clause.
     */
    private final int[] indexes;

    /**
     * Default constructor that assigns the {@link #indexes}.
     *
     * @param indexes to be assigned
     */
    public Clause(int[] indexes) {
        this.indexes = indexes;
    }

    /**
     * Provides the size of this Clause.
     *
     * @return size of this Clause.
     */
    public int getSize() {
        return indexes.length;
    }

    /**
     * Provides the index at the given position.
     *
     * @param index requested position.
     *
     * @return index at the requested position.
     */
    public int getLiteral(int index) {
        return indexes[index];
    }

    /**
     * Checks if this {@code Clause} is satisfied by the given {@link BitVector}.
     *
     * @param assignment used to check the {@code Clause}.
     *
     * @return {@code true} if the {@code Clause} is satisfied,
     * otherwise {@code false}.
     */
    public boolean isSatisfied(BitVector assignment) {
        for (int index : indexes) {
            boolean bit = assignment.get(Math.abs(index) - 1);
            if ((bit && index > 0) || (!bit && index < 0)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int index : indexes) {
            sb.append(index).append(" ");
        }

        return sb.toString() + " 0";
    }
}
