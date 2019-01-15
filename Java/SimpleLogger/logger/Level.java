package logger;

/**
 * Represents the severity of the message logged.
 *
 * @author Luka Kralj
 * @version 1.0
 */
public enum Level {
    /**The message marked with INFO represents general information about the state of the program at that point.*/
    INFO,
    /**The message marked with WARNING requires more attention, but reports no errors in the workflow of the program.*/
    WARNING,
    /**The message marked with ERROR tells that something went wrong in the program (important exception caught etc.).
     * This messages are very important as they probably compromised the normal/expected workflow of the program*/
    ERROR,
    /**The message marked with DEBUG, is used to log messages that help debugging. Generally, there should be no
     * such logs in the production code.*/
    DEBUG
}
