package core.models.concretes;

import java.util.ArrayList;
import java.util.Map;

import core.enums.CourseDay;
import core.enums.CourseSlot;

public class CourseSession {
    private String id;
    private Map<CourseDay, ArrayList<CourseSlot>> courseSessions;
    private Lecturer lecturer;

    // Constructor with parameters
    public CourseSession(String id, Map<CourseDay, ArrayList<CourseSlot>> courseSessions, Lecturer lecturer) {
        this.id = id;
        this.courseSessions = courseSessions;
        this.lecturer = lecturer;
    }

    // Constructor with a map parameter
    public CourseSession(Map<String, Object> objects) {
        this.id = (String) objects.get("id");
        // TODO: Implement the logic to assign the values from the map to the fields.
        this.courseSessions = (Map<CourseDay, ArrayList<CourseSlot>>) objects.get("courseSessions");
        this.lecturer = (Lecturer) objects.get("lecturer");
    }

    public Map<CourseDay, ArrayList<CourseSlot>> getCourseSessions() {
        return courseSessions;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

}