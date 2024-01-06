from core.enums.Menu import Menu
from features.student.course_registration.CourseRegistrationController import CourseRegistrationController
from features.student.transcript.TranscriptController import TranscriptController
from features.notification.NotificationController import NotificationController

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
            CourseRegistrationController()
        elif self == StudentMenu.WEEKLY_SCHEDULE:
            from features.student.weekly_schedule.WeeklyScheduleController import WeeklyScheduleController
            WeeklyScheduleController()
        elif self == StudentMenu.TRANSCRIPT:
            TranscriptController()
        elif self == StudentMenu.NOTIFICATION:
            NotificationController()
        elif self == StudentMenu.LOGOUT:
            from features.login.LoginController import LoginController
            LoginController()    
        
    def is_notification(self):
        return self == StudentMenu.NOTIFICATION
    