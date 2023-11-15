package core.enums;

public enum AdvisorMenu implements Menu{
    CourseApproval, StudentList, Logout;
    public String getItemMessage() {
        switch(this){
            case CourseApproval:
                return ("Course Approvaln");
            case StudentList:
                return ("Student List");
            case Logout:
                return ("Logout");
            }
        return this.name();
    }
}
