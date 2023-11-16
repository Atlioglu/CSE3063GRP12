package core.enums;

import features.login.LoginController;

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

    public void navigate(){
        switch(this){
            case CourseApproval:
                // UNCOMMENT: new CourseApprovalController();
                break;
            case StudentList:
                // UNCOMMENT: new StudentListController();
                break;
            case Logout:
                    new LoginController();
                break;
        }
    }
}
