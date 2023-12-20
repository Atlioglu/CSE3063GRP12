package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.exceptions.UserNotFoundException;
import core.exceptions.WrongPasswordException;
import core.general_providers.SessionController;
import core.models.abstracts.User;
import core.models.concretes.Student;
import core.repositories.UserRepository;

public class UserRepositoryTest {

    @Test
    public void testLoginCheckSuccessful() throws UserNotFoundException, WrongPasswordException {
        UserRepository userRepository = new UserRepository();
        userRepository.loginCheck("o150121534", "123123");
        User user = SessionController.getInstance().getCurrentUser();
        assertNotNull(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginCheckUserNotFound() throws UserNotFoundException, WrongPasswordException {
        UserRepository userRepository = new UserRepository();
        userRepository.loginCheck("o1501215", "123123");
    }

    @Test(expected = WrongPasswordException.class)
    public void testLoginCheckWrongPassword() throws UserNotFoundException, WrongPasswordException {
        UserRepository userRepository = new UserRepository();
        userRepository.loginCheck("o150121534", "1231234");
    }
}