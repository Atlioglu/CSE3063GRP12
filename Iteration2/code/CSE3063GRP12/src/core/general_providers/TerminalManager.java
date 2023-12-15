package core.general_providers;
import java.util.Scanner;

public class TerminalManager {
    private static TerminalManager instance;
    private Scanner scanner;

    private Logger logger;

    private TerminalManager() {
        scanner = new Scanner(System.in);
        logger = new Logger();
    }

    public static TerminalManager getInstance() {
        if (instance == null) {
            instance = new TerminalManager();
        }
        return instance;
    }

    // public void dispose() {
    // scanner.close();
    // }

    public String read() {
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            logger.write(input);
            return input;
        }
        return null;
        // return scanner.nextLine();
    }
}