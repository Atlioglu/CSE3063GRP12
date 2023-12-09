package core.exceptions;
import java.lang.Exception;

public class UnexpectedInputException extends Exception{
    public UnexpectedInputException(){
        super("Unexpected Input!");
    }
}