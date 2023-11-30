package test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UnexpectedInputExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testUnexpectedInputException() throws UnexpectedInputException {
        thrown.expect(UnexpectedInputException.class);
        thrown.expectMessage("Unexpected Input!");

        // Burada UnexpectedInputException fırlatılacak bir metod çağrılmalı
        throw new UnexpectedInputException();
    }
}