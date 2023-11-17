package core.exceptions;
import java.lang.Exception;

public class WrongNumberOfCoursesSelectedException extends Exception{
    public WrongNumberOfCoursesSelectedException(){
        super("Wrong Number of Courses Selected! Please choose 5 courses at max!");
    }
    
}
