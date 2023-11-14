package enums;

public enum AdvisorMenu implements Menu{
    CourseApproval, StudentList, Logout;
    public String getItemMessage() {
        return this.name();
    }
}
