package test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UserNotFoundExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testUserNotFoundException() throws UserNotFoundException {
        thrown.expect(UserNotFoundException.class);
        thrown.expectMessage("User Not Found!");

        // Burada UserNotFoundException fırlatılacak bir metod çağrılmalı
        throw new UserNotFoundException();
    }
}