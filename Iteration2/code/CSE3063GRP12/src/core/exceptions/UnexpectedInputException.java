package core.exceptions;
import java.lang.Exception;
import core.general_providers.Logger;

public class UnexpectedInputException extends Exception{
    private Logger logger;
    public UnexpectedInputException(){
        super("Unexpected Input!");
        logger = new Logger();
        logger.writeException("Exception Unexpected Input");
    }
}