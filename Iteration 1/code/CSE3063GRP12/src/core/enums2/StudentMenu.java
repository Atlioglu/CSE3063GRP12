package core.enums;

public enum StudentMenu implements Menu{
    CourseRegistration, WeeklySchedule, Transcript, Logout;
    public String getItemMessage() {
        return this.name();
    }
}

