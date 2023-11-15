package core.models.concretes;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

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

    @SuppressWarnings("unchecked")
    public Lecturer(Map<String, Object> attributes) {
        this.id = attributes.get("id") != null ? (String) attributes.get("id") : null;
        this.firstName = attributes.get("firstName") != null ? (String) attributes.get("firstName") : null;
        this.lastName = attributes.get("lastName") != null ? (String) attributes.get("lastName") : null;
        this.userName = attributes.get("userName") != null ? (String) attributes.get("userName") : null;
        this.password = attributes.get("password") != null ? (String) attributes.get("password") : null;
        this.listOfCoursesGiven = (attributes.get("listOfCoursesGiven") != null
                && attributes.get("listOfCoursesGiven") instanceof ArrayList)
                        ? ((ArrayList<Map<String, Object>>) attributes.get("listOfCoursesGiven")).stream()
                                .map(
                                        courseData -> new Course(courseData))
                                .collect(Collectors.toCollection(ArrayList::new))
                        : null;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", this.id);
        jsonMap.put("firstName", this.firstName);
        jsonMap.put("lastName", this.lastName);
        jsonMap.put("userName", this.userName);
        jsonMap.put("password", this.password);
        if (this.listOfCoursesGiven != null) {
            jsonMap.put("listOfCoursesGiven", this.listOfCoursesGiven.stream()
                    .map(Course::toJson)
                    .collect(Collectors.toList()));
        } else {
            jsonMap.put("listOfCoursesGiven", null);
        }

        return jsonMap;
    }

    public ArrayList<Course> getListOfCoursesGiven() {
        return listOfCoursesGiven;
    }
}
