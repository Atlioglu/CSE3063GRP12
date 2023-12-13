package core.general_providers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.io.IOException;



public class Logger {

    private Path filePath;
    private String className;
    private String methodName;
    public Logger(String path){
        filePath = Paths.get(path);
        className = "";
        methodName = "";
        try {
            // Create the file if it doesn't exist
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        }catch (IOException e){}
    }
    public Logger(){
        filePath = Paths.get("./Iteration2/code/CSE3063GRP12/database/log/log.txt");
        try {
            // Create the file if it doesn't exist
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        }catch (IOException e){}
    }
    public void write(String input) {
        try {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            if (stackTrace.length >= 5) {
                StackTraceElement caller = stackTrace[4];
                className = caller.getClassName();
                methodName = caller.getMethodName();}

            // Write content to the file
            String message = input + " is user's choice in method " + methodName + " in class " + className + System.lineSeparator();
            Files.write(filePath, message.getBytes(), StandardOpenOption.APPEND);

        } catch (IOException e) {}
    }
    public void writeException(String input) {
        try {
            Files.write(filePath, (input + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e){}
    }
}


