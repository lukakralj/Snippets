package logger;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * This class unifies the output of the debug statements.
 *
 * @author Luka Kralj
 * @version 1.0
 */
public class Logger {
    private static boolean consoleOutput;
    private static boolean fileOutput;

    private static StringBuffer buffer = new StringBuffer();
    private static File file;
    private static boolean started;

    /**
     * This needs to be called in the main method and should be called only once.
     * This ensures that each run is saving logs into a unique file.
     *
     * @param consoleOutputIn True if we want to output to the console, false if not.
     * @param fileOutputIn True if we want to output to the file, false if not.
     */
    public static void startLogger(boolean consoleOutputIn, boolean fileOutputIn) {
        started = true;
        consoleOutput = consoleOutputIn;
        fileOutput = fileOutputIn;
        if (fileOutput) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Timestamp(System.currentTimeMillis()));
            String filename = "logger/logs/debug_log_" + timestamp + ".log"; // append timestamp and extension
            file = new File(filename);
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                fileOutput = false;
                consoleOutput = true;
                log("Could not create new log file. Error message:\n" + e.getMessage(), Level.ERROR);
                log("Logging to the console only.", Level.WARNING);
            }
        }
        log("Logger started.");
    }

    /**
     * @see #startLogger(boolean, boolean)
     * By default the logger will be outputting to the console but not into a file.
     */
    public static void startLogger() {
        startLogger(true, false);
    }

    /**
     * Formats the new message and saves it to the buffer.
     * Call flush() if you want to show the buffered messages.
     *
     * @param message Message that we want to log.
     * @param level The importance of the message.
     */
    public static void log(String message, Level level) {
        if (!started) {
            return;
        }
        String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
        buffer.append(timestamp).append(" ");

        switch (level) {
            case INFO: buffer.append("[INFO]"); break;
            case WARNING: buffer.append("[WARNING]"); break;
            case ERROR: buffer.append("[ERROR]"); break;
            case DEBUG: buffer.append("[DEBUG]"); break;
            default: buffer.append("[unknown]"); break;
        }

        buffer.append(" ").append(message).append("\n");
    }

    /**
     * @see #log(String, Level)
     * Level of this message is INFO.
     *
     * @param message Message that we want to log.
     */
    public static void log(String message) {
        log(message, Level.INFO);
    }

    /**
     * Displays the buffered messages into either the console or a file, depends on the
     * flags set at the beginning of the program.
     */
    public static void flush() {
        if (!started) {
            return;
        }
        if (fileOutput) {
            FileWriter fw = null;
            BufferedWriter bw = null;
            PrintWriter out = null;
            try {
                fw = new FileWriter(file, true);
                bw = new BufferedWriter(fw);
                out = new PrintWriter(bw);
                out.print(buffer);
                out.close();
                bw.close();
                fw.close();
            }
            catch (IOException e) {
                consoleOutput = true;
                log("Could not flush to the log file. Error message:\n" + e.getMessage(), Level.WARNING);
            }
        }
        if (consoleOutput) {
            System.out.println(buffer);
        }
        buffer = new StringBuffer();
    }
}
