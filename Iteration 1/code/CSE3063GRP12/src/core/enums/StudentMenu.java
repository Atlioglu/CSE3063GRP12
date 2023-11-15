package core.enums;

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
}

