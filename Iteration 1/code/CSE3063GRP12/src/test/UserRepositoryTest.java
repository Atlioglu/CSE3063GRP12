package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import core.general_providers.SessionController;
import core.models.concretes.Student;
import core.repositories.UserRepository;
import core.general_providers.SessionController;
import core.models.abstracts.User;
import core.models.concretes.Advisor;
import core.models.concretes.Student;

public class UserRepositoryTest {

    @Test
    public void testUserFetching() {
        UserRepository userRepository = new UserRepository();
        Advisor advisor = new Advisor(null, null, null, null, null, null, null);
        ArrayList<Student> users = userRepository.getStudentsByAdvisor(advisor);
        assertNotNull("User should be found for valid username", users);
    }

    @Mock
    private SessionController sessionController;
    @Test
    public void testSetCurrentUser() throws Exception {
        UserRepository userRepository = new UserRepository();

         Student mockStudent = new Student();
        userRepository.setCurrentUser(mockStudent);

         verify(sessionController).setCurrentUser(mockStudent);
    }
}