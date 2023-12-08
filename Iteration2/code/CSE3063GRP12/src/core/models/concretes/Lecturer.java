package core.models.concretes;

import java.util.ArrayList; 
import core.models.abstracts.User;

public class Lecturer extends User {
    private ArrayList<Course> listOfCoursesGiven;

    public Lecturer(String id, String firstName, String lastName, String userName,
            String password, ArrayList<Course> listOfCoursesGiven) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.listOfCoursesGiven = listOfCoursesGiven;
    }

    public void setListOfCoursesGiven(ArrayList<Course> listOfCoursesGiven) {
        this.listOfCoursesGiven = listOfCoursesGiven;
    }

    public ArrayList<Course> getListOfCoursesGiven() {
        return listOfCoursesGiven;
    }

}
