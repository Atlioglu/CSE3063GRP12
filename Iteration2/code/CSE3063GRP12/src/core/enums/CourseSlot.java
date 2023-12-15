package core.enums;

public enum CourseSlot {
    one("08.30 - 9.20"),
    two("09.30 - 10.20"),
    three("10.30 - 11.20"),
    four("11.30 - 12.20"),
    five("13.00 - 13.50"),
    six("14.00 - 14.50"),
    seven("15.00 - 15.50"),
    eight("16.00 - 16.50");

    private final String hour;

    CourseSlot(String hour) {
        this.hour = hour;
    }

    public String getSlot() {
        return hour;
    }
}
