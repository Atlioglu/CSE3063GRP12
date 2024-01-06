from core.enums.Menu import Menu
from features.notification.NotificationController import NotificationController
from features.advisor.course_approval.CourseApprovalController import CourseApprovalController
from features.advisor.student_list.AdvisorStudentListController import AdvisorStudentListController

class AdvisorMenu(Menu):
    COURSE_APPROVAL = "Course Approval"
    STUDENT_LIST = "Student List"
    NOTIFICATION = "Notification"
    LOGOUT = "Logout"

    def get_item_message(self):
        return self.value
    
    def navigate(self):
        if self == AdvisorMenu.COURSE_APPROVAL:
            CourseApprovalController()
        elif self == AdvisorMenu.STUDENT_LIST:
            AdvisorStudentListController()
        elif self == AdvisorMenu.NOTIFICATION:
            NotificationController()
        elif self == AdvisorMenu.LOGOUT:
            from features.login.LoginController import LoginController
            LoginController()  

    def is_notification(self):
        return self == AdvisorMenu.NOTIFICATION