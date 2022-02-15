package hr.fer.zemris.trisat;

import hr.fer.zemris.trisat.algorithms.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Class used to launch the program.
 *
 * @author Mateo Imbrišak
 */

public class TriSATSolver {

    /**
     * Don't let anyone instantiate this class.
     */
    private TriSATSolver() {}

    /**
     * Used to start the program.
     *
     * @param args two arguments:
     *             number of requested algorithm and
     *             path to the config file containing the formula.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Must provide number of requested algorithm and path to a configuration file.");
            return;
        }

        SATFormula formula;

        try {
            List<String> lines = Files.readAllLines(Path.of(args[1]));
            Clause[] clauses = null;
            int vars = 0, i = 0;

            for (String line : lines) {
                line = line.strip();

                if (line.startsWith("%")) {
                    break;
                }

                if (line.startsWith("c")) {
                    continue;
                }

                if (line.startsWith("p cnf")) {
                    String[] info = line.substring(5).strip().split("\\s+");

                    clauses = new Clause[Integer.parseInt(info[1])];
                    vars = Integer.parseInt(info[0]);
                } else if (clauses != null && vars != 0) {
                    clauses[i] = parse(line);
                    i++;
                }
            }

            if (clauses == null) {
                System.out.println("File does not contain configuration line.");
                return;
            }

            formula = new SATFormula(vars, clauses);
        } catch (IOException exc) {
            System.out.println("Error while reading the file.");
            return;
        }

        int alg = Integer.parseInt(args[0]);
        BitVector solution = null;

        switch (alg) {
            case 1:
                solution = FullSearch.search(formula);
                break;
            case 2:
                solution = IteratedSearch.search(formula);
                break;
            case 3:
                solution = WeightedIteratedSearch.search(formula);
                break;
            case 4:
                solution = GSAT.search(formula);
                break;
            case 5:
                solution = RandomWalkSAT.search(formula);
                break;
            case 6:
                solution = IteratedLocalSearch.search(formula);
                break;
            default:
                System.out.println("Algorithm number " + alg + " doesn't exist.");
                break;
        }

        if (solution == null) {
            System.out.println("Couldn't find a solution.");
        } else {
            System.out.println("Solution: " + solution);
            int satisfied = Util.fit(solution, formula);
            System.out.println("Satisfied " + (satisfied == formula.getNumberOfClauses() ?
                    "all" : satisfied) + " clauses.");
        }
    }

    /**
     * Used internally to parse config file.
     *
     * @param line being parsed.
     *
     * @return a {@link Clause} parsed from the given line.
     */
    private static Clause parse(String line) {
        String[] parts = line.split("\\s+");
        int[] indexes = new int[parts.length - 1];

        for (int i = 0, size = parts.length - 1; i < size; i++) {
            indexes[i] = Integer.parseInt(parts[i]);
        }

        return new Clause(indexes);
    }
}
