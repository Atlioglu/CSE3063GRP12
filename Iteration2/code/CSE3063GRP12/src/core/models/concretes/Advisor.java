package core.models.concretes;

import java.util.ArrayList;

public class Advisor extends Lecturer {
    private ArrayList<String> listOfStudentIds;

    public Advisor(String id, String firstName, String lastName, String userName,
            String password, ArrayList<Course> listOfCoursesGiven,
            ArrayList<String> listOfStudentIds) {
        super(id, firstName, lastName, userName, password, listOfCoursesGiven);
        this.listOfStudentIds = listOfStudentIds;
    }

    public void setListOfStudentIds(ArrayList<String> listOfStudents) {
        this.listOfStudentIds = listOfStudents;
    }

    public ArrayList<String> getListOfStudentIds() {
        return listOfStudentIds;
    }
}
