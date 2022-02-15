package test;

import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;
import hr.fer.zemris.optjava.rng.provimpl.ThreadBoundRNGProvider;

/**
 * A simple program that tests {@link RNG} that uses {@link ThreadBoundRNGProvider}.
 *
 * @author Mateo Imbrišak
 */

public class Test2 {

    /**
     * Used to start the program.
     *
     * @param args nothing in this case.
     */
    public static void main(String[] args) {
        Runnable job = () -> {
            IRNG rng = RNG.getRNG();

            for(int i = 0; i < 20; i++) {
                System.out.println(rng.nextInt(-5, 5));
            }
        };

        EVOThread thread = new EVOThread(job);
        thread.start();
    }
}
