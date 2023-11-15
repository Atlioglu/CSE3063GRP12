package core.exceptions;
import java.lang.Exception;

public class WrongPasswordException extends Exception{
    public WrongPasswordException(String message){
        super(message);
    }
}