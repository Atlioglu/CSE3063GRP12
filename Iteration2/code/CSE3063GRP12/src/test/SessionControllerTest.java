package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import core.enums.UserType;
import core.general_providers.SessionController;
import core.models.concretes.Student;

public class SessionControllerTest {

    @Test
    public void testUserType() {
        SessionController sessionController = SessionController.getInstance();

        Student student = new Student();
        sessionController.setCurrentUser(student);

        UserType userType = sessionController.getUserType();

        assertEquals(userType, UserType.Student);

    }

    @Test
    public void testSetCurrentUser() {
        Student student = new Student();
        student.setUserName("test");
        SessionController sessionController = SessionController.getInstance();
        sessionController.setCurrentUser(student);
        assertEquals(sessionController.getCurrentUser(), student);
    }

}