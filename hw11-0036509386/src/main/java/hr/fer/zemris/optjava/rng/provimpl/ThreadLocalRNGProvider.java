package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

/**
 * A class that provides {@link IRNG} to threads.
 *
 * @author Mateo Imbrišak
 */

public class ThreadLocalRNGProvider implements IRNGProvider {

    /**
     * Provides {@link IRNG} for this thread.
     */
    private ThreadLocal<IRNG> threadLocal;

    /**
     * Default constructor that initializes {@link #threadLocal}.
     */
    public ThreadLocalRNGProvider() {
        threadLocal = new ThreadLocal<>();
    }

    @Override
    public IRNG getRNG() {
        if (threadLocal.get() == null) {
            threadLocal.set(new RNGRandomImpl());
        }

        return threadLocal.get();
    }
}
