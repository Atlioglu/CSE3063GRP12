package core.models.concretes;

import java.util.ArrayList;
import java.util.Map;

public class Advisor extends Lecturer {
    private ArrayList<Student> listOfStudents;

    public Advisor(String id, String firstName, String lastName, String userName, 
                   String password, ArrayList<Course> listOfCoursesGiven, 
                   ArrayList<Student> listOfStudents) {
        super(id, firstName, lastName, userName, password, listOfCoursesGiven);
        this.listOfStudents = listOfStudents;
    }
    //this is not completed yet
    public Advisor(Map<String, Object> attributes) {
    super(attributes); // This calls the Lecturer(Map<String, Object>) constructor
    this.listOfStudents = (ArrayList<Student>) attributes.get("listOfStudents");
}


    public ArrayList<Student> getListOfStudents() {
        return listOfStudents;
    }
}
