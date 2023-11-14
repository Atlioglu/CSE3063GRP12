package core.models.concretes;

import java.util.ArrayList;
import java.util.Map;

public class Course {
    private String id;
    private String courseCode;
    private String name;
    private int credit;
    private ArrayList<CourseSession> sessions;
    private ArrayList<Course> prerequisites;
    private int quota;

    // Constructor with parameters
    public Course(String id, String courseCode, String name, int credit,
                  ArrayList<CourseSession> sessions, ArrayList<Course> prerequisites, int quota) {
        this.id = id;
        this.courseCode = courseCode;
        this.name = name;
        this.credit = credit;
        this.sessions = sessions;
        this.prerequisites = prerequisites;
        this.quota = quota;
    }

    // Constructor with a map parameter
    public Course(Map<String, Object> attributes) {
        this.id = (String) attributes.get("id");
        this.courseCode = (String) attributes.get("courseCode");
        this.name = (String) attributes.get("name");
        this.credit = (Integer) attributes.get("credit");
        // TODO: Implement the logic to assign the values from the map to the fields.
        this.sessions = (ArrayList<CourseSession>) attributes.get("sessions");
        this.prerequisites = (ArrayList<Course>) attributes.get("prerequisites");
        this.quota = (Integer) attributes.get("quota");
    }

    public Map<String, Object> toJson() {
        
        return null; // Placeholder
    }

    // Getters
    public String getId() { return id; }
    public String getCourseCode() { return courseCode; }
    public String getName() { return name; }
    public int getCredit() { return credit; }
    public ArrayList<CourseSession> getSessions() { return sessions; }
    public ArrayList<Course> getPrerequisites() { return prerequisites; }
    public int getQuota() { return quota; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setName(String name) { this.name = name; }
    public void setCredit(int credit) { this.credit = credit; }
    public void setSessions(ArrayList<CourseSession> sessions) { this.sessions = sessions; }
    public void setPrerequisites(ArrayList<Course> prerequisites) { this.prerequisites = prerequisites; }
    public void setQuota(int quota) { this.quota = quota; }
}