package core.exceptions;
import java.lang.Exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(){
        super("User Not Found!");
    }
}