import logger.Level;
import logger.Logger;

/**
 * The program starts executing here.
 *
 * @author Luka Kralj
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        Logger.startLogger(true, true);
        Logger.log("This is debug message", Level.DEBUG);
        Logger.log("This is info message.");
        Logger.flush();
    }
}
