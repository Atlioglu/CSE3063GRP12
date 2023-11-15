package core.exceptions;
import java.lang.Exception;

public class WrongNumberOfCoursesSelectedException extends Exception{
    public WrongNumberOfCoursesSelectedException(String message){
        super(message);
    }
    
}
