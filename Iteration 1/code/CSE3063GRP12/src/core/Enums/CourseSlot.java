package core.Enums;

public enum CourseSlot {
    one("08.30"),
    two("09.30"),
    three("10.30"),
    four("11.30"),
    five("13.00"),
    six("14.00"),
    seven("15.00"),
    eight("16.00");

    private final String hour;

    CourseSlot(String hour) {
        this.hour = hour;
    }

    public String getDay() {
        return hour;
    }
}
