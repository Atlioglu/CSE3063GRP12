package enums;

public enum CourseDay {
    one("Monday"),
    two("Tuesday"),
    three("Wednesday"),
    four("Thursday"),
    five("Friday");

    private final String day;

    CourseDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }
}
