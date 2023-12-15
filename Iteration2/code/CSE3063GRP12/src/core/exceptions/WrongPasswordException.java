package core.exceptions;
import java.lang.Exception;
import core.general_providers.Logger;

public class WrongPasswordException extends Exception{
    private Logger logger;
    public WrongPasswordException(){
        super("Wrong Password!");
        logger = new Logger();
        logger.writeException("Exception Wrong Password!");
    }
}