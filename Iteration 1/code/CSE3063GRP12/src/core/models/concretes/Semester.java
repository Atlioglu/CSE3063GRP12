package core.models.concretes;

import java.util.Map;

import core.Enums.CourseGrade;

public class Semester {
    private String id;
    private Map<Course, CourseGrade> listOfCoursesTaken;
    private int creditsTaken;
    private double yano;

    public Semester(String id, Map<Course, CourseGrade> listOfCoursesTaken, 
                    int creditsTaken, double yano) {
        this.id = id;
        this.listOfCoursesTaken = listOfCoursesTaken;
        this.creditsTaken = creditsTaken;
        this.yano = yano;
    }

    public Semester(Map<String, Object> attributes) {
        this.id = (String) attributes.get("id");
        this.listOfCoursesTaken = (Map<Course, CourseGrade>) attributes.get("listOfCoursesTaken");
        this.creditsTaken = (int) attributes.get("creditsTaken");
        this.yano = (double) attributes.get("yano");
    }

    public Map<String, Object> toJson() {
        // Convert to JSON map
        return null; // Placeholder
    }

    public String getId() {
        return id;
    }

    public Map<Course, CourseGrade> getListOfCoursesTaken() {
        return listOfCoursesTaken;
    }

    public int getCreditsTaken() {
        return creditsTaken;
    }

    public double getYano() {
        return yano;
    }
}
