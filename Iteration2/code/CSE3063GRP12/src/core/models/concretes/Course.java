package core.models.concretes;

import java.util.ArrayList;

public class Course {
    private String id;
    private String courseCode;
    private String name;
    private int credit;
    private CourseSession session;
    private ArrayList<Course> prerequisites;
    private int quota;
    private int currentQuota;
    private int semester;

    // Constructor with parameters
    public Course(String id, String courseCode, String name, int credit,
            CourseSession session, ArrayList<Course> prerequisites, int quota, int currentQuota, int semester) {
        this.id = id;
        this.courseCode = courseCode;
        this.name = name;
        this.credit = credit;
        this.session = session;
        this.prerequisites = prerequisites;
        this.quota = quota;
        this.currentQuota = currentQuota;
        this.semester = semester;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public int getCredit() {
        return credit;
    }

    public CourseSession getSession() {
        return session;
    }

    public ArrayList<Course> getPrerequisites() {
        return prerequisites;
    }

    public int getQuota() {
        return quota;
    }

    public int getCurrentQuota(){
        return currentQuota;
    }

    public int getSemester() {
        return semester;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void setSessions(CourseSession session) {
        this.session = session;
    }

    public void setPrerequisites(ArrayList<Course> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public void setCurrentQuota(int currentQuota){
        this.currentQuota = currentQuota;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

}