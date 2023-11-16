package core.models.concretes;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

public class Advisor extends Lecturer {
    private ArrayList<Student> listOfStudents;

    public Advisor(String id, String firstName, String lastName, String userName,
            String password, ArrayList<Course> listOfCoursesGiven,
            ArrayList<Student> listOfStudents) {
        super(id, firstName, lastName, userName, password, listOfCoursesGiven);
        this.listOfStudents = listOfStudents;
    }

    @SuppressWarnings("unchecked")
    public Advisor(Map<String, Object> attributes) {
        super(attributes); 
        this.listOfStudents = (attributes.get("listOfStudents") != null
                && attributes.get("listOfStudents") instanceof ArrayList)
                        ? ((ArrayList<Map<String, Object>>) attributes.get("listOfStudents")).stream()
                                .map(studentData -> new Student(studentData))
                                .collect(Collectors.toCollection(ArrayList::new))
                        : new ArrayList<>();
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.putAll(super.toJson());
        if (this.listOfStudents != null) {
            jsonMap.put("listOfStudents", this.listOfStudents.stream()
                    .map(Student::toJson)
                    .collect(Collectors.toList()));
        } else {
            jsonMap.put("listOfStudents", null);
        }

        return jsonMap;
    }

    public ArrayList<Student> getListOfStudents() {
        return listOfStudents;
    }
}
