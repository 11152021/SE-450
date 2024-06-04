package edu.depaul;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingSystem {
	
	// Obtain an instance of the Logger class 
    private static final Logger logger = Logger.getLogger(LoggingSystem.class.getName());
    private static boolean initialized = false;

    // Initialize the logger
    public static void initializeLogger() {
        if (!initialized) {
            try {
                FileHandler fileHandler = new FileHandler("logfile.log");
                fileHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(fileHandler);
                initialized = true;
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to create FileHandler for logging.", e);
            }
        }
    }

    // Log Info
    public static void logInfo(String message) {
        initializeLogger();
        logger.info(message);
    }

    // Log Warnings
    public static void logWarning(String message) {
        initializeLogger();
        logger.warning(message);
    }

    // Log Severe
    public static void logSevere(String message) {
        initializeLogger();
        logger.severe(message);
    }
}