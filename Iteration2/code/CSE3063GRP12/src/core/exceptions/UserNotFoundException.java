package core.exceptions;
import java.lang.Exception;
import core.general_providers.Logger;

public class UserNotFoundException extends Exception{
    private Logger logger;
    public UserNotFoundException(){
        super("User Not Found!");
        logger = new Logger();
        logger.writeException("Exception User Not Found!");
    }
}