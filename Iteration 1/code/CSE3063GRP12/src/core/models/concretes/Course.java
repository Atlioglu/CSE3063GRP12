package core.models.concretes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    @SuppressWarnings("unchecked")
    public Course(Map<String, Object> attributes) {
        this.id = attributes.get("id") != null ? (String) attributes.get("id") : null;
        this.courseCode = attributes.get("courseCode") != null ? (String) attributes.get("courseCode") : null;
        this.name = attributes.get("name") != null ? (String) attributes.get("name") : null;
        this.credit = attributes.get("credit") != null ? (Integer) attributes.get("credit") : 0;
        this.quota = attributes.get("quota") != null ? (Integer) attributes.get("quota") : 0;
        this.sessions = (attributes.get("sessions") != null && attributes.get("sessions") instanceof ArrayList)
                ? ((ArrayList<Map<String, Object>>) attributes.get("sessions")).stream().map(
                        sessions -> new CourseSession(sessions)).collect(Collectors.toCollection(ArrayList::new))
                : null;
        this.prerequisites = (attributes.get("prerequisites") != null
                && attributes.get("prerequisites") instanceof ArrayList)
                        ? ((ArrayList<Map<String, Object>>) attributes.get("prerequisites")).stream().map(
                                prerequisites -> new Course(prerequisites))
                                .collect(Collectors.toCollection(ArrayList::new))
                        : null;

    }

    public Map<String, Object> toJson() {
        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("id", this.id);
        jsonMap.put("courseCode", this.courseCode);
        jsonMap.put("name", this.name);
        jsonMap.put("credit", this.credit);

        // Assuming CourseSession class has a toJson() method
        if (this.sessions != null) {
            List<Map<String, Object>> sessionList = this.sessions.stream()
                    .map(CourseSession::toJson)
                    .collect(Collectors.toList());
            jsonMap.put("sessions", sessionList);
        }

        // Assuming Course class itself has a toJson() method for prerequisites
        if (this.prerequisites != null) {
            List<Map<String, Object>> prerequisiteList = this.prerequisites.stream()
                    .map(Course::toJson)
                    .collect(Collectors.toList());
            jsonMap.put("prerequisites", prerequisiteList);
        }

        jsonMap.put("quota", this.quota);

        return jsonMap;
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

    public ArrayList<CourseSession> getSessions() {
        return sessions;
    }

    public ArrayList<Course> getPrerequisites() {
        return prerequisites;
    }

    public int getQuota() {
        return quota;
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

    public void setSessions(ArrayList<CourseSession> sessions) {
        this.sessions = sessions;
    }

    public void setPrerequisites(ArrayList<Course> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }
}