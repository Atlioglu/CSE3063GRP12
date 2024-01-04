from core.enums.Menu import Menu

class AdvisorMenu(Menu):
    COURSE_APPROVAL = "Course Approval"
    STUDENT_LIST = "Student List"
    NOTIFICATION = "Notification"
    LOGOUT = "Logout"

    def get_item_message(self):
        return self.value
    
    def navigate(self):
        if self == AdvisorMenu.COURSE_APPROVAL:
            # Placeholder for course approval action
            print("Navigating to Course Approval")
        elif self == AdvisorMenu.STUDENT_LIST:
            # Placeholder for student list action
            print("Navigating to Student List")
        elif self == AdvisorMenu.NOTIFICATION:
            # Placeholder for notification action
            print("Navigating to Notifications")
        elif self == AdvisorMenu.LOGOUT:
            # Placeholder for logout action
            print("Logging out")

    def is_notification(self):
        return self == AdvisorMenu.NOTIFICATION