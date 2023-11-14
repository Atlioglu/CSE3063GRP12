package core.models.concretes;

import java.util.ArrayList;
import java.util.Map;
import core.Enums.ApprovalState;

public class CourseEnrollment {
    private String id;
    private ArrayList<Course> selectedCourseList;
    private String studentId;
    private ApprovalState approvalState;

    // Constructor with parameters
    public CourseEnrollment(String id, ArrayList<Course> selectedCourseList, String studentId, ApprovalState approvalState) {
        this.id = id;
        this.selectedCourseList = selectedCourseList;
        this.studentId = studentId;
        this.approvalState = approvalState;
    }

    // Constructor with a map parameter
    public CourseEnrollment(Map<String, Object> attributes) {
        this.id = (String) attributes.get("id");
        // TODO: Fix this
        this.selectedCourseList = (ArrayList<Course>) attributes.get("selectedCourseList");
        this.studentId = (String) attributes.get("studentId");
        this.approvalState = (ApprovalState) attributes.get("approvalState");
    }

    public Map<String, Object> toJson() {
   
        return null; // Placeholder
    }

    // Getters
    public String getId() { return id; }
    public ArrayList<Course> getSelectedCourseList() { return selectedCourseList; }
    public String getStudentId() { return studentId; }
    public ApprovalState getApprovalState() { return approvalState; }

}