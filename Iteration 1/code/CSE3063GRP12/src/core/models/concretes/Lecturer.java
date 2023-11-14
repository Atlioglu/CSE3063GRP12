package core.models.concretes;

import java.util.ArrayList;
import java.util.Map;

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
    // this is not completed yet
    public Lecturer(Map<String, Object> attributes) {
        this.listOfCoursesGiven = (ArrayList<Course>) attributes.get("listOfCoursesGiven");
    }

    @Override
    public Map<String, Object> toJson() {
        // Convert to JSON map
        return null; // Placeholder
    }

    public ArrayList<Course> getListOfCoursesGiven() {
        return listOfCoursesGiven;
    }
}
