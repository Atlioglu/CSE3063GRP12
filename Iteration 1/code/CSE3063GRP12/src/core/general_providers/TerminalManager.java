package core.general_providers;
import java.util.Scanner;

public class TerminalManager {
    private static TerminalManager instance;
    private Scanner scanner;

    private TerminalManager() {
        scanner = new Scanner(System.in);
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
            return scanner.nextLine();
        }
        return null;
        // return scanner.nextLine();
    }
}