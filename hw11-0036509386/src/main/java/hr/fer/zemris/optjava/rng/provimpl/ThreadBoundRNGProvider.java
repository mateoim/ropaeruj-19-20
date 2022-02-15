package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;

/**
 * An implementation of {@link IRNGProvider} used by {@link Thread}s
 * that implement {@link IRNGProvider}.
 *
 * @author Mateo Imbrišak
 */

public class ThreadBoundRNGProvider implements IRNGProvider {

    @Override
    public IRNG getRNG() {
        return ((IRNGProvider) Thread.currentThread()).getRNG();
    }
}
