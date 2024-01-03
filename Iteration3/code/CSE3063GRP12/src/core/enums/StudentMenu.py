from core.enums.Menu import Menu

class StudentMenu(Menu):
    COURSE_REGISTRATION = "Course Registration"
    WEEKLY_SCHEDULE = "Weekly Schedule"
    TRANSCRIPT = "Transcript"
    NOTIFICATION = "Notification"
    LOGOUT = "Logout"

    def get_item_message(self):
        return self.value
    
    def navigate(self):
        if self == StudentMenu.COURSE_REGISTRATION:
            # Placeholder for course registration action
            print("Navigating to Course Registration")
        elif self == StudentMenu.WEEKLY_SCHEDULE:
            # Placeholder for weekly schedule action
            print("Navigating to Weekly Schedule")
        elif self == StudentMenu.TRANSCRIPT:
            # Placeholder for transcript action
            print("Navigating to Transcript")
        elif self == StudentMenu.NOTIFICATION:
            # Placeholder for notification action
            print("Navigating to Notifications")
        elif self == StudentMenu.LOGOUT:
            # Placeholder for logout action
            print("Logging out")    
        
    def is_notification(self):
        return self == StudentMenu.NOTIFICATION