package core.enums;

import features.login.LoginController;

public enum StudentMenu implements Menu{
    CourseRegistration, WeeklySchedule, Transcript, Logout;
    public String getItemMessage() {
        switch(this){
            case CourseRegistration:
                return ("Course Registration");
            case WeeklySchedule:
                return ("Weekly Schedule");
            case Transcript:
                return ("Transcript");
            case Logout:
                return ("Logout");
            }
        return this.name();
    }

    public void navigate(){
        switch(this){
            case CourseRegistration:
                // UNCOMMENT: new CourseRegistrationController();
                break;
            case WeeklySchedule:
                // UNCOMMENT: new WeeklyScheduleController();
                break;
            case Transcript:
                // UNCOMMENT: new TranscriptController();
                break;
            case Logout:
                new LoginController();
                break;
        }
    }
}

