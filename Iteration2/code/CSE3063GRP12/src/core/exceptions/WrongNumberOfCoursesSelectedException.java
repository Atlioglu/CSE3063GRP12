package core.exceptions;
import java.lang.Exception;
import core.general_providers.Logger;

public class WrongNumberOfCoursesSelectedException extends Exception{
    private Logger logger;
    public WrongNumberOfCoursesSelectedException(){
        super("Wrong Number of Courses Selected! Please choose 5 courses at max!");
        logger = new Logger();
        logger.writeException("Exception Wrong Number of Courses Selected");
    }
    
}
