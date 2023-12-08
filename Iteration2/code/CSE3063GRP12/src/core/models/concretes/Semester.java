package core.models.concretes;

import java.util.Map;

import core.enums.CourseGrade;

public class Semester {
    private String id;
    // <CourseId,CourseGrade>
    private Map<String, CourseGrade> listOfCoursesTaken;
    private int creditsTaken;
    private double yano;

    public Semester(String id, Map<String, CourseGrade> listOfCoursesTaken,
            int creditsTaken, double yano) {
        this.id = id;
        this.listOfCoursesTaken = listOfCoursesTaken;
        this.creditsTaken = creditsTaken;
        this.yano = yano;
    }

    public String getId() {
        return id;
    }

    public Map<String, CourseGrade> getListOfCoursesTaken() {
        return listOfCoursesTaken;
    }

    public int getCreditsTaken() {
        return creditsTaken;
    }

    public double getYano() {
        return yano;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setListOfCoursesTaken(Map<String, CourseGrade> listOfCoursesTaken) {
        this.listOfCoursesTaken = listOfCoursesTaken;
    }

    public void setCreditsTaken(int creditsTaken) {
        this.creditsTaken = creditsTaken;
    }

    public void setYano(double yano) {
        this.yano = yano;
    }

}
