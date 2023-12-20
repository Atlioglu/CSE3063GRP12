package core.enums;

import features.advisor.course_approval.CourseApprovalController;
import features.advisor.student_list.AdvisorStudentListController;
import features.login.LoginController;
import features.notification.NotificationController;

public enum AdvisorMenu implements Menu {
    CourseApproval, StudentList, Notification, Logout;

    public String getItemMessage() {
        switch (this) {
            case CourseApproval:
                return ("Course Approval");
            case StudentList:
                return ("Student List");
            case Notification:
                return ("Notification");
            case Logout:
                return ("Logout");
        }
        return this.name();
    }
    public boolean isNotification(){
        return this == Notification;
    }
    public void navigate() {
        switch (this) {
            case CourseApproval:
                new CourseApprovalController();
                break;
            case StudentList:
                new AdvisorStudentListController();
                break;
            case Notification:
                new NotificationController();
                break;
            case Logout:
                new LoginController();
                break;
        }
        
    }
    
}
