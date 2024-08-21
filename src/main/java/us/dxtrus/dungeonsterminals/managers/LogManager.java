package us.dxtrus.dungeonsterminals.managers;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LogManager {
    private LogManager() {
        throw new IllegalStateException("Cannot create instance of UtilityClass");
    }

    private static final Logger logger = Logger.getLogger("API-Backend");

    public static void info(@NotNull String message) {
        logger.log(Level.INFO, message);
    }

    public static void warn(@NotNull String message) {
        logger.log(Level.WARNING, message);
    }

    public static void warn(@NotNull String message, @NotNull Exception e) {
        logger.log(Level.WARNING, message, e);
    }

    public static void severe(@NotNull String message) {
        logger.severe(message);
    }

    public static void severe(@NotNull String message, @NotNull Exception e) {
        logger.log(Level.SEVERE, message, e);
    }

    public static void debug(@NotNull String message) {
        logger.log(Level.FINE, message);
    }
}
