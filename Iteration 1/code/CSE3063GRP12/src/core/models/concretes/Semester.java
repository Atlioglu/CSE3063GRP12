package core.models.concretes;

import java.util.Map;

import core.enums.CourseGrade;
import java.util.HashMap;
import java.util.stream.Collectors;

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
    // TODO: TEST listOfCoursesTaken

    @SuppressWarnings("unchecked")
    public Semester(Map<String, Object> attributes) {
        this.id = attributes.get("id") != null ? (String) attributes.get("id") : null;
        this.listOfCoursesTaken = new HashMap<>();
        if (attributes.get("listOfCoursesTaken") != null && attributes.get("listOfCoursesTaken") instanceof Map) {
            ((Map<?, ?>) attributes.get("listOfCoursesTaken")).forEach((key, value) -> {
                if (key instanceof Map<?, ?> && value instanceof String) {
                    Course course = new Course((Map<String, Object>) key);
                    CourseGrade grade = CourseGrade.valueOf((String) value);
                    this.listOfCoursesTaken.put(course, grade);
                }
            });
        }
        this.creditsTaken = attributes.get("creditsTaken") != null ? (Integer) attributes.get("creditsTaken") : 0;
        this.yano = attributes.get("yano") != null ? (Double) attributes.get("yano") : 0.0;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("id", this.id);
        jsonMap.put("creditsTaken", this.creditsTaken);
        jsonMap.put("yano", this.yano);
        if (this.listOfCoursesTaken != null) {
            Map<Map<String, Object>, String> coursesJson = new HashMap<>();
            this.listOfCoursesTaken.forEach((course, grade) -> {
                coursesJson.put(course.toJson(), grade.name());
            });
            jsonMap.put("listOfCoursesTaken", coursesJson);
        } else {
            jsonMap.put("listOfCoursesTaken", null);
        }

        return jsonMap;
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
