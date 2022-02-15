package hr.fer.zemris.optjava.rng;

import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

/**
 * A {@link Thread} that possesses it's own {@link IRNG}.
 *
 * @author Mateo Imbrišak
 */

public class EVOThread extends Thread implements IRNGProvider {

    /**
     * Provides {@link IRNG} to this {@link Thread}.
     */
    private IRNG rng = new RNGRandomImpl();

    public EVOThread() {
        super();
    }

    public EVOThread(Runnable target) {
        super(target);
    }

    public EVOThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public EVOThread(String name) {
        super(name);
    }

    public EVOThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public EVOThread(Runnable target, String name) {
        super(target, name);
    }

    public EVOThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public EVOThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    public EVOThread(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals) {
        super(group, target, name, stackSize, inheritThreadLocals);
    }

    @Override
    public IRNG getRNG() {
        return rng;
    }
}
