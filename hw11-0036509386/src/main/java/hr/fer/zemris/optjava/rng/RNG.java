package hr.fer.zemris.optjava.rng;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * A Singleton that provides {@link IRNG} to threads.
 *
 * @author Mateo Imbrišak
 */

public class RNG {

    /**
     * Provides {@link IRNG}.
     */
    private static IRNGProvider rngProvider;

    static {
        Properties properties = new Properties();

        try (InputStream resource = RNG.class.getClassLoader().getResourceAsStream("rng-config.properties")) {
            if (resource != null) {
                properties.load(resource);
            } else {
                System.err.println("File rng-config.properties cannot be found.");
            }
        } catch (IOException exc) {
            System.err.println("Error reading rng-config.properties file.");
        }

        String classPath = properties.getProperty("rng-provider");

        try {
            rngProvider = (IRNGProvider) RNG.class.getClassLoader().loadClass(classPath).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                NoSuchMethodException | ClassNotFoundException exc) {
            System.err.println("Error creating IRNGProvider instance.");
        }
    }

    /**
     * Provides random number generator for the thread requesting it.
     *
     * @return {@link IRNG} used by the calling thread.
     */
    public static IRNG getRNG() {
        return rngProvider.getRNG();
    }
}
