package hr.fer.zemris.optjava.dz13.gui;

import hr.fer.zemris.optjava.dz13.algorithms.GeneticAlgorithm;
import hr.fer.zemris.optjava.dz13.solutions.Ant;

import javax.swing.*;
import java.awt.*;

/**
 * A simple GUI that runs simulates the best solution found by {@link GeneticAlgorithm}.
 *
 * @author Mateo Imbrišak
 */

public class AntTrailBoard extends JFrame {

    /**
     * Keeps the reference to {@link BoardComponent}.
     */
    private final BoardComponent board;

    /**
     * Default constructor that initializes {@link #board}.
     *
     * @param ant used by {@link #board}.
     */
    public AntTrailBoard(Ant ant) {
        this.board = new BoardComponent(ant);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(600, 600);

        add(board, BorderLayout.CENTER);

        initGUI();

        setVisible(true);
    }

    /**
     * Initializes the GUI.
     */
    private void initGUI() {
        JPanel panel = new JPanel();

        JButton next = new JButton("Next");
        JButton run = new JButton("Run");

        next.addActionListener((e) -> {
            boolean hasNext = board.nextIteration();

            if (!hasNext) {
                next.setEnabled(false);
                run.setEnabled(false);
            }
        });

        panel.add(next);

        run.addActionListener((e) -> {
            next.setEnabled(false);
            run.setEnabled(false);

            Thread animation = new Thread(() -> {
                boolean hasNext = true;

                while (hasNext) {
                    hasNext = board.nextIteration();

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ignore) {}
                }
            });

            animation.start();
        });

        panel.add(run);

        add(panel, BorderLayout.SOUTH);
    }
}
