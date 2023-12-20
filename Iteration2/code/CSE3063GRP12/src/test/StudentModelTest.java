package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.models.abstracts.User;
import core.models.concretes.Student;

public class StudentModelTest {

    @Test
    public void testUserEqual() {
        Student student = new Student();
        student.setUserName("test");
        student.setPassword("test");
        Student student2 = new Student();
        student2.setUserName("test");
        student2.setPassword("test");

        assertEquals(student, student2);

    }

    @Test
    public void testUserNotEqual() {
        Student student = new Student();
        student.setUserName("test");
        student.setPassword("test");
        Student student2 = new Student();
        student2.setUserName("test1");
        student2.setPassword("test1");

        assertNotEquals(student, student2);

    }

    @Test
    public void testUserType() {
        User student = new Student();
        assertTrue(student instanceof User);
    }

}
