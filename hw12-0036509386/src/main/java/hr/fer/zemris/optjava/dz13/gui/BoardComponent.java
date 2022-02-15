package hr.fer.zemris.optjava.dz13.gui;

import hr.fer.zemris.optjava.dz13.solutions.Ant;
import hr.fer.zemris.optjava.dz13.solutions.Direction;

import javax.swing.*;
import java.awt.*;

/**
 * A component that displays Ant Trail board.
 *
 * @author Mateo Imbrišak
 */

public class BoardComponent extends JComponent {

    /**
     * Maximum amount of iteration to perform.
     */
    private static final int MAX_ITERATIONS = 601;

    /**
     * Ant used to run the simulation.
     */
    private final Ant ant;

    /**
     * Keeps track of current iteration/
     */
    private int iteration = 0;

    /**
     * Default constructor that assigns {@link #ant}.
     *
     * @param ant that ran the simulation.
     */
    public BoardComponent(Ant ant) {
        this.ant = ant;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.YELLOW);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        boolean[][] worldMap = ant.getWorldMap();

        int x = worldMap[0].length;
        int y = worldMap.length;

        int tileX = (int) Math.round(getWidth() / (double) x);
        int tileY = (int) Math.round(getHeight() / (double) y);

        g2d.setColor(Color.BLUE);

        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                if (worldMap[i][j]) {
                    g2d.fillRect(j * tileX, i * tileY, tileX, tileY);
                }
            }
        }

        int[] xArray = ant.getxArray();
        int[] yArray = ant.getyArray();

        g2d.setColor(Color.GREEN);

        for (int i = 0; i <= iteration; i++) {
            g2d.fillRect(xArray[i] * tileX, yArray[i] * tileY, tileX, tileY);
        }

        int xPosition = xArray[iteration];
        int yPosition = yArray[iteration];
        Direction direction = ant.getDirectionArray()[iteration];

        int[] xPoints = new int[3];
        int[] yPoints = new int[3];

        g2d.setColor(Color.BLACK);

        switch (direction) {
            case NORTH:
                xPoints[0] = xPosition * tileX;
                xPoints[1] = xPosition * tileX + tileX;
                xPoints[2] = xPosition * tileX + tileX / 2;

                yPoints[0] = yPosition * tileY;
                yPoints[1] = yPosition * tileY;
                yPoints[2] = yPosition * tileY + tileY;
                break;
            case SOUTH:
                xPoints[0] = xPosition * tileX;
                xPoints[1] = xPosition * tileX + tileX;
                xPoints[2] = xPosition * tileX + tileX / 2;

                yPoints[0] = yPosition * tileY + tileY;
                yPoints[1] = yPosition * tileY + tileY;
                yPoints[2] = yPosition * tileY;
                break;
            case WEST:
                xPoints[0] = xPosition * tileX + tileX;
                xPoints[1] = xPosition * tileX + tileX;
                xPoints[2] = xPosition * tileX;

                yPoints[0] = yPosition * tileY + tileY;
                yPoints[1] = yPosition * tileY;
                yPoints[2] = yPosition * tileY + tileY / 2;
                break;
            case EAST:
                xPoints[0] = xPosition * tileX;
                xPoints[1] = xPosition * tileX;
                xPoints[2] = xPosition * tileX + tileX;

                yPoints[0] = yPosition * tileY + tileY;
                yPoints[1] = yPosition * tileY;
                yPoints[2] = yPosition * tileY + tileY / 2;
                break;
        }

        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    /**
     * Performs t=next step in the simulation.
     *
     * @return {@code true} if this is not the final iteration,
     * otherwise {@code false}.
     */
    public boolean nextIteration() {
        if (iteration < MAX_ITERATIONS - 1) {
            iteration++;

            validate();
            repaint();

            return true;
        }

        return false;
    }
}
