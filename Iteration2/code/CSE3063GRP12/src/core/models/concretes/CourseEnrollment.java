package core.models.concretes;

import java.util.ArrayList;
import core.enums.ApprovalState;

public class CourseEnrollment {
    private String id;
    private ArrayList<Course> selectedCourseList;
    private ArrayList<Course> approvedCourseList;
    private ArrayList<Course> rejectedCourseList;
    private String studentId;
    private ApprovalState approvalState;

    public CourseEnrollment(String id, ArrayList<Course> selectedCourseList, String studentId,
            ApprovalState approvalState) {
        this.id = id;
        this.selectedCourseList = selectedCourseList;
        this.studentId = studentId;
        this.approvalState = approvalState;
    }

    // Getters
    public String getId() {
        return id;
    }

    public ArrayList<Course> getSelectedCourseList() {
        return selectedCourseList;
    }

    public String getStudentId() {
        return studentId;
    }

    public ApprovalState getApprovalState() {
        return approvalState;
    }
    

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setSelectedCourseList(ArrayList<Course> selectedCourseList) {
        this.selectedCourseList = selectedCourseList;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setApprovalState(ApprovalState approvalState) {
        this.approvalState = approvalState;
    }

    public ArrayList<Course> getApprovedCourseList() {
        return approvedCourseList;
    }

    public void setApprovedCourseList(ArrayList<Course> approvedCourseList) {
        this.approvedCourseList = approvedCourseList;
    }

    public ArrayList<Course> getRejectedCourseList() {
        return rejectedCourseList;
    }

    public void setRejectedCourseList(ArrayList<Course> rejectedCourseList) {
        this.rejectedCourseList = rejectedCourseList;
    }

}