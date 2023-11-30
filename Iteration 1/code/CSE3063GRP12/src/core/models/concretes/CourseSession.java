package core.models.concretes;

import java.util.ArrayList;
import java.util.Map;

import core.enums.CourseDay;
import core.enums.CourseSlot;

public class CourseSession {
    private String id;
    private Map<CourseDay, ArrayList<CourseSlot>> courseSessions;
    private Lecturer lecturer;
    private String name;

    // Constructor with parameters
    public CourseSession(String id, Map<CourseDay, ArrayList<CourseSlot>> courseSessions, Lecturer lecturer) {
        this.id = id;
        this.courseSessions = courseSessions;
        this.lecturer = lecturer;

    }

    public Map<CourseDay, ArrayList<CourseSlot>> getCourseSessions() {
        return courseSessions;
    }


    public String getName() {
        return name;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public String getId() {
        return id;
    }

    public void setCourseSessions(Map<CourseDay, ArrayList<CourseSlot>> courseSessions) {
        this.courseSessions = courseSessions;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;

    }

    // Setters

}