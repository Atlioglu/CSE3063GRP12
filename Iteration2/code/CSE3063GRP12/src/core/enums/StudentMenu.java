package core.enums;

import core.models.concretes.Notification;
import features.login.LoginController;
import features.notification.NotificationController;
import features.student.course_registration.CourseRegistrationController;
import features.student.transcript.TranscriptController;
import features.student.weekly_schedule.WeeklyScheduleController;

public enum StudentMenu implements Menu{
    CourseRegistration, WeeklySchedule, Transcript, Notification, Logout;
    public String getItemMessage() {
        switch(this){
            case CourseRegistration:
                return ("Course Registration");
            case WeeklySchedule:
                return ("Weekly Schedule");
            case Transcript:
                return ("Transcript");
            case Notification:
                return("Notification");
            case Logout:
                return ("Logout");
            }
        return this.name();
    }

    public boolean isNotification(){
        return this == Notification;
    }
    public void navigate(){
        switch(this){
            case CourseRegistration:
                 new CourseRegistrationController();
                break;
            case WeeklySchedule:
                 new WeeklyScheduleController();
                break;
            case Transcript:
                 new TranscriptController();
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

