package enums;

public enum StudentMenu implements Menu{
    CourseRegistiration, WeeklySchedule, Transcript, Logout;
    public String getItemMessage() {
        return this.name();
    }
}

