package core.enums;

import features.advisor.course_approval.CourseApprovalController;
import features.advisor.student_list.AdvisorStudentListController;
import features.login.LoginController;

public enum AdvisorMenu implements Menu {
    CourseApproval, StudentList, Logout;

    public String getItemMessage() {
        switch (this) {
            case CourseApproval:
                return ("Course Approval");
            case StudentList:
                return ("Student List");
            case Logout:
                return ("Logout");
        }
        return this.name();
    }

    public void navigate() {
        switch (this) {
            case CourseApproval:
                new CourseApprovalController();
                break;
            case StudentList:
                new AdvisorStudentListController();
                break;
            case Logout:
                new LoginController();
                break;
        }
    }
}
